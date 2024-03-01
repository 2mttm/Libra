package org.vaadin.example.initializers;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.vaadin.example.entity.Status;
import org.vaadin.example.repository.StatusRepository;

import java.util.stream.Stream;

@Component
public class StatusesInitializer implements ApplicationRunner {
    private final StatusRepository statusRepository;

    public StatusesInitializer(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Stream.of("New", "Pending", "Assigned", "In Progress", "Closed")
                .forEach(this::saveItem);
    }
    private void saveItem(String name){
        if (statusRepository.findByName(name) == null){
            Status status = new Status();
            status.setName(name);

            statusRepository.save(status);
        }
    }
}

