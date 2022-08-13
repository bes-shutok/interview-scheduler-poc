package com.example.demo.controller;

import com.example.demo.domain.Schedule;
import com.example.demo.domain.User;
import com.example.demo.domain.UserType;
import com.example.demo.dto.TimeSlot;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(UserRestController.PATH)
@Transactional
public class UserRestController {
    public static final String PATH = "/api/users";

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/")
    public User saveUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PostMapping ("/{id}/schedules/")
    public User addSchedules(@PathVariable Long id, @RequestBody List<Schedule> schedules) {
        schedules.forEach(
                s -> Schedule.validate(
                        s.getStartDate(), s.getLastDate(),
                        s.getStartHour(), s.getLastHour()
                )
        );
        User user = userRepository.findById(id).orElseThrow();
        user.setSchedules(schedules);
        return user;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    @GetMapping("/")
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /*Returns a map of objects where key is the id of the Interviewer and value - intersection of timeslots
    * of this interviewer and the candidate*/
    @GetMapping("/candidates/{id}/interview_slots")
    public Map<Long, List<TimeSlot>> lookupInterviewSlots(@PathVariable Long id) {

        User candidate = userRepository
                .findByIdEquals(id)
                .orElseThrow();
        Assert.isTrue(candidate.getUserType().equals(UserType.CANDIDATE), "Found user is not a CANDIDATE!");

        List<Schedule> candidateSchedules = candidate.getSchedules();

        List<User> interviewers = userRepository.findAll().stream()
                .filter(u -> u.getUserType().equals(UserType.INTERVIEWER))
                .toList();

        Map<Long, List<TimeSlot>> interviewSlots = new HashMap<>();

        for (User interviewer : interviewers) {
            List<TimeSlot> intersections = interviewer.getSchedules().stream()
                    .flatMap(
                            interviewerSchedule -> candidateSchedules.stream().
                                    map(
                                            candidateSchedule ->
                                                    Schedule.intersection(candidateSchedule, interviewerSchedule)
                                    )
                    )
                    .filter(Objects::nonNull)
                    .toList();
            if (!intersections.isEmpty()) {
                interviewSlots.put(interviewer.getId(), intersections);
            }
        }

        return interviewSlots;
    }
}
