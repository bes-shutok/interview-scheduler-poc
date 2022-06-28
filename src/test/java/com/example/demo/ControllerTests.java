package com.example.demo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.controller.ScheduleRestController;
import com.example.demo.controller.UserRestController;
import com.example.demo.domain.Schedule;
import com.example.demo.domain.User;
import com.example.demo.domain.UserType;
import com.example.demo.dto.CreateUserRequestDto;
import com.example.demo.dto.TimeSlot;
import com.example.demo.repository.UserRepository;

import static java.time.DayOfWeek.*;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Assignment
 * For this assignment, please use the tech stack you feel most confortable with.
 *
 * Build an interview calendar API
 * There may be two roles that use this API, a candidate and an interviewer. A typical scenario is when:
 *
 * An interview slot is a 1-hour period of time that spreads from the beginning of any hour until the beginning
 * of the next hour. For example, a time span between 9am and 10am is a valid interview slot, whereas between
 * 9:30am and 10:30am is not.
 *
 * Each of the interviewers sets their availability slots. For example, the interviewer Ines is available next
 * week each day from 9am through 4pm without breaks and the interviewer Ingrid is available from 12pm
 * to 6pm on Monday and Wednesday next week, and from 9am to 12pm on Tuesday and Thursday.
 *
 * Each of the candidates sets their requested slots for the interview. For example,
 * the candidate Carl is available for the interview from 9am to 10am any weekday next week
 * and from 10am to 12pm on Wednesday.
 *
 * Anyone may then query the API to get a collection of periods of time when it’s possible to arrange
 * an interview for a particular candidate and one or more interviewers. In this example,
 * if the API queries for the candidate Carl and interviewers Ines and Ingrid,
 * the response should be a collection of 1-hour slots: from 9am to 10am on Tuesday, from 9am to 10am on Thursday.
 */
@SpringBootTest
public class ControllerTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRestController userRestController;
    @Autowired
    private ScheduleRestController scheduleRestController;

    private static final Set<DayOfWeek> WORK_DAYS = EnumSet.of(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY);
    private static final LocalDate START_OF_NEXT_WEEK;
    static {
        LocalDate date = LocalDate.now();
        do {
            date = date.plusDays(1);
        } while (!date.getDayOfWeek().equals(MONDAY));
        START_OF_NEXT_WEEK = date;
    }

    private static final List<Schedule> INES_SCHEDULES = List.of(
            new Schedule(
                    START_OF_NEXT_WEEK, START_OF_NEXT_WEEK.plusDays(6),
                    (short) 9, (short) 15,
                    WORK_DAYS
            )
    );
    private static final List<Schedule> INGRID_SCHEDULES = List.of(
            new Schedule(
                    START_OF_NEXT_WEEK, START_OF_NEXT_WEEK.plusDays(6),
                    (short) 12, (short) 17,
                    new TreeSet<>(Set.of(MONDAY, WEDNESDAY))
            ),
            new Schedule(
                    START_OF_NEXT_WEEK, START_OF_NEXT_WEEK.plusDays(6),
                    (short) 9, (short) 11,
                    new TreeSet<>(Set.of(TUESDAY, THURSDAY))
            )
    );
    private static final List<Schedule> CARL_SCHEDULES = List.of(
            new Schedule(
                    START_OF_NEXT_WEEK, START_OF_NEXT_WEEK.plusDays(6),
                    (short) 9, (short) 9,
                    WORK_DAYS
            ),
            new Schedule(
                    START_OF_NEXT_WEEK, START_OF_NEXT_WEEK.plusDays(6),
                    (short) 10, (short) 11,
                    new TreeSet<>(Set.of(WEDNESDAY))
            )
    );

    @AfterEach
    @BeforeEach
    void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    void createInterviewersAndCandidateWithSchedule_ShouldProvideInterviewSlots() {

        User interviewerInes = User.from(new CreateUserRequestDto("Ines", "test", UserType.INTERVIEWER));
        interviewerInes.setSchedules(INES_SCHEDULES);

        User interviewerIngrid = User.from(new CreateUserRequestDto("Ingrid", "test", UserType.INTERVIEWER));
        interviewerIngrid.setSchedules(INGRID_SCHEDULES);

        User candidateCarl = User.from(new CreateUserRequestDto("Carl", "test", UserType.CANDIDATE));
        candidateCarl.setSchedules(CARL_SCHEDULES);

        interviewerInes = userRestController.saveUser(interviewerInes);
        interviewerIngrid = userRestController.saveUser(interviewerIngrid);
        candidateCarl = userRestController.saveUser(candidateCarl);

        validateInterviewTimeSlots(interviewerInes, interviewerIngrid, candidateCarl);
    }

    @Test
    void createInterviewersAndCandidateThenAddSchedule_ShouldProvideInterviewSlots() {

        User interviewerInes = User.from(new CreateUserRequestDto("Ines", "test", UserType.INTERVIEWER));
        interviewerInes = userRestController.saveUser(interviewerInes);
        interviewerInes = userRestController.addSchedules(interviewerInes.getId(), INES_SCHEDULES);

        User interviewerIngrid = User.from(new CreateUserRequestDto("Ingrid", "test", UserType.INTERVIEWER));
        interviewerIngrid = userRestController.saveUser(interviewerIngrid);
        interviewerIngrid = userRestController.addSchedules(interviewerIngrid.getId(), INGRID_SCHEDULES);

        User candidateCarl = User.from(new CreateUserRequestDto("Carl", "test", UserType.CANDIDATE));
        candidateCarl = userRestController.saveUser(candidateCarl);
        candidateCarl = userRestController.addSchedules(candidateCarl.getId(), CARL_SCHEDULES);

        validateInterviewTimeSlots(interviewerInes, interviewerIngrid, candidateCarl);
    }

    private void validateInterviewTimeSlots(User interviewerInes, User interviewerIngrid, User candidateCarl) {
        Map<Long, List<TimeSlot>> interviewSchedules = userRestController.lookupInterviewSlots(candidateCarl.getId());
        assertFalse(interviewSchedules.isEmpty());
        Map<Long, List<TimeSlot>> expected = new HashMap<>();
        expected.put(
                interviewerInes.getId(),
                List.of(
                        new TimeSlot(
                                START_OF_NEXT_WEEK, START_OF_NEXT_WEEK.plusDays(6),
                                (short) 9, (short) 9,
                                new TreeSet<>(WORK_DAYS)
                        ),
                        new TimeSlot(
                                START_OF_NEXT_WEEK, START_OF_NEXT_WEEK.plusDays(6),
                                (short) 10, (short) 11,
                                new TreeSet<>(Set.of(WEDNESDAY))
                        )
                )
        );
        expected.put(
                interviewerIngrid.getId(),
                List.of(
                        new TimeSlot(
                                START_OF_NEXT_WEEK, START_OF_NEXT_WEEK.plusDays(6),
                                (short) 9, (short) 9,
                                new TreeSet<>(Set.of(TUESDAY, THURSDAY))
                        )
                )
        );
        assertEquals(expected, interviewSchedules);
    }

    @Test
    void createSchedule_ShouldBeTheSameAsPersisted() {
        User user = User.from(new CreateUserRequestDto("test", "test", UserType.ADMIN));
        user = userRestController.saveUser(user);
        Schedule schedule =
                new Schedule(
                        LocalDate.now(), LocalDate.now().plusDays(30),
                        (short) 10, (short) 15,
                        WORK_DAYS
                );
        user = userRestController.addSchedules(user.getId(), List.of(schedule));
        List<Schedule> schedules = scheduleRestController.findAllSchedules();
        assertFalse(schedules.isEmpty());
        assertEquals(1, schedules.size());
        assertEquals(1, user.getSchedules().size());
        assertEquals(user.getSchedules().get(0), schedules.get(0));
    }
}
