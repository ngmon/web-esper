package cz.muni.fgdovin.bachelorthesis.core;

import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.dataflow.EPDataFlowInstance;
import com.espertech.esper.client.dataflow.EPDataFlowState;
import cz.muni.fgdovin.bachelorthesis.support.AMQPQueue;
import cz.muni.fgdovin.bachelorthesis.support.EventSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Filip Gdovin on 9. 2. 2015.
 */
@Service
public class EsperServiceImpl implements EsperService {

    @Autowired
    EPServiceProvider esperServiceProvider;

    @Autowired
    EPRuntime esperRuntime;

    private static AMQPQueue source;
    private static EPDataFlowInstance sourceFlow;
    private static AMQPQueue sink;
    private static EPDataFlowInstance sinkFlow;
    private static String eventName;
    private static EPStatement statement;
    private static Map schema;

    @Override
    public long returnTime() {
        return esperRuntime.getCurrentTime();
    }

    @Override
    public void setQuery(String statement) {
        if(sourceFlow == null){
            return;
        }
        EsperServiceImpl.statement = esperServiceProvider.getEPAdministrator().createEPL(statement);
        if(sourceFlow.getState()!= EPDataFlowState.RUNNING){
            sourceFlow.start();
        }
    }

    @Override
    public void removeQuery() {
        EsperServiceImpl.statement.stop();
    }

    @Override
    public AMQPQueue setAMQPSource(AMQPQueue source) {
        if ((EsperServiceImpl.source != null)
                && (EsperServiceImpl.source.toInputString().equalsIgnoreCase(source.toInputString()))){
            return null;
        }
        esperServiceProvider.getEPAdministrator().createEPL(source.toInputString());
        sourceFlow = esperServiceProvider.getEPRuntime().getDataFlowRuntime().instantiate("AMQPIncomingDataFlow");
        EsperServiceImpl.source = source;
        return source;
    }

    @Override
    public void removeAMQPSource() {
        EsperServiceImpl.sourceFlow.cancel();
        EsperServiceImpl.source = null;
    }

    @Override
    public AMQPQueue setAMQPSink(AMQPQueue sink) {
        if ((EsperServiceImpl.sink != null)
                && (EsperServiceImpl.sink.toOutputString().equalsIgnoreCase(sink.toOutputString()))){
            return null;
        }
        esperServiceProvider.getEPAdministrator().createEPL(sink.toOutputString());
        sinkFlow = esperServiceProvider.getEPRuntime().getDataFlowRuntime().instantiate("AMQPOutcomingDataFlow");
        EsperServiceImpl.sink = sink;
        return sink;
    }

    @Override
    public void removeAMQPSink() {
        EsperServiceImpl.sinkFlow.cancel();
        EsperServiceImpl.sink = null;
    }

    @Override
    public void setEventSchema(EventSchema input) {
        EsperServiceImpl.eventName = input.getEventName();
        esperServiceProvider.getEPAdministrator().createEPL("create schema " + eventName
                + "(" + input.getEventSchema()+ ")");
        EsperServiceImpl.schema = input.getEventSchemaAsMap();
    }
}
