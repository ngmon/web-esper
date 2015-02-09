package cz.muni.fgdovin.bachelorthesis.support;

import com.espertech.esper.client.EPServiceProvider;

import cz.muni.fgdovin.bachelorthesis.core.EsperServiceFactoryBean;

import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationContext;

/**
 * Created by Filip Gdovin on 9. 2. 2015.
 */
@Component
public class ApplicationConfig implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean(name = "EPServiceProvider")
    public EPServiceProvider getEsperService() throws Exception {
        EPServiceProvider esperService = applicationContext.getBean(EsperServiceFactoryBean.class)
                .getObject();
        return esperService;
    }
}
