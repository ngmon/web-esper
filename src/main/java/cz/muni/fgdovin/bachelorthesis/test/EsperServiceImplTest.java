package cz.muni.fgdovin.bachelorthesis.test;

import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EPStatementState;
import com.espertech.esper.client.dataflow.EPDataFlowInstance;
import com.espertech.esper.client.dataflow.EPDataFlowState;

import cz.muni.fgdovin.bachelorthesis.Application;
import cz.muni.fgdovin.bachelorthesis.core.EsperService;
import cz.muni.fgdovin.bachelorthesis.support.EPLHelper;

import org.junit.*;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
public class EsperServiceImplTest {

    @Autowired
    EsperService esperService;

    @SuppressWarnings("FieldCanBeLocal")
    private static ApplicationContext context = null;

    private String statementName = "myTestStat";
    private String query = "select avg(p.value) from myEvent where p.value > 4652";
    private String testSchema = "hostname String, application String, level Integer, p.value Integer, p.value2 String, type String, priority Integer, timestamp String";
    private String queueName = "AMQPIncomingStream";

    @BeforeClass
    public static void setUp() throws Exception {
        context = SpringApplication.run(Application.class);
    }

    @Test
    public void testAddAMQPSource() throws Exception {
        System.out.println("Test: addAMQPSource");
        String inputQueue = EPLHelper.createAMQP(queueName, "myEvent", testSchema, "esperQueue", "logs");
        EPDataFlowInstance result = esperService.addAMQPSource(queueName, inputQueue);
        assertEquals(result.getState(), EPDataFlowState.RUNNING);
        assertEquals(result.getDataFlowName(), queueName);
    }

    @Test
    public void testReAddAMQPSource() throws Exception {
        System.out.println("Test: reAddAMQPSource");
        String inputQueue = EPLHelper.createAMQP(queueName, "myEvent", testSchema, "esperQueue", "logs");
        EPDataFlowInstance result = esperService.addAMQPSource(queueName, inputQueue);
        assertNull(result);
    }

    @Test
    public void testAddDiffAMQPSourceWithSameName() throws Exception {
        System.out.println("Test: addDiffAMQPSourceWithSameName");
        String inputQueue = EPLHelper.createAMQP(queueName, "myEvent2", testSchema + ", madeUpParam String", "esperQueue", "logs");
        EPDataFlowInstance result = esperService.addAMQPSource(queueName, inputQueue);
        assertNull(result);
    }

    @Test
    public void testAddSameAMQPSourceWithDiffName() throws Exception {
        System.out.println("Test: addSameAMQPSourceWithDiffName");
        String inputQueue = EPLHelper.createAMQP(queueName+"WithDiffName", "myEvent", testSchema, "esperQueue", "logs");
        EPDataFlowInstance result = esperService.addAMQPSource(queueName+"WithDiffName", inputQueue);
        assertEquals(result.getState(), EPDataFlowState.RUNNING);
        assertEquals(result.getDataFlowName(), queueName+"WithDiffName");
    }

    @Test
    public void testRemoveExistingAMQPSource() throws Exception {
        System.out.println("Test: removeExistingAMQPSource");
        assertEquals(EPDataFlowState.CANCELLED, esperService.removeAMQPSource(queueName));
    }

    @Test
    public void testRemoveNonExistingAMQPSource() throws Exception {
        System.out.println("Test: removeNonExistingAMQPSource");
        assertNull(esperService.removeAMQPSource("SomeCertainlyNonExistingAMQPSource"));
    }

    @Test
    public void testShowAMQPSources() throws Exception {
        System.out.println("Test: showAMQPSources");
        List<String> result = esperService.showAMQPSources();

        assertEquals(1, result.size());
        assertEquals(queueName, result.get(0));
    }



    @Test
    public void testAddStatement() throws Exception {
        System.out.println("Test: addAndRemoveStatement");
        String statement = EPLHelper.createStatement(statementName, "myEvent", testSchema, query, "esperOutputQueue", "sortedLogs");
        EPStatement result = esperService.addStatement(statementName, statement);
        assertEquals(EPStatementState.STARTED, result.getState());
        assertEquals(statementName, result.getName());
    }

    @Test
    public void testRemoveStatement() throws Exception {
        System.out.println("Test: removeStatement");
        EPStatementState result = esperService.removeStatement(statementName);
        assertEquals(EPStatementState.DESTROYED, result);
    }

    @Test
    public void testAddSameStatementWithDiffName() throws Exception {
        System.out.println("Test: addSameStatementWithDiffName");
        String statement = EPLHelper.createStatement(statementName + "WithDiffName", "myEvent", testSchema, query, "esperOutputQueue", "sortedLogs");
        EPStatement result = esperService.addStatement(statementName + "WithDiffName", statement);
        assertEquals(EPStatementState.STARTED, result.getState());
        assertEquals(statementName + "WithDiffName", result.getName());
    }

    @Test
    public void testRemoveNonExistingStatement() throws Exception {
        System.out.println("Test: removeNonExistingStatement");
        EPStatementState result = esperService.removeStatement("SomeCertainlyNonExistingStatement");
        assertNull(result);
    }

    @Test
    public void testShowStatements() throws Exception {
        System.out.println("Test: showStatements");
        List<String> result = esperService.showStatements();

        assertEquals(0, result.size());
    }
}