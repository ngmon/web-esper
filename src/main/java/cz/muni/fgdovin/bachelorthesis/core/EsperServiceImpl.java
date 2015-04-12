package cz.muni.fgdovin.bachelorthesis.core;

import com.espertech.esper.client.*;
import com.espertech.esper.client.dataflow.EPDataFlowInstance;
import com.espertech.esper.client.dataflow.EPDataFlowInstantiationException;
import com.espertech.esper.client.dataflow.EPDataFlowRuntime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * This class implements basic control of dataflows and event schemas,
 * using AMQP IO adapter.
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
    public void addEventType(String eventName, Map<String, Object> schema) throws ConfigurationException {
        this.configurationOperations.addEventType(eventName, schema);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeEventType(String eventName) throws ConfigurationException, NullPointerException {
        boolean result;
        if(this.showEventType(eventName) == null) {
            throw new NullPointerException("Event type with this name is not present!");
        }
        //for debugging purposes, because event types are deleted even if there are dataflows relying :(
        logger.info("Statements using " + eventName + ": " + configurationOperations.getEventTypeNameUsedBy(eventName));

        try {
            result = this.configurationOperations.removeEventType(eventName, false);
        } catch (ConfigurationException ex) {
            throw new ConfigurationException("Trying to remove event type with active statements", ex);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EventType showEventType(String eventName) throws NullPointerException{
        return this.configurationOperations.getEventType(eventName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EventType[] showEventTypes() {
        return this.configurationOperations.getEventTypes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDataflow(String dataflowName, String dataflowDetails) throws NullPointerException, EPException, IllegalStateException {
        EPDataFlowInstance currentInstance = this.esperDataFlowRuntime.getSavedInstance(dataflowName);
        EPStatement currentStatement = this.esperAdministrator.getStatement(dataflowName);
        if((currentInstance != null) || (currentStatement != null)){
            throw new NullPointerException("Dataflow with this name is already defined!");
        }
        try {
            currentStatement = this.esperAdministrator.createEPL(dataflowDetails, dataflowName);
            currentInstance = this.esperDataFlowRuntime.instantiate(dataflowName);
        } catch (EPDataFlowInstantiationException ex) {
            if(logger.isDebugEnabled()) {
                logger.debug("Dataflow creation aborted! Dataflow instance instantiation failed, destroying EPL statement!");
            }
            currentStatement.destroy();
            throw new EPDataFlowInstantiationException("Dataflow creation aborted! Dataflow instance instantiation failed!", ex);
        }
        currentInstance.start();
        this.esperDataFlowRuntime.saveInstance(dataflowName, currentInstance); //only save if it started successfully
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeDataflow(String queueName) throws NullPointerException {
        boolean result;
        EPDataFlowInstance sourceInstance = this.esperDataFlowRuntime.getSavedInstance(queueName);
        if(sourceInstance == null) {
            throw new NullPointerException("No dataflow instance with given name found.");
        }
        EPStatement creatingStatement = this.esperAdministrator.getStatement(queueName);
        sourceInstance.cancel();
        result = this.esperDataFlowRuntime.removeSavedInstance(queueName);

        if (creatingStatement != null) {
            creatingStatement.destroy();
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EPStatement showDataflow(String queueName) throws NullPointerException {
        return this.esperAdministrator.getStatement(queueName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] showDataflows(){
        return this.esperDataFlowRuntime.getSavedInstances();
    }
}