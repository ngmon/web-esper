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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    static Map<String, Object> schema = null;

    String statementName = "myTestStat";
    String statementName2 = "myTestStat2";
    String outputQueueName = "esperOutputQueue";
    String outputExchangeName = "sortedLogs";
    String query = "select avg(p.value) from instream where p.value > 4652";
    String query2 = "select * from instream";

    @BeforeClass
    public static void setUp() throws Exception {
        context = SpringApplication.run(Application.class);
        schema = new HashMap<String, Object>();
        schema.put("hostname", String.class);
        schema.put("application", String.class);
        schema.put("level", Integer.class);
        schema.put("p.value", Integer.class);
        schema.put("p.value2", String.class);
        schema.put("type", String.class);
        schema.put("priority", Integer.class);
        schema.put("timestamp", String.class);
    }

    @Test
    public void testAddAMQPSource() throws Exception {
        esperService.setSchema(eventType, schema);
        String inputQueue = EPLHelper.createAMQP(AMQPQueueName, eventType,
                inputQueueName, inputExchangeName);
        EPDataFlowInstance result = esperService.addAMQPSource(AMQPQueueName, inputQueue);
        assertEquals(result.getState(), EPDataFlowState.RUNNING);
        assertEquals(result.getDataFlowName(), AMQPQueueName);
        assertTrue(esperService.removeAMQPSource(AMQPQueueName));
    }

    @Test
    public void testReAddAMQPSource() throws Exception {
        String savedSchema = esperService.showSchema(eventType);
        if(savedSchema==null) {
            esperService.setSchema(eventType, schema);
        }
        if(esperService.showAMQPSource(AMQPQueueName) != null) {
            esperService.removeAMQPSource(AMQPQueueName);
        }
        String inputQueue = EPLHelper.createAMQP(AMQPQueueName, eventType,
                inputQueueName, inputExchangeName);
        EPDataFlowInstance result = esperService.addAMQPSource(AMQPQueueName, inputQueue);
        assertEquals(result.getState(), EPDataFlowState.RUNNING);
        assertEquals(result.getDataFlowName(), AMQPQueueName);

        String inputQueue2 = EPLHelper.createAMQP(AMQPQueueName, eventType,
                inputQueueName, inputExchangeName);
        EPDataFlowInstance result2 = esperService.addAMQPSource(AMQPQueueName, inputQueue2);
        assertNull(result2);

        assertTrue(esperService.removeAMQPSource(AMQPQueueName));
    }

    @Test
    public void testAddDiffAMQPSourceWithSameName() throws Exception {
        String savedSchema = esperService.showSchema(eventType);
        if(savedSchema==null) {
            esperService.setSchema(eventType, schema);
        }
        if(esperService.showAMQPSource(AMQPQueueName) != null) {
            esperService.removeAMQPSource(AMQPQueueName);
        }
        String inputQueue = EPLHelper.createAMQP(AMQPQueueName, eventType,
                inputQueueName, inputExchangeName);
        EPDataFlowInstance result = esperService.addAMQPSource(AMQPQueueName, inputQueue);
        assertEquals(result.getState(), EPDataFlowState.RUNNING);
        assertEquals(result.getDataFlowName(), AMQPQueueName);

        String inputQueue2 = EPLHelper.createAMQP(AMQPQueueName, eventType+"2",
                inputQueueName, inputExchangeName);
        EPDataFlowInstance result2 = esperService.addAMQPSource(AMQPQueueName, inputQueue2);
        assertNull(result2);
        assertTrue(esperService.removeAMQPSource(AMQPQueueName));
    }

    @Test
    public void testAddSameAMQPSourceWithDiffName() throws Exception {
        String savedSchema = esperService.showSchema(eventType);
        if(savedSchema==null) {
            esperService.setSchema(eventType, schema);
        }
        if(esperService.showAMQPSource(AMQPQueueName+"WithDiffName") != null){
            assertTrue(esperService.removeAMQPSource(AMQPQueueName+"WithDiffName"));
        }
        String inputQueue = EPLHelper.createAMQP(AMQPQueueName+"WithDiffName", eventType, inputQueueName, inputExchangeName);
        EPDataFlowInstance result = esperService.addAMQPSource(AMQPQueueName+"WithDiffName", inputQueue);
        assertEquals(result.getState(), EPDataFlowState.RUNNING);
        assertEquals(result.getDataFlowName(), AMQPQueueName+"WithDiffName");
        assertTrue(esperService.removeAMQPSource(AMQPQueueName + "WithDiffName"));
    }

    @Test
    public void testRemoveExistingAMQPSource() throws Exception {
        if(esperService.showAMQPSource(AMQPQueueName+"WithDiffName") == null){
            String inputQueue = EPLHelper.createAMQP(AMQPQueueName+"WithDiffName", eventType, inputQueueName, inputExchangeName);
            EPDataFlowInstance result = esperService.addAMQPSource(AMQPQueueName+"WithDiffName", inputQueue);
            assertEquals(result.getState(), EPDataFlowState.RUNNING);
            assertEquals(result.getDataFlowName(), AMQPQueueName + "WithDiffName");
        }
        assertTrue(esperService.removeAMQPSource(AMQPQueueName+"WithDiffName"));
    }

    @Test
    public void testRemoveNonExistingAMQPSource() throws Exception {
        assertFalse(esperService.removeAMQPSource("SomeCertainlyNonExistingAMQPSource"));
    }

    @Test
    public void testAddStatement1() throws Exception {
        esperService.removeStatement(statementName);
        String statement = EPLHelper.createStatement(statementName, eventType,
                query, outputQueueName, outputExchangeName);
        EPStatement result = esperService.addStatement(statementName, statement);
        assertEquals(EPStatementState.STARTED, result.getState());
        assertEquals(statementName, result.getName());
    }

    @Test
    public void testAddStatement2() throws Exception {
        String statement = EPLHelper.createStatement(statementName2, eventType,
                query2, outputQueueName, outputExchangeName);
        EPStatement result = esperService.addStatement(statementName2, statement);
        assertEquals(EPStatementState.STARTED, result.getState());
        assertEquals(statementName2, result.getName());
    }

    @Test
    public void testRemoveStatement2() throws Exception {
        String statement = EPLHelper.createStatement(statementName2, eventType,
                query2, outputQueueName, outputExchangeName);
        EPStatement result = esperService.addStatement(statementName2, statement);
        assertEquals(EPStatementState.STARTED, result.getState());

        assertEquals(statementName2, result.getName());
        boolean result2 = esperService.removeStatement(statementName2);
        assertTrue(result2);
    }

    @Test
    public void testAddSameStatementWithDiffName() throws Exception {
        String statement = EPLHelper.createStatement(statementName + "WithDiffName", eventType,
                query, outputQueueName, outputExchangeName);
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
        String statement = EPLHelper.createStatement(statementName, eventType,
                query, outputQueueName, outputExchangeName);
        EPStatement result = esperService.addStatement(statementName, statement);
        assertNotNull(result);
        assertEquals(EPStatementState.STARTED, result.getState());
        assertEquals(statementName, result.getName());

        List<String> existingStatements = esperService.showStatements();
        assertNotNull(existingStatements);
        if(existingStatements.size() > 0) {
            for(int i = 0; i < existingStatements.size(); i++) {
                esperService.removeStatement(existingStatements.get(i));
            }
        }
        List<String> result2 = esperService.showStatements();
        assertNull(result2);
    }
}