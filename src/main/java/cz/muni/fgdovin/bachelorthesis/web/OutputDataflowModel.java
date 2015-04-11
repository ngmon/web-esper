package cz.muni.fgdovin.bachelorthesis.web;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Filip Gdovin on 11. 4. 2015.
 */
public class OutputDataflowModel {

    private String dataflowName;
    private String firstEventType;
    private String secondEventType;
    private String thirdEventType;
    private String outputEventType;
    private String query;
    private String queueName;
    private String exchangeName;

    public OutputDataflowModel() {
    }

    //for one-stream output dataflows
    public OutputDataflowModel(String dataflowName, String firstEventType, String outputEventType, String query, String queueName, String exchangeName) {
        this.dataflowName = dataflowName;
        this.firstEventType = firstEventType;
        this.outputEventType = outputEventType;
        this.query = query;
        this.queueName = queueName;
        this.exchangeName = exchangeName;
    }

    //for two-stream output dataflows
    public OutputDataflowModel(String dataflowName, String firstEventType, String secondEventType, String outputEventType, String query, String queueName, String exchangeName) {
        this.dataflowName = dataflowName;
        this.firstEventType = firstEventType;
        this.secondEventType = secondEventType;
        this.outputEventType = outputEventType;
        this.query = query;
        this.queueName = queueName;
        this.exchangeName = exchangeName;
    }

    //for three-stream output dataflows
    public OutputDataflowModel(String dataflowName, String firstEventType, String secondEventType, String thirdEventType, String outputEventType, String query, String queueName, String exchangeName) {
        this.dataflowName = dataflowName;
        this.firstEventType = firstEventType;
        this.secondEventType = secondEventType;
        this.thirdEventType = thirdEventType;
        this.outputEventType = outputEventType;
        this.query = query;
        this.queueName = queueName;
        this.exchangeName = exchangeName;
    }

    public String getDataflowName() {
        return dataflowName;
    }

    public void setDataflowName(String dataflowName) {
        this.dataflowName = dataflowName;
    }

    public String getFirstEventType() {
        return firstEventType;
    }

    public void setFirstEventType(String firstEventType) {
        this.firstEventType = firstEventType;
    }

    public String getSecondEventType() {
        return secondEventType;
    }

    public void setSecondEventType(String secondEventType) {
        this.secondEventType = secondEventType;
    }

    public String getThirdEventType() {
        return thirdEventType;
    }

    public void setThirdEventType(String thirdEventType) {
        this.thirdEventType = thirdEventType;
    }

    public List<String> getAllEventTypes() {
        List<String> result = new ArrayList<String>();
        result.add(0, firstEventType);
        result.add(1, secondEventType);
        result.add(2, thirdEventType);
        return result;
    }

    public String getOutputEventType() {
        return outputEventType;
    }

    public void setOutputEventType(String outputEventType) {
        this.outputEventType = outputEventType;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
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
