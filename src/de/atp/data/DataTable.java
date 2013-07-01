package de.atp.data;

import java.util.ArrayList;
import java.util.List;

public class DataTable {

    /**
     * The table containing all rows
     */
    private List<Row> table;

    /**
     * Create a new object to manage the rows and synchronize the data with the
     * csv
     * 
     * @param probandCode
     *            The proband code
     */
    public DataTable() {
        this(new ArrayList<Row>());
    }
    
    public DataTable(List<Row> rows) {
        this.table = rows;
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
    }

    /**
     * @return Copy of the last row of the table
     */
    public Row getLastRow() {
        return getRow(table.size() - 1);
    }
}
