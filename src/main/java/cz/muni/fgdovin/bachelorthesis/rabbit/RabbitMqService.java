package cz.muni.fgdovin.bachelorthesis.rabbit;

import java.util.List;

/**
 * @author Filip Gdovin
 * @version 20. 4. 2015
 */
public interface RabbitMqService {

    public String isAlive();

    public boolean createQueue(String queueName);

    public void deleteQueue(String queueName);

    public List<String> listQueues();

    public void deleteAllQueues();

    public String getExchangeName();
}
