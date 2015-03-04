package cz.muni.fgdovin.bachelorthesis.core;

import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EPStatementState;
import com.espertech.esper.client.dataflow.EPDataFlowInstance;
import com.espertech.esper.client.dataflow.EPDataFlowState;

import java.util.List;
import java.util.Map;

/**
 * Created by Filip Gdovin on 9. 2. 2015.
 */
public interface EsperService{
    public boolean setSchema(String eventName, Map<String, Object> schema);
    public boolean removeSchema(String eventName);
    public String showSchema(String eventName);
    public List<String> showSchemas();

    public EPDataFlowInstance addAMQPSource(String queueName, String queueProperties);
    public boolean removeAMQPSource(String queueName);
    public String showAMQPSource(String queueName);
    public List<String> showAMQPSources();

    public EPStatement addStatement(String statementName, String statement);
    public boolean removeStatement(String statementName);
    public List<String> showStatements();

}