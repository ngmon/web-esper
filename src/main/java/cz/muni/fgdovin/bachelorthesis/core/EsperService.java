/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fgdovin.bachelorthesis.core;

import com.espertech.esper.client.*;
import com.espertech.esper.client.dataflow.EPDataFlowInstance;
import com.espertech.esperio.amqp.AMQPSink;
import com.espertech.esperio.amqp.AMQPSource;

import java.util.Map;

import cz.muni.fgdovin.bachelorthesis.rest.JsonSerializable;
import cz.muni.fgdovin.bachelorthesis.support.CreateEventSchema;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author Filip Gdovin, 410328
 */
@Component
public final class EsperService {

    private static EsperService instance = null;

    private static com.espertech.esper.client.Configuration config;
    private static EPServiceProviderManager cepManager;
    private static EPAdministrator cepAdm;
    private static EPRuntime cepRT;

    private static Map schema;
    private static String inputQueueName;
    private static String inputExchangeName;
    private static int inputHost;

    private static String outputQueueName;
    private static String outputExchangeName;
    private static int outputHost;

    private static String eventType;
    private static String query;

    private static final Logger logger = Logger.getLogger(EsperService.class);

    public EsperService() {
        setConfig();
    }


    private static void setConfig() {
        config.getEngineDefaults().getLogging().setEnableExecutionDebug(true);
        config.getEngineDefaults().getLogging().setEnableQueryPlan(true);
        cepRT = cepManager.getDefaultProvider().getEPRuntime();
        cepAdm = cepManager.getDefaultProvider().getEPAdministrator();
        cepAdm.getConfiguration().addImport(AMQPSource.class.getPackage().getName() + ".*");
        cepAdm.getConfiguration().addImport(AMQPSink.class.getPackage().getName() + ".*");
    }

    public void setEverything(JsonSerializable input) {
        inputQueueName = input.getInputQueueParams().get("inputQueueName").toString();
        inputExchangeName = input.getInputQueueParams().get("inputExchangeName").toString();
        inputHost = Integer.parseInt(input.getInputQueueParams().get("inputHost").toString());

        outputQueueName = input.getOutputQueueParams().get("outputQueueName").toString();
        outputExchangeName = input.getOutputQueueParams().get("outputExchangeName").toString();
        outputHost = Integer.parseInt(input.getOutputQueueParams().get("outputHost").toString());

        setSchema(input.getEventSchema());
        startInputQueue();
        setQuery(input.getQueryStatement());
        executeQuery();
    }

    void setSchema(String currentEventSchema) {
        Map newSchema = CreateEventSchema.stringToSchemaMap(currentEventSchema);
        if (newSchema != schema) {
            System.out.println(newSchema);
            cepAdm.createEPL("create map schema " + eventType + "(" + currentEventSchema + ")");
        }
        schema = newSchema;
    }

    void setQuery(String statement) {
        query = "@Name('JSON-test') @Audit('expression') " + statement;
    }

    public void setInputQueue() {
        cepAdm.createEPL(
                "create dataflow AMQPIncomingDataFlow\n" +
                        "AMQPSource -> instream<" + eventType + "> {" +
                        "host: '" + inputHost + "',\n" +
                        "inputQueueName: '" + inputQueueName + "',\n" +
                        "declareDurable: true,\n" +
                        "declareExclusive: false,\n" +
                        "declareAutoDelete: true,\n" +
                        "exchange: '" + inputExchangeName + "',\n" +
                        "collector: {class: 'cz.muni.fgdovin.bachelorthesis.core.AMQPToEvent'},\n" +
                        "logMessages: true" +
                        "}\n" +
                        " EventBusSink(instream){}");
    }

    void startInputQueue() {
        cepRT.getDataFlowRuntime().instantiate("AMQPIncomingDataFlow").start();
    }

    public void setOutputQueue() {
        cepAdm.createEPL(
                "Create Dataflow AMQPOutgoingDataFlow \n" +
                        "EventBusSource -> outstream<" + eventType + ">{} \n" +
                        "AMQPSink(outstream)" +
                        "{host: '" + outputHost + "'," +
                        "inputQueueName: '" + outputQueueName + "'," + //ina nez pri Source
                        "declareDurable: true,\n" +
                        "declareExclusive: false,\n" +
                        "declareAutoDelete: true,\n" +
                        "exchange: '" + outputExchangeName + "',\n" +
                        "collector: {class: 'cz.muni.fgdovin.bachelorthesis.core.EventToAMQP'}" +
//                        "collector: {class: 'cz.muni.fgdovin.bachelorthesis.support.EventToAMQP'},\n" +  //toto...
                        "}");

        EPDataFlowInstance instance2 = cepRT.getDataFlowRuntime().instantiate("AMQPOutgoingDataFlow");
        instance2.start();
    }

    void executeQuery() {
        if (query != null) {
            EPStatement myQuery = cepAdm.createEPL(query);
            myQuery.addListener(new UpdateListener() {
                @Override
                public void update(EventBean[] newEvents, EventBean[] oldEvents) {
                }
            });

        }
    }

// --Commented out by Inspection START (5. 2. 2015 12:53):
//    public static void setEventType(String newEventType) {
//        eventType = newEventType;
//    }
// --Commented out by Inspection STOP (5. 2. 2015 12:53)

}
