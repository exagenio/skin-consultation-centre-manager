package com.company;

import java.time.LocalDate;

public class Doctor extends Person implements Comparable<Doctor>{
    //initialize variables to set doctor's data
    private int licenceNumber;
    private String specialisation;

    //constructor
    public Doctor(String name, String surname, LocalDate dateOfBirth, int mobileNumber, int licenceNumber, String specialisation) {
        super(name, surname, dateOfBirth, mobileNumber);
        this.licenceNumber = licenceNumber;
        this.specialisation = specialisation;
    }
    //get licence number
    public int getLicenceNumber() {
        return licenceNumber;
    }
    //get specialization
    public String getSpecialisation() {
        return specialisation;
    }

    //sort method to compare doctors by surname alphabetically
    public int compareTo(Doctor doctor) {
        return this.getSurname().compareTo(doctor.getSurname());
    }
}
