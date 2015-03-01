package cz.muni.fgdovin.bachelorthesis.core;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPException;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EPStatementState;
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
    public EPDataFlowInstance addAMQPSource(String queueName, String queueProperties) {
        try{
            esperServiceProvider.getEPAdministrator().createEPL(queueProperties, queueName);
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
    public boolean removeAMQPSource(String queueName) {
        EPDataFlowInstance sourceInstance = esperDataFlowRuntime.getSavedInstance(queueName);
        EPStatement creatingStatement = esperServiceProvider.getEPAdministrator().getStatement(queueName);

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
    public List<String> showAMQPSources(){
        return Arrays.asList(esperDataFlowRuntime.getSavedInstances());
    }

    @Override
    public EPStatement addStatement(String statementName, String statement) {
        try{
            esperServiceProvider.getEPAdministrator().createEPL(statement, statementName);
        }
        catch(EPException ex){
            logger.warn(ex);
            return null;
        }
        EPStatement savedQuery = esperServiceProvider.getEPAdministrator().getStatement(statementName);
        savedQuery.start();
        return savedQuery;
    }

    @Override
    public boolean removeStatement(String statementName) {
        EPStatement result = esperServiceProvider.getEPAdministrator().getStatement(statementName);
        if(result != null){
            result.destroy();
            return  true;
        }
        return false;
    }

    public List<String> showStatements(){
        String[] allStats = esperServiceProvider.getEPAdministrator().getStatementNames();
        List diff = ListUtils.subtract(Arrays.asList(allStats), showAMQPSources()); //had to substract queues, as they are also statements
        return diff;
    }


}
