package cz.muni.fgdovin.bachelorthesis.test;

import cz.muni.fgdovin.bachelorthesis.core.EsperUserFriendlyService;
import cz.muni.fgdovin.bachelorthesis.support.DataflowHelper;
import cz.muni.fgdovin.bachelorthesis.web.DataflowModel;
import cz.muni.fgdovin.bachelorthesis.web.MvcConfig;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MvcConfig.class)
public class EsperServiceTest {

    @Autowired
    EsperUserFriendlyService esperService;

    private static ApplicationContext context = null;

    private final String AMQPQueueName = "AMQPIncomingStream";
    private final String eventType = "myEventType";
    private final String inputQueueName = "esperQueue";
    private final String inputExchangeName = "logs";
    private static Map<String, Object> schema = null;

    private String statementName = "myTestStat";
    private String statementName2 = "myTestStat2";
    private String outputQueueName = "esperOutputQueue";
    private String outputExchangeName = "sortedLogs";
    private String query = "select avg(p.value) from instream where p.value > 4652";
    private String query2 = "select * from instream";

    @BeforeClass
    public static void setUp() throws Exception {
        context = new AnnotationConfigApplicationContext(MvcConfig.class);
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
        esperService.addEventType(eventType, schema);

        DataflowModel input1 = new DataflowModel(AMQPQueueName, eventType, inputQueueName, inputExchangeName);
        String inputQueue = DataflowHelper.generateEPL(input1);
        assertTrue(esperService.addDataflow(AMQPQueueName, inputQueue));
        assertTrue(esperService.removeDataflow(AMQPQueueName));
    }

    @Test
    public void testReAddAMQPSource() throws Exception {
        String savedSchema = esperService.showEventType(eventType);
        if(savedSchema==null) {
            esperService.addEventType(eventType, schema);
        }
        if(esperService.showDataflow(AMQPQueueName) != null) {
            esperService.removeDataflow(AMQPQueueName);
        }

        DataflowModel input1 = new DataflowModel(AMQPQueueName, eventType, inputQueueName, inputExchangeName);

        String inputQueue = DataflowHelper.generateEPL(input1);
        assertTrue(esperService.addDataflow(AMQPQueueName, inputQueue));

        String inputQueue2 = DataflowHelper.generateEPL(input1);
        assertFalse(esperService.addDataflow(AMQPQueueName, inputQueue2));

        assertTrue(esperService.removeDataflow(AMQPQueueName));
    }

    @Test
    public void testAddDiffAMQPSourceWithSameName() throws Exception {
        String savedSchema = esperService.showEventType(eventType);
        if(savedSchema==null) {
            esperService.addEventType(eventType, schema);
        }
        if(esperService.showDataflow(AMQPQueueName) != null) {
            esperService.removeDataflow(AMQPQueueName);
        }

        DataflowModel input1 = new DataflowModel(AMQPQueueName, eventType, inputQueueName, inputExchangeName);

        String inputQueue = DataflowHelper.generateEPL(input1);
        assertTrue(esperService.addDataflow(AMQPQueueName, inputQueue));

        DataflowModel input2 = new DataflowModel(AMQPQueueName, eventType + "2", inputQueueName, inputExchangeName);

        String inputQueue2 = DataflowHelper.generateEPL(input2);
        assertFalse(esperService.addDataflow(AMQPQueueName, inputQueue2));
        assertTrue(esperService.removeDataflow(AMQPQueueName));
    }

    @Test
    public void testAddSameAMQPSourceWithDiffName() throws Exception {
        String savedSchema = esperService.showEventType(eventType);
        if(savedSchema==null) {
            esperService.addEventType(eventType, schema);
        }
        if(esperService.showDataflow(AMQPQueueName + "WithDiffName") != null){
            assertTrue(esperService.removeDataflow(AMQPQueueName + "WithDiffName"));
        }
        DataflowModel input1 = new DataflowModel(AMQPQueueName + "WithDiffName", eventType, inputQueueName, inputExchangeName);

        String inputQueue = DataflowHelper.generateEPL(input1);
        assertTrue(esperService.addDataflow(AMQPQueueName + "WithDiffName", inputQueue));
        assertTrue(esperService.removeDataflow(AMQPQueueName + "WithDiffName"));
    }

    @Test
    public void testRemoveExistingAMQPSource() throws Exception {
        if(esperService.showDataflow(AMQPQueueName + "WithDiffName") == null){
            DataflowModel input1 = new DataflowModel(AMQPQueueName + "WithDiffName", eventType, inputQueueName, inputExchangeName);

            String inputQueue = DataflowHelper.generateEPL(input1);
            assertTrue(esperService.addDataflow(AMQPQueueName + "WithDiffName", inputQueue));
        }
        assertTrue(esperService.removeDataflow(AMQPQueueName + "WithDiffName"));
    }

    @Test
    public void testRemoveNonExistingAMQPSource() throws Exception {
        assertFalse(esperService.removeDataflow("SomeCertainlyNonExistingAMQPSource"));
    }
}