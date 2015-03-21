package cz.muni.fgdovin.bachelorthesis;

import com.espertech.esper.client.*;
import com.espertech.esper.client.dataflow.EPDataFlowRuntime;

import com.espertech.esper.client.time.CurrentTimeEvent;
import com.espertech.esperio.amqp.AMQPSink;
import com.espertech.esperio.amqp.AMQPSource;

import cz.muni.fgdovin.bachelorthesis.support.JSONFlattener;
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

    private EPServiceProvider epServiceProvider;
    private EPRuntime epRuntime;

    @Bean
    public EPServiceProvider epServiceProvider() {
        if(this.epServiceProvider==null) {
            Configuration config = new Configuration();
            config.getEngineDefaults().getThreading().setInternalTimerEnabled(false); //getting time from events enabled
            this.epServiceProvider = EPServiceProviderManager.getDefaultProvider(config);
            this.epServiceProvider.getEPAdministrator().getConfiguration().addImport(AMQPSource.class.getPackage().getName() + ".*");
            this.epServiceProvider.getEPAdministrator().getConfiguration().addImport(AMQPSink.class.getPackage().getName() + ".*");
        }
        return this.epServiceProvider;
    }

    @Bean
    public EPAdministrator epAdministrator() {
        return epServiceProvider().getEPAdministrator();
    }

    @Bean
    public EPRuntime epRuntime() {
        if(this.epRuntime==null) {
            this.epRuntime = EPServiceProviderManager.getDefaultProvider().getEPRuntime();
            long timeInMillis = JSONFlattener.parseDate("2015-03-21T08:05:00.000"); //set time for engine because we want to manage it from events later
            CurrentTimeEvent timeEvent = new CurrentTimeEvent(timeInMillis);
            this.epRuntime.sendEvent(timeEvent);
        }
        return this.epRuntime;
    }

    @Bean
    public EPDataFlowRuntime epdataFlowRuntime() {
        return epRuntime().getDataFlowRuntime();
    }

    @Bean
    public ConfigurationOperations configurationOperations() {
        return EPServiceProviderManager.getDefaultProvider().getEPAdministrator().getConfiguration();
    }
}
