package cz.muni.fgdovin.bachelorthesis;

import com.espertech.esper.client.*;
import com.espertech.esper.client.dataflow.EPDataFlowRuntime;

import com.espertech.esper.client.time.CurrentTimeEvent;
import com.espertech.esperio.amqp.AMQPSink;
import com.espertech.esperio.amqp.AMQPSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by Filip Gdovin on 6. 2. 2015.
 */
//for beans definitions needed by Demo
@Component
public class Application {

    @Bean
    public EPServiceProvider epServiceProvider() {
        Configuration config = new Configuration();
        config.getEngineDefaults().getThreading().setInternalTimerEnabled(false); //getting time from events enabled
        EPServiceProvider esperProvider = EPServiceProviderManager.getDefaultProvider(config);
        esperProvider.getEPAdministrator().getConfiguration().addImport(AMQPSource.class.getPackage().getName() + ".*");
        esperProvider.getEPAdministrator().getConfiguration().addImport(AMQPSink.class.getPackage().getName() + ".*");
        return esperProvider;
    }

    @Bean
    public EPAdministrator epAdministrator() {
        return epServiceProvider().getEPAdministrator();
    }

    @Bean
    public EPRuntime epRuntime() {
        EPRuntime myRuntime = EPServiceProviderManager.getDefaultProvider().getEPRuntime();
        long timeInMillis = System.currentTimeMillis(); //set time for engine because we want to manage it from events later
        CurrentTimeEvent timeEvent = new CurrentTimeEvent(timeInMillis);
        myRuntime.sendEvent(timeEvent);
        return myRuntime;
    }

    @Bean
    public EPDataFlowRuntime epdataFlowRuntime() {
        return EPServiceProviderManager.getDefaultProvider().getEPRuntime().getDataFlowRuntime();
    }

    @Bean
    public ConfigurationOperations configurationOperations() {
        return EPServiceProviderManager.getDefaultProvider().getEPAdministrator().getConfiguration();
    }
}
