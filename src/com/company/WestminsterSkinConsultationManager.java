package com.company;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import javax.swing.*;
public class WestminsterSkinConsultationManager implements SkinConsultationManager {
    public static void main(String[] args) {
        //initialize an arraylist of doctors to sve doctor objects
        ArrayList<Doctor> doctorList = new ArrayList<Doctor>();
        //initialize a scanner to read inputs
        Scanner input = new Scanner(System.in);
        //load saved doctors' info from file
        loadInfo(doctorList);
        while (true) {
            //error catching for detect invalid type data entries.
            try{
                //set Console menu texts
                System.out.print("""
                    --Main Menu--
                    A: Add a Doctor
                    P: Print the list of Doctors
                    D: Delete a doctor
                    S: Save data in a file
                    R: Run GUI
                    E: Exit program
                    """);
                String mSelect = input.next();

                //options selection conditions of menu
                if(mSelect.equalsIgnoreCase("A")){
                    System.out.println("Enter Doctor's name");
                    String name = input.next();
                    System.out.println("Enter Doctor's surname");
                    String surname = input.next();
                    System.out.println("Enter Doctor's birth year");
                    int birthYear = input.nextInt();
                    if(birthYear <1880 || birthYear >2023){
                        System.out.println("Invalid birth year. Please try again!\n");
                        continue;
                    }
                    System.out.println("Enter Doctor's birth month");
                    int birthMonth = input.nextInt();
                    if(birthMonth<1 || birthMonth>12){
                        System.out.println("Invalid birth month. Please try again!\n");
                        continue;
                    }
                    System.out.println("Enter Doctor's birth day of the month");
                    int birthDay = input.nextInt();
                    LocalDate fullBirthday = LocalDate.of(birthYear,birthMonth,birthDay);
                    System.out.println("Enter Doctor's mobile number");
                    int mobileNumber = input.nextInt();
                    if(mobileNumber<99999999){
                        System.out.println("Invalid mobile number. Please try again!\n");
                        continue;
                    }
                    System.out.println("Enter Doctor's lisence number");
                    int licenseNumber = input.nextInt();
                    Boolean hasNumber = false;
                    if(!doctorList.isEmpty()){
                        for (int i = 0; i<doctorList.size(); i++){
                            int currentDoctorNum = doctorList.get(i).getLicenceNumber();
                            if(currentDoctorNum == licenseNumber){
                                hasNumber = true;
                                System.out.println("This license number is already available. Please try again\n");
                                break;
                            }
                        }
                    }
                    if(hasNumber){
                        continue;
                    }
                    input.nextLine();
                    System.out.println("Enter Doctor's specialization");
                    String specialisation = input.nextLine();
                    //add new doctor object to the doctor arraylist
                    Doctor newDoctor = new Doctor(capitalize(name), capitalize(surname), fullBirthday,mobileNumber,licenseNumber,specialisation);
                    addDoctor(doctorList, newDoctor);
                }else if(mSelect.equalsIgnoreCase("P")){
                    printDoctors(doctorList);
                }else if(mSelect.equalsIgnoreCase("D")){
                    //get the license number
                    System.out.println("Enter doctor's license number :");
                    int licenseNumber = input.nextInt();
                    //initialize variables to count matching no.of doctors and get the doctor object no.of the arraylist
                    int matchCount = 0;
                    int doctorNo = -1;
                    //run a loop to search is there any matching license number in the doctor list and assign the number
                    for (int i=0; i<doctorList.size(); i++) {
                        if(doctorList.get(i).getLicenceNumber() == licenseNumber ){
                            doctorNo = i;
                            matchCount++;
                        }
                    }
                    //check is there any matching result found
                    if(matchCount > 0){
                        //print the details of the selected doctor
                        System.out.print("Full name : "+doctorList.get(doctorNo).getSurname()+" "+doctorList.get(doctorNo).getName()+"\nDate of Birth : "+doctorList.get(doctorNo).getDateOfBirth()+
                                "\nMobile number : "+doctorList.get(doctorNo).getMobileNumber()+"\nLicense number : "+doctorList.get(doctorNo).getLicenceNumber()+"\nSpecialisation : "+doctorList.get(doctorNo).getSpecialisation()+"\n________________\n\nDo you want to delete the above Doctor?(Yes = Y, No = N)\n");
                        String choice = input.next();
                        //run a while loop to get an input to select the option
                        while(true){
                            if(choice.equalsIgnoreCase("Y")){
                                deleteDoctor(doctorList,doctorNo);
                                break;
                            }else if(choice.equalsIgnoreCase("N")){
                                //if user choosed N, break the loop and return to menu
                                break;
                            }else{
                                //if user selected an invalid option run the loop from the beginning
                                System.out.println("Invalid option try again! \nDo you want to delete the above Doctor?(Yes = Y, No = N");
                                choice = input.next();
                                continue;
                            }
                        }
                        //display an alert if there is no any matching license number
                    }else{
                        System.out.println("Sorry, Can't find any matching doctor with the given license number\n");
                    }

                }else if(mSelect.equalsIgnoreCase("S")){
                    saveInfo(doctorList);
                }else if(mSelect.equalsIgnoreCase("R")){
                    runGUI(doctorList);
                }else if(mSelect.equalsIgnoreCase("E")){
                    break;
                }else{
                    System.out.println("Invalid input. Please try again!\n");
                }
            }catch (Exception e){
                System.out.println("Invalid input. Please try again!\n");
            }
        }
    }

    //run GUI
    public static void runGUI(ArrayList<Doctor> doctorList){
        //Create a frame from mainLayout class
        MainLayout frame = new MainLayout(doctorList);
        //set exit on close button
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //title of the frame
        frame.setTitle("Skin Disease Centre");
        //size of the frame
        frame.setSize(1000,700);
        //show frame
        frame.setVisible(true);
    }

    //process of adding a doctor
    public static void addDoctor (ArrayList<Doctor> doctorList, Doctor doctor){
        //check available no.of doctors is less than or equal to 10
        if(doctorList.size()<11 ){
            //add doctor object to the doctor list
            doctorList.add(doctor);
            System.out.println("Successfully added the doctor\n");
        }else{
            System.out.println("You exceed the maximum no.of Doctors\n");
        }
    }

    //print available doctors
    public static void printDoctors (ArrayList<Doctor> doctorList){
        //get the size of the doctor list
        int listSize = doctorList.size();
        //get a clone of the doctorlist
        ArrayList<Doctor> cloneList = (ArrayList<Doctor>) doctorList.clone();
        //check the size of doctor list
        if(listSize == 0){
            System.out.println("Don't have any doctors at the moment. Check again later!\n");
        }else{
            //sort doctorlist
            Collections.sort(cloneList);
            //print every doctor with all info
            for (Doctor doctor : cloneList) {
                System.out.print("Full name : "+doctor.getSurname()+" "+doctor.getName()+"\nDate of Birth : "+doctor.getDateOfBirth()+
                        "\nMobile number : "+doctor.getMobileNumber()+"\nLicense number : "+doctor.getLicenceNumber()+"\nSpecialisation : "+doctor.getSpecialisation()+"\n________________\n\n");
            }
            //print total no.of doctors
            System.out.println("Total no.of Doctors :"+doctorList.size()+"\n");
        }
    }

    //delete selected doctor from the arraylist
    public  static void deleteDoctor(ArrayList<Doctor> doctorList,int doctorNo){
        System.out.print("You has successfully deleted the below Doctor\n\nFull name : "+doctorList.get(doctorNo).getSurname()+" "+doctorList.get(doctorNo).getName()+"\nDate of Birth : "+doctorList.get(doctorNo).getDateOfBirth()+
                "\nMobile number : "+doctorList.get(doctorNo).getMobileNumber()+"\nLicense number : "+doctorList.get(doctorNo).getLicenceNumber()+"\nSpecialisation : "+doctorList.get(doctorNo).getSpecialisation()+"\n________________\n");
        doctorList.remove(doctorNo);
        System.out.println("Total no.of Doctors in the centre : "+doctorList.size());
    }

    //save doctors data to a file
    public static void saveInfo(ArrayList<Doctor> doctorList) {
        //error catching for data store
        try {
            FileWriter myWriter = new FileWriter("centreInfo.txt");// initialize centreInfo.txt to write data
            for (int i = 0; i < doctorList.size(); i++) {
                //arrange each data according to an order to detect when it's load back to the program
                myWriter.write(doctorList.get(i).getName() + "--" +
                        doctorList.get(i).getSurname() + "--" +
                        doctorList.get(i).getDateOfBirth() + "--" +
                        doctorList.get(i).getMobileNumber() + "--" +
                        doctorList.get(i).getLicenceNumber() + "--" +
                        doctorList.get(i).getSpecialisation() + "\n"
                );
            }
            myWriter.close();
            System.out.println("Successfully stored program data to the (centreInfo.txt) file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    //load program data from a file
    public static void loadInfo(ArrayList<Doctor> doctorList) {
        //error catching for data loading
        try {
            File myObj = new File("centreInfo.txt");//load data from centreInfo.txt
            if (myObj.exists()){
                Scanner myReader = new Scanner(myObj);
                // loop for run until file has lines to read each line of the file
                while (myReader.hasNextLine()) {
                    //recognize each doctor from "--" and insert their data into an array
                    String[] dataArray = myReader.nextLine().split("--");
                    doctorList.add(new Doctor(dataArray[0],dataArray[1],LocalDate.parse(dataArray[2]),Integer.parseInt(dataArray[3]) ,Integer.parseInt(dataArray[4]),dataArray[5]));
                }
                //close file
                myReader.close();
                System.out.println("Successfully loaded program data of the file.");
            }else{
                System.out.println("No saved data can be found. Program will start without any loading of data");
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    //change a first letter of a word to uppercase
    public static String capitalize(String str) {
        //check whether the string is null
        if(str == null || str.isEmpty()) {
            return str;
        }
        //capitalize the word
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
