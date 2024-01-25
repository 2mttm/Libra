package org.vaadin.example.services;

import org.springframework.stereotype.Service;
import org.vaadin.example.entity.Issue;
import org.vaadin.example.entity.Pos;
import org.vaadin.example.entity.User;
import org.vaadin.example.entity.UserGroup;
import org.vaadin.example.repository.IssueRepository;
import org.vaadin.example.repository.PosRepository;
import org.vaadin.example.repository.UserGroupRepository;
import org.vaadin.example.repository.UserRepository;

import java.util.List;

@Service
public class CrmService {
    private final IssueRepository issueRepository;
    private final PosRepository posRepository;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;

    public CrmService(IssueRepository issueRepository, PosRepository posRepository, UserRepository userRepository, UserGroupRepository userGroupRepository) {
        this.issueRepository = issueRepository;
        this.posRepository = posRepository;
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
    }

    public List<Issue> findAllIssues(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return issueRepository.findAll();
        } else {
            return issueRepository.search(stringFilter);
        }
    }
    public List<Issue> findAllIssues() {
        return issueRepository.findAll();
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
    public void saveUser(User user) {
        if (user == null) {
            System.err.println("User is null. Are you sure you have connected your form to the application?");
            return;
        }
        userRepository.save(user);
    }

    public List<Pos> findAllPoses() {
        return posRepository.findAll();
    }

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }
    public List<UserGroup> findAllGroups(){
        return userGroupRepository.findAll();
    }
}