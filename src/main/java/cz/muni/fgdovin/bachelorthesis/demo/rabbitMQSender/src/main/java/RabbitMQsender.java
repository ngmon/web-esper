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
 *
 * @author Filip Gdovin
 * @version 28. 4. 2015
 */
public class RabbitMQsender {

    public void sendFileContents(String fileName, int sleepTime){

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
                    publish("inputQueue", toSend); //will be dropped till queue is declared (so, declare)
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

    private File loadFileFromResources(String fileName) throws NullPointerException {
//        return new File(RabbitMQsender.class.getClassLoader().getResource(fileName).getFile());
        return new File("C:\\" + fileName);
    }

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