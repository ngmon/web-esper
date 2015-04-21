package cz.muni.fgdovin.bachelorthesis.rabbit;

import org.nigajuan.rabbit.management.client.RabbitManagementApi;
import org.nigajuan.rabbit.management.client.domain.aliveness.Status;
import org.nigajuan.rabbit.management.client.domain.exchange.Exchange;
import org.nigajuan.rabbit.management.client.domain.queue.Queue;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Filip Gdovin on 20. 4. 2015.
 */
@Service
public class RabbitMqServiceImpl implements RabbitMqService {

    @Resource
    private Environment environment; //to load RabbitMQ server info from properties
    private RabbitManagementApi rabbitManagementApi;
    private String host;

    @PostConstruct
    public void setUp() {
        String username = this.environment.getProperty("serverUsername");
        String password = this.environment.getProperty("serverPassword");
        this.host = "http://" + this.environment.getProperty("serverHost")
                + ":" + this.environment.getProperty("serverPort");
        this.rabbitManagementApi = RabbitManagementApi.newInstance(this.host, username, password);
    }

    public String isAlive() {
        Status serverStatus = this.rabbitManagementApi.alivenessTest("/");  //takes vhost name as argument
        return serverStatus.getStatus();
    }

    public List<String> listQueues() {
        List<Queue> allQueues = this.rabbitManagementApi.listQueues();
        List<String> result = allQueues.stream().map(Queue::getName).collect(Collectors.toList());

        return result;
    }

    public List<String> listExchanges() {
        List<Exchange> allExchanges = this.rabbitManagementApi.listExchanges();
        List<String> result = allExchanges.stream().map(Exchange::getName).collect(Collectors.toList());

        return result;
    }
}
