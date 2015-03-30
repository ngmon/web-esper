package cz.muni.fgdovin.bachelorthesis.web;

import cz.muni.fgdovin.bachelorthesis.core.EsperService;
import cz.muni.fgdovin.bachelorthesis.core.EsperServiceImpl;
import cz.muni.fgdovin.bachelorthesis.core.EsperUserFriendlyService;
import cz.muni.fgdovin.bachelorthesis.core.EsperUserFriendlyServiceImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.espertech.esper.client.*;
import com.espertech.esper.client.dataflow.EPDataFlowRuntime;
import com.espertech.esperio.amqp.AMQPSink;
import com.espertech.esperio.amqp.AMQPSource;

import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


/**
 * Created by Filip Gdovin on 4. 3. 2015.
 */
@SpringBootApplication
public class MvcConfig extends WebMvcConfigurerAdapter {

    private static final Logger logger = LogManager.getLogger("MvcConfig");

    private EPServiceProvider epServiceProvider;
    private EPRuntime epRuntime;

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
        if(this.epRuntime == null) {
            this.epRuntime = EPServiceProviderManager.getDefaultProvider().getEPRuntime();
        }
        return this.epRuntime;
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
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean
    public EsperService esperService() {
        return new EsperServiceImpl();
    }

    @Bean
    public EsperUserFriendlyService esperUserFriendlyService() {
        return new EsperUserFriendlyServiceImpl();
    }

    public static void main(String[] args) {
        SpringApplication.run(MvcConfig.class, args);
    }
}
