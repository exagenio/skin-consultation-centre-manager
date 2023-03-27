package com.company;
import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ConsultTableModel extends AbstractTableModel {
    //add column names to an array
    private String[] columnNames = {"Patient's name","Patient's ID","Doctor's name", "Doctor's specialization","Received Date", "Received Time","Consultation ID"};
    private ArrayList<Consultation> consultations;

    //get consultations data and column names by the constructor
    public ConsultTableModel(ArrayList<Consultation> consultations) {
        this.columnNames = columnNames;
        this.consultations = consultations;
    }
    //set column count
    public int getColumnCount() {
        return columnNames.length;
    }
    //set row count
    public int getRowCount() {
        return consultations.size();
    }
    //set values for table cells by column wise
    public Object getValueAt(int row, int col) {
        Object bucket = null;
        if (col == 0) {
            bucket =consultations.get(row).getPatient().getSurname()+" "+consultations.get(row).getPatient().getName();
        }
        else if (col == 1) {
            bucket = consultations.get(row).getPatient().getPatientID();
        }
        else if (col == 2) {
            bucket = consultations.get(row).getDoctor().getSurname();
        }
        else if (col == 3) {
            bucket = consultations.get(row).getDoctor().getSpecialisation();
        }
        else if (col == 4) {
            bucket = consultations.get(row).getDateTime().getYear()+"-"+consultations.get(row).getDateTime().getMonth()+"-"+consultations.get(row).getDateTime().getDayOfMonth();
        }
        else if (col == 5) {
            bucket = +consultations.get(row).getDateTime().getHour()+":00";
        }
        else if (col == 6) {
            bucket = +consultations.get(row).getId();
        }
        return bucket;
    }

    //set column header names
    public String getColumnName(int col) {
        return columnNames[col];
    }
    //set column classes
    public Class getColumnClass(int col) {
        if (col == 4 || col ==5) {
            return LocalDateTime.class;
        }else if(col == 1 || col == 6){
            return Integer.class;
        }
        else {
            return String.class;
        }
    }

}
