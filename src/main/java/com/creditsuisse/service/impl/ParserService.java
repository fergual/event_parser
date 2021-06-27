package com.creditsuisse.service.impl;

import com.creditsuisse.service.Parser;
import com.creditsuisse.service.impl.domain.Event;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

@Service
public class ParserService implements Parser {

    @Override
    public Event parseEvent(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Event.class);
    }

}
