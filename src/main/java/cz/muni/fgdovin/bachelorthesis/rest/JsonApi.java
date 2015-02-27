package cz.muni.fgdovin.bachelorthesis.rest;

import cz.muni.fgdovin.bachelorthesis.core.EsperService;
import cz.muni.fgdovin.bachelorthesis.support.EPLHelper;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Filip Gdovin on 28. 1. 2015.
 */

@RestController
public class JsonApi {

    @Autowired
    private EsperService esperService;
    private static final Logger logger = Logger.getLogger(JsonApi.class);

    //here comes API
    @RequestMapping(value = URIConstants.CREATE_DUMMY)
    String getDummy() {
        logger.info("Dummy request:");

        String testSchema = ("hostname String, application String, level Integer, p.value Integer, p.value2 String, type String, priority Integer, timestamp String");
        String query = "select avg(p.value) from myEventType where p.value > 4652";

        String inputQueue = EPLHelper.createAMQP("AMQPIncomingStream", "myEventType", "(" + testSchema + ")", "esperQueue", "logs");

        String statement = EPLHelper.createStatement("AMQPOutcomingStream", "myEventType", "(" + testSchema + ")", query, "esperOutputQueue", "sortedLogs");

        return "Ready to consume events";
    }

    @RequestMapping(value = URIConstants.CREATE_QUEUE)
    public
    @ResponseBody
    String createAmqpSource(@RequestParam String newQueue) {
        return esperService.addAMQPSource("AMQPIncomingStream", newQueue).getDataFlowName() + " successfully created.";
    }

    @RequestMapping(value = URIConstants.DELETE_QUEUE)
    public
    @ResponseBody
    void deleteSource() {
        esperService.removeAMQPSource("AMQPIncomingStream");
    }

    @RequestMapping(value = URIConstants.CREATE_STATEMENT)
    public
    @ResponseBody
    String createStatement(@RequestParam String statement) {
        esperService.addStatement("myTestStat", statement);
        return ("Executing " + statement);
    }

    @RequestMapping(value = URIConstants.DELETE_STATEMENT)
    public
    @ResponseBody
    void deleteStatement() {
        esperService.removeStatement("myTestStat");
    }
}
