package cz.muni.fgdovin.bachelorthesis.web;

/**
 * Created by Filip Gdovin on 26. 3. 2015.
 */
public class InputDataflowModel {

    private String dataflowName;
    private String eventType;
    private String queueName;
    private String exchangeName;

    public InputDataflowModel() {
    }

    //for input dataflows
    public InputDataflowModel(String dataflowName, String eventType, String queueName, String exchangeName) {
        this.dataflowName = dataflowName;
        this.eventType = eventType;
        this.queueName = queueName;
        this.exchangeName = exchangeName;
    }

    public String getDataflowName() {
        return dataflowName;
    }

    public void setDataflowName(String dataflowName) {
        this.dataflowName = dataflowName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }
}
