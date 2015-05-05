package cz.muni.fgdovin.bachelorthesis.rabbit;

import org.nigajuan.rabbit.management.client.domain.exchange.Exchange;

import java.util.List;
import java.util.Map;

/**
 * This interface is used to communicate with RabbitMQ API library,
 * in particular to:
 * test if server is responding
 * add/remove//show all queues
 * show(by name) exchange
 *
 * @author Filip Gdovin
 * @version 20. 4. 2015
 */
public interface RabbitMqService {

    /**
     * This method is used to test whether RabbitMQ server
     * configured in config.properties is available and responding.
     *
     * @return Returns string "ok" in case server is ready,
     * something else otherwise.
     */
    public String isAlive();

    /**
     * This method creates new AMQP queue bound to implicit (output) exchange.
     *
     * @param queueName Name used to create queue with,
     *                  also serves as routing key to
     *                  define binding to exchange.
     *
     * @return Returns true if creation returned with HTTP code
     * meaning success, false otherwise.
     */
    public boolean createQueue(String queueName);

    /**
     * This method creates new AMQP queue bound to specific exchange.
     *
     * @param queueName Name used to create queue with,
     *                  also serves as routing key to
     *                  define binding to exchange.
     *
     * @param exchangeName Name used to define exchange for binding.
     *
     * @return Returns true if creation returned with HTTP code
     * meaning success, false otherwise.
     */
    public boolean createQueue(String queueName, String exchangeName);

    /**
     * Method used to delete queue with given name.
     *
     * @param queueName String describing name of the queue.
     */
    public void deleteQueue(String queueName);

    /**
     * Method used to delete all queues present.
     * (used only for testing)
     */
    public void deleteAllQueues();

    /**
     * Method returns list of all queue names bound to Esper Root exchange.
     *
     * @return List of all queue names.
     */
    public List<String> listQueues();

    /**
     * Method returns list of all queue names bound to specific exchange.
     *
     * @return List of all queue names.
     */
    public List<String> listQueues(String exchangeName);

    /**
     * Method returns list of all Esper exchanges known to RabbitMQ server.
     *
     * @return List of all Esper exchanges.
     */
    public List<Exchange> listExchanges();

    public List<String> listExchangeNames();

    /**
     * Method returns flatted map of JSON schema for given exchange.
     *
     * @return flatted map of JSON schema.
     */
    public Map<String, Object> getSchemaForExchange(String exchangeName);

    /**
     * Method used for convenient way to find which exchange is loaded from
     * config file and therefore used in entire application.
     *
     * @return String containing name of current exchange.
     */
    public String getExchangeName();
}
