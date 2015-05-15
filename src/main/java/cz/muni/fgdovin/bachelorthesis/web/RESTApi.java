package cz.muni.fgdovin.bachelorthesis.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fgdovin.bachelorthesis.rabbit.RabbitMqService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Filip Gdovin
 * @version 5. 5. 2015
 */
@RestController
public class RESTApi {

    private static final Logger logger = LogManager.getLogger("RESTApi");

    @Resource
    Environment environment;

    @Autowired
    private RabbitMqService rabbitMqService;

    @RequestMapping("/getSchema")
    public String getSchema(@RequestParam(value="name") String name) {
        if(!(name.startsWith(this.environment.getProperty("exchangePrefix")))) {
            logger.warn("Bad prefix :/, returning null schema for " + name);
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        String result = null;
        try {
            result = mapper.writeValueAsString(this.rabbitMqService.getSchemaForExchange(name));
        } catch (Exception e) {
            logger.error("Failed to get schema for exchange " + name + ", returning " + result);
        }
        if(logger.isDebugEnabled()){
            logger.debug("Returning " + result + " as schema for " + name);
        }
        return result;
    }
}
