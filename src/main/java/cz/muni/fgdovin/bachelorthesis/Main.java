package cz.muni.fgdovin.bachelorthesis;

import cz.muni.fgdovin.bachelorthesis.core.EsperService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Filip Gdovin on 6. 2. 2015.
 */
@SpringBootApplication
public class Main {

    @Autowired
    private static EsperService esperEngine;
    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
    }

}
