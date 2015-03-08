package cz.muni.fgdovin.bachelorthesis.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Filip Gdovin on 10. 2. 2015.
 */
public class EPLHelper {

    private String dataflowName;
    private String eventType;
    private String query;
    private String queueName;
    private String exchangeName;

    public EPLHelper() {
    }

    public EPLHelper(String dataflowName, String eventType, String query, String queueName, String exchangeName) {
        this.dataflowName = dataflowName;
        this.eventType = eventType;
        this.query = query;
        this.queueName = queueName;
        this.exchangeName = exchangeName;
    }

    public EPLHelper(String dataflowName, String eventType, String queueName, String exchangeName) {
        this.dataflowName = dataflowName;
        this.eventType = eventType;
        this.queueName = queueName;
        this.exchangeName = exchangeName;
    }

    public String getDataflowName() {
        return dataflowName;
    }

    public void setDataflowName(String dataflowName) {
        this.dataflowName = dataflowName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public static void loadFromPropFile(Properties prop) {
        InputStream in = EPLHelper.class.getClassLoader().getResourceAsStream("config.properties");
        try {
            prop.load(in);
            in.close();
        } catch(IOException ex) {
            return;
        }
    }

    public static String toString(String name, String eventName, String query, String queueName, String exchangeName) {
        Properties prop = new Properties();
        loadFromPropFile(prop);

        if((query == "null") || (query == null) || (query.isEmpty())) {
            return  "Create Dataflow " + name + "\n" +
                    "AMQPSource -> instream<" + eventName + "> {\n" +
                    "host: '" + prop.getProperty("inputHost") + "',\n" +
                    "port: " + Integer.parseInt(prop.getProperty("inputPort")) + ",\n" +
                    "queueName: '" + queueName + "',\n" +
                    "declareDurable: " + prop.getProperty("inputDeclareDurable") + ",\n" +
                    "declareExclusive: " + prop.getProperty("inputDeclareExclusive") + ",\n" +
                    "declareAutoDelete: " + prop.getProperty("inputDeclareAutoDelete") + ",\n" +
                    "exchange: '" + exchangeName +"',\n" +
                    "collector: {class: '" + prop.getProperty("inputCollector") + "'}\n" +
                    "}" +
                    "EventBusSink(instream) {}";
        }

        return "Create dataflow " + name + "\n" +
                "EventBusSource -> instream<" + eventName + "> {}\n" +
                "Select(instream) -> output"+ eventName + " {\n" +
                "select: (\n" + query +")\n" +
                "}\n" +
                "AMQPSink(output"+ eventName + ") {\n" +
                "host: '" + prop.getProperty("outputHost") + "',\n" +
                "port: " + Integer.parseInt(prop.getProperty("outputPort")) + ",\n" +
                "queueName: '" + queueName + "',\n" +
                "declareDurable: " + prop.getProperty("outputDeclareDurable") + ",\n" +
                "declareExclusive: " + prop.getProperty("outputDeclareExclusive") + ",\n" +
                "declareAutoDelete: " + prop.getProperty("outputDeclareAutoDelete") + ",\n" +
                "exchange: '" + exchangeName + "',\n" +
                "collector: {class: '" + prop.getProperty("outputCollector") + "'}\n" +
                "}";
    }
}
