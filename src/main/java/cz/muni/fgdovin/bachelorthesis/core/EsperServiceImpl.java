package cz.muni.fgdovin.bachelorthesis.core;

import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.dataflow.EPDataFlowInstance;
import com.espertech.esper.client.dataflow.EPDataFlowRuntime;

import cz.muni.fgdovin.bachelorthesis.support.AMQPQueue;
import cz.muni.fgdovin.bachelorthesis.support.EventSchema;

import cz.muni.fgdovin.bachelorthesis.support.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Filip Gdovin on 9. 2. 2015.
 */
@Service
public class EsperServiceImpl implements EsperService {

    @Autowired
    EPServiceProvider esperServiceProvider;

    @Autowired
    EPRuntime esperRuntime;

    @Autowired
    EPDataFlowRuntime esperDataFlowRuntime;

    private static String eventName;

    @Override
    public long returnTime() {
        return esperRuntime.getCurrentTime();
    }

    @Override
    public void setQuery(Statement statement) {
        EPStatement query = esperServiceProvider.getEPAdministrator().createEPL(statement.getName(), statement.getQuery());
        query.start();
    }

    @Override
    public void removeQuery(String statName) {
        esperServiceProvider.getEPAdministrator().getStatement(statName).stop();
    }

    @Override
    public AMQPQueue setAMQPSource(AMQPQueue source) {
        esperServiceProvider.getEPAdministrator().createEPL(source.toInputString());
        EPDataFlowInstance sourceInstance = esperDataFlowRuntime.instantiate(source.getName());
        sourceInstance.start();
        return source;
    }

    @Override
    public void removeAMQPSource(String dataFlowName) {
        esperDataFlowRuntime.getSavedInstance(dataFlowName).cancel();
    }

    @Override
    public AMQPQueue setAMQPSink(AMQPQueue sink) {
        esperServiceProvider.getEPAdministrator().createEPL(sink.toOutputString());
        EPDataFlowInstance sourceInstance = esperDataFlowRuntime.instantiate(sink.getName());
        sourceInstance.start();
        return sink;
    }

    @Override
    public void removeAMQPSink(String dataFlowName) {
        esperDataFlowRuntime.getSavedInstance(dataFlowName).cancel();
    }

    @Override
    public void setEventSchema(EventSchema input) {
        EsperServiceImpl.eventName = input.getEventName();
        esperServiceProvider.getEPAdministrator().createEPL("create schema " + eventName
                + "(" + input.getEventSchema()+ ")");
    }
}
