package cz.muni.fgdovin.bachelorthesis.support;

import cz.muni.fgdovin.bachelorthesis.web.InputDataflowModel;
import cz.muni.fgdovin.bachelorthesis.web.OutputDataflowModel;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

/**
 * Created by Filip Gdovin on 10. 2. 2015.
 */
public class DataflowHelper {

    @Resource
    private Environment environment;

    public String generateInputDataflow(InputDataflowModel model) {
        return
        "Create Dataflow " + model.getDataflowName() + "\n" +
        "AMQPSource -> instream<" + model.getEventType() + "> {\n" +
        "host: '" + environment.getProperty("inputHost") + "',\n" +
        "port: " + Integer.parseInt(environment.getProperty("inputPort")) + ",\n" +
        "queueName: '" + model.getQueueName() + "',\n" +
        "declareDurable: " + environment.getProperty("inputDeclareDurable") + ",\n" +
        "declareExclusive: " + environment.getProperty("inputDeclareExclusive") + ",\n" +
        "declareAutoDelete: " + environment.getProperty("inputDeclareAutoDelete") + ",\n" +
        "exchange: '" + model.getExchangeName() +"',\n" +
        "collector: {class: '" + environment.getProperty("inputCollector") + "'}\n" +
        "}" +
        "EventBusSink(instream) {}";
    }

    public String generateOutputDataflow(OutputDataflowModel model) {
        StringBuilder result = new StringBuilder("Create dataflow " + model.getDataflowName() + "\n");
        StringBuilder selector = new StringBuilder("Select(");

        model.getAllEventTypes().stream().filter(eventType -> (eventType != null)
                && (eventType != "null")).forEach(eventType -> {
            result.append("EventBusSource -> " + eventType + "<" + eventType + "> {}\n");
            selector.append(eventType + ", ");
        });
        int lastComma = selector.lastIndexOf(", ");
        result.append(selector.substring(0, lastComma));
        result.append(
                ") -> " + model.getOutputEventType() + " {\n" +
                "select: (\n" + model.getQuery() + ")\n" +
                "}\n" +
                "AMQPSink(" + model.getOutputEventType() + ") {\n" +
                "host: '" + environment.getProperty("outputHost") + "',\n" +
                "port: " + Integer.parseInt(environment.getProperty("outputPort")) + ",\n" +
                "queueName: '" + model.getQueueName() + "',\n" +
                "declareDurable: " + environment.getProperty("outputDeclareDurable") + ",\n" +
                "declareExclusive: " + environment.getProperty("outputDeclareExclusive") + ",\n" +
                "declareAutoDelete: " + environment.getProperty("outputDeclareAutoDelete") + ",\n" +
                "exchange: '" + model.getExchangeName() + "',\n" +
                "collector: {class: '" + environment.getProperty("outputCollector") + "'}\n" +
            "}");
        return result.toString();
    }
}
