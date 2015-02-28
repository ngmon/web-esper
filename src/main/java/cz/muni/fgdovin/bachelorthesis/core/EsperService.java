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
    public EPDataFlowInstance addAMQPSource(String queryName, String query);
    public EPDataFlowState removeAMQPSource(String queueName);
    public List<String> showAMQPSources();

    public EPStatement addStatement(String statementName, String query);
    public EPStatementState removeStatement(String statementName);
    public List showStatements();

}