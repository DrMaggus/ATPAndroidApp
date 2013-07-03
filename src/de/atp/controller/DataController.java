package de.atp.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

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

        this.parser = new CSVParser(probandCode, HEAD);
        List<Row> rows = null;
        try {
            rows = parser.parse();
        } catch (Exception e) {
            rows = null;
        }
        if (rows == null)
            this.table = new DataTable();
        else
            this.table = new DataTable(rows);
    }

    /**
     * Construct a data controlling using the proband code
     * 
     * @param probandCode
     *            The unique proband code
     */
    private DataController(String probandCode) {
        this.probandCode = probandCode;
        this.table = new DataTable();
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
    public List<LocalTime> getTodaysAlarms() {
        List<LocalTime> res = new ArrayList<LocalTime>();

        DateTime today = new DateTime();

        // Search for the alarm times
        for (Row row : table.getRows()) {
            if (row.getDate().equals(today)) {
                res.add(row.getAlarmTime());
            }
        }

        return res;
    }

    // TODO: Fix me
    public LocalTime getNextAlarm() {
        List<LocalTime> alarms = getTodaysAlarms();
        LocalTime now = new LocalTime();
        for (LocalTime atpTime : alarms) {
            if (now.isAfter(atpTime) || now.equals(atpTime))
                return atpTime;
        }
        if (alarms.isEmpty())
            return new LocalTime();
        // Alarm not today
        return alarms.get(0);
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

        DateTime date = new DateTime();
        date = date.plusDays(1);

        LocalTime alarmTime = new LocalTime(alarmHour, alarmMinute);

        Row row = new Row(probandCode, date, alarmTime);

        table.addRow(row);
        parser.write(table.getRows());
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
        row.setAnswerTime(new LocalTime());
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
        DateTime date = cur.getDate();
        date = date.plusDays(1);

        LocalTime alarmTime = new LocalTime(cur.getAlarmTime());

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
        LocalTime now = new LocalTime();
        Row minimum = table.getRow(0);
        long min = Long.MAX_VALUE;
        List<Row> rows = table.getRows();
        for (int i = rows.size() - 1; i >= 0; --i) {
            Row row = rows.get(i);
            if (row.getAlarmTime().isAfter(now)) {
                long diff = now.compareTo(row.getAlarmTime());
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

        LocalTime oldTime = new LocalTime(oldHour, oldMinute);
        // Search for the old alarm time
        for (Row row : table.getRows()) {
            // Change only times for unused rows
            if (row.getStatus().equals(RowStatus.DIRTY)) {
                LocalTime alarmTime = new LocalTime(row.getAlarmTime());
                if (alarmTime.equals(oldTime)) {
                    row.setAlarmTime(new LocalTime(newHour, newMinute));
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
    public DateTime getLastAnsweredDate() {

        List<Row> rows = table.getRows();

        for (int i = rows.size() - 1; i >= 0; --i) {
            Row row = rows.get(i);
            if (row.getStatus().equals(RowStatus.OK)) {
                return new DateTime(row.getDate().getYear(), row.getDate().getMonthOfYear(), row.getDate().getDayOfMonth(), row.getAnswerTime().getHourOfDay(), row.getAnswerTime().getMinuteOfHour(), row.getAnswerTime().getSecondOfMinute());
            }
        }

        if (rows.isEmpty())
            return null;
        else {
            // return file creation date
            File f = ((CSVParser) parser).getFile();
            return new DateTime(f.lastModified());
        }
    }

}
