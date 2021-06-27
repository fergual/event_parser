package com.creditsuisse.service;

import com.creditsuisse.service.impl.ParserService;
import com.creditsuisse.service.impl.PersistEventService;
import com.creditsuisse.service.impl.ProcessEventService;
import com.creditsuisse.service.impl.domain.Event;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ProcessEventServiceTest {

    @Mock
    private ParserService parserService;

    @Mock
    private PersistEventService persistEventService;

    @InjectMocks
    private ProcessEventService processEventService;

    @Captor
    private ArgumentCaptor<Event> firstEventCapture;

    @Captor
    private ArgumentCaptor<Event> secondEventCapture;

    @Test
    public void processTest() throws URISyntaxException {

        List<Event> events = createMockEvents("ID1", "ID2", "ID2", "ID1");
        Mockito.when(parserService.parseEvent(Mockito.any(String.class)))
                .thenReturn(events.get(0), events.get(1), events.get(2), events.get(3));


        processEventService.process(loadTestFile("processEvent_fourEvents"));

        Mockito.verify(persistEventService, Mockito.times(2))
                .processEvent(firstEventCapture.capture(), secondEventCapture.capture());

        Mockito.verify(persistEventService, Mockito.times(1)).finish();

        List<Event> firstParamEvents = firstEventCapture.getAllValues();
        List<Event> secondParamEvents = secondEventCapture.getAllValues();

        Assert.assertEquals(2, firstParamEvents.size());
        Assert.assertEquals(2, secondParamEvents.size());

        Assert.assertEquals(events.get(1), firstParamEvents.get(0));
        Assert.assertEquals(events.get(2), secondParamEvents.get(0));

        Assert.assertEquals(events.get(0), firstParamEvents.get(1));
        Assert.assertEquals(events.get(3), secondParamEvents.get(1));
    }

    @Test
    public void processEmptyFileTest() throws URISyntaxException {
        processEventService.process(loadTestFile("processEvent_empty"));

        Mockito.verify(parserService, Mockito.times(0))
                .parseEvent(Mockito.anyString());
        Mockito.verify(persistEventService, Mockito.times(0))
                .processEvent(Mockito.any(), Mockito.any());
        Mockito.verify(persistEventService, Mockito.times(1))
                .finish();
    }


    private List<Event> createMockEvents(String... ids) {
        List<Event> events = new ArrayList<>();
        Event event;
        for (String id : ids) {
            event = Mockito.mock(Event.class);
            Mockito.when(event.getId()).thenReturn(id);
            events.add(event);
        }
        return events;
    }

    private File loadTestFile(String fileName) throws URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource(String.format("service/%s.log", fileName));
        Assert.assertNotNull(url);
        return new File(url.toURI());
    }
}
