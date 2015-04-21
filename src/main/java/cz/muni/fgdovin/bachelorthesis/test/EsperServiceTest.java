package cz.muni.fgdovin.bachelorthesis.test;

import cz.muni.fgdovin.bachelorthesis.esper.EsperUserFriendlyService;
import cz.muni.fgdovin.bachelorthesis.support.DataflowHelper;
import cz.muni.fgdovin.bachelorthesis.support.EventTypeHelper;
import cz.muni.fgdovin.bachelorthesis.web.InputDataflowModel;
import cz.muni.fgdovin.bachelorthesis.web.SpringBootApp;

import org.junit.Before;
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
@ContextConfiguration(classes = SpringBootApp.class)
public class EsperServiceTest {

    @Autowired
    private EsperUserFriendlyService esperService;

    private DataflowHelper dataflowHelper;
    private EventTypeHelper eventTypeHelper;

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
        context = new AnnotationConfigApplicationContext(SpringBootApp.class);
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

    @Before
    public void setHelpers() {
        dataflowHelper = new DataflowHelper();
        eventTypeHelper = new EventTypeHelper();
    }

    @Test
    public void testAddAMQPSource() throws Exception {
        esperService.addEventType(eventType, schema);

        InputDataflowModel input1 = new InputDataflowModel(AMQPQueueName, eventType, inputQueueName, inputExchangeName);
        String inputQueue = dataflowHelper.generateInputDataflow(input1);
        assertTrue(esperService.addDataflow(AMQPQueueName, inputQueue));
        assertTrue(esperService.removeInputDataflow(AMQPQueueName));
    }

    @Test
    public void testReAddAMQPSource() throws Exception {
        String savedSchema = esperService.showEventType(eventType);
        if(savedSchema==null) {
            esperService.addEventType(eventType, schema);
        }
        esperService.removeInputDataflow(AMQPQueueName);

        InputDataflowModel input1 = new InputDataflowModel(AMQPQueueName, eventType, inputQueueName, inputExchangeName);

        String inputQueue = dataflowHelper.generateInputDataflow(input1);
        assertTrue(esperService.addDataflow(AMQPQueueName, inputQueue));

        String inputQueue2 = dataflowHelper.generateInputDataflow(input1);
        assertFalse(esperService.addDataflow(AMQPQueueName, inputQueue2));

        assertTrue(esperService.removeInputDataflow(AMQPQueueName));
    }

    @Test
    public void testAddDiffAMQPSourceWithSameName() throws Exception {
        String savedSchema = esperService.showEventType(eventType);
        if(savedSchema==null) {
            esperService.addEventType(eventType, schema);
        }

        InputDataflowModel input1 = new InputDataflowModel(AMQPQueueName, eventType, inputQueueName, inputExchangeName);

        String inputQueue = dataflowHelper.generateInputDataflow(input1);
        assertTrue(esperService.addDataflow(AMQPQueueName, inputQueue));

        InputDataflowModel input2 = new InputDataflowModel(AMQPQueueName, eventType + "2", inputQueueName, inputExchangeName);

        String inputQueue2 = dataflowHelper.generateInputDataflow(input2);
        assertFalse(esperService.addDataflow(AMQPQueueName, inputQueue2));
        assertTrue(esperService.removeInputDataflow(AMQPQueueName));
    }

    @Test
    public void testAddSameAMQPSourceWithDiffName() throws Exception {
        String savedSchema = esperService.showEventType(eventType);
        if(savedSchema==null) {
            esperService.addEventType(eventType, schema);
        }
        assertTrue(esperService.removeInputDataflow(AMQPQueueName + "WithDiffName"));
        InputDataflowModel input1 = new InputDataflowModel(AMQPQueueName + "WithDiffName", eventType, inputQueueName, inputExchangeName);

        String inputQueue = dataflowHelper.generateInputDataflow(input1);
        assertTrue(esperService.addDataflow(AMQPQueueName + "WithDiffName", inputQueue));
        assertTrue(esperService.removeInputDataflow(AMQPQueueName + "WithDiffName"));
    }

    @Test
    public void testRemoveExistingAMQPSource() throws Exception {
        InputDataflowModel input1 = new InputDataflowModel(AMQPQueueName + "WithDiffName", eventType, inputQueueName, inputExchangeName);
        String inputQueue = dataflowHelper.generateInputDataflow(input1);
        assertTrue(esperService.addDataflow(AMQPQueueName + "WithDiffName", inputQueue));
        assertTrue(esperService.removeInputDataflow(AMQPQueueName + "WithDiffName"));
    }

    @Test
    public void testRemoveNonExistingAMQPSource() throws Exception {
        assertFalse(esperService.removeInputDataflow("SomeCertainlyNonExistingAMQPSource"));
    }
}