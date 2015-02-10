package cz.muni.fgdovin.bachelorthesis.core;

import com.espertech.esper.client.EPOnDemandQueryResult;

import com.espertech.esper.client.EPStatement;
import cz.muni.fgdovin.bachelorthesis.support.AMQPQueue;

import java.util.Map;

/**
 * Created by Filip Gdovin on 9. 2. 2015.
 */
public interface EsperService{
    public long returnTime();
    public EPStatement executeQuery(String statement);
    public void setAMQPSource(AMQPQueue source);
    public void setAMQPSink(AMQPQueue sink);
    public void setEventSchema(String eventName, Map schema);
    public void removeAMQPSource(AMQPQueue source);
    public void removeAMQPSink(AMQPQueue sink);
}