package cz.muni.fgdovin.bachelorthesis.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.espertech.esper.client.ConfigurationException;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventType;
import com.espertech.esper.client.dataflow.EPDataFlowAlreadyExistsException;
import com.espertech.esper.client.dataflow.EPDataFlowInstantiationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * This class is used to control Esper,
 * manage exceptions and convert
 * user input to Esper-preferred
 * one and vice versa.
 *
 * @author Filip Gdovin
 * @version 9. 2. 2015
 */
@Service
public class EsperUserFriendlyServiceImpl implements EsperUserFriendlyService {

    private static final Logger logger = LogManager.getLogger("EsperUserFriendlyServiceImpl");

    @Autowired
    private EsperService esperService;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addEventType(String eventName, Map<String, Object> schema) {
        try {
            this.esperService.addEventType(eventName, schema);
        } catch (ConfigurationException ex) {
            if(logger.isDebugEnabled()) {
                logger.debug("Error while adding new event type!", ex);
            }
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeEventType(String eventName) {
        boolean result;
        try {
            result = this.esperService.removeEventType(eventName);
        } catch (ConfigurationException | NullPointerException ex) {
            if(logger.isDebugEnabled()) {
                logger.debug("Error while removing event type!", ex);
            }
            return false;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String showEventType(String eventName) {
        EventType myEvent;
        try {
            myEvent = this.esperService.showEventType(eventName);
        } catch (NullPointerException ex) {
            if(logger.isDebugEnabled()) {
                logger.debug("No event type with given name[" + eventName + "] was found.", ex);
            }
            return null;
        }
        return myEvent.getName() + ":" + Arrays.toString(myEvent.getPropertyNames());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> showEventTypes() {
        EventType[] allEvents = this.esperService.showEventTypes();
        if((allEvents == null) || (allEvents.length == 0)) {
            return null;
        }
        List<String> result = new ArrayList<>();
        for (EventType allEvent : allEvents) {
            result.add(showEventType(allEvent.getName()));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addDataflow(String dataflowName, String dataflowProperties) {
        try {
            this.esperService.addDataflow(dataflowName, dataflowProperties);
        } catch (NullPointerException ex) {
            if(logger.isDebugEnabled()) {
                logger.debug("Failed to create dataflow!", ex);
            }
            return false;
        } catch (EPDataFlowInstantiationException ex) {
            if(logger.isDebugEnabled()) {
                logger.debug("Failed to instantiate new dataflow!", ex);
            }
            return false;
        } catch (EPDataFlowAlreadyExistsException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("Failed to save new dataflow instance, which causes it to be unreachable by its name!", ex);
            }
            return false;
        } catch (IllegalStateException ex) {
            if(logger.isDebugEnabled()) {
                logger.debug("Failed to start new dataflow!", ex);
            }
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeInputDataflow(String dataflowName) {
        String dataflowInfo = this.showDataflow(dataflowName);
        if(dataflowInfo == null) {
            if(logger.isDebugEnabled()) {
                logger.debug("No dataflow with given name present: " + dataflowName);
            }
            return false;
        }
        if(isInputDataflow(dataflowInfo)) { //its an input dataflow
            return this.removeDataflow(dataflowName);
        } else {
            if(logger.isDebugEnabled()) {
                logger.debug("Dataflow with given name is not an input one: " + dataflowName);
            }
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeOutputDataflow(String dataflowName) {
        String dataflowInfo = this.showDataflow(dataflowName);
        if(dataflowInfo == null) {
            if(logger.isDebugEnabled()) {
                logger.debug("No dataflow with given name present: " + dataflowName);
            }
            return false;
        }
        if(!(isInputDataflow(dataflowInfo))) { //its NOT an input dataflow
            return this.removeDataflow(dataflowName);
        } else {
            if(logger.isDebugEnabled()) {
                logger.debug("Dataflow with given name is not an output one: " + dataflowName);
            }
            return false;
        }
    }

    /**
     * Method used to remove any dataflow by providing its name (used by other methods).
     *
     * @param dataflowName String describing dataflow name.
     * @return True if dataflow with provided name was deleted,
     * false if Esper doesn't contain dataflow with such name.
     *
     */
    private boolean removeDataflow(String dataflowName) {
        String dataflowInfo = this.showDataflow(dataflowName);
        if(dataflowInfo == null) {
            if(logger.isDebugEnabled()) {
                logger.debug("No dataflow with given name present: " + dataflowName);
            }
            return false;
        }
        boolean result;
        try {
            result = this.esperService.removeDataflow(dataflowName);
        } catch (NullPointerException ex) {
            if(logger.isDebugEnabled()) {
                logger.debug("Failed to remove dataflow " + dataflowName + ".", ex);
            }
            return false;
        }
        return result;
    }

    /**
     * Method used to show dataflow by providing its name (used by other methods).
     *
     * @param dataflowName String describing dataflow name.
     * @return String containing dataflow in format 'dataflowDetails[state]:dataflowParameters',
     * or null if there is no dataflow with provided name present (never created or already removed).
     */
    private String showDataflow(String dataflowName) {
        EPStatement myDataflow = null;
        try {
            myDataflow = this.esperService.showDataflow(dataflowName);
        } catch (NullPointerException ex) {
            if(logger.isDebugEnabled()) {
                logger.debug("Failed to find dataflow!", ex);

            }
            return null;
        }
        if(myDataflow == null) {
            if(logger.isDebugEnabled()) {
                logger.debug("Failed to find dataflow! NULL");

            }
            return null;
        }
        return myDataflow.getName() + "[" + myDataflow.getState() + "]:\n\"" + myDataflow.getText() + "\"\n";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> showInputDataflows() {
        String[] allDataFlows = this.esperService.showDataflows();
        List<String> result = new ArrayList<>();

        if((allDataFlows == null) || (allDataFlows.length == 0)) {
            if(logger.isDebugEnabled()) {
                logger.debug("No input dataflows present.");
            }
            return result;
        }
        for (String oneDataFlow : allDataFlows) {
            String oneDataflowDetails = showDataflow(oneDataFlow);
            if(isInputDataflow(oneDataflowDetails)) {  //only input dataflows, NO output ones
                result.add(oneDataflowDetails);
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> showOutputDataflows() {
        String[] allDataFlows = this.esperService.showDataflows();
        List<String> result = new ArrayList<>();

        if((allDataFlows == null) || (allDataFlows.length == 0)) {
            if(logger.isDebugEnabled()) {
                logger.debug("No output dataflows present.");
            }
            return result;
        }
        for (String oneDataFlow : allDataFlows) {
            String oneDataflowDetails = showDataflow(oneDataFlow);
            if(!isInputDataflow(oneDataflowDetails)) {  //only output dataflows, NO input ones
                result.add(oneDataflowDetails);
            }
        }
        return result;
    }

    /**
     * Method used to decide whether given dataflow is input dataflow (used by other methods).
     *
     * @param dataflowDetails String describing dataflow with its details.
     * @return true if given dataflow is input one (ends with "EventBusSink(instream) {}""),
     * or false otherwise.
     */
    private boolean isInputDataflow(String dataflowDetails) {
        return dataflowDetails.endsWith("EventBusSink(instream) {}\"\n");
    }
}