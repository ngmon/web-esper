package cz.muni.fgdovin.bachelorthesis.core;

import com.espertech.esper.client.*;
import com.espertech.esper.client.dataflow.EPDataFlowInstance;
import com.espertech.esper.client.dataflow.EPDataFlowRuntime;

import com.espertech.esper.client.dataflow.EPDataFlowState;
import org.apache.commons.collections.ListUtils;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

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
    public EPDataFlowInstance addAMQPSource(String queueName, String inputQueue) {
        try{
            esperServiceProvider.getEPAdministrator().createEPL(inputQueue, queueName);
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
    public EPDataFlowState removeAMQPSource(String dataFlowName) {
        EPDataFlowInstance sourceInstance = esperDataFlowRuntime.getSavedInstance(dataFlowName);
        if(sourceInstance == null){
            return null;
        }
        sourceInstance.cancel();
        return sourceInstance.getState();
    }

    @Override
    public List<String> showAMQPSources(){
        return Arrays.asList(esperDataFlowRuntime.getSavedInstances());
    }

    @Override
    public EPStatement addStatement(String queryName, String query) {
        try{
            esperServiceProvider.getEPAdministrator().createEPL(query, queryName);
        }
        catch(EPException ex){
            logger.warn(ex);
            return null;
        }
        EPStatement savedQuery = esperServiceProvider.getEPAdministrator().getStatement(queryName);
        savedQuery.start();
        return savedQuery;
    }

    @Override
    public EPStatementState removeStatement(String statName) {
        EPStatement result = esperServiceProvider.getEPAdministrator().getStatement(statName);
        if(result == null){
            return null;
        }
        result.stop();
        result.destroy();
        return result.getState();
    }

    public List showStatements(){
        String[] allStats = esperServiceProvider.getEPAdministrator().getStatementNames();
        List diff = ListUtils.subtract(Arrays.asList(allStats), showAMQPSources()); //had to substract queues, as they are also statements
        System.out.println(diff);
        return diff;
    }


}
