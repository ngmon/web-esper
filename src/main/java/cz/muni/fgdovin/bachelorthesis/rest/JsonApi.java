package cz.muni.fgdovin.bachelorthesis.rest;

import com.espertech.esper.client.EPStatement;
import cz.muni.fgdovin.bachelorthesis.core.EsperService;
import cz.muni.fgdovin.bachelorthesis.support.AMQPQueue;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
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
    @RequestMapping(value = "/")
    String getDummy() {
        AMQPQueue testInputQueue = new AMQPQueue("myEventType","testQueue", "logs");

        AMQPQueue testOutputQueue = new AMQPQueue("myEventType","testOutputQueue", "sortedLogs");

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

        String statement = "select avg(p.value) from myEventType.win:length(8)";

        esperService.setEventSchema("myEventType", eventSchema);
        esperService.setAMQPSource(testInputQueue);
        esperService.executeQuery(statement);

        return "Ready to consume events";
    }

    @RequestMapping(value = URIConstants.GET_COMMAND, method = RequestMethod.GET)
    public
    @ResponseBody
    void getCommand(@PathVariable("id") int commandId) {
    }

    @RequestMapping(value = URIConstants.CREATE_COMMAND, method = RequestMethod.POST)
    public
    @ResponseBody
    void createCommand(@RequestBody JsonSerializable newCommand) {

    }

    @RequestMapping(value = URIConstants.DELETE_COMMAND, method = RequestMethod.PUT)
    public
    @ResponseBody
    void deleteCommand(@PathVariable("id") int commandId) {
    }
}
