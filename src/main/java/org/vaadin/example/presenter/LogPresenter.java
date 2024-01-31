package org.vaadin.example.presenter;

import org.springframework.stereotype.Service;
import org.vaadin.example.entity.Issue;
import org.vaadin.example.entity.Log;
import org.vaadin.example.services.CrmService;

import java.util.List;
@Service
public class LogPresenter {
    private final CrmService crmService;

    public LogPresenter(CrmService crmService) {
        this.crmService = crmService;
    }
    public List<Log> findAllLogsByIssue(Issue issue){
        return crmService.findAllLogsByIssue(issue);
    }
}
