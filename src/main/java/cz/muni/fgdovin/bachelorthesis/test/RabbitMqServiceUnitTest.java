package cz.muni.fgdovin.bachelorthesis.test;

import cz.muni.fgdovin.bachelorthesis.rabbit.RabbitMqService;
import cz.muni.fgdovin.bachelorthesis.web.SpringBootApp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author Filip Gdovin
 * @version 28. 4. 2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringBootApp.class)
public class RabbitMqServiceUnitTest {

    @Autowired
    private RabbitMqService rabbitMqService;

    @Before
    public void resetEverything() {
        //reset AMQP queues
        this.rabbitMqService.deleteAllQueues();
    }

    @Test
    public void testQueueListShouldContainTwoQueues() {
        assertEquals(0, this.rabbitMqService.listQueues().size());
    }

    @Test
    public void testCreateNewQueue() {
        this.rabbitMqService.createQueue("someNewQueue");
        assertEquals(1, this.rabbitMqService.listQueues().size());
    }

    @Test
    public void testRecreateAlreadyDefinedQueue() {
        this.rabbitMqService.createQueue("someNewQueue");
        this.rabbitMqService.createQueue("someNewQueue");
        assertEquals(1, this.rabbitMqService.listQueues().size());
    }
}
