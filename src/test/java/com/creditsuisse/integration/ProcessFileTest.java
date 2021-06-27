package com.creditsuisse.integration;

import com.creditsuisse.Application;
import com.creditsuisse.controller.EventController;
import com.creditsuisse.persistance.dao.DaoManager;
import com.creditsuisse.persistance.model.CompletedEvent;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)

public class ProcessFileTest {

    @Autowired
    private EventController eventController;

    @Autowired
    private DaoManager<CompletedEvent> daoManager;

    private static Set<CompletedEvent> expectedEvents = new HashSet<>();

    @BeforeClass
    public static void setUp() {
        CompletedEvent event1 = new CompletedEvent();
        CompletedEvent event2 = new CompletedEvent();
        CompletedEvent event3 = new CompletedEvent();

        event1.setId("scsmbstgra");
        event1.setAlert(true);
        event1.setDuration(5);
        event1.setType("APPLICATION_LOG");
        event1.setHost("12345");

        event2.setId("scsmbstgrb");
        event2.setAlert(false);
        event2.setDuration(3);

        event3.setId("scsmbstgrc");
        event3.setAlert(true);
        event3.setDuration(8);
        expectedEvents.add(event1);
        expectedEvents.add(event2);
        expectedEvents.add(event3);
    }

    @Test
    public void testEventsLoaded() throws URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("input.log");
        Assert.assertNotNull(url);

        eventController.process(url.toURI().getPath());
        List<CompletedEvent> actualEvents = daoManager.findAll();
        Assert.assertEquals(expectedEvents.size(), actualEvents.size());

        // remove returns true if object was found in list
        actualEvents.forEach(actual -> Assert.assertTrue(expectedEvents.remove(actual)));
    }
}
