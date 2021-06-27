package com.creditsuisse;

import com.creditsuisse.controller.EventController;
import com.creditsuisse.exception.ApplicationException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private EventController eventController;

    @Autowired
    private ConfigurableApplicationContext context;

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        if (args.length == 0) {
            System.out.println("A valid log file path must be provided");
            closeApplication(ExitCode.ERROR.getValue());
        }
        int exitCode = ExitCode.SUCCESSFUL.getValue();

        try {
            eventController.process(args[0]);
            System.out.println("DONE - Events loaded to DB");
        }
        catch (ApplicationException e) {
            exitCode = ExitCode.ERROR.getValue();
            System.out.println(e.getMessage());
        }
        catch (Exception e) {
            exitCode = ExitCode.ERROR.getValue();
            System.out.println("Unexpected error.");
        }
        closeApplication(exitCode);
    }

    private void closeApplication(int exitCode) {
        SpringApplication.exit(context);
        System.exit(exitCode);
    }

    private enum ExitCode {
        SUCCESSFUL(0), ERROR(1);

        @Getter
        private int value;

        ExitCode (int value) {
            this.value = value;
        }
    }
}
