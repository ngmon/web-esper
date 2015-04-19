package cz.muni.fgdovin.bachelorthesis.support;/*
 * *************************************************************************************
 *  Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 *  http://esper.codehaus.org                                                          *
 *  http://www.espertech.com                                                           *
 *  ---------------------------------------------------------------------------------- *
 *  The software in this package is published under the terms of the GPL license       *
 *  a copy of which has been included with this distribution in the license.txt file.  *
 * *************************************************************************************
 */

/**
 * Modified by by Filip Gdovin on 17. 4. 2015.
 */
import cz.muni.fgdovin.bachelorthesis.support.EsperModifiedClasses.CustomJSONRenderer;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventType;
import com.espertech.esper.client.util.EventPropertyRenderer;
import com.espertech.esper.client.util.EventPropertyRendererContext;
import com.espertech.esper.client.util.JSONEventRenderer;
import com.espertech.esper.client.util.JSONRenderingOptions;
import com.espertech.esper.dataflow.annotations.DataFlowOpPropertyHolder;
import com.espertech.esper.dataflow.annotations.DataFlowOperator;
import com.espertech.esper.dataflow.interfaces.*;
import com.espertech.esper.event.EventBeanAdapterFactory;
import com.espertech.esperio.amqp.AMQPEmitter;
import com.espertech.esperio.amqp.AMQPSettingsSink;
import com.espertech.esperio.amqp.ObjectToAMQPCollectorContext;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@DataFlowOperator
public class CustomAMQPSink implements DataFlowOpLifecycle {
    private static final Log log = LogFactory.getLog(CustomAMQPSink.class);

    @DataFlowOpPropertyHolder
    private AMQPSettingsSink settings;

    private EventBeanAdapterFactory adapterFactories[];
    private JsonFormatter renderer;

    private transient Connection connection;
    private transient Channel channel;
    private ThreadLocal<ObjectToAMQPCollectorContext> collectorDataTL = new ThreadLocal<ObjectToAMQPCollectorContext>() {
        protected synchronized ObjectToAMQPCollectorContext initialValue() {
            return null;
        }
    };

    public DataFlowOpInitializeResult initialize(DataFlowOpInitializateContext context) throws Exception {
        if (!context.getOutputPorts().isEmpty()) {
            throw new IllegalArgumentException("AMQPSink operator does not provide an output stream");
        }

        EventType[] eventTypes = new EventType[context.getInputPorts().size()];
        for (int i = 0; i < eventTypes.length; i++) {
            eventTypes[i] = context.getInputPorts().get(i).getTypeDesc().getEventType();
        }

        adapterFactories = new EventBeanAdapterFactory[eventTypes.length];
        for (int i = 0; i < eventTypes.length; i++) {
            adapterFactories[i] = context.getServicesContext().getEventAdapterService().getAdapterFactoryForType(eventTypes[i]);
        }
        renderer = new JsonFormatter();
        return null;
    }

    public void open(DataFlowOpOpenContext openContext) {
        log.info("Opening AMQP, settings are: " + settings.toString());

        try {
            final ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(settings.getHost());
            if (settings.getPort() > -1) {
                connectionFactory.setPort(settings.getPort());
            }
            if (settings.getUsername() != null) {
                connectionFactory.setUsername(settings.getUsername());
            }
            if (settings.getPassword() != null) {
                connectionFactory.setPassword(settings.getPassword());
            }
            if (settings.getVhost() != null) {
                connectionFactory.setVirtualHost(settings.getVhost());
            }

            connection = connectionFactory.newConnection();
            channel = connection.createChannel();

            if (settings.getExchange() != null) {
                channel.exchangeDeclarePassive(settings.getExchange());
            }

            final AMQP.Queue.DeclareOk queue;
            if (settings.getQueueName() == null || settings.getQueueName().trim().length() == 0) {
                queue = channel.queueDeclare();
            }
            else {
                // java.lang.String queue,boolean durable,boolean exclusive,boolean autoDelete,java.util.Map<java.lang.String,java.lang.Object> arguments) throws java.io.IOException
                queue = channel.queueDeclare(settings.getQueueName(), settings.isDeclareDurable(), settings.isDeclareExclusive(), settings.isDeclareAutoDelete(), settings.getDeclareAdditionalArgs());
            }
            if (settings.getExchange() != null && settings.getRoutingKey() != null) {
                channel.queueBind(queue.getQueue(), settings.getExchange(), settings.getRoutingKey());
            }

            final String queueName = queue.getQueue();
            log.info("AMQP producing queue is " + queueName + (settings.isLogMessages() ? " with logging" : ""));
        }
        catch (IOException e) {
            String message = "AMQP setup failed: " + e.getMessage();
            log.error(message, e);
            throw new RuntimeException(message, e);
        }
    }

    public void onInput(int port, Object event) {

        String result = getEventOut(port, event);

        ObjectToAMQPCollectorContext holder = collectorDataTL.get();
        if (holder == null) {
            if (settings.getExchange() != null && settings.getRoutingKey() != null) {
                if (log.isDebugEnabled()) {
                    log.debug("Using exchange " + settings.getExchange() + " routing-key " + settings.getRoutingKey());
                }
                holder = new ObjectToAMQPCollectorContext(new AMQPEmitter() {
                    public void send(byte[] bytes) {
                        try {
                            channel.basicPublish(settings.getExchange(), settings.getRoutingKey(), null, bytes);
                        }
                        catch (IOException e) {
                            String message = "Failed to publish to AMQP: " + e.getMessage();
                            log.error(message, e);
                            throw new RuntimeException(message, e);
                        }
                    }
                }, result);
            }
            else {
                if (log.isDebugEnabled()) {
                    log.debug("Using queue " + settings.getQueueName());
                }
                holder = new ObjectToAMQPCollectorContext(new AMQPEmitter() {
                    public void send(byte[] bytes) {
                        try {
                            channel.basicPublish("", settings.getQueueName(), null, bytes);
                        }
                        catch (IOException e) {
                            String message = "Failed to publish to AMQP: " + e.getMessage();
                            log.error(message, e);
                            throw new RuntimeException(message, e);
                        }
                    }
                }, result);
            }
            collectorDataTL.set(holder);
        }
        else {
            holder.setObject(result);
        }

        settings.getCollector().collect(holder);
    }

    private String getEventOut(int port, Object theEvent) {

        if (theEvent instanceof EventBean) {
            return renderer.render((EventBean) theEvent);

        }

        if (adapterFactories[port] != null) {
            synchronized(this) {
                EventBean event = adapterFactories[port].makeAdapter(theEvent);
                return renderer.render(event);
            }
        }
        else {
            return "Unknown underlying";
        }
    }

    public void close(DataFlowOpCloseContext openContext) {
        try {
            if (channel != null) {
                channel.close();
            }
        }
        catch (IOException e) {
            log.warn("Error closing AMQP channel", e);
        }

        try {
            if (connection != null) {
                connection.close();
            }
        }
        catch (IOException e) {
            log.warn("Error closing AMQP connection", e);
        }
    }

    public static class JsonFormatter {

        private final Map<EventType, JSONEventRenderer> jsonRendererCache = new HashMap<EventType, JSONEventRenderer>();

        public String render(EventBean theEvent) {
            String result;
            JSONEventRenderer renderer = jsonRendererCache.get(theEvent.getEventType());
            if (renderer == null) {
                renderer = getJsonEventRenderer(theEvent.getEventType());
                jsonRendererCache.put(theEvent.getEventType(), renderer);
            }
            result = renderer.render(theEvent);

            return result;
        }

        protected JSONEventRenderer getJsonEventRenderer(EventType eventType) {
            return new CustomJSONRenderer(eventType, RenderingOptions.getJsonOptions());
        }
    }

    public static class RenderingOptions {
        private static JSONRenderingOptions jsonOptions;

        static {
            jsonOptions = new JSONRenderingOptions();
            jsonOptions.setPreventLooping(true);
            jsonOptions.setRenderer(JsonEventPropertyRenderer.INSTANCE);
        }

        public static JSONRenderingOptions getJsonOptions() {
            return jsonOptions;
        }

        public static void setJsonOptions(JSONRenderingOptions jsonOptions) {
            RenderingOptions.jsonOptions = jsonOptions;
        }
    }

    public static class JsonEventPropertyRenderer implements EventPropertyRenderer {
        public final static JsonEventPropertyRenderer INSTANCE = new JsonEventPropertyRenderer();

        public void render(EventPropertyRendererContext context) {
            if (context.getPropertyValue() instanceof Object[]) {
                context.getStringBuilder().append(Arrays.toString((Object[]) context.getPropertyValue()));
            }
            else {
                context.getDefaultRenderer().render(context.getPropertyValue(), context.getStringBuilder());
            }
        }
    }
}