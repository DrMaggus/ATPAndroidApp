package de.atp.data;

import java.util.Date;

import android.annotation.SuppressLint;

@SuppressLint("DefaultLocale")
public class Row {

	public final static int DATA_LENGTH = 8;

	private String code;
	private Date date;
	private Date alarmTime;
	private Date answerTime;
	private RowStatus status;
	private int contacts;
	private int hours;
	private int minutes;

	public Row(String code, Date date, Date alarmTime) {
		this(code, date, alarmTime, null, RowStatus.DIRTY, -1, -1, -1);
	}

	public Row(String code, Date date, Date alarmTime, Date answerTime,
			RowStatus status, int contacts, int hours, int minutes) {
		this.code = code;
		this.date = date;
		this.alarmTime = alarmTime;
		this.answerTime = answerTime;
		this.status = status;
		this.contacts = contacts;
		this.hours = hours;
		this.minutes = minutes;
	}

	/**
	 * @return The code of the proband
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            The code of the proband
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return Creation date of the row
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            Creation date of the row
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return The desired to ring the alarm
	 */
	public Date getAlarmTime() {
		return alarmTime;
	}

	/**
	 * @param alarmTime
	 *            The desired to ring the alarm
	 */
	public void setAlarmTime(Date alarmTime) {
		this.alarmTime = alarmTime;
	}

	/**
	 * @return Time where the user answered the request
	 */
	public Date getAnswerTime() {
		return answerTime;
	}

	/**
	 * @param answerTime
	 *            Time where the user answered the request
	 */
	public void setAnswerTime(Date answerTime) {
		this.answerTime = answerTime;
	}

	/**
	 * @return The status of the row, see {@link RowStatus} for more information
	 */
	public RowStatus getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            The status of the row, see {@link RowStatus} for more
	 *            information
	 */
	public void setStatus(RowStatus status) {
		this.status = status;
	}

	/**
	 * @return Number of social contacts
	 */
	public int getContacts() {
		return contacts;
	}

	/**
	 * @param contacts
	 *            Number of social contacts
	 */
	public void setContacts(int contacts) {
		this.contacts = contacts;
	}

	/**
	 * @return Time of social contact in hours
	 */
	public int getHours() {
		return hours;
	}

	/**
	 * @param hours
	 *            Time of social contact in hours
	 */
	public void setHours(int hours) {
		this.hours = hours;
	}

	/**
	 * @return Time of social contact in minutes
	 */
	public int getMinutes() {
		return minutes;
	}

	/**
	 * @param minutes
	 *            Time of social contact in hours
	 */
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	@Override
	protected Object clone() {
		return new Row(code, date, alarmTime, answerTime, status, contacts,
				hours, minutes);
	}

}