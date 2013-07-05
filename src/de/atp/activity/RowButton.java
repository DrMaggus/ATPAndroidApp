package de.atp.activity;


import android.widget.ToggleButton;

public class RowButton {
    private ToggleButton togglebutton;
    private int row;
    private int time;
    
    public RowButton(ToggleButton togglebutton, int row, int time) {
        this.togglebutton = togglebutton;
        this.row = row;
        this.time = time;
    }

    public ToggleButton getTogglebutton() {
        return togglebutton;
    }

    public int getRow() {
        return row;
    }

    public int getTime() {
        return time;
    }
    
    
}
