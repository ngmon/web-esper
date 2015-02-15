package cz.muni.fgdovin.bachelorthesis.core;

import com.espertech.esper.client.EPOnDemandQueryResult;

import com.espertech.esper.client.EPStatement;
import cz.muni.fgdovin.bachelorthesis.support.AMQPQueue;
import cz.muni.fgdovin.bachelorthesis.support.EventSchema;

import java.util.Map;

/**
 * Created by Filip Gdovin on 9. 2. 2015.
 */
public interface EsperService{
    public long returnTime();
    public void setQuery(String statement);
    public void removeQuery();
    public AMQPQueue setAMQPSource(AMQPQueue source);
    public AMQPQueue setAMQPSink(AMQPQueue sink);
    public void setEventSchema(EventSchema schema);
    public void removeAMQPSource();
    public void removeAMQPSink();
}