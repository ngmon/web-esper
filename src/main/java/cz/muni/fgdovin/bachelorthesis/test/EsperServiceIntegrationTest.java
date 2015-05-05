package cz.muni.fgdovin.bachelorthesis.test;

import cz.muni.fgdovin.bachelorthesis.demo.rabbitMQReceiver.src.main.RabbitMQReceiver;
import cz.muni.fgdovin.bachelorthesis.demo.rabbitMQSender.src.main.java.RabbitMQsender;
import cz.muni.fgdovin.bachelorthesis.esper.EsperUserFriendlyService;
import cz.muni.fgdovin.bachelorthesis.rabbit.RabbitMqService;
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

/**
 * @author Filip Gdovin
 * @version 28. 4. 2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringBootApp.class)
public class EsperServiceIntegrationTest {

    @Autowired
    private EsperUserFriendlyService esperService;

    @Autowired
    private EventTypeHelper eventTypeHelper;

    @Autowired
    private DataflowHelper dataflowHelper;

    @Autowired
    private RabbitMqService rabbitMqService;

    private String outputDataflow = "esperOutputQueue";

    @Before
    public void resetEverything() {
        //reset AMQP queues
        this.rabbitMqService.deleteAllQueues();

        //reset event types
        List<String> allEventTypes = this.esperService.showEventTypeNames();
        allEventTypes.forEach(this.esperService::removeEventType);

        //reset input dataflows
        List<String> inputDataflows = this.esperService.showInputDataflows();
        inputDataflows.forEach(this.esperService::removeInputDataflow);

        //reset output dataflows
        List<String> outputDataflows = this.esperService.showOutputDataflows();
        outputDataflows.forEach(this.esperService::removeOutputDataflow);

        //add basic event type for tests
        List<EventProperty> schema = new ArrayList<>();
        schema.add(new EventProperty("timestamp", Long.class));
        schema.add(new EventProperty("type", String.class));
        schema.add(new EventProperty("p.value", Integer.class));
        schema.add(new EventProperty("p.value2", String.class));
        schema.add(new EventProperty("hostname", String.class));
        schema.add(new EventProperty("application", String.class));
        schema.add(new EventProperty("process", String.class));
        schema.add(new EventProperty("processId", Integer.class));
        schema.add(new EventProperty("level", Integer.class));
        schema.add(new EventProperty("priority", Integer.class));

        Map<String, Object> mapSchema = this.eventTypeHelper.toMap(schema);
        String inputDataflow = "myEventType";
        this.esperService.addEventType(inputDataflow, mapSchema);

        InputDataflowModel inputModel = new InputDataflowModel();
        inputModel.setDataflowName(inputDataflow);
        inputModel.setEventType(inputDataflow);
        inputModel.setQueueName(inputDataflow);
        String dataflowParams = this.dataflowHelper.generateInputDataflow(inputModel);
        this.esperService.addDataflow(inputDataflow, dataflowParams);

        //this.rabbitMqService.createQueue(inputDataflow);

        OutputDataflowModel outputModel = new OutputDataflowModel();
        outputModel.setDataflowName(this.outputDataflow + System.currentTimeMillis());
        outputModel.setFirstEventType(inputDataflow);
        outputModel.setOutputEventType(this.outputDataflow);
        String query = "select * from  " + inputDataflow;
        outputModel.setQuery(query);
        outputModel.setQueueName(this.outputDataflow);
        dataflowParams = this.dataflowHelper.generateOutputDataflow(outputModel);
        this.esperService.addDataflow(this.outputDataflow, dataflowParams);

        this.rabbitMqService.createQueue(this.outputDataflow);
    }

    @Test
    public void testSendOneMessageAndSelectAll() throws Exception {
        RabbitMQsender mQsender = new RabbitMQsender();
        mQsender.sendFileContents("jsonInput.json", "inputQueue", 0);

        RabbitMQReceiver mqReceiver = new RabbitMQReceiver();
        mqReceiver.listen(this.outputDataflow, 10); //in new thread?
    }
}

