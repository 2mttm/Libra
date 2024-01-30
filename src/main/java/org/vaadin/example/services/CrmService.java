package org.vaadin.example.services;

import org.springframework.stereotype.Service;
import org.vaadin.example.entity.*;
import org.vaadin.example.repository.*;

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

    public CrmService(IssueRepository issueRepository, PosRepository posRepository, UserRepository userRepository, UserGroupRepository userGroupRepository, CityRepository cityRepository, ConnectionTypeRepository connectionTypeRepository) {
        this.issueRepository = issueRepository;
        this.posRepository = posRepository;
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
        this.cityRepository = cityRepository;
        this.connectionTypeRepository = connectionTypeRepository;
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

}