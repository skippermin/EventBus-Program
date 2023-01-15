package Components.Student;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ReservationComponent {
	protected ArrayList<Reservation> vReservation;
	private String sReservationFileName;

	public ReservationComponent(String sReservationFileName) throws FileNotFoundException, IOException {

		this.sReservationFileName = sReservationFileName;

		BufferedReader bufferedReader = new BufferedReader(new FileReader(sReservationFileName));
		this.vReservation = new ArrayList<Reservation>();
		while (bufferedReader.ready()) {
			String reservationInfo = bufferedReader.readLine();
			if (!reservationInfo.equals(""))
				this.vReservation.add(new Reservation(reservationInfo));
		}
		bufferedReader.close();
	}

	public ArrayList<Reservation> getReservationList() {
		return vReservation;
	}

	public void setvStudent(ArrayList<Reservation> vReservation) {
		this.vReservation = vReservation;
	}

	public boolean isRegisteredStudent(String sSID) {
		for (int i = 0; i < this.vReservation.size(); i++) {
			if (((Reservation) this.vReservation.get(i)).match(sSID))
				return true;
		}
		return false;
	}

	public void updateReservationList() {
		try {
			new FileOutputStream(this.sReservationFileName).close();

			File studentFile = new File(this.sReservationFileName);
			FileWriter fwStu = new FileWriter(studentFile, true);

			for (Reservation temp : vReservation) {
				fwStu.write(temp.studentId + ' ');
				fwStu.write(temp.courseId + ' ');

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
