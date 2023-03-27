package com.company;
import java.time.LocalDate;

public class Person {
    //initialize variables of the person's data
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private int mobileNumber;

    //constructor
    public Person(String name, String surname, LocalDate dateOfBirth, int mobileNumber) {
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.mobileNumber = mobileNumber;
    }
    //get name
    public String getName() {
        return name;
    }
    //get surname
    public String getSurname() {
        return surname;
    }
    //get date of birth
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    //get mobile number
    public int getMobileNumber() {
        return mobileNumber;
    }
}
