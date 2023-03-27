package com.company;
import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class TableModel extends AbstractTableModel {
    //add column names to an array
    private String[] columnNames = {"Surname","Name","Specialisation", "license Number","Mobile Number", "Date of Birth"};
    private ArrayList<Doctor> doctorList;
    //get doctors data and column names by the constructor
    public TableModel(ArrayList <Doctor> doctorList) {
        this.columnNames = columnNames;
        this.doctorList = doctorList;
    }
    //set column count
    public int getColumnCount() {
        return columnNames.length;
    }
    //set row count
    public int getRowCount() {
        return doctorList.size();
    }
    //set values for table cells by column wise
    public Object getValueAt(int row, int col) {
        Object bucket = null;

        if (col == 0) {
            bucket =doctorList.get(row).getSurname();
        }
        else if (col == 1) {
            bucket = doctorList.get(row).getName();
        }
        else if (col == 2) {
            bucket = doctorList.get(row).getSpecialisation();
        }
        else if (col == 3) {
            bucket = doctorList.get(row).getLicenceNumber();
        }
        else if (col == 4) {
            bucket = doctorList.get(row).getMobileNumber();
        }
        else if (col == 5) {
            bucket = doctorList.get(row).getDateOfBirth();
        }
        return bucket;
    }

    //set column header names
    public String getColumnName(int col) {
        return columnNames[col];

    }
    //set column classes
    public Class getColumnClass(int col) {
        if (col == 3 || col ==4) {
            return Integer.class;
        }else if(col == 5){
            return LocalDate.class;
        }
        else {
            return String.class;
        }

    }

}
