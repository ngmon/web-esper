package cz.muni.fgdovin.bachelorthesis.rabbit;

import java.util.List;

/**
 * Created by Filip Gdovin on 20. 4. 2015.
 */
public interface RabbitMqService {

    public String isAlive();

    public List<String> listQueues();

    public List<String> listExchanges();
}
