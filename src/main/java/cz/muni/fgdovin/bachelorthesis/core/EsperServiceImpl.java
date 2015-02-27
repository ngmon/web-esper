package cz.muni.fgdovin.bachelorthesis.core;

import com.espertech.esper.client.*;
import com.espertech.esper.client.dataflow.EPDataFlowInstance;
import com.espertech.esper.client.dataflow.EPDataFlowRuntime;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Filip Gdovin on 9. 2. 2015.
 */
@Service
public class EsperServiceImpl implements EsperService {

    private static final Logger logger = Logger.getLogger(EsperServiceImpl.class);

    @Autowired
    EPServiceProvider esperServiceProvider;

    @Autowired
    EPRuntime esperRuntime;

    @Autowired
    EPDataFlowRuntime esperDataFlowRuntime;

    @Override
    public String addStatement(String queryName, String query) {
        EPStatement statement = null;
        try{
            statement = esperServiceProvider.getEPAdministrator().createEPL(query, queryName);
        }
        catch(EPException ex){
            logger.warn(ex);
            return null;
        }
        EPStatement savedQuery = esperServiceProvider.getEPAdministrator().getStatement(queryName);
        System.out.println("Query is: " + savedQuery.getText());
        savedQuery.start();
        return savedQuery.getText();
    }

    @Override
    public EPStatement removeStatement(String statName) {
        esperServiceProvider.getEPAdministrator().getStatement(statName).stop();
        return esperServiceProvider.getEPAdministrator().getStatement(statName);
    }

    @Override
    public EPDataFlowInstance addAMQPSource(String queueName, String inputQueue) {
        try{
            esperServiceProvider.getEPAdministrator().createEPL(inputQueue);
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
    public boolean removeAMQPSource(String dataFlowName) {
        EPDataFlowInstance sinkInstance = esperDataFlowRuntime.getSavedInstance(dataFlowName);
        if(sinkInstance == null){
            return false;
        }
        sinkInstance.cancel();
        return true;
    }
}
