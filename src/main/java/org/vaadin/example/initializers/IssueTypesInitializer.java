package org.vaadin.example.initializers;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.vaadin.example.entity.IssueType;
import org.vaadin.example.repository.IssueTypeRepository;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Component
public class IssueTypesInitializer implements ApplicationRunner {
    private final IssueTypeRepository issueTypeRepository;

    public IssueTypesInitializer(IssueTypeRepository issueTypeRepository) {
        this.issueTypeRepository = issueTypeRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Stream.of("IT Help", "Incident", "Problem", "New Feature", "Support")
                .forEach(this::saveItem);
    }
    private void saveItem(String name){
        if (issueTypeRepository.findByName(name) == null){
            IssueType issueType = new IssueType();
            issueType.setIssueLevel(1);
            issueType.setInsertDate(LocalDateTime.now());
            issueType.setName(name);

            issueTypeRepository.save(issueType);
        }
    }
}
