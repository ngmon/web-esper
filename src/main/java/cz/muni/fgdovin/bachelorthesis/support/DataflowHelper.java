package cz.muni.fgdovin.bachelorthesis.support;

import cz.muni.fgdovin.bachelorthesis.web.DataflowModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Filip Gdovin on 10. 2. 2015.
 */
public class DataflowHelper {

    private static void loadFromPropFile(Properties prop) throws IOException {
        InputStream in = DataflowHelper.class.getClassLoader().getResourceAsStream("config.properties");
        prop.load(in);
        in.close();
    }

    public static String generateEPL(DataflowModel model) {
        Properties prop = new Properties();
        try {
            loadFromPropFile(prop);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if((model.getQuery() == null) || (model.getQuery().equals("null")) || (model.getQuery().isEmpty())) {
            return
            "Create Dataflow " + model.getDataflowName() + "\n" +
            "AMQPSource -> instream<" + model.getFirstEventType() + "> {\n" +
            "host: '" + prop.getProperty("inputHost") + "',\n" +
            "port: " + Integer.parseInt(prop.getProperty("inputPort")) + ",\n" +
            "queueName: '" + model.getQueueName() + "',\n" +
            "declareDurable: " + prop.getProperty("inputDeclareDurable") + ",\n" +
            "declareExclusive: " + prop.getProperty("inputDeclareExclusive") + ",\n" +
            "declareAutoDelete: " + prop.getProperty("inputDeclareAutoDelete") + ",\n" +
            "exchange: '" + model.getExchangeName() +"',\n" +
            "collector: {class: '" + prop.getProperty("inputCollector") + "'}\n" +
            "}" +
            "EventBusSink(instream) {}";
        }
        StringBuilder result = new StringBuilder("Create dataflow " + model.getDataflowName() + "\n");
        StringBuilder selector = new StringBuilder("Select(");
        for(String eventType : model.getAllEventTypes()) {
            if ((eventType != null) && (eventType != "null")){
                result.append(
                    "EventBusSource -> " + eventType + "<" + eventType + "> {}\n");
                    selector.append(eventType + ", ");
            }
        }
        int lastComma = selector.lastIndexOf(", ");
        result.append(selector.substring(0, lastComma));
        result.append(
                ") -> " + model.getOutputEventType() + " {\n" +
                "select: (\n" + model.getQuery() + ")\n" +
                "}\n" +
                "AMQPSink(" + model.getOutputEventType() + ") {\n" +
                "host: '" + prop.getProperty("outputHost") + "',\n" +
                "port: " + Integer.parseInt(prop.getProperty("outputPort")) + ",\n" +
                "queueName: '" + model.getQueueName() + "',\n" +
                "declareDurable: " + prop.getProperty("outputDeclareDurable") + ",\n" +
                "declareExclusive: " + prop.getProperty("outputDeclareExclusive") + ",\n" +
                "declareAutoDelete: " + prop.getProperty("outputDeclareAutoDelete") + ",\n" +
                "exchange: '" + model.getExchangeName() + "',\n" +
                "collector: {class: '" + prop.getProperty("outputCollector") + "'}\n" +
            "}");
        System.out.println("Generated: " + result);
        return result.toString();
    }
}
