package de.atp.controller;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.atp.data.DataTable;
import de.atp.data.Row;
import de.atp.data.RowStatus;

public class DataController {

	private final String probandCode;
	private final DataTable table;

	/**
	 * Construct a data controlling and search for the proband code
	 */
	public DataController() {
		this.probandCode = searchProbandCode();
		this.table = new DataTable(probandCode);
	}

	/**
	 * Construct a data controlling using the proband code
	 * 
	 * @param probandCode
	 *            The unique proband code
	 */
	public DataController(String probandCode) {
		this.probandCode = probandCode;
		this.table = new DataTable(probandCode);
	}

	@SuppressWarnings("deprecation")
	private String searchProbandCode() {
		File f = new File("").getParentFile();
		String[] fNames = f.list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".csv");
			}
		});

		if (fNames.length != 1) {
			Logger.global.log(Level.SEVERE,
					"More than one .csv files in the system");
			return null;
		}
		return fNames[0].substring(0, fNames[0].indexOf(".csv"));

	}

	/**
	 * @return A list containing todays alarm times
	 */
	public List<Date> getTodaysAlarms() {
		List<Date> res = new ArrayList<Date>();

		// Todays calendar
		Calendar today = GregorianCalendar.getInstance();
		// Calendar for values from the table
		Calendar date = GregorianCalendar.getInstance();

		// Search for the alarm times
		for (Row row : table.getRows()) {
			date.setTime(row.getDate());

			// Entry in table has the same date as today
			if (sameDay(today, date)) {
				res.add(row.getAlarmTime());
			}
		}

		return res;
	}

	/**
	 * Generate a new row using the information about the alarmtime from the
	 * copy
	 * 
	 * @param copy
	 *            Delivers information about the alarmtime and date
	 */
	public void generateNewRow(Row copy) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(copy.getDate());
		cal.roll(Calendar.DAY_OF_MONTH, true);
		Date date = cal.getTime();

		cal.setTime(copy.getAlarmTime());
		cal.roll(Calendar.DAY_OF_MONTH, true);
		Date alarmTime = cal.getTime();

		Row row = new Row(probandCode, date, alarmTime);

		table.addRow(row);
	}

	/**
	 * Create a new row using the alarm hour and alarm minute. Use this function
	 * after first creation of alarms
	 * 
	 * @param alarmHour
	 *            Time from 1-23
	 * @param alarmMinute
	 *            Time from 1-59
	 */
	public void createDummyRow(int alarmHour, int alarmMinute) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.roll(Calendar.DAY_OF_MONTH, true);
		Date date = cal.getTime();

		cal.set(Calendar.HOUR_OF_DAY, alarmHour);
		cal.set(Calendar.MINUTE, alarmMinute);

		Date alarmTime = cal.getTime();
		Row row = new Row(probandCode, date, alarmTime);

		table.addRow(row);
	}

	/**
	 * Checks if both dates are on the same day
	 * 
	 * @param c1
	 *            Date one
	 * @param c2
	 *            Date two
	 * @return True, when both cal have the same day
	 */
	private boolean sameDay(Calendar c1, Calendar c2) {
		return c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)
				&& c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
				&& c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
	}

	/**
	 * The proband has completed a questions. This function persists the data
	 * 
	 * @param hour
	 *            How many hours of social contact
	 * @param minutes
	 *            How many minutes of social contact
	 * @param contacts
	 *            How many social contacts
	 */
	public void completeQuestions(int hour, int minutes, int contacts) {

		Row row = getCurrentRow();
		row.setAnswerTime(new Date());
		row.setStatus(RowStatus.OK);
		row.setHours(hour);
		row.setMinutes(minutes);
		row.setContacts(contacts);

		table.updateRow(row);
	}

	/**
	 * The proband has aborted the question
	 */
	public void abortedQuestion() {
		Row row = getCurrentRow();
		row.setStatus(RowStatus.ABORTED);
		table.updateRow(row);
	}

	/**
	 * Search for the row containing the alarm time which is the most current
	 * time
	 * 
	 * @return
	 */
	private Row getCurrentRow() {
		Date now = new Date();
		Row minimum = table.getRow(0);
		long min = Long.MAX_VALUE;
		for (Row row : table.getRows()) {
			if (row.getAlarmTime().after(now)) {
				long diff = now.getTime() - row.getAlarmTime().getTime();
				if (diff <= min) {
					min = diff;
					minimum = row;
				}
			}
		}
		return minimum;
	}
}
