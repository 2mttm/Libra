package org.vaadin.example.services;

import org.springframework.stereotype.Service;
import org.vaadin.example.entity.*;
import org.vaadin.example.repository.*;
import org.vaadin.example.security.SecurityService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CrmService {
    private final IssueRepository issueRepository;
    private final PosRepository posRepository;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final CityRepository cityRepository;
    private final ConnectionTypeRepository connectionTypeRepository;
    private final IssueTypeRepository issueTypeRepository;
    private final StatusRepository statusRepository;
    private final LogRepository logRepository;
    private final SecurityService securityService;

    public CrmService(IssueRepository issueRepository, PosRepository posRepository, UserRepository userRepository, UserGroupRepository userGroupRepository, CityRepository cityRepository, ConnectionTypeRepository connectionTypeRepository, IssueTypeRepository issueTypeRepository, StatusRepository statusRepository, LogRepository logRepository, SecurityService securityService) {
        this.issueRepository = issueRepository;
        this.posRepository = posRepository;
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
        this.cityRepository = cityRepository;
        this.connectionTypeRepository = connectionTypeRepository;
        this.issueTypeRepository = issueTypeRepository;
        this.statusRepository = statusRepository;
        this.logRepository = logRepository;
        this.securityService = securityService;
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

    public Issue saveIssue(Issue issue) {
        if (issue == null) {
            System.err.println("Issue is null. Are you sure you have connected your form to the application?");
            return null;
        }
        Issue oldIssue;
        Issue savedIssue;
        if (issue.getId() == null) {
            savedIssue = issueRepository.save(issue);
            saveLog(null, issue);
        } else {
            oldIssue = issueRepository.findById(issue.getId()).get();
            savedIssue = issueRepository.save(issue);
            saveLog(oldIssue, issue);
        }
        return savedIssue;
    }
    private void saveLog(Issue oldIssue, Issue newIssue){
        Log log = new Log();
        log.setIssue(newIssue);
        log.setUser(userRepository.findByLogin(securityService.getAuthenticatedUser().getUsername()));
        log.setInsertDate(LocalDateTime.now());
        if (oldIssue == null){
            log.setNotes(
                "Initial fields: " +
                (newIssue.getStatus() == null ? "" : "status ") +
                (newIssue.getType() == null ? "" : "type ") +
                (newIssue.getPriority() == 0 ? "" : "priority ") +
                (newIssue.getDescription() == null || newIssue.getDescription().isEmpty() ? "" : "description ") +
                (newIssue.getSolution() == null || newIssue.getSolution().isEmpty() ? "" : "solution ") +
                (newIssue.getMemo() == null || newIssue.getMemo().isEmpty() ? "" : "memo ")
            );
        } else {
            log.setNotes(
                "Updated fields: " +
                (newIssue.getStatus().getId().equals(oldIssue.getStatus().getId()) ? "" : "status ") +
                (newIssue.getType().getId().equals(oldIssue.getType().getId()) ? "" : "type ") +
                (newIssue.getPriority() == oldIssue.getPriority() ? "" : "priority ") +
                (newIssue.getDescription().equals(oldIssue.getDescription()) ? "" : "description ") +
                (newIssue.getSolution().equals(oldIssue.getSolution()) ? "" : "solution ") +
                (newIssue.getAssignedGroup().equals(oldIssue.getAssignedGroup()) ? "" : "assignee ") +
                (newIssue.getMemo().equals(oldIssue.getMemo()) ? "" : "memo ")
            );
        }

        String status = newIssue.getStatus().getName();
        if (status.equals("New"))
            log.setAction("Created");
        else if (status.equals("Closed"))
            log.setAction("Closed");
        else
            log.setAction("Modified");


        logRepository.save(log);
    }
    public User saveUser(User user) {
        if (user == null) {
            System.err.println("User is null. Are you sure you have connected your form to the application?");
            return null;
        }
        return userRepository.save(user);
    }
    public void deleteUser(User user){
        userRepository.delete(user);
    }
    public void savePos(Pos pos){
        posRepository.save(pos);
    }
    public void deletePos(Pos pos){
        posRepository.delete(pos);
    }

    public List<Pos> findAllPoses() {
        return posRepository.findAll();
    }
    public Optional<Pos> findPosById(Long id){
        return posRepository.findById(id);
    }

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }
    public List<UserGroup> findAllGroups(){
        return userGroupRepository.findAll();
    }
    public Optional<User> findUserById(Long id){
        return userRepository.findById(id);
    }
    public List<City> findAllCities(){
        return cityRepository.findAll();
    }
    public List<ConnectionType> findAllConnectionTypes(){
        return connectionTypeRepository.findAll();
    }

    public Optional<Issue> findIssueById(Long id) {
        return issueRepository.findById(id);
    }

    public List<IssueType> findAllIssueTypes() {
        return issueTypeRepository.findAll();
    }

    public List<Status> findAllStatuses() {
        return statusRepository.findAll();
    }

    public User findUserByUsername(String username) {
        return userRepository.findByLogin(username);
    }

    public List<Issue> findAllIssuesByStatus(Long statusId) {
        return issueRepository.findAllByStatus(statusRepository.findById(statusId).get());
    }

    public List<Log> findAllLogsByIssue(Issue issue) {
        return logRepository.findAllByIssue(issue);
    }
}