package cz.muni.fgdovin.bachelorthesis.demo.rabbitMQReceiver.src.main;

import com.rabbitmq.client.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Filip Gdovin
 * @version 19. 2. 2015
 */

public class RabbitMQReceiver {

    private static final Logger logger = LogManager.getLogger(RabbitMQReceiver.class);

    //queueName will NOT be created if non-existing, purpose is to create only in app!!
    //set numOfMessages to 0 for endless message waiting
    public void listen(String queueName, int numOfMessages) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);

        String pattern = "@timestamp\":";

        int messageCount = 0;
        while (messageCount < numOfMessages) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            int startIndex = message.indexOf(pattern) + pattern.length()+1;
            int endIndex = message.indexOf('"', startIndex);
            String timestamp = message.substring(startIndex, endIndex);
            ZonedDateTime timestampLong = parseDate(timestamp);
            measureTime(timestampLong);

            if(numOfMessages > 0) {
                messageCount++;
            }
        }
    }

    private ZonedDateTime parseDate(String input) {
        final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME; //"yyyy-MM-dd'T'HH:mm:ss.SSSz" format
        return ZonedDateTime.parse(input, formatter);
    }

    private void measureTime(ZonedDateTime timestamp) {
        ZonedDateTime currentTime = LocalDateTime.now().atZone(ZoneId.of("+02:00"));
        System.out.println("Event came in '"
                + TimeUnit.MILLISECONDS.convert(currentTime.getNano() - timestamp.getNano(), TimeUnit.NANOSECONDS)
                + "' milliseconds after its creation");
    }
}
