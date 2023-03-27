package com.company;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class WestminsterSkinConsultationManagerTest {
    //initialize a doctor arraylist
    ArrayList<Doctor> doctorList;
    private final PrintStream standardOut = System.out;
    //capture console prints using new output stream
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        //setup custom outputstream before run a test case
        System.setOut(new PrintStream(outputStreamCaptor));
        //reset doctorlist Array to remove all elements
        doctorList = new ArrayList<>();
    }

    @AfterEach
    public void tearDown() {
        //reassign default output stream after each test case
        System.setOut(standardOut);
    }

    @Test
    void addDoctor() {
        //add one doctor object and check the doctor arraylist size after the insertion.
        WestminsterSkinConsultationManager.addDoctor(doctorList,new Doctor("John","Doe", LocalDate.of(1980,7,15),0715425456,123,"Dermatology"));
        assertEquals(1,doctorList.size());
    }

    @Test
    void printDoctorsNull() {
        //check if the programme identify empty doctor arraylist and showing the required alert for printDoctors method
        WestminsterSkinConsultationManager.printDoctors(doctorList);
        assertEquals("Don't have any doctors at the moment. Check again later!", outputStreamCaptor.toString().trim());
    }

    @Test
    void printDoctorsWithData() {
        //Add a doctor to doctor arraylist
        doctorList.add(new Doctor("John","Doe", LocalDate.of(1980,7,15),0715425456,123,"Dermatology"));
        //run printDoctors method
        WestminsterSkinConsultationManager.printDoctors(doctorList);
        //check whether programme can display relevant patient's info in the console menu with the correct doctors count
        String newExpect = """
                Full name : Doe John
                Date of Birth : 1980-07-15
                Mobile number : 120990510
                License number : 123
                Specialisation : Dermatology
                ________________
                             
                Total no.of Doctors :1""";
        assertEquals(newExpect,outputStreamCaptor.toString().trim());
    }

    @Test
    void deleteDoctor(){
        //Add a doctor
        doctorList.add(new Doctor("John","Doe", LocalDate.of(1980,7,15),0715425456,123,"Dermatology"));
        //delete that doctor using selected doctor after the license no searching
        WestminsterSkinConsultationManager.deleteDoctor(doctorList,0);
        //cmatch the console printed information of the deleted doctor as well as the doctor count
        String expectString = """
                You has successfully deleted the below Doctor
                
                Full name : Doe John
                Date of Birth : 1980-07-15
                Mobile number : 120990510
                License number : 123
                Specialisation : Dermatology
                ________________
                Total no.of Doctors in the centre : 0""";
        assertEquals(expectString,outputStreamCaptor.toString().trim());
    }


    @Test
    void saveLoadInfo(){
        //add a doctor to dctorlist
        doctorList.add(new Doctor("John","Doe", LocalDate.of(1980,7,15),0715425456,123,"Dermatology"));
        //save doctor list to a file
        WestminsterSkinConsultationManager.saveInfo(doctorList);
        //reset doctor arraylist
        doctorList = new ArrayList<>();
        //load saved data into the doctor arraylist
        WestminsterSkinConsultationManager.loadInfo(doctorList);
        //Check loadedDoctor license no get equal to input license no
        assertEquals(123, doctorList.get(0).getLicenceNumber());
    }
}