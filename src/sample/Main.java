package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.sql.*;
import java.util.Locale;
import java.util.Properties;


class Department implements Comparable<Department>{
    private String depName;

    GenericLinkedList<Department> depLL = new GenericLinkedList<>();

    public Department(String depName) {
        this.depName = depName;
    }

    public String getDepName() {
        return depName;
    }

    public GenericLinkedList getList(){
        return depLL;
    }
    @Override
    public int compareTo (Department o) {
        int compareResult = this.depName.compareTo(o.depName);
        if (compareResult < 0){
            return -1;
        } else if (compareResult > 0){
            return 1;
        } else
            return 0;
    }

    public String toString() {
        return getDepName();
    }
}

class GenericLinkedList <T extends Comparable<T>>{

    //represent the head an tail of the singly linked list
    private Node<T> first = null;
    private Node<T> last = null;
    int count = 0;

    public class Node<T>{
        T value;
        Node<T> next = null;

        public Node(T value){
            this.value = value;
            this.next = null;
        }
    }

    public void add (T element){
        Node<T> newnode = new Node<T>(element);

        if(first==null){
            first = newnode;
            last = newnode;
        }
        else{
            Node<T> lastnode = gotolastnode(first);
            lastnode.next = newnode;
        }
        count++;
    }

    public T get(int pos)
    {
        Node<T> Nodeptr = first;
        int hopcount=0;
        while (hopcount < count && hopcount<pos)
        {   if(Nodeptr!=null)
        {
            Nodeptr = Nodeptr.next;
        }
            hopcount++;
        }
        return  Nodeptr.value;
    }

    private Node<T> gotolastnode(Node<T> nodepointer) {
        if (nodepointer == null) {
            return nodepointer;
        } else {
            if (nodepointer.next == null)
                return nodepointer;
            else
                return gotolastnode(nodepointer.next);

        }
    }

    public void sortList(){
        Node<T> currentNode = first;
        Node<T> index = null;
        T temp;

        if(first == null){
            return;
        }else{
            while (currentNode != null){
                //node index will point to node next to current
                index = currentNode.next;
                while(index != null) {
                    //If current node's value is greater than index's value, swap the values
                    if(currentNode.value.compareTo(index.value) > 0){
                        temp = currentNode.value;
                        currentNode.value = index.value;
                        index.value = temp;
                    }
                    index = index.next;
                }currentNode = currentNode.next;
            }
        }
    }
    public void display(){
        Node<T> current = first;
        if(first == null){
            System.out.println("List is empty");
            return;
        }
        while(current != null){
            System.out.print(current.value + " ");
            current = current.next;
        }
        System.out.println();
    }
    public int size(){
        Node<T> current = first;
        int size= 0;
        while(current != null){
            current = current.next;
            size++;
        }
        return size;
    }
    /* Given a key, deletes the first
        occurrence of key in
      * linked list */
    public void deleteNode(T key)
    {
        // Store head node
        Node temp = first, prev = null;

        // If head node itself holds the key to be deleted
        if (temp != null && temp.value == key) {
            first = temp.next; // Changed head
            return;
        }

        // Search for the key to be deleted, keep track of
        // the previous node as we need to change temp.next
        while (temp != null && temp.value != key) {
            prev = temp;
            temp = temp.next;
        }

        // If key was not present in linked list
        if (temp == null)
            return;

        // Unlink the node from linked list
        prev.next = temp.next;
    }
}




class Enrollment {
    private String studentid;
    private String courseNum;
    private String year, semester, grade;


    public Enrollment(String studentID, String courseNum, String year, String semester, String grade) throws IOException{
        this.studentid = studentID;
        this.courseNum = courseNum;
        this.year = year;
        this.semester = semester;
        this.grade = grade;
    }




    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentID) {
        this.studentid = studentID;
    }

    public String getCourseNum() {
        return courseNum;
    }

    public void setCourseNum(String courseNum) {
        this.courseNum = courseNum;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }


}



public class Main extends Application {

    MenuBar menuBar;
    Stage window;
    Scene addInstructorScene, editInstructorScene;
    Scene addDepartmentScene, editDepartmentScene;
    Scene addStudentScene, editStudentScene, viewStudentScene;
    Scene addCourseScene, editCourseScene, viewCourseScene;
    Scene addEnrollmentScene, viewEnrollmentScene;
    Scene byStudentScene;
    Scene reportScene;


    public static Connection connection;
    public static void main(String[] args) throws SQLException {
        //This code sets up file reader object

        try(FileReader reader = new FileReader("app.config")) {
            Properties properties = new Properties();
            properties.load(reader);

            //some strings
            String dbUser= properties.getProperty("dbUser");
            String dbPassword= properties.getProperty("dbPassword");
            String dbUrl= properties.getProperty("dbUrl");
            connection = DriverManager.getConnection(dbUrl,dbUser, dbPassword);

        } catch (IOException e) {
            e.printStackTrace();
        }


        launch(args);
        connection.close();
    }
    public long numOfRecordsinTable(String tablename){
        long num_records = 0;
        try {
            Statement st = connection.createStatement();
            String sql = String.format("SELECT COUNT(*) FROM university.%s",tablename);
            ResultSet result = st.executeQuery(sql);
            while (result.next())
                return num_records += Integer.parseInt(result.getString(1));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }return num_records;
    }

    public String getChoice(ChoiceBox<String> choiceBox){
        return choiceBox.getValue();
    }


    public static void displayError(String title, String message){

        Stage window =  new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(300);

        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("Ok");
        closeButton.setOnAction(e -> window.close());

        VBox layout =  new VBox(10);
        layout.getChildren().addAll(label,closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout);
        window.setScene(scene);

        window.showAndWait();

    }

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        window = primaryStage;
        //set tittle for the Stage=Window
        window.setTitle("University Records");

        //Create the menu bar
        menuBar = new MenuBar();

        //Create the File menu
        Menu fileMenu = new Menu("File");
        MenuItem exitItem = new MenuItem("Exit");
        fileMenu.getItems().add(exitItem);

        //Register an event handler for the exit item.
        exitItem.setOnAction(e -> {
            window.close();
        });

        //Create the Faculty menu
        Menu facultyMenu = new Menu("Faculty");
        Menu instructor = new Menu("Instructor");
        Menu department = new Menu("Department");
        facultyMenu.getItems().addAll(department, instructor);
        MenuItem addInst = new MenuItem("Add");
        MenuItem editInst= new MenuItem("Edit");
        instructor.getItems().addAll(addInst, editInst);
        MenuItem addDep = new MenuItem("Add");
        MenuItem editDep= new MenuItem("Edit");
        department.getItems().addAll(addDep, editDep);

        //Register event handler for the Faculty menu items

        addInst.setOnAction(e -> {
            try {
                window.setScene(addInstructorScene());
            } catch (IOException | SQLException ioException) {
                ioException.printStackTrace();
            }
        });
        addDep.setOnAction(e -> {
            try {
                window.setScene(addDepartmentScene());
            } catch (IOException | SQLException ioException) {
                ioException.printStackTrace();
            }
        });
        editInst.setOnAction(e -> {
            try {
                window.setScene(editInstructorScene());
            } catch (IOException | SQLException ioException) {
                ioException.printStackTrace();
            }
        });
        editDep.setOnAction(e -> {
            try {
                window.setScene(editDepartmentScene());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        //Create the Student menu
        Menu studentsMenu = new Menu("Students");
        MenuItem addStudent = new MenuItem("Add Student");
        MenuItem editStudent = new MenuItem("Edit Student");
        MenuItem viewStudent = new MenuItem("View Student");

        //Register an event handler for the Student menu items
        addStudent.setOnAction(e -> {
            try {
                window.setScene(addStudentScene());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        editStudent.setOnAction(e -> {
            window.setScene(editStudentScene());
        });
        viewStudent.setOnAction(e -> {
            window.setScene(viewStudentScene());
        });
        studentsMenu.getItems().addAll(addStudent, editStudent, viewStudent);

        //Create Courses menu
        Menu coursesMenu = new Menu("Courses");
        MenuItem addCourse = new MenuItem("Add Course");
        MenuItem editCourse = new MenuItem("Edit Course");
        MenuItem viewCourse = new MenuItem("View Course");

        //Register Event handlers for the Course menu items
        addCourse.setOnAction(e -> {
            try {
                window.setScene(addCourseScene());
            } catch (IOException | SQLException ioException) {
                ioException.printStackTrace();
            }
        });
        editCourse.setOnAction(e -> {
            try {
                window.setScene(editCourseScene());
            } catch (IOException | SQLException ioException) {
                ioException.printStackTrace();
            }
        });
        viewCourse.setOnAction(e -> {
            window.setScene(viewCourseScene());
        });
        coursesMenu.getItems().addAll(addCourse, editCourse, viewCourse);

        //Create Enrollments menu
        Menu enrollmentsMenu = new Menu("Enrollments");
        MenuItem addEnrollments = new MenuItem("Add Enrollment");
        //MenuItem editEnrollments = new MenuItem("Edit Enrollment");
        MenuItem viewEnrollments = new MenuItem("View/Edit Enrollment");
        //Register Event handlers for the Enrollements menu items

        addEnrollments.setOnAction(e -> {

            try {
                window.setScene(addEnrollmentScene());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        viewEnrollments.setOnAction(e -> {
            window.setScene(viewEnrollmentScene());

        });
        enrollmentsMenu.getItems().addAll(addEnrollments, viewEnrollments);

        //Create Grades menu
        Menu gradesMenu = new Menu("Grades");
        Menu addEditGrades= new Menu("Add/Edit Grades");
        gradesMenu.getItems().add(addEditGrades);
        MenuItem byStudent = new MenuItem("By Student");
        MenuItem byCourse = new MenuItem("By Course");
        //Register Event handlers for the Enrollments menu items
        byStudent.setOnAction(e -> {
            try {
                window.setScene(byStudentScene());
            } catch (IOException | SQLException ioException) {
                ioException.printStackTrace();
            }
        });

        addEditGrades.getItems().addAll(byStudent);

        //Create Reports menu
        Menu reportMenu = new Menu("Reports");
        MenuItem generateRep = new MenuItem("Generate Report");
        //Register Event handlers for the Reports menu items
        generateRep.setOnAction(e -> {
            try {
                window.setScene(reportScene());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        reportMenu.getItems().add(generateRep);

        //Add the File/Students/Courses/Enrollments/Grades/Reports menus to the menuBar
        menuBar.getMenus().addAll(fileMenu, facultyMenu, studentsMenu, coursesMenu, enrollmentsMenu, gradesMenu, reportMenu);


        window.setScene(addDepartmentScene());
        window.setResizable(false);
        window.show();


    }

    public Scene addStudentScene () throws IOException {

        TextField tf1 = new TextField();
        tf1.setDisable(true);
        TextField tf2 = new TextField();
        TextField tf3 = new TextField();
        TextField tf4 = new TextField();
        TextField tf5 = new TextField();
        TextField tf6 = new TextField();

        //create a labels to display prompt
        Label sceneLabel = new Label("New Student Information");
        Label studentIDLabel = new Label("Student ID");
        Label firstName = new Label("First Name");
        Label lastName = new Label("Last Name");
        Label address = new Label("Address");
        Label city = new Label("City");
        Label state = new Label("State");
        Label zip = new Label("Zip Code");
        ChoiceBox<String> statesList = new ChoiceBox<>();

        statesList.getItems().addAll("AL", "AK", "AZ", "AR", "AS", "CA", "CO", "DE", "DC", "FM", "FL", "GA", "GU",
                "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MH", "MD", "MA", "MI", "MS", "MO", "MT", "NE", "NV", "NH",
                "NJ", "NM", "NY", "NC", "MP", "OH", "OK", "OR", "PW", "PA", "PR", "RI", "SC", "TN", "TX", "UT", "VT", "VA", "VI",
                "WA", "WV", "WI", "WY");
        statesList.setValue("");
        //int index = statesList.getSelectionModel().getSelectedIndex();


        HBox hbox0 = new HBox(10, sceneLabel);
        hbox0.setAlignment(Pos.CENTER);
        HBox hbox1 = new HBox(10, studentIDLabel, tf1);
        hbox1.setAlignment(Pos.CENTER);
        HBox hbox2 = new HBox(9, firstName, tf2);
        hbox2.setAlignment(Pos.CENTER);
        HBox hbox3 = new HBox(11, lastName, tf3);
        hbox3.setAlignment(Pos.CENTER);
        HBox hbox4 = new HBox(24, address, tf4);
        hbox4.setAlignment(Pos.CENTER);
        HBox hbox5 = new HBox(46, city, tf5);
        hbox5.setAlignment(Pos.CENTER);
        HBox hbox6 = new HBox(19, zip, tf6);
        hbox6.setAlignment(Pos.CENTER);
        HBox hbox7 = new HBox(40, state, statesList);
        hbox7.setAlignment(Pos.CENTER);

        ;
        VBox vbox = new VBox(12);
        vbox.getChildren().addAll(hbox0, hbox1, hbox2, hbox3, hbox4, hbox5, hbox6, hbox7);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);


        BorderPane layout = new BorderPane();
        layout.setTop(menuBar);
        layout.setCenter(vbox);

        Button button = new Button("Create Student");

        VBox vCreateStud = new VBox(button);
        vCreateStud.setPadding(new Insets(30));
        vCreateStud.setAlignment(Pos.CENTER);
        layout.setBottom(vCreateStud);
        tf1.setText(String.valueOf(numOfRecordsinTable("students")+1));


        button.setOnAction(e-> {

            try {
                if (tf1.getText().isEmpty() || tf2.getText().isEmpty() || tf3.getText().isEmpty() ||
                        tf4.getText().isEmpty() || tf5.getText().isEmpty() || tf6.getText().isEmpty() ||
                        getChoice(statesList).isEmpty()) {
                    displayError("Invalid", "Missing Information to Create Student");
                } else {
                    //now send copy of data to the university database
                    CallableStatement preparedStatement = connection.prepareCall("{call add_student(?,?,?,?,?,?)}");
                    //Statement st = connection.createStatement();
                    //String sql = "INSERT INTO university.students(firstName,lastName,address,city,zipcode,state)\nVALUES" +
                    //        "('%s', '%s', '%s', '%s', '%s', '%s')";
                    //sql = String.format(sql,tf2.getText().toUpperCase(Locale.ROOT),
                         //   tf3.getText().toUpperCase(Locale.ROOT),tf4.getText().toUpperCase(Locale.ROOT),
                       //     tf5.getText().toUpperCase(Locale.ROOT),tf6.getText().toUpperCase(Locale.ROOT),
                     //       getChoice(statesList).toUpperCase(Locale.ROOT));
                    preparedStatement.setString(1, tf2.getText().toUpperCase(Locale.ROOT));
                    preparedStatement.setString(2, tf3.getText().toUpperCase(Locale.ROOT));
                    preparedStatement.setString(3, tf4.getText().toUpperCase(Locale.ROOT));
                    preparedStatement.setString(4, tf5.getText().toUpperCase(Locale.ROOT));
                    preparedStatement.setString(5, tf6.getText().toUpperCase(Locale.ROOT));
                    preparedStatement.setString(6, getChoice(statesList).toUpperCase(Locale.ROOT));
                    preparedStatement.execute();
                    Statement st = connection.createStatement();
                    ResultSet result = st.executeQuery("SELECT COUNT(*) FROM university.students");
                    int count = 1;
                    while (result.next())
                        count += Integer.parseInt(result.getString(1));
                        tf1.setText(String.valueOf(count));
                    //now clear fields before displaying the confirmation of the creation of the profile
                    tf2.clear();
                    tf3.clear();
                    tf4.clear();
                    tf5.clear();
                    tf6.clear();
                    statesList.setValue("");
                    displayError("Success!", "Student Profile created!");
                }

            } catch (SQLException ioException) {
                ioException.printStackTrace();
            }

        });

        addStudentScene = new Scene(layout, 450, 400);

        return addStudentScene;
    }

    public Scene editStudentScene () {
        TextField tf1 = new TextField();
        TextField tf2 = new TextField();
        TextField tf3 = new TextField();
        TextField tf4 = new TextField();
        TextField tf5 = new TextField();
        TextField tf6 = new TextField();
        //create a labels to display prompt
        Label sceneLabel = new Label("Edit Student Information");
        Label studentIDLabel = new Label("Student ID");
        Label firstName = new Label("First Name");
        Label lastName = new Label("Last Name");
        Label address = new Label("Address");
        Label city = new Label("City");
        Label state = new Label("State");
        Label zip = new Label("Zip Code");

        ChoiceBox<String> statesList = new ChoiceBox<>();

        statesList.getItems().addAll("AL", "AK", "AZ", "AR", "AS", "CA", "CO", "DE", "DC", "FM", "FL", "GA", "GU",
                "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MH", "MD", "MA", "MI", "MS", "MO", "MT", "NE", "NV", "NH",
                "NJ", "NM", "NY", "NC", "MP", "OH", "OK", "OR", "PW", "PA", "PR", "RI", "SC", "TN", "TX", "UT", "VT", "VA", "VI",
                "WA", "WV", "WI", "WY");


        HBox hbox0 = new HBox(40, sceneLabel);
        hbox0.setAlignment(Pos.TOP_RIGHT);
        hbox0.setPadding(new Insets(30));
        HBox hbox1 = new HBox(10, studentIDLabel, tf1);
        hbox1.setAlignment(Pos.CENTER);
        HBox hbox2 = new HBox(9, firstName, tf2);
        hbox2.setAlignment(Pos.CENTER);
        HBox hbox3 = new HBox(11, lastName, tf3);
        hbox3.setAlignment(Pos.CENTER);
        HBox hbox4 = new HBox(24, address, tf4);
        hbox4.setAlignment(Pos.CENTER);
        HBox hbox5 = new HBox(46, city, tf5);
        hbox5.setAlignment(Pos.CENTER);
        HBox hbox6 = new HBox(19, zip, tf6);
        hbox6.setAlignment(Pos.CENTER);
        HBox hbox7 = new HBox(40, state, statesList);
        hbox7.setAlignment(Pos.CENTER);


        VBox vbox = new VBox(12);
        vbox.getChildren().addAll(hbox0, hbox1, hbox2, hbox3, hbox4, hbox5, hbox6, hbox7);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.TOP_LEFT);

        Button update = new Button("Update");
        Button reset =new Button("Reset");
        Button searchButton = new Button("Search");
        searchButton.setAlignment(Pos.CENTER);
        update.setOnAction(e-> {
            try {

                if (tf1.getText().isEmpty() ||  (tf1.getText().equals("")) || tf2.getText().isEmpty()
                || tf3.getText().isEmpty() || tf4.getText().isEmpty() || tf5.getText().isEmpty()
                || tf6.getText().isEmpty() || getChoice(statesList).equals("")){

                    displayError("Invalid", "Missing Information to Update Student");

                } else {
                    long recordNum = Long.parseLong(tf1.getText());
                    //Now UPDATE database
                    Statement st = connection.createStatement();
                    String sql;
                    sql = String.format("UPDATE university.students SET firstName = '%s', lastName = '%s',"+
                            " address = '%s', city = '%s', zipCode = '%s', state = '%s' WHERE ID = '%s'",
                            tf2.getText().toUpperCase(Locale.ROOT), tf3.getText().toUpperCase(Locale.ROOT)
                            ,tf4.getText().toUpperCase(Locale.ROOT),tf5.getText().toUpperCase(Locale.ROOT)
                            ,tf6.getText().toUpperCase(Locale.ROOT), getChoice(statesList).toUpperCase(Locale.ROOT),
                            tf1.getText());
                    String sql1 = String.format("UPDATE university.enrollments SET studentName = '%s' WHERE studentID = '%s'",
                            tf2.getText().toUpperCase(Locale.ROOT).trim()+" "+ tf3.getText().toUpperCase(Locale.ROOT).trim(),
                            tf1.getText());

                    st.executeUpdate(sql);

                    Statement statement = connection.createStatement();
                    statement.executeUpdate(sql1);

                    tf1.setDisable(false);
                    tf1.clear();
                    tf2.clear();
                    tf3.clear();
                    tf4.clear();
                    tf5.clear();
                    tf6.clear();

                    displayError("Updated", "Student Info Updated");
                }
            } catch (SQLException ioException) {
                ioException.printStackTrace();
            }
        });
        reset.setOnAction(e-> {
            tf1.clear();
            tf1.setDisable(false);
            tf2.clear();
            tf3.clear();
            tf4.clear();
            tf5.clear();
            tf6.clear();
            statesList.setValue("");

        });
        searchButton.setOnAction(e-> {
            try {
                Statement st = connection.createStatement();
                long l = Long.parseLong(tf1.getText());

                if (l < 0 || l > numOfRecordsinTable("students")  ){

                    tf1.setDisable(false);
                    tf1.clear();
                    tf2.clear();
                    tf3.clear();
                    tf4.clear();
                    tf5.clear();
                    tf6.clear();
                    statesList.setValue("");
                    displayError("Invalid StudentID", "Record not found");
                }
                else {
                    tf1.setDisable(true);
                    String query = String.format("SELECT * FROM university.students WHERE ID = '%s'", tf1.getText());
                    ResultSet result = st.executeQuery(query);

                    if(result.next()){
                        tf2.setText(result.getString(2));
                        tf3.setText(result.getString(3));
                        tf4.setText(result.getString(4));
                        tf5.setText(result.getString(5));
                        tf6.setText(result.getString(6));
                        statesList.setValue(result.getString(7));
                    }

                }
            } catch (SQLException ioException) {
                ioException.printStackTrace();
            }


        });
        VBox column3 = new VBox(20,searchButton,update, reset);
        column3.setPadding(new Insets(20));
        column3.setAlignment(Pos.CENTER_LEFT);

        BorderPane layout = new BorderPane();
        layout.setTop(menuBar);
        layout.setCenter(vbox);
        layout.setRight(column3);

        editStudentScene = new Scene(layout, 450, 400);

        return editStudentScene;
    }

    public Scene viewStudentScene () {
        TextField tf1 = new TextField();
        TextField tf2 = new TextField();
        TextField tf3 = new TextField();
        TextField tf4 = new TextField();
        TextField tf5 = new TextField();
        TextField tf6 = new TextField();
        TextField statesList = new TextField();
        //create a labels to display prompt
        Label sceneLabel = new Label("New Student Information");
        Label studentIDLabel = new Label("Student ID");
        Label firstName = new Label("First Name");
        Label lastName = new Label("Last Name");
        Label address = new Label("Address");
        Label city = new Label("City");
        Label state = new Label("State");
        Label zip = new Label("Zip Code");



        HBox hbox1 = new HBox(10, studentIDLabel, tf1);
        hbox1.setAlignment(Pos.CENTER);
        HBox hbox2 = new HBox(9, firstName, tf2);
        hbox2.setAlignment(Pos.CENTER);
        HBox hbox3 = new HBox(11, lastName, tf3);
        hbox3.setAlignment(Pos.CENTER);
        HBox hbox4 = new HBox(24, address, tf4);
        hbox4.setAlignment(Pos.CENTER);
        HBox hbox5 = new HBox(46, city, tf5);
        hbox5.setAlignment(Pos.CENTER);
        HBox hbox6 = new HBox(19, zip, tf6);
        hbox6.setAlignment(Pos.CENTER);
        HBox hbox7 = new HBox(40, state, statesList);
        hbox7.setAlignment(Pos.CENTER);

        Button search_student = new Button("Search Student");
        VBox vCreateStud = new VBox(search_student);
        vCreateStud.setPadding(new Insets(10));
        vCreateStud.setAlignment(Pos.CENTER);

        search_student.setOnAction(e-> {
            try {
                if (tf1.getText().isBlank() || Integer.parseInt(tf1.getText()) > numOfRecordsinTable("students") ||
                Integer.parseInt(tf1.getText())<= 0){
                    tf1.setDisable(false);
                    tf1.clear();
                    tf2.clear();
                    tf3.clear();
                    tf4.clear();
                    tf5.clear();
                    tf6.clear();
                    tf2.setDisable(false);
                    tf4.setDisable(false);
                    tf5.setDisable(false);
                    tf6.setDisable(false);
                    tf3.setDisable(false);
                    statesList.setText("");
                    displayError("Invalid StudentID", "Record not found");
                }
                else {

                    Statement st = connection.createStatement();
                    String query = String.format("SELECT * FROM university.students WHERE ID = '%s'", tf1.getText());
                    ResultSet result = st.executeQuery(query);

                    if(result.next()){
                        tf2.setText(result.getString(2));
                        tf3.setText(result.getString(3));
                        tf4.setText(result.getString(4));
                        tf5.setText(result.getString(5));
                        tf6.setText(result.getString(6));
                        statesList.setText(result.getString(7));
                    }
                    tf2.setDisable(true);tf2.setStyle("-fx-opacity: 0.75");
                    tf3.setDisable(true);tf3.setStyle("-fx-opacity: 0.75");
                    tf4.setDisable(true);tf4.setStyle("-fx-opacity: 0.75");
                    tf5.setDisable(true);tf5.setStyle("-fx-opacity: 0.75");
                    tf6.setDisable(true);tf6.setStyle("-fx-opacity: 0.75");
                    statesList.setStyle("-fx-opacity: 0.75");
                    statesList.setDisable(true);

                }
            } catch (SQLException ioException) {
                ioException.printStackTrace();
            }

        });


        VBox vbox = new VBox(12);
        vbox.getChildren().addAll(hbox1, vCreateStud, hbox2, hbox3, hbox4, hbox5, hbox6, hbox7);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        BorderPane layout = new BorderPane();
        layout.setTop(menuBar);
        layout.setCenter(vbox);

        viewStudentScene = new Scene(layout, 450, 400);

        return viewStudentScene;
    }

    public Scene addCourseScene () throws IOException, SQLException {

        //create a labels to display prompt
        Label sceneLabel = new Label("New Course Information");
        Label courseNum = new Label("Course Number");
        Label courseID = new Label("Course ID");
        Label courseName = new Label("Course Name");
        Label instructorName = new Label("Instructor Name");
        Label departmentName = new Label("Department       ");

        Statement st = connection.createStatement();

        GenericLinkedList<Department> depLL = new GenericLinkedList<>();
        ArrayList<String> depL = new ArrayList<>();
        Long z = numOfRecordsinTable("departments");
        int t = z.intValue();
        for (int j = 1; j <= t; j++ ){
            String query = String.format("SELECT depName FROM departments WHERE ID = %d;", j);
            ResultSet result = st.executeQuery(query);
            Department x;
            if(result.next()) {
                x = new Department(result.getString(1));
                depLL.add(x);
            }
        }

        for(int i=0; i < depLL.size(); i++){
            String x;
            x = depLL.get(i).toString();
            depL.add(x);

        }

        ChoiceBox<String> departmentsList = new ChoiceBox<>();
        departmentsList.setMinWidth(150);
        for (int s = 0; s < depL.size();s++){

            departmentsList.getItems().add(depL.get(s));
        }

        ChoiceBox<String> instructorsList = new ChoiceBox<>();
        instructorsList.setMinWidth(150);

//*************************** display instructorList dynamically depending on the department selected
        //listen for selection changes
        ArrayList<String> values = new ArrayList<>();
        departmentsList.getSelectionModel().selectedItemProperty().addListener((v, oldV, newV)->{

            ArrayList<String> iLL = new ArrayList<>();
            iLL.clear();
            values.clear();
            instructorsList.getItems().setAll();
            try {
                String query = String.format("SELECT instructorName FROM university.instructors WHERE depName = '%s'", newV);
                ResultSet instructors = st.executeQuery(query);

                while (instructors.next()) {
                    values.add(instructors.getString("instructorName"));
                }

            } catch (SQLException e) {
                    e.printStackTrace();
            }

            instructorsList.valueProperty().set(null);
            for (int s = 0; s < values.size();s++) {
                instructorsList.getItems().add(values.get(s));
            }
        });

        TextField tf1 = new TextField();
        tf1.setDisable(true);
        tf1.setText(String.valueOf(numOfRecordsinTable("courses")+1));
        TextField tf2 = new TextField();
        TextField tf3 = new TextField();

        HBox hbox0 = new HBox(10, sceneLabel);
        hbox0.setAlignment(Pos.CENTER);
        HBox hbox1 = new HBox(30, courseNum, tf1);
        hbox1.setAlignment(Pos.CENTER);
        HBox hbox2 = new HBox(62, courseID, tf2);
        hbox2.setAlignment(Pos.CENTER);
        HBox hbox3 = new HBox(41, courseName, tf3);
        hbox3.setAlignment(Pos.CENTER);
        HBox hbox4 = new HBox(28, departmentName, departmentsList);
        hbox4.setAlignment(Pos.CENTER);
        HBox hbox5 = new HBox(32, instructorName, instructorsList);
        hbox5.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(12);
        vbox.getChildren().addAll(hbox0, hbox1, hbox2, hbox3, hbox4, hbox5);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        BorderPane layout = new BorderPane();
        layout.setTop(menuBar);
        layout.setCenter(vbox);

        Button createCourse = new Button("Create Course");
        createCourse.setOnAction(e-> {
            try {
                if (tf1.getText().isEmpty() || tf2.getText().isBlank() || tf3.getText().isBlank()
                || getChoice(instructorsList).isEmpty() || getChoice(departmentsList).isEmpty() ||
                departmentsList.getValue().equals("") || instructorsList.getValue().equals("")) {
                    displayError("Invalid", "Missing Information to Create Course");
                } else {
                    String togetinstructorID = String.format("SELECT ID FROM university.instructors WHERE instructorName = '%s';", getChoice(instructorsList));
                    ResultSet r1 = st.executeQuery(togetinstructorID);
                    int ID = 0;
                    while(r1.next()){

                        ID += r1.getInt(1);
                        System.out.println(ID);

                    }
                    Statement newstatement = connection.createStatement();
                    String query = String.format("INSERT INTO university.courses(courseID, courseName, instructorID, " +
                            "depName) VALUES('%s', '%s', %d,'%s');", tf2.getText().toUpperCase(Locale.ROOT),
                            tf3.getText().toUpperCase(Locale.ROOT), ID , getChoice(departmentsList));
                    newstatement.executeUpdate(query);
                    String q = String.format("SELECT ID FROM university.departments WHERE depName = '%s';",
                            getChoice(departmentsList));
                    ResultSet rq = st.executeQuery(q);
                    if (rq.next()){
                        String otherq = String.format("UPDATE university.courses SET depID = %d WHERE depName = '%s';",
                                rq.getInt(1), getChoice(departmentsList));
                        st.executeUpdate(otherq);
                    }

                    tf2.clear();
                    tf3.clear();
                    instructorsList.setValue("");
                    departmentsList.setValue("");
                    tf1.setText(String.valueOf(numOfRecordsinTable("courses")+1));

                    displayError("Success!", "Course Profile created!");
                }

            } catch (SQLException ioException) {
                ioException.printStackTrace();
            }
        });

        VBox forButton = new VBox(createCourse);
        forButton.setPadding(new Insets(30));
        forButton.setAlignment(Pos.CENTER);
        layout.setBottom(forButton);

        addCourseScene = new Scene(layout, 450, 400);

        return addCourseScene;
    }

    public Scene editCourseScene () throws IOException, SQLException {

        //create labels to display prompt
        Label sceneLabel = new Label("Edit Course Information");
        Label courseNum = new Label("Course Number");
        Label courseID = new Label("Course ID");
        Label courseName = new Label("Course Name");
        Label instructorName = new Label("Instructor Name");
        Label departmentName = new Label("Department     ");

        GenericLinkedList<Department> depLL = new GenericLinkedList<>();
        Statement st = connection.createStatement();

        ArrayList<String> depL = new ArrayList<>();
        Long z = numOfRecordsinTable("departments");
        int t = z.intValue();
        for (int j = 1; j <= t; j++ ){
            String query = String.format("SELECT depName FROM university.departments WHERE ID = %d;", j);
            ResultSet result = st.executeQuery(query);
            Department x;
            if(result.next()) {
                x = new Department(result.getString(1));
                depLL.add(x);
            }
        }


        for(int i=0; i < depLL.size(); i++){
            String x;
            x = depLL.get(i).toString();
            depL.add(x);

        }

        ChoiceBox<String> departmentsList = new ChoiceBox<>();
        departmentsList.setMinWidth(150);
        for (int s = 0; s < depL.size();s++){

            departmentsList.getItems().add(depL.get(s));
        }

        ChoiceBox<String> instructorsList = new ChoiceBox<>();
        instructorsList.setMinWidth(150);

//*************************** display instructorList dynamically depending on the department selected
        //listen for selection changes
        ArrayList<String> values = new ArrayList<>();
        departmentsList.getSelectionModel().selectedItemProperty().addListener((v, oldV, newV)->{

            ArrayList<String> iLL = new ArrayList<>();
            iLL.clear();
            values.clear();
            instructorsList.getItems().setAll();
            try {
                String query = String.format("SELECT instructorName FROM university.instructors WHERE depName = '%s'", newV);
                ResultSet instructors = st.executeQuery(query);

                while (instructors.next()) {
                    values.add(instructors.getString("instructorName"));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            //instructorsList.getItems().clear();
            for (int s = 0; s < values.size();s++) {
                instructorsList.getItems().add(values.get(s));
            }
        });
        ///####

        TextField tf1 = new TextField();
        TextField tf2 = new TextField();
        TextField tf3 = new TextField();

        HBox hbox0 = new HBox(10, sceneLabel);
        hbox0.setAlignment(Pos.TOP_RIGHT);
        hbox0.setPadding(new Insets(30));
        HBox hbox1 = new HBox(30, courseNum, tf1);
        hbox1.setAlignment(Pos.CENTER_RIGHT);
        HBox hbox2 = new HBox(62, courseID, tf2);
        hbox2.setAlignment(Pos.CENTER_RIGHT);
        HBox hbox3 = new HBox(41, courseName, tf3);
        hbox3.setAlignment(Pos.CENTER_RIGHT);
        HBox hbox4 = new HBox(32, departmentName, departmentsList);
        hbox4.setAlignment(Pos.CENTER_RIGHT);
        HBox hbox5 = new HBox(28, instructorName, instructorsList);
        hbox5.setAlignment(Pos.CENTER_RIGHT);

        VBox vbox = new VBox(12);
        vbox.getChildren().addAll(hbox0, hbox1, hbox2, hbox3, hbox4, hbox5);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.TOP_RIGHT);

        Button update = new Button("Update");
        Button reset =new Button("Reset");
        Button searchButton = new Button("Search");
        searchButton.setAlignment(Pos.CENTER);
        update.setOnAction(e-> {
            try {
                if (tf1.getText().isEmpty() || tf2.getText().isBlank() || tf3.getText().isBlank() ||
                getChoice(instructorsList).isBlank() || getChoice(departmentsList).isBlank() || instructorsList.getValue().equals("")
                || departmentsList.getValue().equals("")) {

                    displayError("Invalid", "Missing Information to Update Course");

                } else {
                    String togetinstructorID = String.format("SELECT ID FROM university.instructors WHERE instructorName = '%s';", getChoice(instructorsList));
                    ResultSet r1 = st.executeQuery(togetinstructorID);
                    int ID = 0;
                    while(r1.next()){

                        ID += r1.getInt(1);
                        System.out.println(ID);

                    }
                    String query = String.format("UPDATE university.courses SET courseID = '%s', courseName = '%s', " +
                            "instructorID = %d, depName = '%s' WHERE courseNum = %d;", tf2.getText().toUpperCase(Locale.ROOT),
                            tf3.getText().toUpperCase(Locale.ROOT), ID,
                            getChoice(departmentsList).toUpperCase(Locale.ROOT), Integer.parseInt(tf1.getText()));
                    st.executeUpdate(query);
                    String q = String.format("SELECT ID FROM university.departments WHERE depName = '%s';",
                            getChoice(departmentsList));
                    ResultSet rq = st.executeQuery(q);
                    String x;
                    if (rq.next()){
                        Statement stt = connection.createStatement();
                        x =rq.getString(1);
                        String otherq = String.format("UPDATE university.courses SET depID = '%s' WHERE depName = '%s';",
                                x, getChoice(departmentsList));
                        stt.executeUpdate(otherq);
                        x = rq.getString(1);
                    }


                    tf1.setDisable(false);
                    tf1.clear();
                    tf2.clear();
                    tf3.clear();
                    departmentsList.setValue("");
                    //instructorsList.getItems().clear();
                    instructorsList.setValue("");


                    displayError("Complete", "Course Info Updated");
                }
            } catch (SQLException ex ){
                ex.printStackTrace();
            }
        });
        reset.setOnAction(e-> {
            tf1.setDisable(false);
            tf1.clear();
            tf2.clear();
            tf3.clear();
            departmentsList.setValue("");
            instructorsList.setValue("");
            instructorsList.getItems().clear();

        });
        searchButton.setOnAction(e-> {
            try {
                long l = numOfRecordsinTable("courses");

                if (Integer.parseInt(tf1.getText())>l || Integer.parseInt(tf1.getText())<=0){
                    displayError("Invalid Course Number", "Record not found");
                    tf1.setDisable(false);
                }
                else {
                    tf1.setDisable(true);
                    String query = String.format("SELECT * FROM university.courses WHERE courseNum = '%s';", tf1.getText());
                    ResultSet result = st.executeQuery(query);
                    if(result.next()){

                        tf2.setText(result.getString(2));
                        tf3.setText(result.getString(3));
                        departmentsList.setValue(result.getString(5));
                    }
                    String insquery = String.format("SELECT instructorID FROM university.courses WHERE courseNum = '%s';",
                            tf1.getText());
                    Statement otherst = connection.createStatement();
                    ResultSet r = otherst.executeQuery(insquery);
                    int instructorID;
                    if(r.next()){
                        instructorID = r.getInt(1);
                        //another query to find the name fo the isntructor
                        String instructorNameQuery = String.format("select instructorName from university.instructors where ID = %d;", instructorID);
                        ResultSet rs = otherst.executeQuery(instructorNameQuery);
                        while(rs.next()){
                            instructorsList.setValue((rs.getString(1)));
                        }

                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        VBox column3 = new VBox(20,searchButton,update, reset);
        column3.setPadding(new Insets(20));
        column3.setAlignment(Pos.CENTER_LEFT);

        BorderPane layout = new BorderPane();
        layout.setTop(menuBar);
        layout.setCenter(vbox);
        layout.setRight(column3);


        editCourseScene = new Scene(layout, 450, 400);

        return editCourseScene;
    }

    public Scene viewCourseScene () {
        Label sceneLabel = new Label("Edit Course Information");
        Label courseNum = new Label("Course Number");
        Label courseID = new Label("Course ID");
        Label courseName = new Label("Course Name");
        Label instructorName = new Label("Instructor Name");
        Label departmentName = new Label("Department");

        TextField departmentsList = new TextField();
        departmentsList.setMinWidth(150);
        departmentsList.setDisable(true);
        departmentsList.setStyle("-fx-opacity: 2.0;");


        TextField tf1 = new TextField();
        TextField tf2 = new TextField();
        TextField tf3 = new TextField();
        TextField tf4 = new TextField();
        tf2.setDisable(true);
        tf3.setDisable(true);
        tf4.setDisable(true);

        tf2.setStyle("-fx-opacity: .75;");
        tf3.setStyle("-fx-opacity: .75;");
        tf4.setStyle("-fx-opacity: .75;");

        HBox hbox0 = new HBox(10, sceneLabel);
        hbox0.setAlignment(Pos.CENTER);
        hbox0.setPadding(new Insets(30));
        HBox hbox1 = new HBox(30, courseNum, tf1);
        hbox1.setAlignment(Pos.CENTER);
        HBox hbox2 = new HBox(62, courseID, tf2);
        hbox2.setAlignment(Pos.CENTER);
        HBox hbox3 = new HBox(41, courseName, tf3);
        hbox3.setAlignment(Pos.CENTER);
        HBox hbox4 = new HBox(28, instructorName, tf4);
        hbox4.setAlignment(Pos.CENTER);
        HBox hbox5 = new HBox(49, departmentName, departmentsList);
        hbox5.setAlignment(Pos.CENTER);


        Button search_student = new Button("Search Course");
        VBox vCreateStud = new VBox(search_student);
        vCreateStud.setPadding(new Insets(10));
        vCreateStud.setAlignment(Pos.CENTER);
        search_student.setOnAction(e-> {
            try {

                long l = Long.parseLong(tf1.getText());

                if (l <= 0 || l > numOfRecordsinTable("courses")){
                    displayError("Invalid Course Number", "Record not found");
                    tf1.setDisable(false);
                    tf2.clear();
                    tf3.clear();
                    tf4.clear();
                    departmentsList.clear();
                }
                else {
                    String query = String.format("SELECT * FROM university.courses WHERE courseNum = '%s';",
                            tf1.getText());
                    Statement s = connection.createStatement();
                    ResultSet r = s.executeQuery(query);
                    if(r.next()){
                        tf2.setText(r.getString(2));
                        tf2.setStyle("-fx-opacity: 0.75");
                        tf3.setText(r.getString(3));
                        tf3.setStyle("-fx-opacity: 0.75");
                        tf4.setStyle("-fx-opacity: 0.75");
                        departmentsList.setStyle("-fx-opacity: 0.75");
                        departmentsList.setText(r.getString(5));
                        String q = String.format("select instructorName from university.instructors where ID= %d", r.getInt(4));
                        ResultSet rs = s.executeQuery(q);
                        while(rs.next()) {
                            tf4.setText(rs.getString(1));
                        }
                    }
                    tf2.setDisable(true);
                    tf3.setDisable(true);
                    tf4.setDisable(true);
                }
            } catch (SQLException ioException) {
                ioException.printStackTrace();
            }
        });

        VBox vbox = new VBox(12);
        vbox.getChildren().addAll(hbox1, vCreateStud, hbox2, hbox3, hbox4, hbox5);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        BorderPane layout = new BorderPane();
        layout.setTop(menuBar);
        layout.setCenter(vbox);

        viewCourseScene = new Scene(layout, 450, 400);

        return viewCourseScene;
    }
    public boolean uniqueEnrollmentForStudent(String studentID, String courseID, String semester, String year) throws SQLException {

        Statement st = connection.createStatement();
        String query = String.format("SELECT studentID, courseID, semester, year FROM university.enrollments WHERE " +
                "studentID = '%s' and courseID = '%s' and semester = '%s' and year = '%s'",studentID, courseID, semester, year);
        ResultSet result = st.executeQuery(query);
        while (result.next()){
            return false;
        }
        return true;
    }
    public Scene addEnrollmentScene () throws IOException {
        Label sceneLabel = new Label("New Enrollment Information");
        Label semesterLabel = new Label("Semester");
        Label yearLabel = new Label("Year");
        Label empty1 = new Label("");
        Label empty2 = new Label("");
        Label empty3 = new Label("");

        TextField tfFindStudent = new TextField();
        tfFindStudent.setPromptText("StudentID");
        TextField tfNameStudent = new TextField();
        tfNameStudent.setPromptText("Student Name");
        TextField tfCourseNumber = new TextField();
        tfCourseNumber.setPromptText("Course Number");
        TextField tfCourseID = new TextField();
        tfCourseID.setPromptText("Course ID");
        TextField tfCourseName = new TextField();
        tfCourseName.setPromptText("Course Name");

        ChoiceBox<String> yearList =  new ChoiceBox<>();
        yearList.getItems().addAll("2021","2020", "2019", "2018", "2017", "2016");
        yearList.setValue("");
        ChoiceBox<String> semesterList =  new ChoiceBox<>();
        semesterList.getItems().addAll("SPRING", "SUMMER", "FALL", "WINTER");
        semesterList.setValue("");

        Button createEnrollmentButton = new Button("Create Enrollment");
        createEnrollmentButton.setOnAction(e ->{
            try {
                Statement statement1 = connection.createStatement();
                if (tfFindStudent.getText().isEmpty() || tfNameStudent.getText().isEmpty() ||
                    tfCourseNumber.getText().isEmpty() || tfCourseID.getText().isEmpty() || tfCourseName.getText().isEmpty()){
                    displayError("Invalid", "Missing Information to create enrollment");
                }
                else {
                    if(!(uniqueEnrollmentForStudent(tfFindStudent.getText().trim(),tfCourseID.getText().trim(),
                            getChoice(semesterList), getChoice(yearList)))){
                        displayError("Enrolled Already", "Student is already enrolled to this Course");
                    }
                    else {
                        String query = String.format("INSERT INTO university.enrollments(studentName, studentID, courseName, " +
                                "courseID, semester, year, grades) VALUES('%s', '%s', '%s', '%s', '%s', '%s', '*')", tfNameStudent.getText(),
                                tfFindStudent.getText(), tfCourseName.getText(), tfCourseID.getText(), getChoice(semesterList),
                                getChoice(yearList));

                        statement1.executeUpdate(query);
                        long l = numOfRecordsinTable("enrollments");
                        String s = String.format("Enrollment completed\n     Enrollment ID: %d", l);
                        displayError("New enrollment", s);
                    }

                    tfNameStudent.setDisable(false);
                    tfCourseID.setDisable(false);
                    tfCourseName.setDisable(false);
                    tfFindStudent.clear();
                    tfNameStudent.clear();
                    tfCourseNumber.clear();
                    tfCourseID.clear();
                    yearList.setValue("");
                    tfCourseName.clear();
                    semesterList.setValue("");
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });

        Button findStudentButton = new Button("Find Student");
        findStudentButton.setOnAction(e ->{
            try {
                Statement statement = connection.createStatement();
                long sId = Long.parseLong(tfFindStudent.getText());
                if (sId < 0 || sId > numOfRecordsinTable("students")){
                    tfFindStudent.clear();
                    tfNameStudent.clear();
                    displayError("Invalid StudentID", "Record not found");
                }
                else {
                    tfNameStudent.setDisable(true);
                    tfNameStudent.setStyle("-fx-opacity: 0.75");
                    String student_query = String.format("SELECT firstName, lastName FROM university.students WHERE ID = '%s';",
                            tfFindStudent.getText());
                    ResultSet result = statement.executeQuery(student_query);
                    while(result.next()){
                        String first = result.getString(1);
                        String second = result.getString(2);
                        tfNameStudent.setText(first+" "+second);
                    }
                }

            } catch (SQLException x) {
                x.printStackTrace();
            }
        });

        Button findCourseButton = new Button("Find Course");
        findCourseButton.setOnAction(e ->{
            try {
                long cNum = Long.parseLong(tfCourseNumber.getText());
                if (cNum <= 0 || cNum > numOfRecordsinTable("courses")){
                    tfCourseID.clear();
                    tfCourseName.clear();
                    tfCourseNumber.clear();
                    displayError("Invalid", "Record not found");
                }
                else {
                    tfCourseID.setDisable(true);
                    tfCourseID.setStyle("-fx-opacity: 0.75");
                    tfCourseName.setDisable(true);
                    tfCourseName.setStyle("-fx-opacity: 0.75");
                    String course_query = String.format("SELECT courseID, courseName FROM university.courses WHERE " +
                                    "courseNum ='%s';", tfCourseNumber.getText());
                    Statement statement = connection.createStatement();
                    ResultSet result = statement.executeQuery(course_query);
                    while(result.next()){
                        tfCourseID.setText(result.getString(1));
                        tfCourseName.setText(result.getString(2));

                    }

                }

            } catch (SQLException ioException) {
                ioException.printStackTrace();
            }
        });

        BorderPane layout = new BorderPane();
        HBox l1 = new HBox(10, sceneLabel);
        l1.setAlignment(Pos.CENTER);
        HBox l2 = new HBox(14, tfFindStudent, findStudentButton);
        l2.setAlignment(Pos.CENTER);
        HBox l3 = new HBox(95, tfNameStudent, empty1);
        l3.setAlignment(Pos.CENTER);
        HBox l4 = new HBox(18, tfCourseNumber, findCourseButton);
        l4.setAlignment(Pos.CENTER);
        HBox l5 = new HBox(95, tfCourseID, empty2);
        l5.setAlignment(Pos.CENTER);
        HBox l6 = new HBox(95, tfCourseName, empty3);
        l6.setAlignment(Pos.CENTER);
        HBox l7 = new HBox(110, semesterLabel, yearLabel);
        l7.setAlignment(Pos.CENTER);
        HBox l8 = new HBox(77, semesterList, yearList);
        l8.setAlignment(Pos.CENTER);
        HBox l9 = new HBox(createEnrollmentButton);
        l9.setAlignment(Pos.CENTER);
        VBox vbox = new VBox(12);
        vbox.getChildren().addAll(l1,l2,l3,l4,l5,l6,l7,l8,l9);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(30));
        layout.setTop(menuBar);
        layout.setCenter(vbox);

        addEnrollmentScene = new Scene(layout, 450, 400);
        return addEnrollmentScene;
    }



    public ObservableList<Grades> getGrades(String studentID) throws SQLException {
        ObservableList<Grades> grades = FXCollections.observableArrayList();
        try {
            Statement statement = connection.createStatement();
            String query = String.format("SELECT * FROM university.enrollments WHERE studentID = '%s';", studentID);
            ResultSet result = statement.executeQuery(query);
            long numOfRecords = numOfRecordsinTable("enrollments");
            for (long i = 0; i < numOfRecords; i++) {
                while (result.next()){
                    grades.add(new Grades(result.getString(2), result.getString(4),
                            result.getString(5), result.getString(6), result.getString(7), result.getString(8)));
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return grades;
    }

    public Scene viewEnrollmentScene() {

        Label sceneLabel = new Label("View/Edit Enrollment");
        Label semesterLabel = new Label("Semester");
        Label yearLabel = new Label("Year  ");

        Label studentIdLabel = new Label("Student Name");
        Label enrollmentidLabel = new Label("EnrollmentID");
        Label courseNum = new Label("    Course Name ");
        Label empty1 = new Label("                                ");
        Label empty2 = new Label("                                   ");
        Label empty3 = new Label("                  ");
        Label empty4 = new Label("                        ");
        Label empty5 = new Label("                   ");

        TextField tfFindEnrollment = new TextField();
        tfFindEnrollment.setPromptText("EnrollmentID");
        TextField tfStudentID = new TextField();
        tfStudentID.setPromptText("Student Name");
        TextField tfCourseNumber = new TextField();
        tfCourseNumber.setPromptText("Course Name");

        ChoiceBox<String> yearList =  new ChoiceBox<>();
        yearList.getItems().addAll("2021","2020", "2019", "2018", "2017", "2016");
        yearList.setValue("");
        ChoiceBox<String> semesterList =  new ChoiceBox<>();
        semesterList.getItems().addAll("SPRING", "SUMMER", "FALL", "WINTER");
        semesterList.setValue("");
        ChoiceBox<String> gradeList =  new ChoiceBox<>();
        gradeList.getItems().addAll("A", "B", "C", "D", "F");
        gradeList.setValue("");

        Button updateEnrollmentButton = new Button("Update Enrollment");
        updateEnrollmentButton.setOnAction(e ->{
            try {
                long x = numOfRecordsinTable("enrollments");
                Statement st = connection.createStatement();
                if (tfFindEnrollment.getText().isBlank() || tfStudentID.getText().isBlank()||
                        tfCourseNumber.getText().isEmpty()|| getChoice(yearList).equals("") ||
                        getChoice(semesterList).equals("")){

                    tfFindEnrollment.clear();
                    tfStudentID.clear();
                    tfCourseNumber.clear();
                    yearList.setValue("");
                    semesterList.setValue("");
                    displayError("Invalid", "Missing Fields to Update Enrollment");
                }
                else {
                    String query = String.format("UPDATE university.enrollments SET semester = '%s', year= '%s' WHERE" +
                                    " ID = '%s';", getChoice(semesterList), getChoice(yearList), tfFindEnrollment.getText());
                    st.executeUpdate(query);


                    displayError("Updated", "Enrollment Successfully Updated!");
                    tfFindEnrollment.setDisable(false);
                    tfFindEnrollment.clear();
                    tfStudentID.clear();
                    tfCourseNumber.clear();
                    yearList.setValue("");
                    semesterList.setValue("");
                    gradeList.setValue("");
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });

        Button findEnrollment = new Button("Find Enrollment");
        findEnrollment.setOnAction(e ->{
            try {
                long eRecordsNum = numOfRecordsinTable("enrollments");
                Statement st = connection.createStatement();

                if (tfFindEnrollment.getText().isBlank() ||Integer.parseInt(tfFindEnrollment.getText())<= 0 ||
                        eRecordsNum < Long.parseLong(tfFindEnrollment.getText()) ){
                    tfFindEnrollment.clear();
                    tfStudentID.clear();
                    tfCourseNumber.clear();
                    yearList.setValue("");
                    semesterList.setValue("");
                    gradeList.setValue("");
                    displayError("Invalid", "Record not found");
                }

                else {
                    tfFindEnrollment.setStyle("-fx-opacity: 0.75");
                    tfFindEnrollment.setDisable(true);
                    tfStudentID.setStyle("-fx-opacity: 0.75");
                    tfStudentID.setDisable(true);
                    tfCourseNumber.setStyle("-fx-opacity: 0.75");
                    tfCourseNumber.setDisable(true);
                    String query = String.format("SELECT * FROM university.enrollments WHERE ID = '%s';",
                            tfFindEnrollment.getText());
                    ResultSet result = st.executeQuery(query);
                    while(result.next()){
                        tfStudentID.setText(result.getString(2));
                        tfCourseNumber.setText(result.getString(4));
                        yearList.setValue(result.getString(7));
                        semesterList.setValue(result.getString(6));

                    }

                }
            } catch (SQLException x) {
                x.printStackTrace();
            }
        });

        Button resetButton = new Button("Reset Fields");
        resetButton.setMinWidth(118);
        resetButton.setOnAction(e->{
            try {
                tfFindEnrollment.clear();
                tfFindEnrollment.setStyle("-fx-opacity: 1");
                tfFindEnrollment.setDisable(false);
                tfStudentID.clear();
                tfStudentID.setStyle("-fx-opacity: 1");
                tfStudentID.setDisable(false);
                tfCourseNumber.clear();
                tfCourseNumber.setStyle("-fx-opacity: 1.1");
                tfCourseNumber.setDisable(false);
                yearList.setValue("");
                semesterList.setValue("");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        BorderPane layout = new BorderPane();
        HBox l1 = new HBox(10, sceneLabel);
        l1.setAlignment(Pos.CENTER);
        HBox l2 = new HBox(14, enrollmentidLabel, tfFindEnrollment, findEnrollment);
        l2.setAlignment(Pos.CENTER);
        HBox l3 = new HBox(5, studentIdLabel, tfStudentID, empty1);
        l3.setAlignment(Pos.CENTER);
        HBox l4 = new HBox(10, courseNum, tfCourseNumber, empty2);
        l4.setAlignment(Pos.CENTER);
        HBox l5 = new HBox(10, yearLabel, yearList, empty3);
        l5.setAlignment(Pos.CENTER);
        HBox l6 = new HBox(10, semesterLabel, semesterList, empty4);
        l6.setAlignment(Pos.CENTER);
        HBox l9 = new HBox(updateEnrollmentButton);
        l9.setAlignment(Pos.CENTER);
        VBox vbox = new VBox(12);
        vbox.getChildren().addAll(l1,l2,l3,l4,l5,l6, resetButton,l9);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(30));
        layout.setTop(menuBar);
        layout.setCenter(vbox);
        yearList.setMinWidth(150);
        semesterList.setMinWidth(150);
        gradeList.setMinWidth(150);

        viewEnrollmentScene = new Scene(layout, 450, 400);


        return viewEnrollmentScene;
    }


    public Scene byStudentScene() throws IOException, SQLException {
        TextField tfStudentID =  new TextField();
        Label studentIDLabel = new Label("Student ID");
        tfStudentID.setPromptText("StudentID");
        TextField tfCourseNum =  new TextField();
        tfCourseNum.setPromptText("CourseID");
        tfCourseNum.setMaxWidth(90);
        TextField tfGrade =  new TextField();
        tfGrade.setPromptText("Grade");
        Button populateButton =  new Button("Populate");
        Button updateButton =  new Button("Update Grade");
        //TABLE VIEW
        TableView<Grades> table;
        //name column
        TableColumn<Grades, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(135);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        //courseName column
        TableColumn<Grades, String> courseNameColumn = new TableColumn<>("Course Name");
        courseNameColumn.setMinWidth(115);
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        //courseID column
        TableColumn<Grades, String> courseIdColumn = new TableColumn<>("CourseID");
        courseIdColumn.setMaxWidth(60);
        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("courseID"));
        //semester column
        TableColumn<Grades, String> semesterColumn = new TableColumn<>("Semester");
        semesterColumn.setMaxWidth(60);
        semesterColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));
        //year column
        TableColumn<Grades, String> yearColumn = new TableColumn<>("Year");
        yearColumn.setMaxWidth(40);
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        //grade column
        TableColumn<Grades, String> gradeColumn = new TableColumn<>("Grade");
        gradeColumn.setMaxWidth(38);
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));


        table = new TableView<>();
        table.setItems(getGrades(tfStudentID.getText()));
        table.getColumns().addAll(nameColumn,courseNameColumn,courseIdColumn,semesterColumn, yearColumn, gradeColumn);

        //end tableview=======================================================================================================
        ChoiceBox<String> gradeList =  new ChoiceBox<>();
        gradeList.getItems().addAll("A", "B", "C", "D", "F");
        gradeList.setValue("*");
        gradeList.setMaxWidth(50);

        HBox options = new HBox(10, studentIDLabel, tfStudentID, populateButton);
        VBox vBox = new VBox(10, options, table);
        HBox moreOptions = new HBox(10, gradeList,updateButton);
        moreOptions.setAlignment(Pos.CENTER);
        moreOptions.setPadding(new Insets(10));
        options.setAlignment(Pos.TOP_CENTER);
        options.setPadding(new Insets(10));
        BorderPane layout = new BorderPane();
        layout.setTop(menuBar);
        layout.setCenter(vBox);
        layout.setBottom(moreOptions);



        populateButton.setOnAction(event->{

            try {
                table.setItems(getGrades(tfStudentID.getText()));
                table.refresh();

            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
        updateButton.setOnAction(event->{
            try {
                if (tfStudentID.getText().isBlank() || getChoice(gradeList).equals("*")) {
                    displayError("Unable to update", "Missing information/selection to update records");
                } else {
                    Statement statement = connection.createStatement();
                    Grades x = table.getSelectionModel().getSelectedItem();
                    String query = String.format("UPDATE university.enrollments SET grades = '%s' WHERE studentID = %d and courseID = '%s' and semester = '%s' and  year = '%s';",
                            getChoice(gradeList), Integer.parseInt(tfStudentID.getText()), x.getCourseID(), x.getSemester(), x.getYear());
                    statement.executeUpdate(query);
                    table.setItems((getGrades(tfStudentID.getText())));
                    table.refresh();

                    displayError("Updated", "Record successfully updated");

                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        byStudentScene = new Scene(layout, 450, 400);
        return byStudentScene;
    }


    public Scene reportScene() throws IOException{
        Label courseIDLabel = new Label("CourseID       ");
        Label semesterLabel = new Label("Semester    ");
        Label yearLabel = new Label("Year");
        Label emptyLabel = new Label("       ");

        ChoiceBox<String> yearList =  new ChoiceBox<>();
        yearList.getItems().addAll("2021","2020", "2019", "2018", "2017", "2016");

        ChoiceBox<String> semesterList =  new ChoiceBox<>();
        semesterList.getItems().addAll("SPRING", "SUMMER", "FALL", "WINTER");

        TextField tfCourseID =  new TextField();
        tfCourseID.setPromptText("CourseID");
        tfCourseID.setMaxWidth(90);
        Button generateReportButton =  new Button("Generate Report");
        generateReportButton.setMaxWidth(150);
        HBox options = new HBox(10,tfCourseID, semesterList, yearList, generateReportButton, emptyLabel);
        HBox labels = new HBox(30, courseIDLabel, semesterLabel, yearLabel);
        labels.setAlignment(Pos.CENTER_LEFT);
        labels.setPadding(new Insets(30));
        VBox vBox = new VBox(10, labels, options);
        options.setAlignment(Pos.CENTER);

        TextArea textArea =  new TextArea();
        textArea.setPadding(new Insets(10));
        textArea.setMinHeight(200);
        textArea.setMinWidth(200);
        textArea.setFont(Font.font(14));
        textArea.setWrapText(true);
        textArea.setEditable(false);

        BorderPane layout = new BorderPane();
        layout.setTop(menuBar);
        layout.setCenter(vBox);
        layout.setBottom(textArea);



        generateReportButton.setOnAction(event->{
            try{
                textArea.clear();
                Statement statement = connection.createStatement();
                String query = String.format("SELECT * FROM university.enrollments WHERE courseID ='%s' and semester = '%s' and year = '%s';",
                        tfCourseID.getText().toUpperCase(Locale.ROOT), getChoice(semesterList), getChoice(yearList));
                StringBuilder str = new StringBuilder();
                String otherquery = String.format("select courseName from university.enrollments where courseID = '%s';", tfCourseID.getText());
                ResultSet result = statement.executeQuery(otherquery);
                String courseNameStr;
                String studentName;
                String studentGrade;



                if (result.next()){

                    courseNameStr = result.getString(1);
                    String reportTittle = String.format("%s | %s | %s | %s\n", tfCourseID.getText().toUpperCase(Locale.ROOT),
                            courseNameStr, getChoice(semesterList), getChoice(yearList));
                    char [] titleInChar = reportTittle.toCharArray();
                    str.append(reportTittle);
                    for (char x : titleInChar){
                        str.append("*");

                    }

                }

                ResultSet rs = statement.executeQuery(query);
                ArrayList<String> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(rs.getString(2));
                    list.add(rs.getString(8));
                }
                for(int x=0,y=1; y < list.size(); y=y+2,x=x+2){
                    studentName = list.get(x);
                    studentGrade = list.get(y);
                    String studentReport = String.format("\n%-20s %15s", studentName, studentGrade);
                    str.append(studentReport);
                }




                textArea.appendText(str.toString());

            }catch (SQLException e){
                e.printStackTrace();
            }

        });

        reportScene = new Scene(layout, 450, 400);
        return reportScene;
    }
    public Scene addInstructorScene() throws IOException, SQLException {

        //create a labels to display prompt
        Label sceneLabel = new Label("Instructor Information");
        Label instNum= new Label("Instructor ID#");
        Label instructorName = new Label("Instructor Name");
        Label departmentName = new Label("Department");

        GenericLinkedList<Department> depLL = new GenericLinkedList<>();
        ArrayList<String> depL = new ArrayList<>();

        Statement st = connection.createStatement();
        Long z = numOfRecordsinTable("departments");
        int t = z.intValue();
        for (int j = 1; j <= t; j++ ){
            String query = String.format("SELECT depName FROM departments WHERE ID = %d;", j);
            ResultSet result = st.executeQuery(query);
            Department x;
            if(result.next()) {
                x = new Department(result.getString(1));
                depLL.add(x);
            }
        }
        for(int i=0; i < depLL.size(); i++){
            String x;
            x = depLL.get(i).toString();
            depL.add(x);
        }

        ChoiceBox<String> departmentsList = new ChoiceBox<>();
        departmentsList.setMinWidth(150);
        for (int s = 0; s < depL.size();s++){

            departmentsList.getItems().add(depL.get(s));
        }

        TextField tf1 = new TextField();
        tf1.setDisable(true);
        String numOfRecords = String.valueOf(1+numOfRecordsinTable("instructors"));
        tf1.setText(numOfRecords);
        TextField tf4 = new TextField();
        TextField tf5 = new TextField();

        HBox hbox0 = new HBox(20, sceneLabel);
        hbox0.setAlignment(Pos.CENTER);
        HBox hbox1 = new HBox(39, instNum, tf1);
        hbox1.setAlignment(Pos.CENTER);
        HBox hbox4 = new HBox(28, instructorName, tf4);
        hbox4.setAlignment(Pos.CENTER);
        HBox hbox5 = new HBox(50, departmentName, departmentsList);
        hbox5.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(12);
        vbox.getChildren().addAll(hbox0, hbox1, hbox4, hbox5);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        BorderPane layout = new BorderPane();
        layout.setTop(menuBar);
        layout.setCenter(vbox);

        Button addInstructor = new Button("Add Instructor");
        addInstructor.setOnAction(e-> {
            try {
                if (tf1.getText().isBlank() ||tf4.getText().isBlank() || getChoice(departmentsList).isEmpty()) {
                    displayError("Invalid", "Missing Information to Add Instructor");
                } else {
                    //now send copy of data to the university database
                    Statement statement = connection.createStatement();
                    String queryDepID = String.format("SELECT * FROM departments WHERE depName = '%s';", getChoice(departmentsList));
                    ResultSet r = statement.executeQuery(queryDepID);
                    int holder;
                    String sql = "INSERT INTO university.instructors(instructorName,depName, depID)\nVALUES" +
                            "('%s', '%s', %d)";
                    if(r.next()) {
                        holder = r.getInt(1);
                        sql = String.format(sql,tf4.getText().toUpperCase(Locale.ROOT), getChoice(departmentsList), holder);
                    }

                    statement.executeUpdate(sql);
                    ResultSet new_result = st.executeQuery("SELECT COUNT(*) FROM university.instructors");
                    int count = 0;
                    while (new_result.next())
                        count += Integer.parseInt(new_result.getString(1));
                    tf1.setText(String.valueOf(count+1));
                    displayError("Success!", "Instructor Profile created!");
                    tf4.clear();
                    departmentsList.setValue("");
                }
            } catch (SQLException ioException) {
                ioException.printStackTrace();
            }
        });

        VBox forButton = new VBox(addInstructor);
        forButton.setPadding(new Insets(30));
        forButton.setAlignment(Pos.CENTER);
        layout.setBottom(forButton);

        addInstructorScene = new Scene(layout, 450, 400);
        return addInstructorScene;
    }

    public Scene addDepartmentScene() throws IOException, SQLException {

        //create a labels to display prompt
        Label sceneLabel = new Label("Department Information");
        Label depNum = new Label("Department ID#");
        Label departmentName = new Label("Department Name");

        ChoiceBox<String> departmentsList = new ChoiceBox<>();
        departmentsList.setMinWidth(150);
        departmentsList.getItems().addAll("Arts", "Business", "Computer Science","Humanities", "Mathematics", "English");


        TextField tf1 = new TextField();
        tf1.setDisable(true);

        String numOfRecords = String.valueOf(numOfRecordsinTable("departments")+1);
        tf1.setText(numOfRecords);

        tf1.setMinWidth(150);
        TextField tf4 = new TextField();

        HBox hbox0 = new HBox(20, sceneLabel);
        hbox0.setAlignment(Pos.CENTER);
        HBox hbox1 = new HBox(39, depNum, tf1);
        hbox1.setAlignment(Pos.CENTER);
        HBox hbox5 = new HBox(29, departmentName, tf4);
        hbox5.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(12);
        vbox.getChildren().addAll(hbox0, hbox1, hbox5);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        BorderPane layout = new BorderPane();
        layout.setTop(menuBar);
        layout.setCenter(vbox);

        Button addDepartment = new Button("Add Department");
        addDepartment.setOnAction(e-> {
            try {
                if (tf1.getText().isEmpty() ||tf4.getText().isBlank()) {
                    displayError("Invalid", "Missing Information to Add Department");
                } else {
                    Statement st = connection.createStatement();
                    if (uniqueDepartmentName(tf4.getText().toUpperCase(Locale.ROOT))) {
                        String query = String.format("INSERT INTO university.departments(depName) VALUES('%s')",
                                tf4.getText().toUpperCase(Locale.ROOT));
                        st.executeUpdate(query);
                        displayError("Success!", "Department Profile created!");
                    } else {
                        displayError("Duplicate", "Department already exists");
                    }
                    String newRecordsNum = String.valueOf(numOfRecordsinTable("departments")+1);


                    tf1.setText(newRecordsNum);
                    tf4.clear();
                }
            } catch (SQLException ioException) {
                ioException.printStackTrace();
            }
        });

        VBox forButton = new VBox(addDepartment);
        forButton.setPadding(new Insets(30));
        forButton.setAlignment(Pos.TOP_CENTER);
        layout.setBottom(forButton);
        addDepartmentScene = new Scene(layout, 450, 400);

        return addDepartmentScene;
    }
    public Scene editDepartmentScene() throws IOException {

        //create a labels to display prompt
        Label sceneLabel = new Label("Edit Department Information");
        Label depNum = new Label("  Department ID#   ");
        Label departmentName = new Label("Department Name");

        TextField findDep= new TextField();
        findDep.setDisable(false);
        TextField updateDep = new TextField();


        Button updateButton = new Button("Update");
        updateButton.setMinWidth(105);
        updateButton.setOnAction(e ->{
            try {
                if (updateDep.getText().isEmpty() || findDep.getText().isBlank()){
                    findDep.clear();
                    findDep.setDisable(false);


                    displayError("Invalid", "Missing Fields to Update Department");

                }
                else {
                    Statement st = connection.createStatement();

                    if(uniqueDepartmentName(updateDep.getText().toUpperCase(Locale.ROOT))){
                        String query = String.format("UPDATE university.departments SET  depName = '%s' WHERE ID = %d",
                                updateDep.getText().toUpperCase(Locale.ROOT), Integer.parseInt(findDep.getText()));
                        st.executeUpdate(query);
                        String queryIns = String.format("UPDATE university.instructors SET  depName = '%s' WHERE depID = %d",
                                updateDep.getText().toUpperCase(Locale.ROOT), Integer.parseInt(findDep.getText()));
                        st.executeUpdate(queryIns);
                        String queryCourses = String.format("UPDATE university.courses SET depName = '%s', depID = %d " +
                                        "WHERE depID = %d;", updateDep.getText().toUpperCase(Locale.ROOT),
                                Integer.parseInt(findDep.getText()), Integer.parseInt(findDep.getText()));
                        st.executeUpdate(queryCourses);
                        displayError("Updated", "Department successfully updated");
                    } else {
                        displayError("Duplicate", "Department already exists");
                    }


                    findDep.clear();
                    updateDep.clear();
                    findDep.setDisable(false);

                }
            } catch (SQLException ioException) {
                ioException.printStackTrace();
            }

        });
        Button resetButton = new Button("Reset");
        resetButton.setMinWidth(110);
        resetButton.setOnAction(e -> {
            findDep.setDisable(false);
            findDep.clear();
            updateDep.clear();
        });
        Button findDepartment= new Button("Find Department");
        findDepartment.setMinWidth(100);
        findDepartment.setOnAction(e ->{
            try {
                Statement statement = connection.createStatement();
                long eId = Long.parseLong(findDep.getText());

                if (eId <= 0 ||eId > numOfRecordsinTable("departments") || findDep.getText().isEmpty()){
                    findDep.clear();
                    displayError("Invalid", "Record not found");
                }
                else {
                    findDep.setDisable(true);
                    int eid = (int) eId;
                    String query = String.format("SELECT depName FROM departments WHERE ID = %d;",eid);
                    ResultSet result = statement.executeQuery(query);
                    if(result.next()){
                        updateDep.setText(result.getString(1));
                    }


                    //Department depO = file.readFromFile(eId);
                    //updateDep.setText(depO.getDepName());
                }
            } catch (SQLException x) {
                x.printStackTrace();
            }
        });

        HBox hbox0 = new HBox(20, sceneLabel);
        hbox0.setAlignment(Pos.CENTER);
        HBox hbox1 = new HBox(10, depNum, findDep, findDepartment);
        hbox1.setAlignment(Pos.CENTER);
        HBox hbox5 = new HBox(10, departmentName, updateDep, updateButton);
        hbox5.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(15);
        vbox.getChildren().addAll(hbox0, hbox1, hbox5);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        BorderPane layout = new BorderPane();
        layout.setTop(menuBar);
        layout.setCenter(vbox);


        VBox forButton = new VBox(resetButton);
        forButton.setPadding(new Insets(30));
        forButton.setAlignment(Pos.TOP_CENTER);
        layout.setBottom(forButton);

        editDepartmentScene = new Scene(layout, 450, 400);

        return editDepartmentScene;
    }
    public boolean uniqueDepartmentName(String depNametoCheck) throws SQLException {
        Statement st = connection.createStatement();
        String query = String.format("SELECT depName FROM departments WHERE '%s'= depName;", depNametoCheck.toUpperCase());
        ResultSet result = st.executeQuery(query);
        while (result.next()){
            return false;
        }
        return true;
    }
    public boolean uniqueCourseID (String courseIDtocheck) throws SQLException {
        Statement st = connection.createStatement();
        String query = String.format("Select courseID FROM courses WHERE '%s' = courseID;", courseIDtocheck.toUpperCase(Locale.ROOT));
        ResultSet result = st.executeQuery(query);
        while (result.next())
            return false;

        return true;
    }

    public Scene editInstructorScene() throws IOException, SQLException {
        //create a labels to display prompt
        Label sceneLabel = new Label("Edit Instructor Information");
        Label instNumber = new Label("Instructor ID#      ");
        Label instructorName = new Label("Instructor Name  ");
        Label departmentName = new Label("          Department Name");

        GenericLinkedList<Department> depLL = new GenericLinkedList<>();
        ArrayList<String> depL= new ArrayList<>();

        Statement st = connection.createStatement();
        Long z = numOfRecordsinTable("departments");
        int t = z.intValue();
        for (int j = 1; j <= t; j++ ){
            String query = String.format("SELECT depName FROM departments WHERE ID = %d;", j);
            ResultSet result = st.executeQuery(query);
            Department x;
            if(result.next()) {
                x = new Department(result.getString(1));
                depLL.add(x);
            }
        }
        for(int i=0; i < depLL.size(); i++){
            String x;
            x = depLL.get(i).toString();
            depL.add(x);

        }

        ChoiceBox<String> departmentsList = new ChoiceBox<>();
        departmentsList.setMinWidth(150);

        for (int s = 0; s < depL.size();s++){

            departmentsList.getItems().add(depL.get(s));
        }

        TextField instNum= new TextField();
        instNum.setDisable(false);
        TextField instName = new TextField();

        Button findDepartment= new Button("Find Instructor");
        findDepartment.setMinWidth(100);
        findDepartment.setOnAction(e ->{
            try {
                long eId = numOfRecordsinTable("instructors");
                Statement statement = connection.createStatement();
                if (instNum.getText().isEmpty() ||  Integer.parseInt(instNum.getText()) <=0 ||
                        eId> numOfRecordsinTable("instructors")){
                    instNum.clear();
                    instNum.setDisable(false);
                    displayError("Invalid", "Record not found");
                }
                else {

                    String query1 = String.format("SELECT depName FROM university.instructors WHERE ID = %d",
                            Integer.parseInt(instNum.getText()));
                    String query2 = String.format("SELECT instructorName FROM university.instructors WHERE ID = %d",
                            Integer.parseInt(instNum.getText()));
                    ResultSet result1= st.executeQuery(query1);
                    ResultSet result2 = statement.executeQuery(query2);
                    while (result1.next()) {
                        departmentsList.setValue(result1.getString(1));
                    }
                    while (result2.next()) {
                        instName.setText(result2.getString(1));
                    }
                    instNum.setDisable(true);


                }
            } catch (SQLException x) {
                x.printStackTrace();
            }
        });
        Button updateButton = new Button("Update");
        updateButton.setMinWidth(100);
        updateButton.setOnAction(e ->{
            try {
                long l = numOfRecordsinTable("instructors");
                connection.createStatement();
                if (instName.getText().isBlank() || getChoice(departmentsList).isEmpty()){
                    instNum.clear();
                    instName.clear();
                    departmentsList.setValue("");
                    instNum.setDisable(false);
                    displayError("Invalid", "Missing Fields to Update Instructor");

                }
                else {


                    String query = String.format("SELECT ID FROM university.departments WHERE depName = '%s';",
                            getChoice(departmentsList));
                    ResultSet result = resultQuery(query);

                    if (result.next()) {
                        String otherquery = String.format("UPDATE instructors SET instructorName = '%s', depName = '%s', depID = %d " +
                                "WHERE ID = %d", instName.getText().toUpperCase(Locale.ROOT),
                                departmentsList.getValue().toUpperCase(Locale.ROOT), result.getInt(1),
                                Integer.parseInt(instNum.getText()));
                        st.executeUpdate(otherquery);
                    }
                    departmentsList.setValue("");
                    displayError("Updated", "Instructor Successfully Updated!");
                    instNum.clear();
                    instName.clear();
                    instNum.setDisable(false);

                }
            } catch (SQLException ioException) {
                ioException.printStackTrace();
            }

        });
        Button resetButton = new Button("Reset");
        resetButton.setMinWidth(110);
        resetButton.setOnAction(e -> {
            instNum.clear();
            instName.clear();
            instNum.setDisable(false);
            departmentsList.setValue("");

        });

        HBox hbox0 = new HBox(20, sceneLabel);
        hbox0.setAlignment(Pos.CENTER);
        HBox hbox1 = new HBox(10, instNumber, instNum , findDepartment);
        hbox1.setAlignment(Pos.CENTER);
        HBox hbox5 = new HBox(10, instructorName, instName, updateButton);
        hbox5.setAlignment(Pos.CENTER);
        HBox hbox6 = new HBox(4, departmentName, departmentsList);
        hbox5.setAlignment(Pos.CENTER);


        VBox vbox = new VBox(15);
        vbox.getChildren().addAll(hbox0, hbox1, hbox5, hbox6);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        BorderPane layout = new BorderPane();
        layout.setTop(menuBar);
        layout.setCenter(vbox);

        VBox forButton = new VBox(resetButton);
        forButton.setPadding(new Insets(30));
        forButton.setAlignment(Pos.TOP_CENTER);
        layout.setBottom(forButton);

        editInstructorScene = new Scene(layout, 450, 400);

        return editInstructorScene;

    }
    public ResultSet resultQuery(String query) throws SQLException {
        Statement st = connection.createStatement();
        ResultSet result = st.executeQuery(query);
        return result;

    }

}




