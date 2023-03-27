package com.company;

import java.time.LocalDate;
import java.util.Date;

public class Patient extends Person {
    //initialize patientID
    private int patientID;

    //constructor
    public Patient(String name, String surname, LocalDate dateOfBirth, int mobileNumber, int patientID) {
        super(name, surname, dateOfBirth, mobileNumber);
        this.patientID = patientID;
    }
    //get patient's ID
    public int getPatientID() {
        return patientID;
    }
}
