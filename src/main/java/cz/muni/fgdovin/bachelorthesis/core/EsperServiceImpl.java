package cz.muni.fgdovin.bachelorthesis.core;

import com.espertech.esper.client.*;
import com.espertech.esper.client.dataflow.EPDataFlowInstance;
import com.espertech.esper.client.dataflow.EPDataFlowRuntime;

import cz.muni.fgdovin.bachelorthesis.support.AMQPQueue;
import cz.muni.fgdovin.bachelorthesis.support.EventSchema;

import cz.muni.fgdovin.bachelorthesis.support.Statement;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Filip Gdovin on 9. 2. 2015.
 */
@Service
public class EsperServiceImpl implements EsperService {

    private static final Logger logger = Logger.getLogger(EsperServiceImpl.class);

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
        EPStatement query = null;
        try{
            query = esperServiceProvider.getEPAdministrator().createEPL(statement.getQuery(), statement.getName());
        }
        catch(EPException ex){
            logger.warn(ex);
            return;
        }
        System.out.println("Query is: " + esperServiceProvider.getEPAdministrator().getStatement(statement.getName()).getText());
        query.start();
    }

    @Override
    public void removeQuery(String statName) {
        esperServiceProvider.getEPAdministrator().getStatement(statName).stop();
    }

    @Override
    public AMQPQueue setAMQPSource(AMQPQueue source) {
        try{
            esperServiceProvider.getEPAdministrator().createEPL("@Audit " + source.toInputString());
        }
        catch(EPException ex){
            logger.warn(ex);
            return source;
        }

        EPDataFlowInstance sourceInstance = esperDataFlowRuntime.instantiate(source.getName());
        esperDataFlowRuntime.saveInstance(source.getName(), sourceInstance);

        sourceInstance.start();

        return source;
    }

    @Override
    public void removeAMQPSource(String dataFlowName) {
        esperDataFlowRuntime.getSavedInstance(dataFlowName).cancel();
    }

    @Override
    public AMQPQueue setAMQPSink(AMQPQueue sink) {
        try{
            esperServiceProvider.getEPAdministrator().createEPL(sink.toOutputString());
        }
        catch(EPException ex){
            logger.warn(ex);
            return sink;
        }

        EPDataFlowInstance sinkInstance = esperDataFlowRuntime.instantiate(sink.getName());
        esperDataFlowRuntime.saveInstance(sink.getName(),sinkInstance);

        sinkInstance.start();
        return sink;
    }

    @Override
    public void removeAMQPSink(String dataFlowName) {
        esperDataFlowRuntime.getSavedInstance(dataFlowName).cancel();
    }

    @Override
    public void setEventSchema(EventSchema input) {
        EsperServiceImpl.eventName = input.getEventName();
        esperServiceProvider.getEPAdministrator().createEPL("create map schema " + eventName
                + "(" + input.getEventSchema()+ ")");
    }
}
