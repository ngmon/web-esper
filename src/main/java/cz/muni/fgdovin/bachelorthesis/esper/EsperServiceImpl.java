package cz.muni.fgdovin.bachelorthesis.esper;

import com.espertech.esper.client.*;
import com.espertech.esper.client.dataflow.EPDataFlowAlreadyExistsException;
import com.espertech.esper.client.dataflow.EPDataFlowInstance;
import com.espertech.esper.client.dataflow.EPDataFlowInstantiationException;
import com.espertech.esper.client.dataflow.EPDataFlowRuntime;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * {@inheritDoc}
 *
 * @author Filip Gdovin
 * @version 9. 2. 2015
 */

@Service
public class EsperServiceImpl implements EsperService {

    private static final Logger logger = LogManager.getLogger("EsperServiceImpl");

    @Autowired
    EPServiceProvider esperServiceProvider;

    @Autowired
    EPAdministrator esperAdministrator;

    @Autowired
    EPRuntime esperRuntime;

    @Autowired
    EPDataFlowRuntime esperDataFlowRuntime;

    @Autowired
    ConfigurationOperations configurationOperations;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addEventType(String eventName, Map<String, Object> schema) {
        if ((eventName == null) || (eventName.isEmpty()) || (schema == null)
                || (this.showEventType(eventName) != null)) {
            return false;
        }
        
        try {
            this.configurationOperations.addEventType(eventName, schema);
        } catch (ConfigurationException ex) {
            logger.debug("Error while adding event type!");
            return false;
        }
        
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeEventType(String eventName) {
        if ((eventName == null) || (eventName.isEmpty())
                || (this.showEventType(eventName) == null)) {
            return false;
        }
        
        try {
            this.configurationOperations.removeEventType(eventName, false);
        } catch (ConfigurationException ex) {
            logger.debug("Error while removing event type!");
            return false;
        }
        
        return true;
    }
    
    @Override
    public String showEventType(String eventName) {
        EventType myEvent = this.configurationOperations.getEventType(eventName);
        if(myEvent == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("No event type with given name[" + eventName + "] was found.");
            }
            return null;
        }
        return myEvent.getName() + ":" + Arrays.toString(myEvent.getPropertyNames());
    }

    @Override
    public List<String> showEventTypeNames() {
        EventType[] allEvents = this.configurationOperations.getEventTypes();
        List<String> result = new ArrayList<>();

        if((allEvents == null) || (allEvents.length == 0)) {
            return result;
        }
        for (EventType allEvent : allEvents) {
            result.add(allEvent.getName());
        }
        return result;
    }

    @Override
    public List<String> showEventTypes() {
        EventType[] allEvents = this.configurationOperations.getEventTypes();
        List<String> result = new ArrayList<>();

        if((allEvents == null) || (allEvents.length == 0)) {
            return result;
        }
        for (EventType allEvent : allEvents) {
            result.add(this.showEventType(allEvent.getName()));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addDataflow(String dataflowName, String dataflowProperties) {
        try {
            EPDataFlowInstance currentInstance = this.esperDataFlowRuntime.getSavedInstance(dataflowName);
            EPStatement currentStatement = this.esperAdministrator.getStatement(dataflowName);
            if((currentInstance != null) || (currentStatement != null)){
                throw new NullPointerException("Dataflow with this name is already defined!");
            }
            try {
                currentStatement = this.esperAdministrator.createEPL(dataflowProperties, dataflowName);
                currentInstance = this.esperDataFlowRuntime.instantiate(dataflowName);
                currentInstance.start();
            } catch (EPDataFlowInstantiationException ex) {
                if(logger.isDebugEnabled()) {
                    logger.debug("Dataflow creation aborted! Dataflow instance instantiation failed, destroying EPL statement!");
                }
                assert currentStatement != null;
                currentStatement.destroy();
                throw new EPDataFlowInstantiationException("Dataflow creation aborted! Dataflow instance instantiation failed!", ex);
            }
            this.esperDataFlowRuntime.saveInstance(dataflowName, currentInstance); //only save if it started successfully

        } catch (NullPointerException ex) {
            logger.debug("Failed to create dataflow!", ex);
            return false;
        } catch (EPDataFlowInstantiationException ex) {
            logger.debug("Failed to instantiate new dataflow!", ex);
            return false;
        } catch (EPDataFlowAlreadyExistsException ex) {
            logger.debug("Failed to save new dataflow instance, which causes it to be unreachable by its name!", ex);
            return false;
        } catch (IllegalStateException ex) {
            logger.debug("Failed to start new dataflow!", ex);
            return false;
        }
        return true;
    }

    /**
     * Method used to remove dataflow by providing its name.
     *
     * @param dataflowName String describing dataflow name.
     * @return true if dataflow was found and removed, false otherwise.
     */
    public boolean removeDataflow(String dataflowName) {
        String dataflowInfo = showDataflow(dataflowName);
        if (dataflowInfo == null) {
            logger.debug("No dataflow with given name present: " + dataflowName);
            return false;
        }
        
        boolean result = false;
        try {
            EPDataFlowInstance sourceInstance = this.esperDataFlowRuntime.getSavedInstance(dataflowName);
            if(sourceInstance == null) {
                throw new NullPointerException("No dataflow instance with given name found.");
            }
            EPStatement creatingStatement = this.esperAdministrator.getStatement(dataflowName);
            sourceInstance.cancel();
            result = this.esperDataFlowRuntime.removeSavedInstance(dataflowName);

            if (creatingStatement != null) {
                creatingStatement.destroy();
            }
        } catch (NullPointerException ex) {
            logger.debug("Failed to remove dataflow " + dataflowName + ".", ex);
            return false;
        }
        
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeInputDataflow(String dataflowName) {
        String dataflowInfo = showDataflow(dataflowName);
        if (dataflowInfo == null) {
            logger.debug("No dataflow with given name present: " + dataflowName);
            return false;
        }
        if (isInputDataflow(dataflowInfo)) { //its an input dataflow
            return this.removeDataflow(dataflowName);
        } else {
            logger.debug("Dataflow with given name is not an input one: " + dataflowName);
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeOutputDataflow(String dataflowName) {
        String dataflowInfo = showDataflow(dataflowName);
        if (dataflowInfo == null) {
            logger.debug("No dataflow with given name present: " + dataflowName);
            return false;
        }
        if (!(isInputDataflow(dataflowInfo))) { //its NOT an input dataflow
            return this.removeDataflow(dataflowName);
        } else {
            logger.debug("Dataflow with given name is not an output one: " + dataflowName);
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String showDataflow(String dataflowName) {
        EPStatement myDataflow;
        try {
            myDataflow = this.esperAdministrator.getStatement(dataflowName);
        } catch (NullPointerException ex) {
            logger.debug("Failed to find dataflow!", ex);
            return null;
        }
        if (myDataflow == null) {
            logger.debug("Failed to find dataflow! NULL");
            return null;
        }
        String details = myDataflow.getText();

        BufferedReader reader = new BufferedReader(new StringReader(details));
        StringBuilder result = new StringBuilder("");
        String line;
        try {
            line = reader.readLine();
            while (line != null) {
                if (line.startsWith("queueName")) {
                    result.append(line.substring(0, line.length() - 1));
                }
                else if (line.startsWith("exchange")) {
                    result.append(line.substring(0, line.length() - 1));
                }
                else if (line.startsWith("AMQP")) {
                    result.append(line);
                }
                else if (line.startsWith("select")){
                    result.append(line);
                }
                else if (line.startsWith("}")) {
                    result.append("}");
                }
                else {
                    line = reader.readLine();
                    continue;
                }
                result.append("\n");
                line = reader.readLine();
            }
        } catch (IOException e) {
            return null;
        }
        
        return result.toString();
    }

    /**
     * {@inheritDoc}
     */
    public String[] showDataflows(){
        return this.esperDataFlowRuntime.getSavedInstances();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> showInputDataflows() {
        String[] allDataflows = showDataflows();
        List<String> result = new ArrayList<>();

        if((allDataflows == null) || (allDataflows.length == 0)) {
            if(logger.isDebugEnabled()) {
                logger.debug("No input dataflows present.");
            }
            return result;
        }
        for (String oneDataflow : allDataflows) {
            String oneDataflowDetails = showDataflow(oneDataflow);
            if(isInputDataflow(oneDataflowDetails)) {  //only input dataflows, NO output ones
                result.add(oneDataflow);
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> showOutputDataflows() {
        String[] allDataflows = showDataflows();
        List<String> result = new ArrayList<>();

        if((allDataflows == null) || (allDataflows.length == 0)) {
            if(logger.isDebugEnabled()) {
                logger.debug("No output dataflows present.");
            }
            return result;
        }
        for (String oneDataflow : allDataflows) {
            String oneDataflowDetails = showDataflow(oneDataflow);
            if(!isInputDataflow(oneDataflowDetails)) {  //only output dataflows, NO input ones
                result.add(oneDataflow);
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
        return dataflowDetails.contains("AMQPSource");
    }
}