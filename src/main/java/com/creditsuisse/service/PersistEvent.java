package com.creditsuisse.service;

import com.creditsuisse.service.impl.domain.Event;

public interface PersistEvent {
    void processEvent(Event event1, Event event2);

    void finish();

    void persist();
}
