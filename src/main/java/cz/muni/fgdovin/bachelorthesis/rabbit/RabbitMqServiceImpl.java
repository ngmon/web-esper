package cz.muni.fgdovin.bachelorthesis.rabbit;

import org.nigajuan.rabbit.management.client.RabbitManagementApi;
import org.nigajuan.rabbit.management.client.domain.aliveness.Status;
import org.nigajuan.rabbit.management.client.domain.binding.queue.QueueBind;
import org.nigajuan.rabbit.management.client.domain.exchange.Exchange;
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
 * Created by Filip Gdovin on 20. 4. 2015.
 */
@Service
public class RabbitMqServiceImpl implements RabbitMqService {

    @Resource
    private Environment environment; //to load RabbitMQ server info from properties
    private RabbitManagementApi rabbitManagementApi;

    private Exchange exchange;
    private String exchangeName;
    private String vhost;
    private String host;

    @PostConstruct
    public void setUp() {
        String username = this.environment.getProperty("serverUsername");
        String password = this.environment.getProperty("serverPassword");
        this.exchangeName = this.environment.getProperty("exchangeName");
        this.vhost = this.environment.getProperty("vhost");
        this.host = "http://" + this.environment.getProperty("serverHost")
                + ":" + this.environment.getProperty("serverPort");
        this.rabbitManagementApi = RabbitManagementApi.newInstance(this.host, username, password);

        getExchange();
    }

    public String isAlive() {
        Status serverStatus = this.rabbitManagementApi.alivenessTest(this.vhost);  //takes vhost name as argument
        return serverStatus.getStatus();
    }

    private void getExchange() {
        this.exchange = this.rabbitManagementApi.listExchanges(this.vhost).get(0); //only one exchange in system
    }

    public String getExchangeName() {
        return this.exchangeName;
    }

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
        return ((response.getStatus() > 199) && (response.getStatus() < 300));
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
