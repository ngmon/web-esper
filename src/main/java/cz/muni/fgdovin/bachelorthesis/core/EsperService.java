package cz.muni.fgdovin.bachelorthesis.core;

import com.espertech.esper.client.dataflow.EPDataFlowInstance;

import java.util.List;
import java.util.Map;

/**
 * This interface is used to communicate with Esper, in particular to:
 * add/remove/show(by name)/show all dataflows
 * add/remove/show(by name)/show all schemas
 *
 * It is possible to have more than one implementation,
 * as there are more ways to configure Esper,
 * depending on used IO adapters and/or
 * purpose of Engine itself.
 *
 * @author Filip Gdovin
 */
public interface EsperService{

    /**
     * Method used to add new event to the Esper.
     * Event MUST contain a name and properties in format of Map (see param2)
     *
     * @param eventName String describing name of event, it must be known before declaring
     *                  any dataflow above stream of such events.
     * @param schema Map<String, Object> defining properties of the event,
     *               so that Esper can understand values and their types.
     *               It is important those properties match actual event structure,
     *               otherwise it will be impossible to execute certain queries.
     *               (e.g. avg(val1) expects 'val1' to be Number)
     *               Also, for the purpose of this thesis, property containing
     *               string "timestamp" with value of Long is expected and MUST be provided.
     * @return Returns whether the schema was successfully added to the Esper.
     */
    public boolean addSchema(String eventName, Map<String, Object> schema);

    /**
     * Method used to remove schema by providing its name.
     *
     * @param eventName String describing event name.
     * @return True if event schema with provided name was deleted,
     * false if Esper doesn't contain schema with such name,
     * or if schema was found, but wasn't deleted as there are
     * running dataflows using this schema. Remove those first.
     */
    public boolean removeSchema(String eventName);

    /**
     * Method used to show schema by providing its name.
     *
     * @param eventName String describing event name.
     * @return String containing schema in format 'eventName:eventProperties',
     * or null if there is no schema with provided name present.
     */
    public String showSchema(String eventName);

    /**
     * Method used to show all schemas known to Esper.
     *
     * @return List of all present schemas in format 'eventName:eventProperties',
     * or null if there are no schemas present.
     */
    public List<String> showSchemas();

    /**
     * Method used to add new dataflow to the Esper.
     * Dataflow MUST contain a name and properties in String format (see param2)
     *
     * Dataflows are used for two purposes:
     *
     * 1.: To define source for event streams, such dataflow contains only:
     *     name of said dataflow,
     *     name of schema to understand incoming events (MUST be defined before using addSchema() ),
     *     name of AMQP queue containing events of said schema,
     *     name of AMQP exchange distributing events to said queue.
     *
     * This type of dataflow converts incoming events to Map and pushes them into EventBusSink.
     *
     *
     * 2.: To define sink for event streams, such dataflow contains:
     *     name of said dataflow,
     *     name of schema to understand events incoming from EventBusSource (MUST be defined before using addSchema() ),
     *     statement operating above incoming events, its result will be sent to AMQP Sink
     *     name of output AMQP queue to send result of query to,
     *     name of output AMQP exchange.
     *
     * This type of dataflow executes continuous query on events and sends result of query
     * to the given AMQP Sink, which is defined as queue + exchange.
     * As the application relies on fact output AMQP queue and exchange
     * are defined outside this application, submitting non-existing
     * AMQP queue will cause NullPointerException.
     *
     *
     * @param queueName String describing name of dataflow.
     * @param queueProperties String defining properties of the dataflow (see dataflow types above),
     *                        for convenience there is a support class (EPLHelper), which takes dataflow
     *                        parameters and creates String defining the whole dataflow, adding static
     *                        parameters such as host, port, etc. Those properties, however, can be changed
     *                        in properties file 'config.properties', located under 'resources'.
     * @return Returns created EPDataflowInstance if dataflow was successfully created,
     *         or null otherwise.
     */
    public EPDataFlowInstance addDataflow(String queueName, String queueProperties);

    /**
     * Method used to remove dataflow by providing its name.
     *
     * @param queueName String describing dataflow name.
     * @return True if dataflow with provided name was deleted,
     * false if Esper doesn't contain dataflow with such name.
     *
     */
    public boolean removeDataflow(String queueName);

    /**
     * Method used to show dataflow by providing its name.
     *
     * @param queueName String describing dataflow name.
     * @return String containing dataflow in format 'dataflowName[state]:dataflowParameters',
     * (state can be only 'RUNNING', as there was no need to implement option to change
     * state without deletion of such dataflow. however, creation of such method would be trivial.)
     * or null if there is no dataflow with provided name present (never created or already removed).
     */
    public String showDataflow(String queueName);

    /**
     * Method used to show all dataflows known to Esper.
     *
     * @return List of all present(state='RUNNING') dataflows in format 'dataflowName[state]:dataflowParameters',
     * or null if there are no dataflows present.
     */
    public List<String> showDataflows();
}