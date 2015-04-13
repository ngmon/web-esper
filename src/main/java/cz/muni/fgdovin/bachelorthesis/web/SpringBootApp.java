package cz.muni.fgdovin.bachelorthesis.web;

import cz.muni.fgdovin.bachelorthesis.core.EsperService;
import cz.muni.fgdovin.bachelorthesis.core.EsperServiceImpl;
import cz.muni.fgdovin.bachelorthesis.core.EsperUserFriendlyService;
import cz.muni.fgdovin.bachelorthesis.core.EsperUserFriendlyServiceImpl;

import com.espertech.esper.client.*;
import com.espertech.esper.client.dataflow.EPDataFlowRuntime;
import com.espertech.esperio.amqp.AMQPSink;
import com.espertech.esperio.amqp.AMQPSource;

import cz.muni.fgdovin.bachelorthesis.support.DataflowHelper;
import cz.muni.fgdovin.bachelorthesis.support.EventTypeHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Filip Gdovin on 4. 3. 2015.
 */
@SpringBootApplication
@PropertySource("classpath:config.properties")
public class SpringBootApp {

    private EPServiceProvider epServiceProvider;

    //Esper beans
    @Bean
    public EPServiceProvider epServiceProvider() {
        if(this.epServiceProvider == null) {
            com.espertech.esper.client.Configuration config = new com.espertech.esper.client.Configuration();
            config.addImport(AMQPSource.class.getPackage().getName() + ".*");
            config.addImport(AMQPSink.class.getPackage().getName() + ".*");
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

    @Bean
    public EsperUserFriendlyService esperUserFriendlyService() {
        return new EsperUserFriendlyServiceImpl();
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

    public static void main(String[] args) {
        SpringApplication.run(SpringBootApp.class, args);
    }
}
