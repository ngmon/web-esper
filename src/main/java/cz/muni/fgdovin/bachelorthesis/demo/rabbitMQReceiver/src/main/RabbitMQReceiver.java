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
 * Demo RabbitMQ Receiver is used to test the application,
 * as it can listen on given queue and therefore check
 * if the output is correct. Also basic measure of elapsed
 * time happens here by comparing timestamp of each event
 * with current time.
 *
 * @author Filip Gdovin
 * @version 19. 2. 2015
 */

public class RabbitMQReceiver {

    private static final Logger logger = LogManager.getLogger(RabbitMQReceiver.class);

    /**
     * This method is used to listen to given AMQP queue for
     * given number of messages.
     *
     * @param queueName name of AMQP queue to listen to.
     *                  Will NOT be created if non-existing,
     *                  purpose is to create only in app!!
     *
     * @param numOfMessages expected number of messages, after this number
     *                      the receiver will end listening. If less events
     *                      than this number is sent, it will still listen.
     *                      Set to 0 for endless listening.
     * @throws Exception when listening fails, usually
     * because AMQP queue does not exist.
     */
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

    /**
     * Method used to determine timestamp from String for the measuring.
     *
     * @param input String-formatted timestamp expressing time when event was created.
     *
     * @return ZonedDateTime of this String, so it can be compared to current time.
     */
    private ZonedDateTime parseDate(String input) {
        final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME; //"yyyy-MM-dd'T'HH:mm:ss.SSSz" format
        return ZonedDateTime.parse(input, formatter);
    }

    /**
     * This method compares ZoneDateTime received from event with current time
     * and prints the result.
     *
     * @param timestamp Timestamp of event.
     */
    private void measureTime(ZonedDateTime timestamp) {
        ZonedDateTime currentTime = LocalDateTime.now().atZone(ZoneId.of("+02:00"));
        System.out.println("Event came in '"
                + TimeUnit.MILLISECONDS.convert(currentTime.getNano() - timestamp.getNano(), TimeUnit.NANOSECONDS)
                + "' milliseconds after its creation");
    }
}
