package cz.muni.fgdovin.bachelorthesis;

import com.espertech.esper.client.EPServiceProvider;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by Filip Gdovin on 6. 2. 2015.
 */
@SpringBootApplication
public class Main {

    @Autowired
    private static EPServiceProvider esperEngine;
    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {


    ApplicationContext applicationContext = new AnnotationConfigApplicationContext("cz.muni.fgdovin.bachelorthesis");

        esperEngine = (EPServiceProvider) applicationContext.getBean("EPServiceProvider");
        System.out.println(esperEngine.getEPRuntime().getCurrentTime()); //just to test its not null
    }
}
