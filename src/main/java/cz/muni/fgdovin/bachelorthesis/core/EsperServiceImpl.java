package cz.muni.fgdovin.bachelorthesis.core;

import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.dataflow.EPDataFlowInstance;
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
    private static String eventName;
    private static EPStatement result;
    private static Map schema;

    @Override
    public long returnTime() {
        return esperRuntime.getCurrentTime();
    }

    @Override
    public void setQuery(String statement) {
        result = esperServiceProvider.getEPAdministrator().createEPL(statement);
        sourceFlow.start();
    }

    @Override
    public void removeQuery() {

    }

    @Override
    public AMQPQueue setAMQPSource(AMQPQueue source) {
        if (EsperServiceImpl.source == source){
            return null;
        }
        esperServiceProvider.getEPAdministrator().createEPL(source.toString());
        sourceFlow = esperServiceProvider.getEPRuntime().getDataFlowRuntime().instantiate("AMQPIncomingDataFlow");
        return source;
    }

    @Override
    public AMQPQueue setAMQPSink(AMQPQueue sink) {
        if (EsperServiceImpl.sink == sink){
            return null;
        }
        esperServiceProvider.getEPAdministrator().createEPL(sink.toString());
        esperServiceProvider.getEPRuntime().getDataFlowRuntime().instantiate("AMQPOutcomingDataFlow");
        return sink;
    }

    @Override
    public void setEventSchema(EventSchema input) {
        EsperServiceImpl.eventName = input.getEventName();
        esperServiceProvider.getEPAdministrator().createEPL("create schema " + eventName
                + "(" + input.getEventSchema()+ ")");
        EsperServiceImpl.schema = input.getEventSchemaAsMap();
    }

    @Override
    public void removeAMQPSource() {
    }

    @Override
    public void removeAMQPSink() {

    }
}
