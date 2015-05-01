package cz.muni.fgdovin.bachelorthesis.rabbit;

import org.nigajuan.rabbit.management.client.RabbitManagementApi;
import org.nigajuan.rabbit.management.client.domain.aliveness.Status;
import org.nigajuan.rabbit.management.client.domain.binding.queue.QueueBind;
import org.nigajuan.rabbit.management.client.domain.queue.Arguments;
import org.nigajuan.rabbit.management.client.domain.queue.Queue;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import retrofit.client.Response;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@inheritDoc}
 *
 * @author Filip Gdovin
 * @version 20. 4. 2015
 */
@Service
public class RabbitMqServiceImpl implements RabbitMqService {

    @Resource
    private Environment environment; //to load RabbitMQ server info from properties
    private RabbitManagementApi rabbitManagementApi;

    private String exchangeName;
    private String vhost;

    /**
     * Method called right after instance of this class is created.
     * Loads necessary values from config file and creates
     * instance of RabbitManagementApi for accessing
     * RabbitMQ REST interface.
     */
    @PostConstruct
    public void setUp() {
        String username = this.environment.getProperty("serverUsername");
        String password = this.environment.getProperty("serverPassword");
        String serverHost = this.environment.getProperty("serverHost");
        String serverPort = this.environment.getProperty("serverPort");
        this.exchangeName = this.environment.getProperty("exchangeName");
        this.vhost = this.environment.getProperty("vhost");

        String host = "http://" + serverHost + ":" + serverPort;
        this.rabbitManagementApi = RabbitManagementApi.newInstance(host, username, password);
    }

    /**
     * {@inheritDoc}
     */
    public String isAlive() {
        Status serverStatus = this.rabbitManagementApi.alivenessTest(this.vhost);  //takes vhost name as argument
        return serverStatus.getStatus();
    }

    /**
     * {@inheritDoc}
     */
    public boolean createQueue(String queueName) {
        //create new queue
        Queue newOne = new Queue();
        newOne.setAutoDelete(false);
        newOne.setDurable(false);
        newOne.setArguments(new Arguments());
        Response response = this.rabbitManagementApi.createQueue(this.vhost, queueName, newOne);
        if((response.getStatus() > 199) && (response.getStatus() < 300)) {
            //bind this queue to exchange
            QueueBind bind = new QueueBind();
            bind.setRoutingKey(queueName);
            response = this.rabbitManagementApi.bindExchangeToQueue(this.vhost, this.exchangeName, queueName, bind);
        }
        //TODO write this in better way?
        return ((response.getStatus() > 199) && (response.getStatus() < 300));
    }

    /**
     * {@inheritDoc}
     */
    public void deleteQueue(String queueName) {
        //TODO delete only queues bound to specified exchange?
        this.rabbitManagementApi.deleteQueue(this.vhost, queueName);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteAllQueues() {
        List<String> allQueues = this.listQueues();
        for(String queueName : allQueues) {
            this.deleteQueue(queueName);
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<String> listQueues() {
        //TODO show only queues bound to specified exchange?
        List<Queue> allQueues = this.rabbitManagementApi.listQueues();
        return allQueues.stream().map(Queue::getName).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    public String getExchangeName() {
        return this.exchangeName;
    }
}
