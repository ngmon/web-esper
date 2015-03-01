package cz.muni.fgdovin.bachelorthesis.test;

import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EPStatementState;
import com.espertech.esper.client.dataflow.EPDataFlowInstance;
import com.espertech.esper.client.dataflow.EPDataFlowState;

import cz.muni.fgdovin.bachelorthesis.Application;
import cz.muni.fgdovin.bachelorthesis.core.EsperService;
import cz.muni.fgdovin.bachelorthesis.support.EPLHelper;

import org.junit.BeforeClass;
import org.junit.Test;
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
public class EsperServiceTest {

    @Autowired
    EsperService esperService;

    private static ApplicationContext context = null;

    String AMQPQueueName = "AMQPIncomingStream";
    String eventType = "myEventType";
    String inputQueueName = "esperQueue";
    String inputExchangeName = "logs";
    String testSchema = ("hostname String, application String, level Integer, p.value Integer, p.value2 String, type String, priority Integer, timestamp String");

    String statementName = "myTestStat";
    String statementName2 = "myTestStat2";
    String outputQueueName = "esperOutputQueue";
    String outputExchangeName = "sortedLogs";
    String query = "select avg(p.value) from instream where p.value > 4652";
    String query2 = "select * from instream";

    @BeforeClass
    public static void setUp() throws Exception {
        context = SpringApplication.run(Application.class);
    }

    @Test
    public void testAddAMQPSource() throws Exception {
        String inputQueue = EPLHelper.createAMQP(AMQPQueueName, eventType,
                testSchema, inputQueueName, inputExchangeName);
        EPDataFlowInstance result = esperService.addAMQPSource(AMQPQueueName, inputQueue);
        assertEquals(result.getState(), EPDataFlowState.RUNNING);
        assertEquals(result.getDataFlowName(), AMQPQueueName);
    }

    @Test
    public void testReAddAMQPSource() throws Exception {
        String inputQueue = EPLHelper.createAMQP(AMQPQueueName, eventType,
                testSchema, inputQueueName, inputExchangeName);
        EPDataFlowInstance result = esperService.addAMQPSource(AMQPQueueName, inputQueue);
        assertNull(result);
    }

    @Test
    public void testAddDiffAMQPSourceWithSameName() throws Exception {
        String inputQueue = EPLHelper.createAMQP(AMQPQueueName, eventType+"2",
                testSchema + ", madeUpParam String", inputQueueName, inputExchangeName);
        EPDataFlowInstance result = esperService.addAMQPSource(AMQPQueueName, inputQueue);
        assertNull(result);
    }

    @Test
    public void testAddSameAMQPSourceWithDiffName() throws Exception {
        if(esperService.showAMQPSources().contains(AMQPQueueName+"WithDiffName")){
            testRemoveExistingAMQPSource();
        }
        String inputQueue = EPLHelper.createAMQP(AMQPQueueName+"WithDiffName", eventType, testSchema, inputQueueName, inputExchangeName);
        EPDataFlowInstance result = esperService.addAMQPSource(AMQPQueueName+"WithDiffName", inputQueue);
        assertEquals(result.getState(), EPDataFlowState.RUNNING);
        assertEquals(result.getDataFlowName(), AMQPQueueName+"WithDiffName");
    }

    @Test
    public void testRemoveExistingAMQPSource() throws Exception {
        if(!(esperService.showAMQPSources().contains(AMQPQueueName+"WithDiffName"))){
            testAddSameAMQPSourceWithDiffName();
        }
        assertTrue(esperService.removeAMQPSource(AMQPQueueName+"WithDiffName"));
    }

    @Test
    public void testRemoveNonExistingAMQPSource() throws Exception {
        assertFalse(esperService.removeAMQPSource("SomeCertainlyNonExistingAMQPSource"));
    }

    @Test
    public void testShowAMQPSources() throws Exception {
        List<String> result = esperService.showAMQPSources();
        assertEquals(1, result.size());
        assertEquals(AMQPQueueName, result.get(0));
    }

    @Test
    public void testAddStatement1() throws Exception {
        if(esperService.showStatements().contains(statementName)) {
            esperService.removeStatement(statementName);
        }
        String statement = EPLHelper.createStatement(statementName, eventType,
                testSchema, query, outputQueueName, outputExchangeName);
        EPStatement result = esperService.addStatement(statementName, statement);
        assertEquals(EPStatementState.STARTED, result.getState());
        assertEquals(statementName, result.getName());
    }

    @Test
    public void testAddStatement2() throws Exception {
        String statement = EPLHelper.createStatement(statementName2, eventType,
                testSchema, query2, outputQueueName, outputExchangeName);
        EPStatement result = esperService.addStatement(statementName2, statement);
        assertEquals(EPStatementState.STARTED, result.getState());
        assertEquals(statementName2, result.getName());
    }

    @Test
    public void testRemoveStatement2() throws Exception {
        if(!(esperService.showStatements().contains(statementName2))) {
            testAddStatement2();
        }
        boolean result = esperService.removeStatement(statementName2);
        assertTrue(result);
    }

    @Test
    public void testAddSameStatementWithDiffName() throws Exception {
        String statement = EPLHelper.createStatement(statementName + "WithDiffName", eventType,
                testSchema, query, outputQueueName, outputExchangeName);
        EPStatement result = esperService.addStatement(statementName + "WithDiffName", statement);
        assertEquals(EPStatementState.STARTED, result.getState());
        assertEquals(statementName + "WithDiffName", result.getName());
    }

    @Test
    public void testRemoveNonExistingStatement() throws Exception {
        boolean result = esperService.removeStatement("SomeCertainlyNonExistingStatement");
        assertFalse(result);
    }

    @Test
    public void testShowStatements() throws Exception {
        List<String> existingStatements = esperService.showStatements();
        if(existingStatements.size() > 0) {
            for(int i = 0; i < existingStatements.size(); i++) {
                esperService.removeStatement(existingStatements.get(i));
            }
        }
        testAddStatement1();

        List<String> result2 = esperService.showStatements();
        assertEquals(1, result2.size());
        assertEquals(statementName, result2.get(0));
    }
}