package cz.muni.fgdovin.bachelorthesis;

import cz.muni.fgdovin.bachelorthesis.core.EsperService;
import cz.muni.fgdovin.bachelorthesis.support.EPLHelper;

import cz.muni.fgdovin.bachelorthesis.web.MvcConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Filip Gdovin on 1. 3. 2015.
 */

@Controller
@ComponentScan
public class Demo {

    @Autowired
    EsperService esperService;

    String AMQPQueueName = "AMQPIncomingStream";
    String inputQueueName = "esperQueue";
    String inputExchangeName = "logs";

    String outputQueueName = "esperOutputQueue";
    String outputExchangeName = "sortedLogs";

    String eventType = "myEventType";
    static Map<String, Object> schema = null;

    String statementName = "myTestStat";
    String statementName2 = "myTestStat2";

    String query = "select avg(p.value) from instream where p.value > 4652";

    public static void main(String[] argc) throws Exception {
        schema = new HashMap<String, Object>();
        schema.put("hostname", String.class);
        schema.put("application", String.class);
        schema.put("level", Integer.class);
        schema.put("p.value", Integer.class);
        schema.put("p.value2", String.class);
        schema.put("type", String.class);
        schema.put("priority", Integer.class);
        schema.put("timestamp", String.class);

        Demo myTester = new Demo();
        myTester.esperService = new AnnotationConfigWebApplicationContext().getBean(EsperService.class);
        if(myTester.esperService == null) {
            System.out.println("Null");
            return;
        }
        myTester.demo1source();
    }

    public void demo1source() {

        //schema
        esperService.addSchema(eventType, schema);

        //AMQP source
        String inputQueue = EPLHelper.createAMQP(AMQPQueueName, eventType,
                inputQueueName, inputExchangeName);
        esperService.addDataflow(AMQPQueueName, inputQueue);

        //Statement1
        String statement = EPLHelper.createStatement(statementName, eventType,
                query, outputQueueName, outputExchangeName);
        esperService.addDataflow(statementName, statement);

        System.out.println(esperService.showDataflows());
    }
}
