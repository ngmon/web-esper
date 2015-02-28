package cz.muni.fgdovin.bachelorthesis;

import com.espertech.esper.client.*;

import com.espertech.esper.client.dataflow.EPDataFlowInstance;
import com.espertech.esper.client.dataflow.EPDataFlowRuntime;
import com.espertech.esperio.amqp.AMQPSink;
import com.espertech.esperio.amqp.AMQPSource;

import cz.muni.fgdovin.bachelorthesis.support.EPLHelper;
import org.apache.log4j.Logger;

/**
 * Created by Filip Gdovin on 26. 2. 2015.
 */
@SuppressWarnings("WeakerAccess")
public class ManualEsperTester {

    private static final Logger logger = Logger.getLogger(ManualEsperTester.class);

    public static void main(String[] argc) {
        EPServiceProvider esperProvider = EPServiceProviderManager.getDefaultProvider();
        esperProvider.getEPAdministrator().getConfiguration().addImport(AMQPSource.class.getPackage().getName() + ".*");
        esperProvider.getEPAdministrator().getConfiguration().addImport(AMQPSink.class.getPackage().getName() + ".*");

        EPDataFlowRuntime esperDataFlowRuntime = esperProvider.getEPRuntime().getDataFlowRuntime();

        String AMQPQueueName = "AMQPIncomingStream";
        String eventType = "myEventType";
        String inputQueueName = "esperQueue";
        String inputExchangeName = "logs";
        String testSchema = ("hostname String, application String, level Integer, p.value Integer, p.value2 String, type String, priority Integer, timestamp String");

        String AMQPOutputQueueName = "AMQPOutcomingStream";
        String outputQueueName = "esperOutputQueue";
        String outputExchangeName = "sortedLogs";
//        String query = "select avg(p.value) from instream";
        String query = "select * from instream";

        String AMQP = EPLHelper.createAMQP(AMQPQueueName, eventType, testSchema, inputQueueName, inputExchangeName);

        esperProvider.getEPAdministrator().createEPL(AMQP);
        EPDataFlowInstance sourceInstance = esperDataFlowRuntime.instantiate(AMQPQueueName);
        esperDataFlowRuntime.saveInstance(AMQPQueueName,sourceInstance);
        sourceInstance.start();

        String epl1 = EPLHelper.createStatement(AMQPOutputQueueName, eventType, testSchema, query, outputQueueName, outputExchangeName);

        esperProvider.getEPAdministrator().createEPL(epl1);
        EPDataFlowInstance statement1Instance = esperDataFlowRuntime.instantiate(AMQPOutputQueueName);
        statement1Instance.start();
    }
}
