package com.creditsuisse.service.impl;

import com.creditsuisse.exception.ProcessingException;
import com.creditsuisse.service.Parser;
import com.creditsuisse.service.PersistEvent;
import com.creditsuisse.service.ProcessEvent;
import com.creditsuisse.service.impl.domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProcessEventService implements ProcessEvent {

    @Autowired
    private Parser parser;

    @Autowired
    private PersistEvent persistEvent;

    public void process(File file) {
        Map<String, Event> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {

                Event newEvent = parser.parseEvent(line);

                Event existingEvent = map.get(newEvent.getId());
                if (existingEvent == null) {
                    map.put(newEvent.getId(), newEvent);
                } else {
                    map.remove(newEvent.getId());
                    persistEvent.processEvent(existingEvent, newEvent);
                }
            }
        } catch (FileNotFoundException e) {
            throw new ProcessingException("File doesn't exist. File: " + file.toString(), e);
        } catch (IOException e) {
            throw new ProcessingException(e);
        } finally {
            persistEvent.finish();
        }
    }


}
