package cz.muni.fgdovin.bachelorthesis.support;

import com.espertech.esperio.amqp.ObjectToAMQPCollector;
import com.espertech.esperio.amqp.ObjectToAMQPCollectorContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EventToAMQP implements ObjectToAMQPCollector {

    @Override
    public void collect(ObjectToAMQPCollectorContext context) {

        context.getEmitter().send(fromMap(context.getObject()));
    }

    private byte[] fromMap(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        String result = null;
        try {
            result = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //to remove [] from each event because they are maps in Esper...
        return result.substring(1, result.length()-1).getBytes();
    }
}
