/**
 * Copyright(c) 2021 All rights reserved by Jungho Kim in MyungJi University 
 */

package Components.Student;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class StudentComponent {
	protected ArrayList<Student> vStudent;
	private String sStudentFileName;

	public StudentComponent(String sStudentFileName) throws FileNotFoundException, IOException {

		this.sStudentFileName = sStudentFileName;

		BufferedReader bufferedReader = new BufferedReader(new FileReader(sStudentFileName));
		this.vStudent = new ArrayList<Student>();
		while (bufferedReader.ready()) {
			String stuInfo = bufferedReader.readLine();
			if (!stuInfo.equals(""))
				this.vStudent.add(new Student(stuInfo));
		}
		bufferedReader.close();
	}

	public ArrayList<Student> getStudentList() {
		return vStudent;
	}

	public void setvStudent(ArrayList<Student> vStudent) {
		this.vStudent = vStudent;
	}

	public boolean isRegisteredStudent(String sSID) {
		for (int i = 0; i < this.vStudent.size(); i++) {
			if (((Student) this.vStudent.get(i)).match(sSID))
				return true;
		}
		return false;
	}

	public void updateStudentList() {
		try {
			new FileOutputStream(this.sStudentFileName).close();

			File studentFile = new File(this.sStudentFileName);
			FileWriter fwStu = new FileWriter(studentFile, true);

			for (Student temp : vStudent) {
				fwStu.write(temp.studentId + ' ');
				fwStu.write(temp.name + ' ');
				fwStu.write(temp.department + ' ');
				for (int i = 0; i < temp.completedCoursesList.size(); i++) {
					fwStu.write(temp.completedCoursesList.get(i) + ' ');
				}
				fwStu.write(0x0a);
				fwStu.write(0x0a);
				fwStu.write(0x0a);
				fwStu.write(0x0a);
			}
			fwStu.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
