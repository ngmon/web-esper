package cz.muni.fgdovin.bachelorthesis.demo.rabbitMQSender.src.main.java;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class is used to send event to given AMQP queue,
 * with option of setting time between each sending.
 * Also, when event contains attribute with value of
 * "generateTimestampHere", current timestamp in String
 * format is generated and inserted.
 *
 * @author Filip Gdovin
 * @version 28. 4. 2015
 */
public class RabbitMQsender {

    /**
     * Method used to send contents of file to given AMQP queue.
     *
     * @param fileName String containing name of file located in Resources.
     *
     * @param queueName String with name of AMQP queue which should be used to
     *                  send events. If queue with this name does not exist,
     *                  it is not created and nothing is sent.
     *
     * @param sleepTime Number representing sleep time in milliseconds,
     *                  which can be used to delay event sending.
     */

    public void sendFileContents(String fileName, String queueName, int sleepTime){

        File myFile = loadFileFromResources(fileName);

        try{
            //send contents of file
            FileReader inputFile = new FileReader(myFile);
            BufferedReader bufferReader = new BufferedReader(inputFile);
            String line = bufferReader.readLine();

            do {
                Thread.sleep(sleepTime);
                if(line != null) {
                    String toSend = addTimestamp(line);
                    publish(queueName, toSend); //will be dropped till queue is declared (so, declare)
                    System.out.println("Sending '" + toSend + "' from file " + myFile.getAbsolutePath());
                    line = bufferReader.readLine();
                }
            } while (line != null);
            bufferReader.close();
        }
        catch(Exception ex){
            System.out.println("Error while reading file line by line: " + ex.getMessage() + ex.getCause());
            return;
        }
        System.out.println("Everything sent without errors\n");
    }

    /**
     * Method used for actual publishing, it takes name of queue
     * and String representing message contents.
     *
     * @param queueName Name of AMQP queue to send events into.
     *
     * @param message String message to send.
     *
     * @throws Exception In case publishing fails due to non-existing queue
     * or similar reasons.
     */
    private void publish(String queueName, String message) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String EXCHANGE_NAME = "amq.direct";
        channel.basicPublish(EXCHANGE_NAME, queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());

        channel.close();
        connection.close();
    }

    /**
     * This method loads file from resources by specifying its name.
     *
     * @param fileName Name of file to be loaded.
     *
     * @return File with given name from resources.
     *
     * @throws NullPointerException when file does not exist.
     */
    private File loadFileFromResources(String fileName) throws NullPointerException {
        return new File(RabbitMQsender.class.getClassLoader().getResource(fileName).getFile());
    }

    /**
     * Method generating timestamp into each event which is designed for it,
     * namely by providing exact place where to insert timestamp.
     * This is done by using pattern "generateTimestampHere"
     * somewhere in event. Current timestamp in ISO 8601 will
     * be placed in this place.
     *
     * @param line String containing event.
     *
     * @return Modified String of the same event,
     * with generated timestamp.
     */
    //TODO generate always with creation of necessary attribute?
    private String addTimestamp(String line) {
        final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        ZonedDateTime localTime = LocalDateTime.now().atZone(ZoneId.of("+02:00"));
        String currTimeString = localTime.format(formatter);
        String pattern = "generateTimestampHere";
        int index = line.indexOf(pattern);
        if(index < 0) {
            return line;
        }
        return line.substring(0, index) + currTimeString + line.substring(index + pattern.length());
    }
}