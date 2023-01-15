/**
 * Copyright(c) 2021 All rights reserved by Jungho Kim in MyungJi University 
 */
package Components.Course;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.ArrayList;

import Components.Student.Reservation;
import Components.Student.Student;
import Components.Student.StudentComponent;
import Framework.Event;
import Framework.EventId;
import Framework.EventQueue;
import Framework.RMIEventBus;

public class CourseMain {
	static boolean checkCourse = false;

	public static void main(String[] args) throws FileNotFoundException, IOException, NotBoundException {

		RMIEventBus eventBus = (RMIEventBus) Naming.lookup("EventBus");
		long componentId = eventBus.register();

		System.out.println("CourseMain (ID:" + componentId + ") is successfully registered...");

		CourseComponent coursesList = new CourseComponent("../src/Courses.txt");
		Event event = null;
		boolean done = false;

		while (!done) {
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			EventQueue eventQueue = eventBus.getEventQueue(componentId);

			for (int i = 0; i < eventQueue.getSize(); i++) {
				event = eventQueue.getEvent();
				switch (event.getEventId()) {
				case ListCourses:
					printLogEvent("Get", event);
					eventBus.sendEvent(new Event(EventId.ClientOutput, makeCourseList(coursesList)));
					break;
				case RegisterCourses:
					printLogEvent("Get", event);
					eventBus.sendEvent(
							new Event(EventId.ClientOutput, registerCourse(coursesList, event.getMessage())));
					break;
				case DeleteCourses:
					printLogEvent("Get", event);
					eventBus.sendEvent(
							new Event(EventId.ClientOutput, deleteCourseInfo(coursesList, event.getMessage())));
					break;
				case checkCourseInfo:
					printLogEvent("Get", event);
					String messageTemp = checkCourseInfoFilter(coursesList, event.getMessage());

					if (checkCourse == true) {
						ArrayList<String> eventArr = getPreCourseList(coursesList, messageTemp);
						eventBus.sendEvent(new Event(EventId.Reservation, messageTemp, eventArr));
					} else {
						eventBus.sendEvent(new Event(EventId.ClientOutput, messageTemp));
					}
					break;
				case QuitTheSystem:
					printLogEvent("Get", event);
					eventBus.sendEvent(new Event(EventId.ClientOutput, updateCourseList(coursesList)));
					eventBus.unRegister(componentId);
					done = true;
					break;
				default:
					break;
				}
			}
		}
	}

	private static String makeCourseList(CourseComponent coursesList) {
		String returnString = "";
		for (int j = 0; j < coursesList.vCourse.size(); j++) {
			returnString += coursesList.getCourseList().get(j).getString() + "\n";
		}
		return returnString;
	}

	private static String registerCourse(CourseComponent coursesList, String message) {
		Course course = new Course(message);
		if (!coursesList.isRegisteredCourse(course.courseId)) {
			coursesList.vCourse.add(course);
			return "This course is successfully added.";
		} else
			return "This course is already registered.";
	}

	private static String deleteCourseInfo(CourseComponent coursesList, String message) {
		ArrayList<Course> vCourse = coursesList.getCourseList();
		for (Course course : vCourse) {
			if (course.match(message)) {
				if (vCourse.remove(course)) {
					return "This course is succesfully deleted";
				} else {
					return "Error :: This courseInfo has problems";
				}
			}
		}
		return "Error :: This courseInfo has problems";
	}

	private static String checkCourseInfoFilter(CourseComponent coursesList, String message) {
		if (checkCourseInfo(coursesList, message)) {
			checkCourse = true;
			return message;
		} else {
			checkCourse = false;
			return "Reservation :: Fail!! courseInfo Not exist";
		}
	}

	private static boolean checkCourseInfo(CourseComponent coursesList, String message) {
		Reservation reservation = new Reservation(message);
		if (coursesList.isRegisteredCourse(reservation.getCoursetId())) {
			return true;
		} else {
			return false;
		}
	}

	private static ArrayList<String> getPreCourseList(CourseComponent coursesList, String message) {
		Reservation reservation = new Reservation(message);
		ArrayList<Course> vCourse = coursesList.getCourseList();
		ArrayList<String> preCourseList = new ArrayList<String>();

		for (Course course : vCourse) {
			if (course.match(reservation.getCoursetId())) {
				preCourseList = course.getPreCourseList();
				return preCourseList;
			}
		}
		return preCourseList;
	}

	private static String updateCourseList(CourseComponent coursesList) {
		coursesList.updateCourseList();
		return "updateCourseList success !";
	}

	private static void printLogEvent(String comment, Event event) {
		System.out.println(
				"\n** " + comment + " the event(ID:" + event.getEventId() + ") message: " + event.getMessage());
	}
}
