package cz.muni.fgdovin.bachelorthesis.esper;

import com.espertech.esper.client.ConfigurationException;
import com.espertech.esper.client.EPException;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventType;

import java.util.Map;

/**
 * This interface is used to communicate with Esper,
 * in particular to:
 * add/remove/show(by name)/show all dataflows
 * add/remove/show(by name)/show all event types
 *
 * @author Filip Gdovin
 */

public interface EsperService{

    /**
     * Method used to add new event type to the Esper.
     * Event type MUST contain a name and properties in format of Map (see param2)
     *
     * @param eventName String describing name of event type, it must be known before declaring
     *                  any dataflow operating on stream of such events.
     * @param schema Map defining properties of the event type,
     *               so that Esper can understand values and their types.
     *               It is important those properties match actual event structure,
     *               otherwise it will be impossible to execute certain queries.
     *               (e.g. avg(val1) expects 'val1' to be Number)
     *               Also, for the purpose of this thesis, property
     *               carrying timestamp with value of Long is expected and MUST be provided.
     * @return true if the event type was successfully added, false otherwise (e.g. was already defined)
     */
    public boolean addEventType(String eventName, Map<String, Object> schema);

    /**
     * Method used to remove event type by providing its name.
     *
     * @param eventName String describing event type name.
     * @return true is event type was found and removed, false otherwise.
     */
    public boolean removeEventType(String eventName) throws ConfigurationException, NullPointerException;

    /**
     * Method used to show event type by providing its name.
     *
     * @param eventName String describing event type name.
     * @return EventType matching given event type name,
     * or null if there is no event type with provided name present.
     */
    public EventType showEventType(String eventName)throws NullPointerException;

    /**
     * Method used to show all event types known to Esper.
     *
     * @return array of all present event types.
     */
    public EventType[] showEventTypes();

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
     *                        for convenience there is a support class (DataflowModel), which takes dataflow
     *                        parameters and creates String defining the whole dataflow, adding static
     *                        parameters such as host, port, etc. Those properties, however, can be changed
     *                        in properties file 'config.properties', located under 'resources'.
     */
    public void addDataflow(String dataflowName, String dataflowProperties) throws NullPointerException, EPException, IllegalStateException;

    /**
     * Method used to remove dataflow by providing its name.
     *
     * @param dataflowName String describing dataflow name.
     * @return true if dataflow was found and removed, false otherwise.
     */
    public boolean removeDataflow(String dataflowName) throws NullPointerException;

    /**
     * Method used to show dataflow by providing its name.
     *
     * @param dataflowName String describing dataflow name.
     * @return EPStatement containing dataflow with given name,
     * or null if there is no dataflow with provided name present (never created or already removed).
     */
    public EPStatement showDataflow(String dataflowName) throws NullPointerException;

    /**
     * Method used to show all dataflows known to Esper.
     *
     * @return array of all present dataflows.
     */
    public String[] showDataflows();
}