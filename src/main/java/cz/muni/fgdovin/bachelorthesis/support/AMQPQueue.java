package cz.muni.fgdovin.bachelorthesis.support;

/**
 * Created by Filip Gdovin on 10. 2. 2015.
 */
public class AMQPQueue {
    private String name;
    private String eventType;                      //Outstream<test>
    private String host = "localhost";
    private String queueName;                   //queueName
    private boolean durable = true;
    private boolean exclusive = false;
    private boolean autoDelete = true;
    private String exchange;                    //exchangeName

    public AMQPQueue(String name, String eventType, String host,
                     String queueName, boolean durable,
                     boolean exclusive, boolean autoDelete,
                     String exchange) {
        this.name = name;
        this.eventType = eventType;
        this.host = host;
        this.queueName = queueName;
        this.durable = durable;
        this.exclusive = exclusive;
        this.autoDelete = autoDelete;
        this.exchange = exchange;
    }

    public AMQPQueue(String name, String eventType, String queueName, String exchange) {
        this.name = name;
        this.eventType = eventType;
        this.queueName = queueName;
        this.exchange = exchange;
    }

    public String toInputString(){
        return  "Create Dataflow " + name + "\n" +
                "AMQPSource -> Outstream<"+ eventType +">\n" +
                "{host: '" + host + "',\n" +
                "queueName: '" + queueName + "',\n" +
                "declareDurable: " + durable + ",\n" +
                "declareExclusive: " + exclusive + ",\n" +
                "declareAutoDelete: " + autoDelete + ",\n" +
                "exchange: '" + exchange +"',\n" +
                "collector: {class: 'cz.muni.fgdovin.bachelorthesis.support.AMQPToEvent'}}\n" +
                "LogSink(Outstream){}";
    }

    public String toOutputString(){
        return  "Create Dataflow " + name + "\n" +
                "EventBusSource -> outstream<"+ eventType +">{} \n" +
                "AMQPSink(outstream) \n" +
                "{host: '" + host + "',\n" +
                "queueName: '" + queueName + "',\n" +
                "declareDurable: " + durable + ",\n" +
                "declareExclusive: " + exclusive + ",\n" +
                "declareAutoDelete: " + autoDelete + ",\n" +
                "exchange: '" + exchange +"',\n" +
                "collector: {class: 'cz.muni.fgdovin.bachelorthesis.support.EventToAMQP'}}\n";
    }

    public String getName(){
        return this.name;
    }
}
