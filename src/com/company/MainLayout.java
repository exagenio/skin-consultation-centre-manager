package com.company;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import javax.swing.table.DefaultTableCellRenderer;

public class MainLayout extends JFrame implements ActionListener{
    //initialize variables
    JTable table, consultTable;
    JPanel body, timeSlots, patientPanel, infoFieldPanel, consultInfoPanel, formPanel, homeWrapper, consultTabWrapper;
    JScrollPane scrollPane, imageScroll, consultScroll;
    JSpinner dateSelector, pBirthday;
    JButton dateSubmit, consultBtn, formSubmit, pImageBtn,homeBtn, consultTableBtn;
    JTextPane patientLabel, warningText, dateError;
    JTextField pSurname, pName, pMobile, pId, pNotes;
    JLabel cName, cDName, cDspec, cDateTime, cBirth, cCost, cId, cMobile, cNotes, imageText;
    Integer doctor;
    LocalDateTime selectedDate;
    int consultId = 1;
    JLabel [] pImageSet;
    static SecretKey secKey;
    File[] pImages;
    ArrayList<Consultation> consultationList = new ArrayList<Consultation>();
    ArrayList<Doctor> doctors = new ArrayList<Doctor>();
    static Cipher cipher;
    {
        try {
            cipher = initializeCypher();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }
    final String secretKey = "ssshhhhhhhhhhh!!!!";

    public MainLayout(ArrayList<Doctor> doctorList){

        // initialize local arraylist by getting the arraylist from the constructor
        doctors = doctorList;
        // set layout of the main frame
        setLayout(new BorderLayout());
        GridBagConstraints gbcBody = new GridBagConstraints();
        //set main header's style and layouts
        JPanel header = new JPanel();
        header.setBackground(Color.decode("#0081C9"));
        header.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0,5,0,5);
        header.setPreferredSize(new Dimension(1000, 80));
        //set buttons for header panel
        homeBtn = new JButton("Doctors");
        homeBtn.setPreferredSize(new Dimension(120,38));
        homeBtn.setFocusable(false);
        homeBtn.setBackground(Color.decode("#FFC93C"));
        consultTableBtn = new JButton("Consultations");
        consultTableBtn.setBackground(Color.decode("#FFC93C"));
        consultTableBtn.setPreferredSize(new Dimension(120,38));
        consultTableBtn.setFocusable(false);
        homeBtn.addActionListener(this);
        consultTableBtn.addActionListener(this);
        header.add(homeBtn, gbc);
        header.add(consultTableBtn,gbc);
        //set main body panel's style and layouts
        body = new JPanel();
        body.setBackground(Color.decode("#5BC0F8"));
        body.setLayout(new GridBagLayout());
        body.setPreferredSize(new Dimension(1000, 600));

        //set doctors page
        //create a new custom table model Using TableModel class and assign it to doctors table
        TableModel tableModel = new TableModel( doctorList);
        table = new JTable(tableModel);
        table.getTableHeader().setOpaque(true);
        table.getTableHeader().setBackground(Color.decode("#FFC93C"));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        //create sorters for every column
        table.setAutoCreateRowSorter(true);
        //Disable reordering of columns and rows
        table.getTableHeader().setReorderingAllowed(false);
        // scrollpane to wrap up the table
        homeWrapper = new JPanel();
        homeWrapper.setPreferredSize(new Dimension(800, 500));
        homeWrapper.setOpaque(false);
        JLabel doctormainTitle = new JLabel("Select a doctor to make an appointment");
        doctormainTitle.setFont(new Font("Arial", Font.BOLD, 18));
        doctormainTitle.setPreferredSize(new Dimension(700,20));
        doctormainTitle.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel subDocTitle = new JLabel("click table column headers to Sort");
        subDocTitle.setFont(new Font("Arial", Font.BOLD, 12));
        subDocTitle.setBorder(new EmptyBorder(5,20,20,20));
        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        //add a mouselistner to table to detect row clicks
        table.addMouseListener(new TableSelect(doctorList));
        homeWrapper.add(doctormainTitle);
        homeWrapper.add(subDocTitle);
        homeWrapper.add(scrollPane);
        body.add(homeWrapper);

        //set date and time select page
        JLabel dSelectorLabel = new JLabel("Day : Month : Year : Hour");
        dSelectorLabel.setPreferredSize(new Dimension(220, 20));
        dSelectorLabel.setHorizontalAlignment(dSelectorLabel.CENTER);
        dSelectorLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        //set today date and time
        Date today = new Date();
        //set tomorrow date and time
        Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
        //add a date model to spinner to set a date selector
        //user can select a date and a time from 2current time +24 hours to any upcoming date and time
        dateSelector = new JSpinner(new SpinnerDateModel(tomorrow, today, null, Calendar.MONTH));
        //set the format of the date selector input
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSelector, "dd/MM/yyyy/HH:00");
        dateSelector.setEditor(editor);
        //set styles to spinner
        dateSelector.setPreferredSize(new Dimension(220, 50));
        dateSelector.setFont(new Font("Arial", Font.PLAIN, 25));
        //set submit button and its styles
        dateSubmit = new JButton();
        dateSubmit.setText("Check Availability");
        dateSubmit.setBackground(Color.decode("#FFC93C"));
        dateSubmit.setPreferredSize(new Dimension(150, 38));
        dateSubmit.setFocusable(false);
        // add an actionlistener to listen the date submission
        dateSubmit.addActionListener(this);
        dateError = new JTextPane();
        dateError.setOpaque(false);
        dateError.setVisible(false);
        dateError.setForeground(Color.red);
        //create a panel and set its styles to wrap all elements of the "set date and time" page
        timeSlots = new JPanel();
        timeSlots.setLayout(new FlowLayout(FlowLayout.CENTER,180,20));
        timeSlots.setPreferredSize(new Dimension(400, 300));
        timeSlots.setBorder(new EmptyBorder(25,25,25,25));
//        timeSlots.setBackground(Color.green);
        timeSlots.add(new JLabel("Please enter a possible Date and a time"));
        timeSlots.add(dSelectorLabel);
        timeSlots.add(dateSelector);
        timeSlots.add(dateSubmit);
        timeSlots.add(dateError);
        body.add(timeSlots);

        //set patient's info collection page
        //initialize and set styles of Doctor availability alert sentence
        patientLabel = new JTextPane();
        patientLabel.setPreferredSize(new Dimension(500,35));
        patientLabel.setFont(new Font("Arial", Font.BOLD, 12));
        patientLabel.setOpaque(false);
        JLabel commonText  = new JLabel("Please fill the below fields to place your appointment");
        commonText.setBorder(new EmptyBorder(10,0,10,0));
        //create a panel to wrap the patient's form
        formPanel = new JPanel();
        //set 7 rows and 2 columns grid layout to add the labels and text inputs
        formPanel.setLayout(new GridLayout(7,2,0,5));
        formPanel.setPreferredSize(new Dimension(600, 250));
        formPanel.setBorder(new EmptyBorder(0,25,25,25));
        //set input fields and its labels
        JLabel surnameLabel = new JLabel("Enter Patient's surname* :");
        pSurname = new JTextField();
        JLabel nameLabel = new JLabel("Enter Patient's name* :");
        pName = new JTextField();
        JLabel mobileLabel = new JLabel("Enter patient's mobile No* :");
        pMobile = new JTextField();
        JLabel idLabel = new JLabel("Enter patient ID:* ");
        pId = new JTextField();
        JLabel birthLabel = new JLabel("Enter patient's birthday:* ");
        //create a spinner with a spinnerDateModel within a range 1900- today
        pBirthday = new JSpinner(new SpinnerDateModel(new Date(473385600000L),new Date(-2208988800000L) , today, Calendar.MONTH));
        pBirthday.setPreferredSize(new Dimension(220, 50));
        //set input format of the spinner
        JSpinner.DateEditor editor1 = new JSpinner.DateEditor(pBirthday, "dd/MM/yyyy");
        pBirthday.setEditor(editor1);
        JLabel notesLabel = new JLabel("Enter patient's notes:");
        pNotes = new JTextField();
        JLabel imageLabel = new JLabel("Upload images of your skin:");
        pImageBtn = new JButton("Choose some files");
        pImageBtn.setBackground(Color.decode("#FFC93C"));
        pImageBtn.setFocusable(false);
        //add an actionListener to listen clicking of choose some files button
        pImageBtn.addActionListener(this);
        //add labels and input fields to form wrapper panel
        formPanel.add(surnameLabel);
        formPanel.add(pSurname);
        formPanel.add(nameLabel);
        formPanel.add(pName);
        formPanel.add(birthLabel);
        formPanel.add(pBirthday);
        formPanel.add(mobileLabel);
        formPanel.add(pMobile);
        formPanel.add(idLabel);
        formPanel.add(pId);
        formPanel.add(notesLabel);
        formPanel.add(pNotes);
        formPanel.add(imageLabel);
        formPanel.add(pImageBtn);
        //set a submit button and initialize warning text panels
        imageText = new JLabel();
        imageText.setForeground( Color.decode("#108B26"));
        imageText.setPreferredSize(new Dimension(500,30));
        imageText.setHorizontalAlignment(SwingConstants.CENTER);
        imageText.setVisible(false);
        formSubmit = new JButton("Submit");
        formSubmit.setBackground(Color.decode("#FFC93C"));
        formSubmit.setFocusable(false);
        formSubmit.addActionListener(this);
        formSubmit.setPreferredSize(new Dimension(150, 38));
        warningText = new JTextPane();
        warningText.setPreferredSize(new Dimension(500,200));
        warningText.setOpaque(false);
        warningText.setFont(new Font("Arial", Font.BOLD, 14));
        warningText.setForeground(Color.red);
        //set a panel to wrap all elements of patient's info collection page
        patientPanel = new JPanel();
        patientPanel.setPreferredSize(new Dimension(600, 550));
        patientPanel.setBorder(new EmptyBorder(15,15,15,15));
        patientPanel.add(patientLabel);
        patientPanel.add(commonText);
        patientPanel.add(formPanel);
        patientPanel.add(imageText);
        patientPanel.add(formSubmit);
        patientPanel.add(warningText);
        body.add(patientPanel);

        //set consultation info page
        //create a panel to wrap all elements of consultation info page
        consultInfoPanel = new JPanel();
        consultInfoPanel.setLayout(new BoxLayout(consultInfoPanel,BoxLayout.PAGE_AXIS));
        consultInfoPanel.setBorder(new EmptyBorder(25,25,25,25));
        infoFieldPanel = new JPanel();

        //create label and info labels
        JLabel cNameLabel = new JLabel("Patient's name :");
        cName = new JLabel();
        JLabel cDNameLabel = new JLabel("Doctor's name :");
        cDName = new JLabel();
        JLabel cDspecLabel = new JLabel("Doctor's specialisation :");
        cDspec = new JLabel();
        JLabel cDateTimeLabel = new JLabel("Date and time of consultation :");
        cDateTime = new JLabel();
        JLabel cCostLabel = new JLabel("Cost/Hour :");
        cCost = new JLabel();
        JLabel cBirthLabel = new JLabel("Patient's Birthday :");
        cBirth = new JLabel();
        JLabel cIdLabel = new JLabel("Patient's ID :");
        cId = new JLabel();
        JLabel cMobileLabel = new JLabel("Patient's Mobile number :");
        cMobile = new JLabel();
        JLabel cNotesLabel= new JLabel("Patient's notes :");
        cNotes = new JLabel();
        //array to get 10 inputs of image paths of the consultation
        pImageSet = new JLabel[10];
        //set a wrapper to add all images with a box layout
        JPanel imageWrapper = new JPanel();
        imageWrapper.setLayout(new BoxLayout(imageWrapper,BoxLayout.Y_AXIS));
        imageWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageWrapper.setBorder(new EmptyBorder(10,10,25,10));
        //initialize 10 Labels to set images and hide them
        for(int k=0; k<10; k++){
            JLabel image = new JLabel();
            imageWrapper.add(image);
            image.setVisible(false);
            pImageSet[k] = image;

        }
        //set a submit button to close the consultation info page
        consultBtn = new JButton("Show all consultations");
        consultBtn.setFocusable(false);
        consultBtn.setBackground(Color.decode("#FFC93C"));
//        consultBtn.setBorder(new EmptyBorder(25,10,10,10));
        consultBtn.setPreferredSize(new Dimension(150, 38));
        consultBtn.addActionListener(this);
        consultBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        //set a panel to wrap labels and info fields with 2 columns and 10 rows
        infoFieldPanel.setLayout(new GridLayout(10,2,0,5));
        infoFieldPanel.setPreferredSize(new Dimension(500, 200));
        //add labels and panels to the info wrapper
        infoFieldPanel.add(cNameLabel);
        infoFieldPanel.add(cName);
        infoFieldPanel.add(cDNameLabel);
        infoFieldPanel.add(cDName);
        infoFieldPanel.add(cDspecLabel);
        infoFieldPanel.add(cDspec);
        infoFieldPanel.add( cDateTimeLabel);
        infoFieldPanel.add( cDateTime);
        infoFieldPanel.add( cCostLabel);
        infoFieldPanel.add(cCost);
        infoFieldPanel.add(cBirthLabel);
        infoFieldPanel.add(cBirth);
        infoFieldPanel.add(cIdLabel);
        infoFieldPanel.add(cId);
        infoFieldPanel.add(cMobileLabel);
        infoFieldPanel.add(cMobile);
        infoFieldPanel.add(cNotesLabel);
        infoFieldPanel.add(cNotes);
        //wrap submit button, image wrapper and info wrapper inside a new wrapper
        consultInfoPanel.add(infoFieldPanel);
        consultInfoPanel.add(imageWrapper);
        consultInfoPanel.add(consultBtn);
        //initialize and wrap infofieldpanel inside a scroller to extend the height if necessary
        imageScroll = new JScrollPane(consultInfoPanel);
        imageScroll.setPreferredSize(new Dimension(700,500)) ;

        //set consultations table page
        consultTabWrapper = new JPanel();
        consultTabWrapper.setPreferredSize(new Dimension(800, 500));
        consultTabWrapper.setOpaque(false);
        JLabel consultmainTitle = new JLabel("Select a consultation to check all details");
        consultmainTitle.setFont(new Font("Arial", Font.BOLD, 18));
        consultmainTitle.setPreferredSize(new Dimension(700,70));
        consultmainTitle.setHorizontalAlignment(SwingConstants.CENTER);
        //set a new table model to add consultations
        ConsultTableModel consultationsModel = new ConsultTableModel(consultationList);
        //set a new table with the new table model
        consultTable = new JTable(consultationsModel);
        consultTable.getTableHeader().setOpaque(false);
        consultTable.getTableHeader().setBackground(Color.decode("#FFC93C"));
        consultTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        //Align Table's cell data to left side.
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.LEFT );
        for(int x=0;x<6;x++){
            table.getColumnModel().getColumn(x).setCellRenderer( centerRenderer );
        }
        for(int x=0;x<7;x++){
            consultTable.getColumnModel().getColumn(x).setCellRenderer( centerRenderer );
        }
        //consultTable.setAutoCreateRowSorter(true);
        consultTable.getTableHeader().setReorderingAllowed(false);
        consultScroll = new JScrollPane(consultTable);
        consultScroll.setPreferredSize(new Dimension(800,500));
        //add mouse listener to detect row clicks
        consultTable.addMouseListener(new ConsultationSelect(consultationList));

        //add patient's info collection and consultation table pages to body
        body.add(patientPanel);
        consultTabWrapper.add(consultmainTitle);
        consultTabWrapper.add(consultScroll);
        body.add(consultTabWrapper);

        //add header and body panels to frame class
        add(header,BorderLayout.NORTH);
        add(body,BorderLayout.CENTER);

        //hide panels and scrollbars to avoid overlapping of pages
        timeSlots.setVisible(false);
        patientPanel.setVisible(false);
        warningText.setVisible(false);
        consultTabWrapper.setVisible(false);
    }

    //create a method to catch actions event of the frame
    public void actionPerformed(ActionEvent e) {
        //check whether the action occurred from the date selector page's submit button
        if(e.getSource() == dateSubmit){
            dateError.setVisible(false);
            //set a temporary Date variable to get the selected date by the user
            Date tempDate = (Date)dateSelector.getValue() ;
            //convert the Date into LocalDateTime format
            selectedDate = tempDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            //change minutes seconds and nano seconds to zero
            selectedDate = selectedDate.withMinute(0).withSecond(0).withNano(0);
            //initialize a boolean to check a consultation with the same doctor and time
            Boolean matchConsult;
            //call checkExistConsult method to check is there any similar consultation
            matchConsult = checkExistConsult();
            //check the input of the boolean and recognize the availability of similar consultation
            if(matchConsult){
                //Get the old doctorNo TO A new variable
                int oldDoctor = doctor;
                //initialize array to store available arraylist positions of the doctors
                ArrayList<Integer> doctorsRandom = new ArrayList<>();
                //add doctor array positions except the previous selected one
                for(int i=0; i<doctors.size(); i++){
                    if(i != doctor){
                        doctorsRandom.add(i);
                    }
                }
                //create a random number
                int rand = randomNum(doctorsRandom);
                //initialize an boolean to check is there any available doctor
                Boolean haveDoctor = false;
                //run a while loop until random doctor arraylist greater than 1
                try{
                    //set a new random doctor from the array
                    doctor = doctorsRandom.get(rand);
                    while(doctorsRandom.size()>1){
                        if(checkExistConsult()){
                            //remove selected doctor position from the array
                            doctorsRandom.remove(rand);
                            rand = randomNum(doctorsRandom);
                            //set a new random doctor from the available elements
                            doctor = doctorsRandom.get(rand);
                            haveDoctor = false;
                        }else{
                            //change boolean to true and break the loop
                            haveDoctor = true;
                            break;
                        }
                    }
                }catch(IndexOutOfBoundsException ex){
                    doctor = oldDoctor;
                    //set an error text and display it
                    dateError.setText("No doctor available on your selected date and time.\nPlease select another date/time.");
                    dateError.setVisible(true);
                }
                if(haveDoctor){
                    //hide date selector page
                    timeSlots.setVisible(false);
                    //hide date selector page
                    patientPanel.setVisible(true);
                    patientLabel.setText("Sorry, Dr."+doctors.get(oldDoctor).getSurname()+" is not available on your selected date and time ("+selectedDate+"). Dr."+doctors.get(doctor).getSurname()+" will be automatically assigned by the system.");
                    patientLabel.setForeground( Color.red);
                }else{
                    doctor = oldDoctor;
                    //set an error text and display it
                    dateError.setText("No doctor available on your selected date and time.\nPlease select another date/time.");
                    dateError.setVisible(true);
                }
            }else{
                //hide date selector page
                timeSlots.setVisible(false);
                //show patient's info page and set notice about the availability
                patientPanel.setVisible(true);
                patientLabel.setText("Dr."+doctors.get(doctor).getSurname()+" is available on your selected date and time ("+selectedDate+").");
                patientLabel.setForeground( Color.decode("#108B26"));
            }
        //check whether the action occurred from the date selector page's submit button
        }else if(e.getSource() == formSubmit){
            //reset error message and hide it
            warningText.setVisible(false);
            String errorString = "";
            //Validate field entries and create the error messages
            if(pSurname.getText().isEmpty()){
                errorString  = errorString + "\n- Please enter patient's surname";
            }if(pName.getText().isEmpty()){
                errorString  = errorString + "\n- Please enter patient's name";
            }if(!isNumber(pMobile.getText()) || pMobile.getText().isEmpty()){
                errorString  = errorString + "\n- Please enter a valid mobile number(eg:0774512564";
            }if(!isNumber(pId.getText()) || pId.getText().isEmpty()){
                errorString  = errorString + "\n- Please enter a valid patient ID(eg:123)";
            }
            //check is there any error or not
            if(errorString != ""){
                //if there are errors, set the error text and show it
                warningText.setText(errorString);
                warningText.setVisible(true);
            }else{
                try {
                //get the inputs to variables
                String [] paImgPaths = new String[0];
                int paId = Integer.parseInt(pId.getText());
                String paSurname = pSurname.getText();
                String paName = pName.getText();
                int paMobile = Integer.parseInt(pMobile.getText());
                //convert Date to LocalDate
                Date tempDate = (Date)pBirthday.getValue() ;
                LocalDate paBirthday = tempDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                //initialize a variable to convert note string to a byte array
                String paNotes;
                String input;
                //check is there any input or not and convert string to a byte array
                if(pNotes.getText().isEmpty()){
                    input = "No notes";
                }else{
                    input = pNotes.getText();
                }
                    //initialize a byte array with note byte array's length and assign the encrypted note into that array
                    paNotes = AES.encryptData(input, secretKey);

                int preConsultation = 0;
                //check is there any previous appoinment made by the patient or not
                for (int i=0; i<consultationList.size(); i++) {
                    if(consultationList.get(i).getPatient().getPatientID() ==paId ){
                        preConsultation++;
                    }
                }
                //initialize a note object
                Consultation.Notes fullNote;
                //check is there any image inputs

                if(pImages != null){
                    encryptInit();
                    //initialize a string array of imagefile array length
                    paImgPaths = new String[pImages.length];
                    //run a loop upto imagefiles length
                    for(int l=0; l< pImages.length; l++){
                        BufferedImage bImage = null;
                        //get the filename of image and check the extension
                        String fileName = pImages[l].getName();
                        int index = fileName.lastIndexOf('.');
                        String extension = "";
                        if(index > 0) {
                            extension = fileName.substring(index + 1);
                        }

                            //create an input stream
                            FileInputStream fis = new FileInputStream(pImages[l].getAbsolutePath());
                            //convert input stream to a byte array
                            byte data[] = fis.readAllBytes();
                            //create output stream with the save location and name
                            FileOutputStream output = new FileOutputStream("src/images/image"+l+1+"consu"+consultId+"."+extension);
                            //encrypt the data
                            CipherOutputStream cos = new CipherOutputStream(output,cipher);
                            //write encrypted data on the file
                            cos.write(data);
                            //close encrypted output stream
                            cos.close();
                            //save image path to the array
                            paImgPaths[l] = "src/images/image"+l+1+"consu"+consultId+"."+extension;

                    }
                    //insert encrypted note and the image paths to the Note object
                    fullNote = new Consultation.Notes(paNotes,paImgPaths);
                }else{
                    //if there are no images set imagepaths array to null and encrypted note text
                    fullNote = new Consultation.Notes(paNotes,null);
                }
                //initialize an integer to set the price
                int cost;
                //check previous consultation count, set the cost and add the consultation to arraylist.
                if(preConsultation > 0){
                    cost = 25;
                    consultationList.add(new Consultation(consultId,selectedDate,25,fullNote, doctors.get(doctor),new Patient(paName,paSurname,paBirthday,paMobile,paId)));
                }else{
                    cost = 15;
                    consultationList.add(new Consultation(consultId,selectedDate,15,fullNote, doctors.get(doctor),new Patient(paName,paSurname,paBirthday,paMobile,paId)));
                }
                //hide patient's info page
                patientPanel.setVisible(false);
                    //set info fields of the consultation
                    //decrypt note
                    String s = AES.decryptData(paNotes, secretKey);
                    cName.setText(paSurname+" "+paName);
                    cDName.setText(doctors.get(doctor).getSurname()+" "+doctors.get(doctor).getName());
                    cDspec.setText(doctors.get(doctor).getSpecialisation());
                    cDateTime.setText(String.valueOf(selectedDate));
                    cCost.setText("£"+cost);
                    cBirth.setText(String.valueOf(paBirthday));
                    cId.setText(String.valueOf(paId));
                    cMobile.setText(String.valueOf(paMobile));
                    cNotes.setText(s);
                    //check is there any input images
                    if(paImgPaths.length >0){
                        decryptInit();
                        //run a loop to get images and decrypt them
                        for(int m=0; m< paImgPaths.length; m++){
                            //get the file
                            File inputImgFile = new File(paImgPaths[m]);
                            //create an input stream
                            FileInputStream inputFile = new FileInputStream(inputImgFile);
                            //decrypt image data
                            CipherInputStream cis = new CipherInputStream(inputFile,cipher);
                            //write image data to abyte array
                            byte data[] = cis.readAllBytes();;
                            //convert bytearrray to inout stream
                            ByteArrayInputStream bis = new ByteArrayInputStream(data);
                            //create a buffered image
                            BufferedImage readImage = ImageIO.read(bis);
                            //close cipher input stream
                            cis.close();
                            //set the scale of the image
                            Image dimg = readImage.getScaledInstance(400,300,Image.SCALE_SMOOTH);
                            //set image to a one of the JLabel and make it visible
                            pImageSet[m].setIcon(new ImageIcon(dimg));
                            pImageSet[m].setVisible(true);

                        }
                    }



                //increase the consut id by 1
                consultId++;
                //add consultation's info wrapper scroller to the body panel
                body.add(imageScroll);
                //reset viewPort of the scrollpane to top
                imageScroll.getViewport().setViewPosition(new Point(0,0));
                //refresh body panel to show the newly added consultation's info page
                body.revalidate();
                body.repaint();

                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (IllegalBlockSizeException ex) {
                    throw new RuntimeException(ex);
                } catch (BadPaddingException ex) {
                    throw new RuntimeException(ex);
                } catch (InvalidKeyException ex) {
                    throw new RuntimeException(ex);
                }
            }
        //check whether event triggered by the image choose button
        }else if(e.getSource() == pImageBtn){
            // create a fleChooser window
            JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            // allow selection of multiple files
            j.setMultiSelectionEnabled(true);
            // restrict selection of all file types
            j.setAcceptAllFileFilterUsed(false);
            // set title of the filechooser window
            j.setDialogTitle("Select an image/images");
            // only allow image extensions
            FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only images", "jpg", "png","jpeg");
            j.addChoosableFileFilter(restrict);
            // show save dialog
            int r = j.showSaveDialog(null);
            // Check user selected a file or not
            if (r == JFileChooser.APPROVE_OPTION) {
                //save the selected file to the image array
                pImages = j.getSelectedFiles();
                imageText.setText("No.of selected images : "+pImages.length);
                imageText.setVisible(true);
            }
            //check whether event triggered by the show consultations button
        }else if(e.getSource() == consultBtn){
            //remove consultation info page from body
            body.remove(imageScroll);
            //refresh body
            body.revalidate();
            body.repaint();
            //show consultations page
            consultTabWrapper.setVisible(true);
            //update and refresh the table data
            consultTable.revalidate();
            consultTable.repaint();
            //hide all image Labels of the consultations' info page
            imagesHide();
        }
        //check whether event triggered by the header's home button
        else if(e.getSource() == homeBtn){
            //hide all pages and show home page with doctor table
            homeWrapper.setVisible(true);
            table.revalidate();
            table.repaint();
            patientPanel.setVisible(false);
            timeSlots.setVisible(false);
            consultTabWrapper.setVisible(false);
            body.remove(imageScroll);
            body.revalidate();
            body.repaint();
            imagesHide();
            //check whether event triggered by the header's consultations button
        }else if(e.getSource() == consultTableBtn){
            //hide all the pages and show consultations table page
            homeWrapper.setVisible(false);
            patientPanel.setVisible(false);
            timeSlots.setVisible(false);
            body.remove(imageScroll);
            body.revalidate();
            body.repaint();
            consultTabWrapper.setVisible(true);
            consultTable.revalidate();
            consultTable.repaint();
            imagesHide();
        }
    }

    //create a mouselistener class to listen table row clicks
    private class TableSelect implements MouseListener{
        //initialize an arraylist to save doctor list
        private ArrayList<Doctor> doctorList;
        //constructor to call the method with doctors' data
        public TableSelect(ArrayList<Doctor> doctorList) {
            this.doctorList = doctorList;
        }
        //trigger the method to mouse click events
        public void mouseClicked(MouseEvent e) {
            //get the license Number of the doctor of the selected row
            int licenseNumber = (Integer) (table.getValueAt(table.getSelectedRow(), 3));
            int doctorNo = -1;
            //run a loop to search the doctor with the license number
            for (int i=0; i<doctorList.size(); i++) {
                if(doctorList.get(i).getLicenceNumber() == licenseNumber ){
                    doctorNo = i;
                }
            }
            //save selected doctor no of the array to the variable
            doctor = doctorNo;
            //hide doctors table
            homeWrapper.setVisible(false);
            //hide error texts of date select page
            dateError.setVisible(false);
            //show date select page
            timeSlots.setVisible(true);

        }
        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
    }

    //AES encryption for Strng
    public class AES {

        private static SecretKeySpec secretKey;
        private static byte[] key;

        //create an key to encrypt
        public static void setKey(final String myKey) {
            MessageDigest sha1 = null;
            try {
                key = myKey.getBytes("UTF-8");
                sha1 = MessageDigest.getInstance("SHA-1");
                key = sha1.digest(key);
                key = Arrays.copyOf(key, 16);
                secretKey = new SecretKeySpec(key, "AES");
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        //Encrypt a String
        public static String encryptData(final String strToEncrypt, final String secret) {
            try {
                setKey(secret);
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                return Base64.getEncoder()
                        .encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
            } catch (Exception e) {
                System.out.println("Error while encrypting: " + e.toString());
            }
            return null;
        }

        //Decrypt a string
        public static String decryptData(final String strToDecrypt, final String secret) {
            try {
                setKey(secret);
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                return new String(cipher.doFinal(Base64.getDecoder()
                        .decode(strToDecrypt)));
            } catch (Exception e) {
                System.out.println("Error while decrypting: " + e.toString());
            }
            return null;
        }
    }
    //create a mouselistener class to consult table row clicks
    private class ConsultationSelect implements MouseListener{
        //initialize an arraylist to save consultation list
        private ArrayList<Consultation> consultationList;
        //constructor to call the method with consultations' data
        public ConsultationSelect(ArrayList<Consultation> consultationList) {
            this.consultationList = consultationList;
        }

        public void mouseClicked(MouseEvent e) {
            //get the consultation id of the selected row
            int consultId = (Integer) (consultTable.getValueAt(consultTable.getSelectedRow(),6 ));
            int consultNo = -1;
            for (int i=0; i<consultationList.size(); i++) {
                if(consultationList.get(i).getId() == consultId ){
                    consultNo = i;
                }
            }
            //save the selected consultation to the consultation object
            Consultation selectedConsult = consultationList.get(consultNo);
            //set the info fields of the consultation info page
            setConsultDetails(selectedConsult);
            //add consultation info page and refresh body panel
            body.add(imageScroll);
            //reset viewPort of the scrollpane to top
            imageScroll.getViewport().setViewPosition(new Point(0,0));
            body.revalidate();
            body.repaint();

        }
        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
    }

    public void setConsultDetails(Consultation consultation ){
        consultTabWrapper.setVisible(false);
        try {
            //set consultation's data to text fields
            String s = AES.decryptData(consultation.getNotes().getNote(), secretKey);
            cName.setText(consultation.getPatient().getSurname()+" "+consultation.getPatient().getName());
            cDName.setText(consultation.getDoctor().getSurname()+" "+consultation.getDoctor().getName());
            cDspec.setText(consultation.getDoctor().getSpecialisation());
            cDateTime.setText(String.valueOf(consultation.getDateTime()));
            cCost.setText(String.valueOf("£"+consultation.getCost()));
            cBirth.setText(String.valueOf(consultation.getPatient().getDateOfBirth()));
            cId.setText(String.valueOf(consultation.getId()));
            cMobile.setText(String.valueOf(consultation.getPatient().getMobileNumber()));
            cNotes.setText(s);
            //check the consultation has any image
            if(consultation.getNotes().getImages() != null){
                //run a loop to get each image data
                for(int m=0; m< consultation.getNotes().getImages().length; m++){
                    //select image file
                    File inputImgFile = new File(consultation.getNotes().getImages()[m]);
                    FileInputStream inputFile = new FileInputStream(inputImgFile);
                    //decrypt the image
                    CipherInputStream cis = new CipherInputStream(inputFile,cipher);
                    //convert to a byte array
                    byte data[] = cis.readAllBytes();
                    ByteArrayInputStream bis = new ByteArrayInputStream(data);
                    //convert to a buffered image
                    BufferedImage readImage = ImageIO.read(bis);
                    cis.close();
                    //print the image to a JLabel icon
                    Image dimg = readImage.getScaledInstance(400,300,Image.SCALE_SMOOTH);
                    pImageSet[m].setIcon(new ImageIcon(dimg));
                    pImageSet[m].setVisible(true);

                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //hide image JLabels by running a loop for JLabels array
    public void imagesHide(){
        for(JLabel image : pImageSet){
            image.setVisible(false);
        }
    }

    //Check insert string is a number or a not
    public static boolean isNumber(String str) {
        //try to parse Integer and return true if it's success or return false if an error occured
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    //initialize cypher
    public static Cipher initializeCypher() throws InvalidKeyException {
        //initialize a KeyGenerator
        KeyGenerator keyPairGen = null;
        try {
            //generate an AES key
            keyPairGen= KeyGenerator.getInstance("AES");
            keyPairGen.init(128); // The AES key size in number of bits
            secKey = keyPairGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //Creating a Cipher object
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return cipher;
    }


    //check for similar consultations
    public Boolean checkExistConsult(){
        //set a boolean to false
        Boolean matchConsult = false;
        //search is there any similar consultation
        for (int i=0; i<consultationList.size(); i++) {
            //create a variable to get the current license no of doctor of the consultation
            int licenseNuOfConsult = consultationList.get(i).getDoctor().getLicenceNumber();
            //get the licenseNo of the selected doctor by the user
            int selectedLicense = doctors.get(doctor).getLicenceNumber();
            //get the current date and time of the consultation
            LocalDateTime dateOfConsult = consultationList.get(i).getDateTime();
            //check selected license and the date by the user are equals to current consultation data
            if((licenseNuOfConsult  == selectedLicense ) && dateOfConsult.equals(selectedDate)){
                matchConsult = true;
            }
        }
        return matchConsult;
    }
    public int randomNum(ArrayList<Integer> doctorsRandom){
        //Set the maximum no.to a random range
        int max = doctorsRandom.size() - 1;
        //set the minimum no.to a random range
        int min = 0;
        //set the random range
        int range = max - min + 1;
        int rand = (int)(Math.random() * range) + min;
        return rand;
    }

    public void  encryptInit() throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeyException {
        //init cipher encrypt mode
        cipher.init(Cipher.ENCRYPT_MODE, secKey);
        //encrypting the data
    }

    public void decryptInit() throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        cipher.init(Cipher.DECRYPT_MODE, secKey);
    }
}
