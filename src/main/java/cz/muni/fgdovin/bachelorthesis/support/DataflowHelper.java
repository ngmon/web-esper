package cz.muni.fgdovin.bachelorthesis.support;

import cz.muni.fgdovin.bachelorthesis.web.InputDataflowModel;
import cz.muni.fgdovin.bachelorthesis.web.OutputDataflowModel;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

/**
 * Support class for easy-to-use conversion of Input and OutputDataflowModel
 * instances to String formatted dataflow information required by Esper.
 * Config file is used to load other required data concerning AMQp queues.
 *
 * @author Filip Gdovin
 * @version 10. 2. 2015
 */
public class DataflowHelper {

    @Resource
    private Environment environment;

    /**
     * This method is used to create String for input dataflow from
     * InputDataflowModel.
     *
     * @param model InputDataflowModel to convert.
     * @return String containing Esper-friendly dataflow configuration.
     */
    public String generateInputDataflow(InputDataflowModel model) {
        return
        "Create Dataflow " + model.getDataflowName() + "\n" +
        "AMQPSource -> instream<" + model.getEventType() + "> {\n" +
        "host: '" + this.environment.getProperty("host") + "',\n" +
        "port: " + Integer.parseInt(this.environment.getProperty("port")) + ",\n" +
        "queueName: '" + model.getQueueName() + "',\n" +
        "declareDurable: " + this.environment.getProperty("inputDeclareDurable") + ",\n" +
        "declareExclusive: " + this.environment.getProperty("inputDeclareExclusive") + ",\n" +
        "declareAutoDelete: " + this.environment.getProperty("inputDeclareAutoDelete") + ",\n" +
        "collector: {class: '" + this.environment.getProperty("inputCollector") + "'}\n" +
        "}" +
        "EventBusSink(instream) {}";
    }

    /**
     * This method is used to create String for output dataflow from
     * OutputDataflowModel.
     *
     * @param model OutputDataflowModel to convert.
     * @return String containing Esper-friendly dataflow configuration.
     */
    public String generateOutputDataflow(OutputDataflowModel model) {
        StringBuilder result = new StringBuilder("Create dataflow " + model.getDataflowName() + "\n");
        StringBuilder selector = new StringBuilder("Select(");

        model.getAllEventTypes().stream().filter(eventType -> (eventType != null)
                ).forEach(eventType -> {
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
                "host: '" + this.environment.getProperty("host") + "',\n" +
                "port: " + Integer.parseInt(this.environment.getProperty("port")) + ",\n" +
                "queueName: '" + model.getQueueName() + "',\n" +
                "declareDurable: " + this.environment.getProperty("outputDeclareDurable") + ",\n" +
                "declareExclusive: " + this.environment.getProperty("outputDeclareExclusive") + ",\n" +
                "declareAutoDelete: " + this.environment.getProperty("outputDeclareAutoDelete") + ",\n" +
                "exchange: '" + this.environment.getProperty("outputExchangeName") + "',\n" +
                "collector: {class: '" + this.environment.getProperty("outputCollector") + "'}\n" +
                "}");
        return result.toString();
    }
}
