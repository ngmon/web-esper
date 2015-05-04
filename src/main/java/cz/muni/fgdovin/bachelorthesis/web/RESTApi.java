package cz.muni.fgdovin.bachelorthesis.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fgdovin.bachelorthesis.rabbit.RabbitMqService;
import cz.muni.fgdovin.bachelorthesis.support.JSONFlattener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by Filip Gdovin on 4. 5. 2015.
 */
@RestController
public class RESTApi {

    @Autowired
    private RabbitMqService rabbitMqService;

    @RequestMapping("/getSchema")
    public String getSchema(@RequestParam(value="name") String name) {
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
