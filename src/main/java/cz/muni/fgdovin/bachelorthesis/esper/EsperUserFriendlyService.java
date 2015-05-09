package cz.muni.fgdovin.bachelorthesis.esper;

import java.util.List;
import java.util.Map;

/**
 * @author Filip Gdovin
 * @version 26. 3. 2015
 */

/**
 * This interface is used by API to communicate with EsperService,
 * in particular to:
 * add/remove/show(by name)/show all dataflows
 * add/remove/show(by name)/show all event types
 *
 * Provides convenient and mostly String-arguments-based
 * methods and manages all possible exceptions thrown by
 * lower layers.
 *
 * @author Filip Gdovin
 */
public interface EsperUserFriendlyService {

    /**
     * Method used to add new event type to the Esper.
     * Event type MUST contain a name and properties in format of Map (see param2)
     *
     * @param eventName String describing name of event type, it must be known before declaring
     *                  any dataflow above stream of such events.
     * @param schema Map defining properties of the event type,
     *               so that Esper can understand values and their types.
     *               It is important those properties match actual event structure,
     *               otherwise it will be impossible to execute certain queries.
     *               (e.g. avg(val1) expects 'val1' to be Number)
     *               Also, for the purpose of this thesis, property named
     *               "@timestamp" with value of Long is expected and MUST be provided.
     * @return Returns whether the event type was successfully added to the Esper.
     */
    public boolean addEventType(String eventName, Map<String, Object> schema);

    /**
     * Method used to remove event type by providing its name.
     *
     * @param eventName String describing event type name.
     * @return True if event event type with provided name was deleted,
     * false if Esper doesn't contain event type with such name,
     * or if event type was found, but wasn't deleted as there are
     * running dataflows using this event type. Remove those first.
     */
    public boolean removeEventType(String eventName);

    /**
     * Method used to show event type by providing its name.
     *
     * @param eventName String describing event type name.
     * @return String containing event type, or null
     * if there is no event type with provided name present.
     */
    public String showEventType(String eventName);

    /**
     * Method used to show all event type names known to Esper.
     *
     * @return List of all present event type names,
     * or null if there are no event types present.
     */
    public List<String> showEventTypeNames();

    /**
     * Method used to show all event types known to Esper.
     *
     * @return List of all present event types in format
     * 'eventTypeName:eventProperties',
     * or null if there are no event types present.
     */
    public List<String> showEventTypes();

    /**
     * Method used to add new dataflow to the Esper.
     * Dataflow MUST contain a name and properties in String format (see param2)
     *
     * Dataflows are used for two purposes:
     *
     * 1.: To define source for event streams, such dataflow contains only:
     *     name of said dataflow,
     *     name of event type to understand incoming events (MUST be defined before using addEventType() ),
     *     name of AMQP queue containing events of said event type.
     *
     * This type of dataflow converts incoming events to Map and pushes them into EventBusSink.
     *
     *
     * 2.: To define sink for event streams, such dataflow contains:
     *     name of said dataflow,
     *     name of event type to understand events incoming from EventBusSource (MUST be defined before using addEventType() ),
     *     statement operating above incoming events, its result will be sent to AMQP Sink
     *     name of output AMQP queue to send result of query to.
     *
     * This type of dataflow executes continuous query on events and sends result of query
     * to the given AMQP Sink, which is defined with queue name.
     * In case AMQP queue with given name is not defined,
     * it will be created and properly bound to exchange.
     *
     * @param dataflowName String describing name of dataflow.
     * @param dataflowProperties String defining properties of the dataflow (see dataflow types above),
     *                        for convenience there is a support class (DataflowHelper), which takes dataflow
     *                        parameters and creates String defining the whole dataflow, adding static
     *                        parameters such as host, port, etc. Those properties, however, can be changed
     *                        in properties file 'config.properties', located under 'resources'.
     * @return Returns true if dataflow was successfully created,
     *         or false otherwise.
     */
    public boolean addDataflow(String dataflowName, String dataflowProperties);

    /**
     * Method used to remove input dataflow by providing its name.
     *
     * @param dataflowName String describing input dataflow name.
     * @return True if input dataflow with provided name was deleted,
     * false if Esper doesn't contain input dataflow with such name.
     *
     */
    public boolean removeInputDataflow(String dataflowName);

    /**
     * Method used to remove output dataflow by providing its name.
     *
     * @param dataflowName String describing output dataflow name.
     * @return True if output dataflow with provided name was deleted,
     * false if Esper doesn't contain output dataflow with such name.
     *
     */
    public boolean removeOutputDataflow(String dataflowName);

    /**
     * Method used to show dataflow details by providing its name.
     *
     * @param dataflowName String describing dataflow name.
     * @return String containing dataflow details, or null
     * if there is no dataflow with provided name present
     * (never created or already removed).
     */
    public String showDataflow(String dataflowName);

    /**
     * Method used to show all input dataflows known to Esper.
     *
     * @return List of all present input dataflows in format
     * 'dataflowName:dataflowParameters',
     * or null if there are no input dataflows present.
     */
    public List<String> showInputDataflows();

    /**
     * Method used to show all output dataflows known to Esper.
     *
     * @return List of all present output dataflows in format
     * 'dataflowName:dataflowParameters',
     * or null if there are no output dataflows present.
     */
    public List<String> showOutputDataflows();
}