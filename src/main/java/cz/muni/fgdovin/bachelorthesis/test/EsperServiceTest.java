package cz.muni.fgdovin.bachelorthesis.test;

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
        esperService.addSchema(eventType, schema);
        String inputQueue = EPLHelper.createAMQP(AMQPQueueName, eventType,
                inputQueueName, inputExchangeName);
        EPDataFlowInstance result = esperService.addDataflow(AMQPQueueName, inputQueue);
        assertEquals(result.getState(), EPDataFlowState.RUNNING);
        assertEquals(result.getDataFlowName(), AMQPQueueName);
        assertTrue(esperService.removeDataflow(AMQPQueueName));
    }

    @Test
    public void testReAddAMQPSource() throws Exception {
        String savedSchema = esperService.showSchema(eventType);
        if(savedSchema==null) {
            esperService.addSchema(eventType, schema);
        }
        if(esperService.showDataflow(AMQPQueueName) != null) {
            esperService.removeDataflow(AMQPQueueName);
        }
        String inputQueue = EPLHelper.createAMQP(AMQPQueueName, eventType,
                inputQueueName, inputExchangeName);
        EPDataFlowInstance result = esperService.addDataflow(AMQPQueueName, inputQueue);
        assertEquals(result.getState(), EPDataFlowState.RUNNING);
        assertEquals(result.getDataFlowName(), AMQPQueueName);

        String inputQueue2 = EPLHelper.createAMQP(AMQPQueueName, eventType,
                inputQueueName, inputExchangeName);
        EPDataFlowInstance result2 = esperService.addDataflow(AMQPQueueName, inputQueue2);
        assertNull(result2);

        assertTrue(esperService.removeDataflow(AMQPQueueName));
    }

    @Test
    public void testAddDiffAMQPSourceWithSameName() throws Exception {
        String savedSchema = esperService.showSchema(eventType);
        if(savedSchema==null) {
            esperService.addSchema(eventType, schema);
        }
        if(esperService.showDataflow(AMQPQueueName) != null) {
            esperService.removeDataflow(AMQPQueueName);
        }
        String inputQueue = EPLHelper.createAMQP(AMQPQueueName, eventType,
                inputQueueName, inputExchangeName);
        EPDataFlowInstance result = esperService.addDataflow(AMQPQueueName, inputQueue);
        assertEquals(result.getState(), EPDataFlowState.RUNNING);
        assertEquals(result.getDataFlowName(), AMQPQueueName);

        String inputQueue2 = EPLHelper.createAMQP(AMQPQueueName, eventType+"2",
                inputQueueName, inputExchangeName);
        EPDataFlowInstance result2 = esperService.addDataflow(AMQPQueueName, inputQueue2);
        assertNull(result2);
        assertTrue(esperService.removeDataflow(AMQPQueueName));
    }

    @Test
    public void testAddSameAMQPSourceWithDiffName() throws Exception {
        String savedSchema = esperService.showSchema(eventType);
        if(savedSchema==null) {
            esperService.addSchema(eventType, schema);
        }
        if(esperService.showDataflow(AMQPQueueName + "WithDiffName") != null){
            assertTrue(esperService.removeDataflow(AMQPQueueName + "WithDiffName"));
        }
        String inputQueue = EPLHelper.createAMQP(AMQPQueueName+"WithDiffName", eventType, inputQueueName, inputExchangeName);
        EPDataFlowInstance result = esperService.addDataflow(AMQPQueueName + "WithDiffName", inputQueue);
        assertEquals(result.getState(), EPDataFlowState.RUNNING);
        assertEquals(result.getDataFlowName(), AMQPQueueName+"WithDiffName");
        assertTrue(esperService.removeDataflow(AMQPQueueName + "WithDiffName"));
    }

    @Test
    public void testRemoveExistingAMQPSource() throws Exception {
        if(esperService.showDataflow(AMQPQueueName + "WithDiffName") == null){
            String inputQueue = EPLHelper.createAMQP(AMQPQueueName+"WithDiffName", eventType, inputQueueName, inputExchangeName);
            EPDataFlowInstance result = esperService.addDataflow(AMQPQueueName + "WithDiffName", inputQueue);
            assertEquals(result.getState(), EPDataFlowState.RUNNING);
            assertEquals(result.getDataFlowName(), AMQPQueueName + "WithDiffName");
        }
        assertTrue(esperService.removeDataflow(AMQPQueueName + "WithDiffName"));
    }

    @Test
    public void testRemoveNonExistingAMQPSource() throws Exception {
        assertFalse(esperService.removeDataflow("SomeCertainlyNonExistingAMQPSource"));
    }
}