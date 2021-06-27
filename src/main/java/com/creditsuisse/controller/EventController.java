package com.creditsuisse.controller;

import com.creditsuisse.exception.PersistenceException;
import com.creditsuisse.service.PersistEvent;
import com.creditsuisse.service.ProcessEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class EventController {
    private static final Logger LOG = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private ProcessEvent processEventService;

    @Autowired
    private PersistEvent persistEvent;

    public void process(String filePath) {
        File file = new File(filePath);

        // Amount of threads can be increased if required
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() ->  persistEvent.persist());
        processEventService.process(file);

        try {
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new PersistenceException("Couldn't finish persisting all data", e);
        }
    }
}
