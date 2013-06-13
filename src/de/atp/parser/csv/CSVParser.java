package de.atp.parser.csv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import de.atp.data.Row;
import de.atp.data.RowStatus;
import de.atp.parser.InvalidFormatException;
import de.atp.parser.Parser;
import de.atp.parser.RowConverter;

@SuppressLint("DefaultLocale")
public class CSVParser implements Parser, RowConverter {

	private File csvFile;

	private String[] head;

	/**
	 * Create a parser to read and write a CSV file
	 * 
	 * @param probandCode
	 *            The code of the proband - name conversion by task giver
	 * @param head
	 *            The head of the file - the first line
	 */
	public CSVParser(String probandCode, String[] head) {
		this.head = head;

		this.csvFile = new File(probandCode + ".csv");
		if (!csvFile.exists())
			create();
	}

	/**
	 * Create an empty CSV file with the head
	 */
	private void create() {
		write(Collections.<Row> emptyList());
	}

	/**
	 * Read and parse from the CSV file to get a list of rows (a table)
	 * 
	 * @return A list of row - the table containing all data
	 * @throws InvalidFormatException
	 *             Thrown when invalid CSV format found
	 */
	@Override
	public List<Row> parse() throws InvalidFormatException {
		List<Row> table = new ArrayList<Row>();
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(csvFile));
			String line = bReader.readLine(); // Skip first line - the head
			if (line == null) {// file is empty!
				bReader.close();
				throw new InvalidFormatException("Invalid csv format!");

			}
			while ((line = bReader.readLine()) != null) {
				Row row = readRow(line);
				if (row == null) {
					bReader.close();
					throw new InvalidFormatException("Invalid csv format!");

				}
				table.add(readRow(line));
			}
			bReader.close();
		} catch (NumberFormatException e) {
			throw new InvalidFormatException("Invalid csv format!");

		} catch (IOException e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return table;
	}

	/**
	 * Write the table to the CSV file
	 * 
	 * @param table
	 *            The table
	 */
	@Override
	public void write(List<Row> table) {
		try {
			BufferedWriter bWriter = new BufferedWriter(new FileWriter(csvFile));
			for (String header : head) {
				bWriter.append(header);
				bWriter.append(';');
				bWriter.newLine();
			}
			for (Row row : table) {
				bWriter.append(writeRow(row));
				bWriter.newLine();
			}
			bWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	/**
	 * Convert a row to a writable string in CSV format
	 * 
	 * @param row
	 *            The row of the table
	 * @return A string representing the data of the string in a CSV valid
	 *         format
	 */
	public String writeRow(Row row) {
		return String.format("%s;%s;%s;%s;%d;%d;%d;%d", row.getCode(), row
				.getDate(), row.getAlarmTime(), row.getAnswerTime(), row
				.getStatus().getStatus(), row.getContacts(), row.getHours(),
				row.getMinutes());
	}

	/**
	 * Pattern to split a single string - compile once to save performance
	 */
	private final static Pattern SPLIT_PATTERN = Pattern.compile(";");

	/**
	 * Parse a Row from a single line
	 * 
	 * @param line
	 *            The line to be parsed. Must be in CSV format.
	 * @return <code>null</code> when format of the CSV is wrong or integer
	 *         parsing failed
	 */
	public Row readRow(String line) {
		String[] split = SPLIT_PATTERN.split(line);
		if (split == null || split.length != Row.DATA_LENGTH) {
			return null;
		}
		return new Row(split[0], split[1], split[2], split[3],
				RowStatus.getStatus(Integer.parseInt(split[4])),
				Integer.parseInt(split[5]), Integer.parseInt(split[6]),
				Integer.parseInt(split[7]));
	}
}
