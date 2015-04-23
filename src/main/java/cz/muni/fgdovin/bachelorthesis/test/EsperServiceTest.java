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
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringBootApp.class)
public class EsperServiceTest {

    @Autowired
    private EsperUserFriendlyService esperService;

    @Autowired
    private EventTypeHelper eventTypeHelper;

    @Autowired
    private DataflowHelper dataflowHelper;

    @Autowired
    private ApplicationContext context;

    private final String AMQPQueueName = "AMQPIncomingStream";
    private final String eventType = "myEventType";
    private final String inputQueueName = "esperQueue";
    private final String inputExchangeName = "logs";
    private final String stringSchema = "timestamp Long, type String, p.value Integer, p.value2 String, hostname String, application String, process String, processId Integer, level Integer, priority Integer";
    private Map<String, Object> schema;

    private String statementName = "myTestStat";
    private String statementName2 = "myTestStat2";
    private String outputQueueName = "esperOutputQueue";
    private String outputExchangeName = "sortedLogs";
    private String query = "select avg(p.value) from " + eventType + " where p.value > 4652";
    private String query2 = "select * from  " + eventType;

    @Before
    public void resetEverything() {
        //reset event types
        List<String> allEventTypes = this.esperService.showEventTypeNames();
        allEventTypes.forEach(esperService::removeEventType);

        //reset input dataflows
        List<String> inputDataflows = this.esperService.showInputDataflows();
        inputDataflows.forEach(esperService::removeInputDataflow);

        //reset output dataflows
        List<String> outputDataflows = this.esperService.showOutputDataflows();
        outputDataflows.forEach(esperService::removeOutputDataflow);

        //add basic event type for tests
        this.schema = this.eventTypeHelper.toMap(this.stringSchema);
        esperService.addEventType(this.eventType, this.schema);

        System.out.println("Available event types:" + this.esperService.showEventTypeNames());
    }

    @Test
    public void testAddAndRemoveEventType() throws Exception {
        assertTrue(esperService.addEventType(this.eventType + "2", this.schema));
        assertTrue(esperService.removeEventType(this.eventType + "2"));
    }

    @Test
    public void testReAddExistingEventType() throws Exception {
        assertFalse(esperService.addEventType(this.eventType, this.schema));
    }

    @Test
    public void testAddDifferentEventTypesWithSameNames() throws Exception {
        assertTrue(esperService.addEventType(this.eventType + "2", this.schema));
        this.schema.put("previouslyNotIncludedKey", "andItsHardlyGuessableValue");
        assertFalse(esperService.addEventType(this.eventType + "2", this.schema));
    }

    @Test
    public void testAddSameEventTypesWithDifferentNames() throws Exception {
        assertTrue(esperService.addEventType(this.eventType + "2", this.schema));
        assertTrue(esperService.addEventType(this.eventType + "3", this.schema));
    }

    @Test
    public void testRemoveExistingEventType() throws Exception {
        assertTrue(esperService.removeEventType(this.eventType));
    }

    @Test
    public void testRemoveNonExistingEventType() throws Exception {
        assertFalse(esperService.removeEventType(this.eventType + "2"));
    }

    @Test
    public void testAddAndRemoveDataflow() throws Exception {
        InputDataflowModel input1 = new InputDataflowModel(AMQPQueueName, eventType, inputQueueName);
        String inputQueue = dataflowHelper.generateInputDataflow(input1);
        assertTrue(esperService.addDataflow(AMQPQueueName, inputQueue));
        assertTrue(esperService.removeInputDataflow(AMQPQueueName));
    }

    @Test
    public void testReAddExistingDataflow() throws Exception {
        InputDataflowModel input1 = new InputDataflowModel(AMQPQueueName, eventType, inputQueueName);
        String inputQueue = dataflowHelper.generateInputDataflow(input1);
        assertTrue(esperService.addDataflow(AMQPQueueName, inputQueue));

        String inputQueue2 = dataflowHelper.generateInputDataflow(input1);
        assertFalse(esperService.addDataflow(AMQPQueueName, inputQueue2));
    }

    @Test
    public void testAddDifferentDataflowsWithSameNames() throws Exception {
        InputDataflowModel input1 = new InputDataflowModel(AMQPQueueName, eventType, inputQueueName);
        String inputQueue = dataflowHelper.generateInputDataflow(input1);
        assertTrue(esperService.addDataflow(AMQPQueueName, inputQueue));

        InputDataflowModel input2 = new InputDataflowModel(AMQPQueueName, eventType + "2", inputQueueName);
        String inputQueue2 = dataflowHelper.generateInputDataflow(input2);
        assertFalse(esperService.addDataflow(AMQPQueueName, inputQueue2));
    }

    @Test
    public void testAddSameDataflowsWithDifferentNames() throws Exception {
        InputDataflowModel input1 = new InputDataflowModel(AMQPQueueName, eventType, inputQueueName);
        String inputQueue = dataflowHelper.generateInputDataflow(input1);
        assertTrue(esperService.addDataflow(AMQPQueueName, inputQueue));

        InputDataflowModel input2 = new InputDataflowModel(AMQPQueueName + "WithDiffName", eventType, inputQueueName);
        String inputQueue2 = dataflowHelper.generateInputDataflow(input2);
        assertTrue(esperService.addDataflow(AMQPQueueName + "WithDiffName", inputQueue2));
    }

    @Test
    public void testRemoveExistingDataflow() throws Exception {
        InputDataflowModel input1 = new InputDataflowModel(AMQPQueueName, eventType, inputQueueName);
        String inputQueue = dataflowHelper.generateInputDataflow(input1);
        assertTrue(esperService.addDataflow(AMQPQueueName, inputQueue));
        assertTrue(esperService.removeInputDataflow(AMQPQueueName));
    }

    @Test
    public void testRemoveNonExistingDataflow() throws Exception {
        InputDataflowModel input1 = new InputDataflowModel(AMQPQueueName, eventType, inputQueueName);
        String inputQueue = dataflowHelper.generateInputDataflow(input1);
        assertTrue(esperService.addDataflow(AMQPQueueName, inputQueue));
        assertFalse(esperService.removeInputDataflow("SomeCertainlyNonExistingDataflow"));
    }
}