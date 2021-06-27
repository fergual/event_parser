package com.creditsuisse.service.impl;

import com.creditsuisse.exception.PersistenceException;
import com.creditsuisse.persistance.dao.DaoManager;
import com.creditsuisse.persistance.model.CompletedEvent;
import com.creditsuisse.service.PersistEvent;
import com.creditsuisse.service.impl.domain.Event;
import com.creditsuisse.service.impl.domain.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
public class PersistEventService implements PersistEvent {

    @Autowired
    private DaoManager<CompletedEvent> daoManager;

    private static final long TIMEOUT_OFFER = 10L; // Seconds
    private static final Logger LOG = LoggerFactory.getLogger(PersistEventService.class);
    private static final CompletedEvent EOF = new CompletedEvent();
    private final BlockingQueue<CompletedEvent> toPersist = new LinkedBlockingQueue<>();

    @Override
    public void processEvent(Event event1, Event event2) {
        CompletedEvent completedEvent = new CompletedEvent();
        completedEvent.setId(event1.getId());
        completedEvent.setHost(event1.getHost());
        completedEvent.setType(event1.getType());
        if (State.FINISHED.equals(event2.getState())) {
            completedEvent.setDuration(calculateDuration(event1, event2));
        } else {
            completedEvent.setDuration(calculateDuration(event2, event1));
        }

        if (completedEvent.getDuration() > 4) {
            completedEvent.setAlert(true);
        }

        try {
            toPersist.offer(completedEvent, TIMEOUT_OFFER, TimeUnit.SECONDS);
            LOG.debug("Added Event to Queue: " + completedEvent);
        }
        catch (RuntimeException | InterruptedException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void finish() {
        this.toPersist.offer(EOF);
        LOG.debug("Injected EOF");
    }

    @Override
    public void persist() {
        try {
            CompletedEvent event = this.toPersist.take();
            while (event != EOF) {
                LOG.debug("Removed Event from Queue: " + event);
                daoManager.persist(event);
                event = this.toPersist.take();
            }
        } catch (InterruptedException e) {
            throw new PersistenceException(e);
        }
    }

    private long calculateDuration(Event start, Event finished) {
        return finished.getTimestamp() - start.getTimestamp();
    }
}
