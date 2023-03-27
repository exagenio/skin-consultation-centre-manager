package com.company;
import java.awt.event.MouseListener;
import java.time.LocalDateTime;
import java.util.*;

public class Consultation {
    //initialize variables for consultation's data
    private int id;
    private LocalDateTime dateTime;
    private int cost;
    private Notes notes;
    private Doctor doctor;
    private Patient patient;

    //constructor
    public Consultation(int id, LocalDateTime dateTime, int cost, Notes notes, Doctor doctor, Patient patient) {
        this.id = id;
        this.dateTime = dateTime;
        this.cost = cost;
        this.notes = notes;
        this.doctor = doctor;
        this.patient = patient;
    }
    //get consultation ID
    public int getId() {
        return id;
    }
    //get Doctor of the consultation
    public Doctor getDoctor() {
        return doctor;
    }
    //get Patient object of the consultation
    public Patient getPatient() {
        return patient;
    }
    //get selected time and date
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    //get cost
    public int getCost() {
        return cost;
    }
    //get notes object
    public Notes getNotes() {
        return notes;
    }
    //create an inner class to save images and the note
    public static class Notes {
        //initialize variables for Note's data
        String note;
        String[] images;
        //constructor
        public Notes(String note, String[] images) {
            this.note = note;
            this.images = images;
        }
        //get patient's note
        public String getNote() {
            return note;
        }
        //get input images
        public String[] getImages() {
            return images;
        }
    }
}
