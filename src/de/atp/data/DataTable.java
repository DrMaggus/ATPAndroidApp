package de.atp.data;

import java.util.List;

import de.atp.parser.Parser;
import de.atp.parser.csv.CSVParser;

public class DataTable {

    private final static String[] HEAD = {"Code", "Datum", "Alarmzeit", "Antwortzeit", "Abbruch", "Kontakte", "Stunden", "Minuten"};

    private List<Row> table;

    private Parser parser;

    public DataTable(String probandCode) {
        parser = new CSVParser(probandCode, HEAD);
    }

    // Called when some data is updated
    private void onUpdate() {
        parser.write(table);
    }

}
