package de.atp.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.os.Environment;
import de.atp.data.DataTable;
import de.atp.data.Row;
import de.atp.data.RowStatus;
import de.atp.parser.Parser;
import de.atp.parser.csv.CSVParser;

public class DataController {

    private final String probandCode;

    private final DataTable table;
    private final Parser parser;

    private static DataController INSTANCE;

    private static File APP_DIR = Environment.getExternalStorageDirectory();

    /**
     * Search for a csv file and parse it
     * 
     * @return <code>Null</code> when there is no csv file, otherwise the unique
     *         instance
     */
    public static final DataController instance() {
        if (INSTANCE == null) {
            try {
                INSTANCE = new DataController();
            } catch (Exception e) {
                INSTANCE = null;
            }
        }
        return INSTANCE;
    }

    /**
     * Create a new datacontroller with fresh data for the proband code
     * 
     * @param code
     *            The code. When null, {@link #instance()} is called, otherwise
     *            it will create .csv file with the proband code
     * @return The unique instance of the data controller
     */
    public static final DataController instance(String code) {
        if (INSTANCE == null) {
            if (code == null)
                return instance();
            else
                INSTANCE = new DataController(code);
        }
        return INSTANCE;
    }

    /**
     * Looks up, if an file is existing. Does not create an instance of
     * DataController
     * 
     * @return <code>True</code>if, and only if, when one .csv file is found.
     *         False otherwise
     */
    public static final boolean isProbandFileExisting() {
        try {
            return new DataController() != null;
        } catch (CSVNotFoundException e) {
            return false;
        }
    }

    public static File getAppDir() {
        return APP_DIR;
    }

    /**
     * Head of the table
     */
    private final static String[] HEAD = {"Code", "Datum", "Alarmzeit", "Antwortzeit", "Abbruch", "Kontakte", "Stunden", "Minuten"};

    /**
     * Construct a data controlling and search for the proband code
     * 
     * @throws CSVNotFoundException
     *             Throws if the controller does not find any csv file
     */
    private DataController() throws CSVNotFoundException {
        this.probandCode = searchProbandCode();
        this.table = new DataTable(probandCode);
        this.parser = new CSVParser(probandCode, HEAD);
    }

    /**
     * Construct a data controlling using the proband code
     * 
     * @param probandCode
     *            The unique proband code
     */
    private DataController(String probandCode) {
        this.probandCode = probandCode;
        this.table = new DataTable(probandCode);
        this.parser = new CSVParser(probandCode, HEAD);
    }

    private String searchProbandCode() throws CSVNotFoundException {
        File f = DataController.getAppDir();

        if (!(f.isDirectory()))
            f = f.getParentFile();

        String[] fNames = f.list();

        if (fNames == null)
            throw new CSVNotFoundException();

        for (String string : fNames) {
            if (string.length() == "XXXXX.csv".length() && string.endsWith(".csv"))
                return string.substring(0, "XXXXX.csv".indexOf(".csv"));
        }

        throw new CSVNotFoundException();
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
        parser.write(table.getRows());
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
        return c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
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

        this.generateNextAlarm(row);

        table.updateRow(row);
        parser.write(table.getRows());
    }

    /**
     * Generate a new row using the information about the alarmtime from the
     * copy
     * 
     * @param cur
     *            Delivers information about the alarmtime and date
     */
    private void generateNextAlarm(Row cur) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(cur.getDate());
        cal.roll(Calendar.DAY_OF_MONTH, true);
        Date date = cal.getTime();

        cal.setTime(cur.getAlarmTime());
        cal.roll(Calendar.DAY_OF_MONTH, true);
        Date alarmTime = cal.getTime();

        Row row = new Row(probandCode, date, alarmTime);

        table.addRow(row);
        parser.write(table.getRows());
    }

    /**
     * The proband has aborted the question
     */
    public void abortedQuestion() {
        Row row = getCurrentRow();
        row.setStatus(RowStatus.ABORTED);
        table.updateRow(row);
        parser.write(table.getRows());
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
        List<Row> rows = table.getRows();
        for (int i = rows.size() - 1; i >= 0; --i) {
            Row row = rows.get(i);
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

    /**
     * Change the time of one alarm
     * 
     * @param oldHour
     *            The old time in hours (from 0-23)
     * @param oldMinute
     *            The old time in minutes (from 0-59)
     * @param newHour
     *            The new time in hours (from 0-23)
     * @param newMinute
     *            The new time in minutes (from 0-59)
     */
    public void changeAlarmTime(int oldHour, int oldMinute, int newHour, int newMinute) {
        Calendar cal = GregorianCalendar.getInstance();
        // Search for the old alarm time
        for (Row row : table.getRows()) {
            // Change only times for unused rows
            if (row.getStatus().equals(RowStatus.DIRTY)) {
                cal.setTime(row.getAlarmTime());
                // Old time matches the row
                if (cal.get(Calendar.HOUR_OF_DAY) == oldHour && cal.get(Calendar.MINUTE) == oldMinute) {
                    // Update the row
                    cal.set(Calendar.HOUR_OF_DAY, newHour);
                    cal.set(Calendar.MINUTE, newMinute);
                    row.setAlarmTime(cal.getTime());
                    table.updateRow(row);
                    parser.write(table.getRows());
                }
            }
        }
    }

    /**
     * Search for the Date when the proband has successfully answered the last
     * question
     * 
     * @return <code>Null</code> when the proband has never answered
     *         successfully a question, otherwise the date
     */
    public Date getLastAnsweredDate() {

        List<Row> rows = table.getRows();

        for (int i = rows.size() - 1; i >= 0; --i) {
            Row row = rows.get(i);
            if (row.getStatus().equals(RowStatus.OK))
                return row.getAnswerTime();
        }

        if (rows.isEmpty())
            return null;
        else
            return rows.get(0).getDate();
    }

}
