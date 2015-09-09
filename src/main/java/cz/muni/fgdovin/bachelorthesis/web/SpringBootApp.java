package cz.muni.fgdovin.bachelorthesis.web;

import cz.muni.fgdovin.bachelorthesis.esper.EsperService;
import cz.muni.fgdovin.bachelorthesis.esper.EsperServiceImpl;
import cz.muni.fgdovin.bachelorthesis.rabbit.RabbitMqService;
import cz.muni.fgdovin.bachelorthesis.rabbit.RabbitMqServiceImpl;
import cz.muni.fgdovin.bachelorthesis.support.CustomAMQPSink;
import cz.muni.fgdovin.bachelorthesis.support.DataflowHelper;
import cz.muni.fgdovin.bachelorthesis.support.EventTypeHelper;

import com.espertech.esper.client.*;
import com.espertech.esper.client.dataflow.EPDataFlowRuntime;
import com.espertech.esperio.amqp.AMQPSource;

import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * This is the "Main" class for whole application,
 * it contains main method which stars Spring Boot,
 * definition of all used beans and property files.
 *
 * @author Filip Gdovin
 * @version 4. 3. 2015
 */
@SpringBootApplication
@PropertySource("classpath:config.properties")
public class SpringBootApp {

    private EPServiceProvider epServiceProvider;

    //RabbitMQ beans
    @Bean
    public RabbitMqService rabbitMqService() {
        return new RabbitMqServiceImpl();
    }

    //Esper beans
    @Bean
    public EPServiceProvider epServiceProvider() {
        if(this.epServiceProvider == null) {
            com.espertech.esper.client.Configuration config = new com.espertech.esper.client.Configuration();
            config.addImport(AMQPSource.class.getPackage().getName() + ".*");
            config.addImport(CustomAMQPSink.class.getPackage().getName() + ".*");
            config.getEngineDefaults().getEventMeta().setDefaultEventRepresentation(Configuration.EventRepresentation.MAP);
            this.epServiceProvider = EPServiceProviderManager.getDefaultProvider(config);
        }
        return this.epServiceProvider;
    }

    @Bean
    public EPAdministrator epAdministrator() {
        return epServiceProvider().getEPAdministrator();
    }

    @Bean
    public EPRuntime epRuntime() {
        return EPServiceProviderManager.getDefaultProvider().getEPRuntime();
    }

    @Bean
    public EPDataFlowRuntime epdataFlowRuntime() {
        return epRuntime().getDataFlowRuntime();
    }

    @Bean
    public ConfigurationOperations configurationOperations() {
        return epServiceProvider().getEPAdministrator().getConfiguration();
    }

    @Bean
    public EsperService esperService() {
        return new EsperServiceImpl();
    }

    //support beans
    @Bean
    public DataflowHelper dataflowHelper() {
        return new DataflowHelper();
    }

    @Bean
    public EventTypeHelper eventTypeHelper() {
        return new EventTypeHelper();
    }

    /**
     * main method which will run Spring Boot.
     *
     * @param args String[] arguments to keep convention,
     *             could be probably used in Spring Boot.
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringBootApp.class, args);
    }
}
