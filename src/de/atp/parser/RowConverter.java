package de.atp.parser;

import de.atp.data.Row;

public interface RowConverter {

    /**
     * Convert a row to a writable string the specific file format
     * 
     * @param row
     *            The row of the table
     * @return A string representing the data of the string in a CSV valid
     *         format
     */
    public String writeRow(Row row);

    /**
     * Parse a Row from a single line
     * 
     * @param line
     *            The line to be parsed
     * @return <code>null</code> when the file format is invalid
     */
    public Row readRow(String line);

}
