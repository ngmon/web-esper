package cz.muni.fgdovin.bachelorthesis.core;

import com.espertech.esper.client.dataflow.EPDataFlowInstance;

import java.util.List;
import java.util.Map;

/**
 * Created by Filip Gdovin on 9. 2. 2015.
 */
public interface EsperService{
    public boolean addSchema(String eventName, Map<String, Object> schema);
    public boolean removeSchema(String eventName);
    public String showSchema(String eventName);
    public List<String> showSchemas();

    public EPDataFlowInstance addDataflow(String queueName, String queueProperties);
    public boolean removeDataflow(String queueName);
    public String showDataflow(String queueName);
    public List<String> showDataflows();
}