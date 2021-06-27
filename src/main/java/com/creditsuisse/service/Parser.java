package com.creditsuisse.service;

import com.creditsuisse.service.impl.domain.Event;

public interface Parser {
    Event parseEvent(String json);
}
