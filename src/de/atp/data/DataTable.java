package de.atp.data;

import java.util.ArrayList;
import java.util.List;

import de.atp.parser.Parser;
import de.atp.parser.csv.CSVParser;

public class DataTable {

    /**
     * Head of the table
     */
    private final static String[] HEAD = {"Code", "Datum", "Alarmzeit", "Antwortzeit", "Abbruch", "Kontakte", "Stunden", "Minuten"};

    /**
     * The table containing all rows
     */
    private List<Row> table;

    /**
     * The parser parsing the table to csv and read from it
     */
    private Parser parser;

    /**
     * Create a new object to manage the rows and synchronize the data with the
     * csv
     * 
     * @param probandCode
     *            The proband code
     */
    public DataTable(String probandCode) {
        parser = new CSVParser(probandCode, HEAD);
    }

    /**
     * Called when the table change to write the content of the table to the
     * file
     */
    private void onUpdate() {
        parser.write(table);
    }

    /**
     * @return Get a copy of the table
     */
    public List<Row> getRows() {
        return new ArrayList<Row>(table);
    }

    /**
     * @param rowIndex
     *            Index of the row to get
     * @return The copy of the row
     */
    public Row getRow(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= table.size())
            return null;
        return (Row) table.get(rowIndex).clone();
    }

    /**
     * @return Count of rows in the table
     */
    public int getRowSize() {
        return table.size();
    }

    /**
     * Update a single row. After this operation the table flushes to the drive
     * 
     * @param rowIndex
     *            The index of the row to update
     * @param row
     *            The content of the row
     */
    public void updateRow(Row row) {
        table.set(row.index, row);
        onUpdate();
    }

    /**
     * Add a new row the table. This operations start a flush of the tableto the
     * drive
     * 
     * @param row
     *            New row to insert
     */
    public void addRow(Row row) {

        row.index = table.size();
        table.add(row);
        onUpdate();
    }

    /**
     * @return Copy of the last row of the table
     */
    public Row getLastRow() {
        return getRow(table.size() - 1);
    }
}
