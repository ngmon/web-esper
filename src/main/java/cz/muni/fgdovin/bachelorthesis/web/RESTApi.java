package cz.muni.fgdovin.bachelorthesis.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fgdovin.bachelorthesis.rabbit.RabbitMqService;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by Filip Gdovin on 4. 5. 2015.
 */
@RestController
public class RESTApi {

    @Resource
    Environment environment;

    @Autowired
    private RabbitMqService rabbitMqService;

    @RequestMapping("/getSchema")
    public String getSchema(@RequestParam(value="name") String name) {
        if(!(name.startsWith(this.environment.getProperty("exchangePrefix")))) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        String result = null;
        try {
            result = mapper.writeValueAsString(this.rabbitMqService.getSchemaForExchange(name));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
