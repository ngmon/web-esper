package cz.muni.fgdovin.bachelorthesis.core;

import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.dataflow.EPDataFlowInstance;

/**
 * Created by Filip Gdovin on 9. 2. 2015.
 */
public interface EsperService{
    public String addStatement(String statementName, String query);
    public EPDataFlowInstance addAMQPSource(String queryName, String query);

    public EPStatement removeStatement(String statementName);
    public boolean removeAMQPSource(String queueName);
}