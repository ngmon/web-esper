package cz.muni.fgdovin.bachelorthesis.core;

import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EPStatementState;
import com.espertech.esper.client.dataflow.EPDataFlowInstance;
import com.espertech.esper.client.dataflow.EPDataFlowState;

import java.util.List;

/**
 * Created by Filip Gdovin on 9. 2. 2015.
 */
public interface EsperService{
    public EPDataFlowInstance addAMQPSource(String queueName, String queueProperties);
    public boolean removeAMQPSource(String queueName);
    public List<String> showAMQPSources();

    public EPStatement addStatement(String statementName, String statement);
    public boolean removeStatement(String statementName);
    public List<String> showStatements();

}