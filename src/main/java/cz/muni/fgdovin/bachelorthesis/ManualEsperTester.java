package cz.muni.fgdovin.bachelorthesis;

import cz.muni.fgdovin.bachelorthesis.core.EsperService;
import cz.muni.fgdovin.bachelorthesis.support.EPLHelper;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by Filip Gdovin on 26. 2. 2015.
 */
@Controller
@ContextConfiguration(classes = Application.class)
public class ManualEsperTester {

    @Autowired
    EsperService epService;
    private static ApplicationContext context = null;
    private static final Logger logger = Logger.getLogger(ManualEsperTester.class);

    public static void main(String[] argc) {
        context = SpringApplication.run(Application.class);
        ManualEsperTester myTester = new ManualEsperTester();
        myTester.testEverything();
    }

    private void testEverything() {
        String AMQPQueueName = "AMQPIncomingStream";
        String eventType = "myEventType";
        String inputQueueName = "esperQueue";
        String inputExchangeName = "logs";
        String testSchema = ("hostname String, application String, level Integer, p.value Integer, p.value2 String, type String, priority Integer, timestamp String");

        String statementName = "statementStream";
        String statementName2 = "statementStream2";
        String outputQueueName = "esperOutputQueue";
        String outputExchangeName = "sortedLogs";
        String query = "select avg(p.value) from instream";
        String query2 = "select * from instream";

        String AMQP = EPLHelper.createAMQP(AMQPQueueName, eventType, testSchema, inputQueueName, inputExchangeName);
        epService.addAMQPSource(AMQPQueueName, AMQP);


        String epl1 = EPLHelper.createStatement(statementName, eventType, testSchema, query, outputQueueName, outputExchangeName);
        epService.addStatement(statementName, epl1);


        String epl2 = EPLHelper.createStatement(statementName2, eventType, testSchema, query2, outputQueueName, outputExchangeName);
        epService.addStatement(statementName2, epl2);
    }
}
