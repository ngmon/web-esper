package cz.muni.fgdovin.bachelorthesis.core;

import com.espertech.esper.client.*;
import com.espertech.esper.client.dataflow.EPDataFlowInstance;
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
    public void removeEventType(String eventName) throws ConfigurationException{
        this.configurationOperations.removeEventType(eventName, false);
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
    public void addDataflow(String queueName, String queueProperties) throws NullPointerException, EPException, IllegalStateException {
        if(this.esperDataFlowRuntime.getSavedInstance(queueName) != null) {
            throw new NullPointerException("Dataflow with this name is already defined!");
        }
        this.esperAdministrator.createEPL(queueProperties, queueName);
        EPDataFlowInstance sourceInstance = this.esperDataFlowRuntime.instantiate(queueName);
        sourceInstance.start();
        this.esperDataFlowRuntime.saveInstance(queueName, sourceInstance); //only save if it started successfully
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeDataflow(String queueName) throws NullPointerException {
        EPDataFlowInstance sourceInstance = this.esperDataFlowRuntime.getSavedInstance(queueName);
        EPStatement creatingStatement = this.esperAdministrator.getStatement(queueName);
        if(sourceInstance == null) {
            throw new NullPointerException("No dataflow instance with given name found.");
        }
        sourceInstance.cancel();
        this.esperDataFlowRuntime.removeSavedInstance(queueName);

        if (creatingStatement != null) {
            creatingStatement.destroy();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EPStatement showDataflow(String queueName){
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