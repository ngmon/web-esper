package cz.muni.fgdovin.bachelorthesis.core;

import com.espertech.esper.client.*;
import com.espertech.esper.client.dataflow.EPDataFlowInstance;
import com.espertech.esper.client.dataflow.EPDataFlowRuntime;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Filip Gdovin on 9. 2. 2015.
 */
@Service
public class EsperServiceImpl implements EsperService {

    private static final Logger logger = Logger.getLogger(EsperServiceImpl.class);

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

    @Override
    public boolean addSchema(String eventName, Map<String, Object> schema) {
        try{
            configurationOperations.addEventType(eventName, schema);
        }
        catch(ConfigurationException ex){
            logger.warn(ex);
            return false;
        }
        return true;
    }

    @Override
    public boolean removeSchema(String eventName) {
        boolean result;
        try{
            result = configurationOperations.removeEventType(eventName, false);
        }
        catch(ConfigurationException ex) {
            logger.warn(ex);
            return false;
        }
        return result;
    }

    @Override
    public String showSchema(String eventName) {
        EventType myEvent = configurationOperations.getEventType(eventName);
        if(myEvent == null) {
            return null;
        }
        return myEvent.getName() + ":" + myEvent.getPropertyNames();
    }

    @Override
    public List<String> showSchemas() {
        EventType[] allEvents = configurationOperations.getEventTypes();
        if((allEvents == null) || (allEvents.length == 0)) {
            return null;
        }
        List<String> result = new ArrayList<String>();
        for(int i = 0; i < allEvents.length; i++) {
            result.add(showSchema(allEvents[i].getName()));
        }
        return result;
    }

    @Override
    public EPDataFlowInstance addDataflow(String queueName, String queueProperties) {
        if(esperDataFlowRuntime.getSavedInstance(queueName) != null) {
            return null;
        }
        try{
            esperAdministrator.createEPL(queueProperties, queueName);
        }
        catch(EPException ex){
            logger.warn(ex);
            return null;
        }
        EPDataFlowInstance sourceInstance = esperDataFlowRuntime.instantiate(queueName);
        esperDataFlowRuntime.saveInstance(queueName, sourceInstance);

        sourceInstance.start();

        return sourceInstance;
    }

    @Override
    public boolean removeDataflow(String queueName) {
        EPDataFlowInstance sourceInstance = esperDataFlowRuntime.getSavedInstance(queueName);
        EPStatement creatingStatement = esperAdministrator.getStatement(queueName);

        if(sourceInstance != null){
            sourceInstance.cancel();
            esperDataFlowRuntime.removeSavedInstance(queueName);
        } else {
            return false;
        }

        if (creatingStatement == null) {
            return true;
        }
        creatingStatement.destroy();

        return true;
    }

    @Override
    public String showDataflow(String queueName){
        EPStatement myStatement = esperAdministrator.getStatement(queueName);
        if(myStatement == null) {
            return null;
        }
        return myStatement.getName() + "[" + myStatement.getState() + "]:\n\"" + myStatement.getText() + "\"\n";
    }


    @Override
    public List<String> showDataflows(){
        String[] allDataFlows = esperDataFlowRuntime.getSavedInstances();

        if((allDataFlows == null) || (allDataFlows.length == 0)) {
            return null;
        }

        //in case we would like to receive more detailed info sometimes
        List<String> result = new ArrayList<String>();
        for(int i = 0; i < allDataFlows.length; i++) {
            result.add(showDataflow(allDataFlows[i]));
        }
        return result;
    }
}