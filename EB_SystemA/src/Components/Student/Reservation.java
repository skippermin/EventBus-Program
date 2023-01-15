package Components.Student;

import java.util.StringTokenizer;

public class Reservation {
	protected String studentId;
	protected String courseId;

	public Reservation(String inputString) {
		StringTokenizer stringTokenizer = new StringTokenizer(inputString);
		this.studentId = stringTokenizer.nextToken();
		this.courseId = stringTokenizer.nextToken();
	}

	public boolean match(String studentId) {
		return this.studentId.equals(studentId);
	}

	public String getStudentId() {
		return this.studentId;
	}

	public String getCoursetId() {
		return this.courseId;
	}

	public String getString() {
		String stringReturn = this.studentId + " " + this.courseId;
		return stringReturn;
	}
}
