package cz.muni.fgdovin.bachelorthesis.rest;

import cz.muni.fgdovin.bachelorthesis.core.EsperService;
import cz.muni.fgdovin.bachelorthesis.support.AMQPQueue;
import cz.muni.fgdovin.bachelorthesis.support.EventSchema;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by Filip Gdovin on 28. 1. 2015.
 */

@RestController
public class JsonApi {

    @Autowired
    private EsperService esperService;
    private static final Logger logger = Logger.getLogger(JsonApi.class);

    //here comes API
    @RequestMapping(value = URIConstants.CREATE_DUMMY, method = RequestMethod.POST)
    String getDummy() {
        AMQPQueue testInputQueue = new AMQPQueue("myEventType","testQueue", "logs");

        Map<String, Object> eventSchema = new HashMap<>();
        eventSchema.put("timestamp", "String");
        eventSchema.put("type", "String");
        eventSchema.put("p.value", "Integer");
        eventSchema.put("p.value2", "String");
        eventSchema.put("hostname", "String");
        eventSchema.put("application", "String");
        eventSchema.put("process", "String");
        eventSchema.put("processId", "Integer");
        eventSchema.put("level", "Integer");
        eventSchema.put("priority", "Integer");

        EventSchema testSchema = new EventSchema("myEventType",eventSchema);

        String statement = "select avg(p.value) from myEventType.win:length(8)";

        esperService.setEventSchema(testSchema);
        esperService.setAMQPSource(testInputQueue);
        esperService.setQuery(statement);

        return "Ready to consume events";
    }

    @RequestMapping(value = URIConstants.CREATE_SCHEMA)
    public
    @ResponseBody
    String createEventSchema(@RequestBody EventSchema input) {
        esperService.setEventSchema(input);
        return input.toString();
    }

    @RequestMapping(value = URIConstants.DELETE_SCHEMA)
    public
    @ResponseBody
    void deleteEventSchema() {
        esperService.removeAMQPSource();
    }

    @RequestMapping(value = URIConstants.CREATE_INPUT)
    public
    @ResponseBody
    AMQPQueue createAmqpSource(@RequestBody AMQPQueue newQueue) {
        return esperService.setAMQPSource(newQueue);
    }

    @RequestMapping(value = URIConstants.DELETE_INPUT)
    public
    @ResponseBody
    void deleteSource() {
        esperService.removeAMQPSource();
    }

    @RequestMapping(value = URIConstants.CREATE_OUTPUT)
    public
    @ResponseBody
    AMQPQueue createAmqpSink(@RequestBody AMQPQueue newQueue) {
        return esperService.setAMQPSink(newQueue);
    }

    @RequestMapping(value = URIConstants.DELETE_OUTPUT)
    public
    @ResponseBody
    void deleteSink() {
        esperService.removeAMQPSink();
    }

    @RequestMapping(value = URIConstants.CREATE_STAT)
    public
    @ResponseBody
    String createStatement(@RequestBody String statement) {
        return ("Executing " + statement);
    }

    @RequestMapping(value = URIConstants.DELETE_STAT)
    public
    @ResponseBody
    void deleteStatement() {
        esperService.removeQuery();
    }
}
