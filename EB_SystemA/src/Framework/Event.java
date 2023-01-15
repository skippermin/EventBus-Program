/**
 * Copyright(c) 2021 All rights reserved by Jungho Kim in MyungJi University 
 */

package Framework;

import java.io.Serializable;
import java.util.ArrayList;

public class Event implements Serializable {
	private static final long serialVersionUID = 1L; // Default serializable value
	private String message;
	private EventId eventId;
	private ArrayList<String> eventArr;

	public Event(EventId id, String text) {
		this.message = text;
		this.eventId = id;
	}

	public Event(EventId id) {
		this.message = null;
		this.eventId = id;
	}

	public Event(EventId id, String text, ArrayList<String> eventArr) {
		this.message = text;
		this.eventId = id;
		this.eventArr = new ArrayList<String>();
		this.eventArr = eventArr;
	}

	public EventId getEventId() {
		return eventId;
	}

	public String getMessage() {
		return message;
	}

	public void setEvnetArr(ArrayList<String> eventArr) {
		this.eventArr = eventArr;
	}

	public ArrayList<String> getEvnetArr() {
		return this.eventArr;
	}
}