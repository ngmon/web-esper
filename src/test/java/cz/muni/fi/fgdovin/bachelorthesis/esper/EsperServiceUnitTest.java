package cz.muni.fi.fgdovin.bachelorthesis.esper;

import cz.muni.fgdovin.bachelorthesis.esper.EsperUserFriendlyService;
import cz.muni.fgdovin.bachelorthesis.support.DataflowHelper;
import cz.muni.fgdovin.bachelorthesis.support.EventProperty;
import cz.muni.fgdovin.bachelorthesis.support.EventTypeHelper;
import cz.muni.fgdovin.bachelorthesis.web.InputDataflowModel;
import cz.muni.fgdovin.bachelorthesis.web.OutputDataflowModel;
import cz.muni.fgdovin.bachelorthesis.web.SpringBootApp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringBootApp.class)
public class EsperServiceUnitTest {

    @Autowired
    private EsperUserFriendlyService esperService;

    @Autowired
    private EventTypeHelper eventTypeHelper;

    @Autowired
    private DataflowHelper dataflowHelper;

    private final String AMQPQueueName = "inputQueue";
    private final String eventType = "myEventType";
    private final String inputQueueName = "inputQueue";
    private final String inputExchangeName = "amq.direct";
    private Map<String, Object> schema;

    private String statementName = "myTestStat";
    private String query = "select * from " + eventType;

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
        List<EventProperty> listSchema = new ArrayList<>();
        listSchema.add(new EventProperty("timestamp", Long.class));
        listSchema.add(new EventProperty("type", String.class));
        listSchema.add(new EventProperty("p.value", Integer.class));
        listSchema.add(new EventProperty("p.value2", String.class));
        listSchema.add(new EventProperty("hostname", String.class));
        listSchema.add(new EventProperty("application", String.class));
        listSchema.add(new EventProperty("process", String.class));
        listSchema.add(new EventProperty("processId", Integer.class));
        listSchema.add(new EventProperty("level", Integer.class));
        listSchema.add(new EventProperty("priority", Integer.class));

        this.schema = this.eventTypeHelper.toMap(listSchema);
        esperService.addEventType(this.eventType, this.schema);
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
        InputDataflowModel input1 = new InputDataflowModel(this.AMQPQueueName, this.eventType, this.inputQueueName);
        String inputDataflow = this.dataflowHelper.generateInputDataflow(input1);
        assertTrue(this.esperService.addDataflow(this.AMQPQueueName, inputDataflow));
        assertTrue(this.esperService.removeInputDataflow(this.AMQPQueueName));
    }

    @Test
    public void testReAddExistingDataflow() throws Exception {
        InputDataflowModel input1 = new InputDataflowModel(this.AMQPQueueName, this.eventType, this.inputQueueName);
        String inputDataflow = this.dataflowHelper.generateInputDataflow(input1);
        assertTrue(this.esperService.addDataflow(this.AMQPQueueName, inputDataflow));

        String inputDataflow2 = this.dataflowHelper.generateInputDataflow(input1);
        assertFalse(this.esperService.addDataflow(this.AMQPQueueName, inputDataflow2));
    }

    @Test
    public void testAddDifferentDataflowsWithSameNames() throws Exception {
        InputDataflowModel input1 = new InputDataflowModel(this.AMQPQueueName, this.eventType, this.inputQueueName);
        String inputDataflow = this.dataflowHelper.generateInputDataflow(input1);
        assertTrue(this.esperService.addDataflow(this.AMQPQueueName, inputDataflow));

        InputDataflowModel input2 = new InputDataflowModel(this.AMQPQueueName, this.eventType + "2", this.inputQueueName);
        String inputDataflow2 = this.dataflowHelper.generateInputDataflow(input2);
        assertFalse(this.esperService.addDataflow(this.AMQPQueueName, inputDataflow2));
    }

    @Test
    public void testAddSameDataflowsWithDifferentNames() throws Exception {
        InputDataflowModel input1 = new InputDataflowModel(this.AMQPQueueName, this.eventType, this.inputQueueName);
        String inputDataflow = this.dataflowHelper.generateInputDataflow(input1);
        assertTrue(this.esperService.addDataflow(this.AMQPQueueName, inputDataflow));

        InputDataflowModel input2 = new InputDataflowModel(this.AMQPQueueName + "WithDiffName",
                this.eventType, this.inputQueueName);
        String inputDataflow2 = this.dataflowHelper.generateInputDataflow(input2);
        assertTrue(this.esperService.addDataflow(this.AMQPQueueName + "WithDiffName", inputDataflow2));
    }

    @Test
    public void testRemoveExistingDataflow() throws Exception {
        InputDataflowModel input1 = new InputDataflowModel(this.AMQPQueueName, this.eventType, this.inputQueueName);
        String inputDataflow = this.dataflowHelper.generateInputDataflow(input1);
        assertTrue(this.esperService.addDataflow(this.AMQPQueueName, inputDataflow));
        assertTrue(this.esperService.removeInputDataflow(this.AMQPQueueName));
    }

    @Test
    public void testRemoveNonExistingDataflow() throws Exception {
        InputDataflowModel input1 = new InputDataflowModel(this.AMQPQueueName, this.eventType, this.inputQueueName);
        String inputDataflow = this.dataflowHelper.generateInputDataflow(input1);
        assertTrue(this.esperService.addDataflow(this.AMQPQueueName, inputDataflow));
        assertFalse(this.esperService.removeInputDataflow("SomeCertainlyNonExistingDataflow"));
    }

    @Test
    public void testShowAllInputDataflowsWhenThereIsNone() throws Exception {
        List<String> allDataflows = this.esperService.showInputDataflows();
        assertTrue(allDataflows.isEmpty());
    }

    @Test
    public void testShowAllOutputDataflowsWhenThereIsNone() throws Exception {
        List<String> allDataflows = this.esperService.showOutputDataflows();
        assertTrue(allDataflows.isEmpty());
    }

    @Test
    public void testShowAllInputDataflowsWhenThereIsSome() throws Exception {
        InputDataflowModel input1 = new InputDataflowModel(this.AMQPQueueName, this.eventType, this.inputQueueName);
        String inputDataflow = this.dataflowHelper.generateInputDataflow(input1);
        assertTrue(this.esperService.addDataflow(this.AMQPQueueName, inputDataflow));

        List<String> allDataflows = this.esperService.showInputDataflows();
        assertFalse(allDataflows.isEmpty());
        assertEquals(1, allDataflows.size());
        assertEquals(this.AMQPQueueName, allDataflows.get(0));
    }

    @Test
    public void testShowAllOutputDataflowsWhenThereIsSome() throws Exception {
        OutputDataflowModel output1 = new OutputDataflowModel(this.statementName, this.eventType,
                "output"+this.eventType, this.query, this.inputQueueName, this.inputExchangeName);
        String outputDataflow = this.dataflowHelper.generateOutputDataflow(output1);
        assertTrue(this.esperService.addDataflow(this.statementName, outputDataflow));

        List<String> allDataflows = this.esperService.showOutputDataflows();
        assertFalse(allDataflows.isEmpty());
        assertEquals(1, allDataflows.size());
        assertEquals(this.statementName, allDataflows.get(0));
    }

    @Test
    public void testShowAllInputDataflowsWhenThereIsOnlyOutput() throws Exception {
        OutputDataflowModel output1 = new OutputDataflowModel(this.statementName, this.eventType,
                "output"+this.eventType, this.query, this.inputQueueName, this.inputExchangeName);
        String outputDataflow = this.dataflowHelper.generateOutputDataflow(output1);
        assertTrue(this.esperService.addDataflow(this.statementName, outputDataflow));

        List<String> allDataflows = this.esperService.showInputDataflows();
        assertTrue(allDataflows.isEmpty());
    }

    @Test
    public void testShowAllOutputDataflowsWhenThereIsOnlyInput() throws Exception {
        InputDataflowModel input1 = new InputDataflowModel(this.AMQPQueueName, this.eventType, this.inputQueueName);
        String inputDataflow = this.dataflowHelper.generateInputDataflow(input1);
        assertTrue(this.esperService.addDataflow(this.AMQPQueueName, inputDataflow));

        List<String> allDataflows = this.esperService.showOutputDataflows();
        assertTrue(allDataflows.isEmpty());
    }
}