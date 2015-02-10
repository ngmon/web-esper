package cz.muni.fgdovin.bachelorthesis.core;

import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.dataflow.EPDataFlowInstance;
import cz.muni.fgdovin.bachelorthesis.support.AMQPQueue;
import cz.muni.fgdovin.bachelorthesis.support.CreateEventSchema;
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
    public EPStatement executeQuery(String statement) {
        result = esperServiceProvider.getEPAdministrator().createEPL(statement);
        sourceFlow.start();
        return result;
    }

    @Override
    public void setAMQPSource(AMQPQueue source) {
        if (EsperServiceImpl.source == source){
            return;
        }
        esperServiceProvider.getEPAdministrator().createEPL(source.toString());
        sourceFlow = esperServiceProvider.getEPRuntime().getDataFlowRuntime().instantiate("AMQPIncomingDataFlow");
    }

    @Override
    public void setAMQPSink(AMQPQueue sink) {
        if (EsperServiceImpl.sink == sink){
            return;
        }
        esperServiceProvider.getEPAdministrator().createEPL(sink.toString());
        esperServiceProvider.getEPRuntime().getDataFlowRuntime().instantiate("AMQPOutcomingDataFlow");
    }

    @Override
    public void setEventSchema(String eventName, Map schema) {
        EsperServiceImpl.eventName = eventName;
        esperServiceProvider.getEPAdministrator().createEPL("create schema " + eventName
                + "(" + CreateEventSchema.schemaMapToString(schema)+ ")");
        EsperServiceImpl.schema = schema;
        System.out.println(CreateEventSchema.schemaMapToString(schema));
    }

    @Override
    public void removeAMQPSource(AMQPQueue source) {
    }

    @Override
    public void removeAMQPSink(AMQPQueue sink) {

    }

}
