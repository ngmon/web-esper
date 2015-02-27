package cz.muni.fgdovin.bachelorthesis.support;

/**
 * Created by Filip Gdovin on 10. 2. 2015.
 */
public class EPLHelper {

    public static String createAMQP(String name, String eventName, String eventSchema, String queueName, String exchangeName){
        return  "Create Dataflow " + name + "\n" +
                "create map schema " + eventName + "(" + eventSchema + "),\n" +
                "AMQPSource -> instream<" + eventName + "> {\n" +
                "host: 'localhost',\n" +
                "queueName: '" + queueName + "',\n" +
                "declareDurable: false,\n" +
                "declareExclusive: false,\n" +
                "declareAutoDelete: false,\n" +
                "exchange: '" + exchangeName +"',\n" +
                "collector: {class: 'cz.muni.fgdovin.bachelorthesis.support.AMQPToEvent'}\n" +
                "}" +
                "EventBusSink(instream) {}";
    }

    public static String createStatement(String name, String eventName, String eventSchema, String query, String queueName, String exchangeName) {
        return "Create dataflow " + name + "\n" +
                "create map schema " + eventName + "(" + eventSchema + "),\n" +
                "EventBusSource -> instream<" + eventName + "> {}" +
                "Select(instream) -> output"+ eventName + " {\n" +
                "select: (\n" + query +")}\n" +
                "AMQPSink(output"+ eventName + ") {\n" +
                "host: 'localhost',\n" +
                "queueName: '" + queueName + "',\n" +
                "declareDurable: false,\n" +
                "declareExclusive: false,\n" +
                "declareAutoDelete: false,\n" +
                "exchange: '" + exchangeName + "',\n" +
                "collector: {class: 'cz.muni.fgdovin.bachelorthesis.support.EventToAMQP'}\n" +
                "}";
    }
}
