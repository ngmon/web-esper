package cz.muni.fgdovin.bachelorthesis.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Filip Gdovin on 10. 2. 2015.
 */
public class EPLHelper {

    public static void loadFromPropFile(Properties prop) {
        InputStream in = EPLHelper.class.getClassLoader().getResourceAsStream("config.properties");
        try {
            prop.load(in);
            in.close();
        } catch(IOException ex) {
            return;
        }
    }

    public static String createAMQP(String name, String eventName, String queueName, String exchangeName){
        Properties prop = new Properties();
        loadFromPropFile(prop);

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

    public static String createStatement(String name, String eventName, String query, String queueName, String exchangeName) {
        Properties prop = new Properties();
        loadFromPropFile(prop);

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
