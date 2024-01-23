package org.vaadin.example.services;

import org.springframework.stereotype.Service;
import org.vaadin.example.entity.Issue;
import org.vaadin.example.entity.Pos;
import org.vaadin.example.entity.User;
import org.vaadin.example.repository.IssueRepository;
import org.vaadin.example.repository.PosRepository;
import org.vaadin.example.repository.UserRepository;

import java.util.List;

@Service
public class CrmService {
    private final IssueRepository issueRepository;
    private final PosRepository posRepository;
    private final UserRepository userRepository;

    public CrmService(IssueRepository issueRepository, PosRepository posRepository, UserRepository userRepository) {
        this.issueRepository = issueRepository;
        this.posRepository = posRepository;
        this.userRepository = userRepository;
    }

    public List<Issue> findAllIssues(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return issueRepository.findAll();
        } else {
            return issueRepository.search(stringFilter);
        }
    }

    public long countIssues() {
        return issueRepository.count();
    }

    public void deleteIssue(Issue issue) {
        issueRepository.delete(issue);
    }

    public void saveIssue(Issue issue) {
        if (issue == null) {
            System.err.println("Issue is null. Are you sure you have connected your form to the application?");
            return;
        }
        issueRepository.save(issue);
    }

    public List<Pos> findAllPoses() {
        return posRepository.findAll();
    }

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }
}