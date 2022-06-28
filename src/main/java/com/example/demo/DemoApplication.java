package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * This is a demo (POC) project to implement below requirements.
 * See test for the proposed pipeline in
 * {@link ControllerTests#createInterviewersAndCandidateThenAddSchedule_ShouldProvideInterviewSlots()}  }
 *
 * Assignment
 * For this assignment, please use the tech stack you feel most comfortable with.
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
@SuppressWarnings("JavadocReference")
@EntityScan
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
