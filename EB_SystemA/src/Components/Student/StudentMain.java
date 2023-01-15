/**
 * Copyright(c) 2021 All rights reserved by Jungho Kim in MyungJi University 
 */

package Components.Student;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.ArrayList;

import Framework.Event;
import Framework.EventId;
import Framework.EventQueue;
import Framework.RMIEventBus;

public class StudentMain {
	static boolean checkStudent = false;
	static boolean checkReservation = true;

	public static void main(String args[]) throws FileNotFoundException, IOException, NotBoundException {
		RMIEventBus eventBus = (RMIEventBus) Naming.lookup("EventBus");
		long componentId = eventBus.register();
		System.out.println("** StudentMain(ID:" + componentId + ") is successfully registered. \n");

		StudentComponent studentsList = new StudentComponent("../src/Students.txt");
		ReservationComponent reservationList = new ReservationComponent("../src/Reservations.txt");
		Event event = null;
		boolean done = false;

		while (!done) {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			EventQueue eventQueue = eventBus.getEventQueue(componentId);
			for (int i = 0; i < eventQueue.getSize(); i++) {
				event = eventQueue.getEvent();
				switch (event.getEventId()) {
				case ListStudents:
					printLogEvent("Get", event);
					eventBus.sendEvent(new Event(EventId.ClientOutput, makeStudentList(studentsList)));
					break;
				case RegisterStudents:
					printLogEvent("Get", event);
					eventBus.sendEvent(
							new Event(EventId.ClientOutput, registerStudent(studentsList, event.getMessage())));
					break;
				case DeleteStudents:
					printLogEvent("Get", event);
					eventBus.sendEvent(
							new Event(EventId.ClientOutput, deleteStudentInfo(studentsList, event.getMessage())));
					break;
				case checkStudentInfo:
					printLogEvent("Get", event);
					String messageTemp = checkStudentInfoFilter(studentsList, event.getMessage());

					if (checkStudent == true) {
						eventBus.sendEvent(new Event(EventId.checkCourseInfo, messageTemp));
					} else {
						eventBus.sendEvent(new Event(EventId.ClientOutput, messageTemp));
					}
					break;
				case Reservation:
					// time
					printLogEvent("Get", event);
					String messagetemp = checkStudentInfoFilter(studentsList, event.getMessage());

					eventBus.sendEvent(new Event(EventId.ClientOutput,
							reservation(studentsList, reservationList, messagetemp, event.getEvnetArr())));
					break;
				case ListReservation:
					printLogEvent("Get", event);
					eventBus.sendEvent(new Event(EventId.ClientOutput, makeReservation(reservationList)));
					break;
				case QuitTheSystem:
					printLogEvent("Get", event);
					eventBus.sendEvent(new Event(EventId.ClientOutput, updateStudentList(studentsList)));
					eventBus.unRegister(componentId);
					done = true;
					break;
				default:
					break;
				}
			}
		}
	}

	// show studentList
	private static String makeStudentList(StudentComponent studentsList) {
		String returnString = "";
		for (int j = 0; j < studentsList.vStudent.size(); j++) {
			returnString += studentsList.getStudentList().get(j).getString() + "\n";
		}
		return returnString;
	}

	// register Student Info
	private static String registerStudent(StudentComponent studentsList, String message) {
		Student student = new Student(message);
		if (!studentsList.isRegisteredStudent(student.studentId)) {
			studentsList.vStudent.add(student);
			return "This student is successfully added.";
		} else
			return "This student is already registered.";
	}

	// delete Student Info
	private static String deleteStudentInfo(StudentComponent studentsList, String message) {
		ArrayList<Student> vStudent = studentsList.getStudentList();
		for (Student student : vStudent) {
			if (student.match(message)) {
				if (vStudent.remove(student)) {
					return "This student is successfully deleted";
				} else {
					return "Error :: This studentInfo has problems";
				}
			}
		}
		return "Error :: This studentInfo has problems";
	}

	private static String checkStudentInfoFilter(StudentComponent studentsList, String message) {
		if (checkStudentInfo(studentsList, message)) {
			checkStudent = true;
			return message;
		} else {
			checkStudent = false;
			return "Reservation :: Fail!! studentInfo Not exist";
		}

	}

	private static boolean checkStudentInfo(StudentComponent studentsList, String message) {
		Reservation reservation = new Reservation(message);
		if (studentsList.isRegisteredStudent(reservation.getStudentId())) {
			return true;
		} else {
			return false;
		}
	}

	private static String reservation(StudentComponent studentsList, ReservationComponent reservationList,
			String message, ArrayList<String> eventArr) {
		Reservation reservation = new Reservation(message);

		ArrayList<String> studentCourseList = new ArrayList<String>();

		for (Student student : studentsList.vStudent) {
			if (student.match(reservation.getStudentId())) {
				studentCourseList = student.getCompletedCourses();
				if (studentCourseList.containsAll(eventArr)) {
					reservationList.vReservation.add(reservation);
					checkReservation = true;
					return "Reservation :: Success!!";
				} else {
					checkReservation = false;
					return "Reservation :: Fail!!!";
				}
			}
		}
		return "Reservation :: Fail!!!";
	}

	private static String makeReservation(ReservationComponent reservationList) {
		String returnString = "";
		for (int j = 0; j < reservationList.vReservation.size(); j++) {
			returnString += reservationList.getReservationList().get(j).getString() + "\n";
		}
		return returnString;
	}

	private static String updateStudentList(StudentComponent studentsList) {
		studentsList.updateStudentList();
		return "updateStudentList success !";
	}

	private static void printLogEvent(String comment, Event event) {
		System.out.println(
				"\n** " + comment + " the event(ID:" + event.getEventId() + ") message: " + event.getMessage());
	}
}
