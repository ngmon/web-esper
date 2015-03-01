package cz.muni.fgdovin.bachelorthesis.support;

import com.espertech.esperio.amqp.AMQPToObjectCollector;
import com.espertech.esperio.amqp.AMQPToObjectCollectorContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.*;

/**
 * Created by Filip Gdovin on 6. 2. 2015.
 */
public class AMQPToEvent implements AMQPToObjectCollector {

    final private static Gson gson = new GsonBuilder().create();

    @Override
    public void collect(final AMQPToObjectCollectorContext context) {
        String receivedEvent = new String(context.getBytes());
        context.getEmitter().submit(gson.fromJson(JSONFlattener.encode(receivedEvent), Map.class));
    }
}
