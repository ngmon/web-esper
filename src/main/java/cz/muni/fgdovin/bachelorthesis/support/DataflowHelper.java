package cz.muni.fgdovin.bachelorthesis.support;

import cz.muni.fgdovin.bachelorthesis.web.InputDataflowModel;
import cz.muni.fgdovin.bachelorthesis.web.OutputDataflowModel;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

/**
 * @author Filip Gdovin
 * @version 10. 2. 2015
 */
public class DataflowHelper {

    @Resource
    private Environment environment;

    public String generateInputDataflow(InputDataflowModel model) {
        return
        "Create Dataflow " + model.getDataflowName() + "\n" +
        "AMQPSource -> instream<" + model.getEventType() + "> {\n" +
        "host: '" + this.environment.getProperty("inputHost") + "',\n" +
        "port: " + Integer.parseInt(this.environment.getProperty("inputPort")) + ",\n" +
        "queueName: '" + model.getQueueName() + "',\n" +
        "declareDurable: " + this.environment.getProperty("inputDeclareDurable") + ",\n" +
        "declareExclusive: " + this.environment.getProperty("inputDeclareExclusive") + ",\n" +
        "declareAutoDelete: " + this.environment.getProperty("inputDeclareAutoDelete") + ",\n" +
        "collector: {class: '" + this.environment.getProperty("inputCollector") + "'}\n" +
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
                "CustomAMQPSink(" + model.getOutputEventType() + ") {\n" +
                "host: '" + this.environment.getProperty("outputHost") + "',\n" +
                "port: " + Integer.parseInt(this.environment.getProperty("outputPort")) + ",\n" +
                "queueName: '" + model.getQueueName() + "',\n" +
                "declareDurable: " + this.environment.getProperty("outputDeclareDurable") + ",\n" +
                "declareExclusive: " + this.environment.getProperty("outputDeclareExclusive") + ",\n" +
                "declareAutoDelete: " + this.environment.getProperty("outputDeclareAutoDelete") + ",\n" +
                "exchange: '" + model.getExchangeName() + "',\n" +
                "collector: {class: '" + this.environment.getProperty("outputCollector") + "'}\n" +
                "}");
        return result.toString();
    }
}
