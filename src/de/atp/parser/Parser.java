package de.atp.parser;

import java.util.List;

import de.atp.data.Row;

public interface Parser {

    /**
     * Read a file and parse its format.
     * 
     * @return A list containing all rows
     * @throws InvalidFormatException
     */
    public List<Row> parse() throws InvalidFormatException;

    /**
     * Write the table to the specific file format
     * 
     * @param table
     *            The table
     */
    public void write(List<Row> table);

}