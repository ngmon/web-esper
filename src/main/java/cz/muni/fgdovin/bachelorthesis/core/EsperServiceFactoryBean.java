/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fgdovin.bachelorthesis.core;

import com.espertech.esper.client.*;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author Filip Gdovin, 410328
 */
@Component
public class EsperServiceFactoryBean implements FactoryBean<EPServiceProvider> {

    private static final Logger logger = Logger.getLogger(EsperServiceFactoryBean.class);

    @Override
    public EPServiceProvider getObject()
    {
        return EPServiceProviderManager.getDefaultProvider();
    }

    @Override
    public Class<?> getObjectType()
    {
        return EPServiceProvider.class;
    }

    @Override
    public boolean isSingleton()
    {
        return true;
    }
}
