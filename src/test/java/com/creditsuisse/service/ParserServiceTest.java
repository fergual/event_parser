package com.creditsuisse.service;

import com.creditsuisse.Application;
import com.creditsuisse.service.impl.domain.Event;
import com.creditsuisse.service.impl.domain.State;
import com.google.gson.JsonSyntaxException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
public class ParserServiceTest {

    @Autowired
    private Parser parser;

    @Test
    public void parseFullEventTest() {
        String fullJson = "{\"id\":\"scsmbstgra\", \"state\":\"STARTED\", " +
                "\"type\":\"APPLICATION_LOG\", \"host\":\"12345\"," +
                " \"timestamp\":1491377495212}";

        Event event = parser.parseEvent(fullJson);
        Assert.assertEquals("scsmbstgra", event.getId());
        Assert.assertEquals(State.STARTED, event.getState());
        Assert.assertEquals("APPLICATION_LOG", event.getType());
        Assert.assertEquals("12345", event.getHost());
        Assert.assertEquals(1491377495212L, event.getTimestamp());
    }

    @Test
    public void parseIncompleteEventTest() {
        String partialJson = "{\"id\":\"scsmbstgrb\", \"state\":\"FINISHED\"," +
                " \"timestamp\":1491377495213}";

        Event event = parser.parseEvent(partialJson);
        Assert.assertEquals("scsmbstgrb", event.getId());
        Assert.assertEquals(State.FINISHED, event.getState());
        Assert.assertNull(event.getType());
        Assert.assertNull(event.getHost());
        Assert.assertEquals(1491377495213L, event.getTimestamp());
    }

    @Test
    public void parseOnlyIdTest() {
        String onlyIdJson = "{\"id\":\"scsmbstgrb\"}";

        Event event = parser.parseEvent(onlyIdJson);
        Assert.assertEquals("scsmbstgrb", event.getId());
        Assert.assertNull(event.getState());
        Assert.assertNull(event.getType());
        Assert.assertNull(event.getHost());
        Assert.assertEquals(0L, event.getTimestamp());
    }

    @Test
    public void parseEmptyTest() {
        Event event = parser.parseEvent("");
        Assert.assertNull(event);
    }

    @Test(expected = JsonSyntaxException.class)
    public void parseMalformedTest() {
        String malformedJson = "{\"id\":\"scsmbstgrb\", {}\"state\":\"FINISHED\"}";

        parser.parseEvent(malformedJson);
    }
}
