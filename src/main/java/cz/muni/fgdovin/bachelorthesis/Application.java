package cz.muni.fgdovin.bachelorthesis;

import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.dataflow.EPDataFlowRuntime;

import com.espertech.esperio.amqp.AMQPSink;
import com.espertech.esperio.amqp.AMQPSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Created by Filip Gdovin on 6. 2. 2015.
 */
@SpringBootApplication
public class Application {

    @Bean
    public EPServiceProvider epServiceProvider() {
        EPServiceProvider esperProvider = EPServiceProviderManager.getDefaultProvider();
        esperProvider.getEPAdministrator().getConfiguration().addImport(AMQPSource.class.getPackage().getName() + ".*");
        esperProvider.getEPAdministrator().getConfiguration().addImport(AMQPSink.class.getPackage().getName() + ".*");
        return esperProvider;
    }

    @Bean
    public EPRuntime epRuntime() {
        return EPServiceProviderManager.getDefaultProvider().getEPRuntime();
    }

    @Bean
    public EPDataFlowRuntime epdataFlowRuntime() {
        return EPServiceProviderManager.getDefaultProvider().getEPRuntime().getDataFlowRuntime();
    }

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);
    }
}
