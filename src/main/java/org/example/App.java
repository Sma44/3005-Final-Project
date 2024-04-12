package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.util.HashSet;

public class App 
{
    static String url = "jdbc:postgresql://localhost:5432/Student";
    static String user = "postgres";
    static String password = "admin";
    static Connection connection;
    static int memberID;
    static int trainerID;
    static int adminID;

    public static void main(String[] args) {
        // initializes scanner to read user input
        Scanner in = new Scanner(System.in);

        // checks for a valid connection to the DB before preceding
        try {
            // Tests connection validity
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            if (connection != null){
                System.out.println("Connection Successful");
            }else{
                System.out.println("Connection Unsuccessful");
                System.exit(1);
            }
            connection.close();
            // calls function display menu
            showMenu(in);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static void showMenu(Scanner input){
        int ret;
        do {
            System.out.print("\n\nSelect your view:\n");
            System.out.println("(1) Member");
            System.out.println("(2) Trainer");
            System.out.println("(3) Admin");
            System.out.println("(0) EXIT");
            System.out.println("Enter Your Selection: ");
            ret = input.nextInt();
            input.nextLine();
            if (ret == 0) break;

            while (ret < 0 || ret > 3) {
                System.out.println("Selection out of range. Try again: ");
                input.nextLine();
                ret = input.nextInt();
            }

            if (ret == 1) {
                memberCredScreen(input);
            } else if (ret == 2) {
                trainerCredScreen(input);
            } else{
                adminCredScreen(input);
            }
        } while (ret != 0);
    }
    public static void memberCredScreen(Scanner input){
        int ret;
        do {
            System.out.print("\n\nEnter Selection:\n");
            System.out.println("(1) Register");
            System.out.println("(2) Log In");
            System.out.println("(0) EXIT");
            System.out.println("Enter Your Selection: ");
            ret = input.nextInt();
            input.nextLine();
            if (ret == 0) break;

            while (ret < 0 || ret > 2) {
                System.out.println("Selection out of range. Try again: ");
                input.nextLine();
                ret = input.nextInt();
            }

            if (ret == 1) {
                registerMember(input);
            } else {
                memberView(input);
            }
        }while (ret != 0);
    }
    public static void registerMember(Scanner input){
        String fn,ln,email,sex;
        int height,weight,age;
        System.out.println("Enter first name:");
        fn = input.nextLine();
        System.out.println("Enter last name:");
        ln = input.nextLine();
        System.out.println("Enter email:");
        email = input.nextLine();
        System.out.println("Enter sex:");
        sex = input.nextLine();
        System.out.println("Enter height in CM:");
        height = input.nextInt();
        input.nextLine();
        System.out.println("Enter weight in KG:");
        weight = input.nextInt();
        input.nextLine();
        System.out.println("Enter age:");
        age = input.nextInt();
        input.nextLine();
        try{
            // creates the connection to DB
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            // creates query statement and send request to DB
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO Members(FName, LName, Email, GoalWeightKGs, GoalDeadline, HeightCM, WeightKGs, Age, Sex) VALUES ('" +
                    fn + "', '" + ln + "', '" + email + "', 100, '2000-01-01', " + height + ", " + weight + ", " + age + ", '" + sex + "');");
            // closes connection to DB
            connection.close();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static void memberView(Scanner input){
        String email = " ";
        HashSet<String> memberEmails = new HashSet<>();

        try{
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT Email FROM Members;");
            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next()){
                memberEmails.add(resultSet.getString("Email"));
            }
            connection.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        // TODO: user login validation
        System.out.print("Enter email or type 'exit' to leave menu:");
        email = input.nextLine();
        System.out.println();

        while (!memberEmails.contains(email) || !(email.equals("exit"))){
            if (email.equals("exit")){
                return;
            }
            System.out.println("invalid email:");
            System.out.print("Enter email:");
            email = input.nextLine();
            System.out.println();
        }

        try{
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT MemberID FROM Members WHERE Email='" + email + "';");
            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next()){
                memberID = resultSet.getInt("MemberID");
            }
            connection.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        int ret = -1;
        while (ret != 0) {
            System.out.print("\n\nSelect option:\n");
            System.out.println("(1) Profile Management");
            System.out.println("(2) View Dashboard");
            System.out.println("(3) Manage Routines");
            System.out.println("(4) Manage Classes");
            System.out.println("(0) EXIT");
            System.out.println("Enter Your Selection: ");
            ret = input.nextInt();
            input.nextLine();
            if (ret == 0) break;

            while (ret < 0 || ret > 4) {
                System.out.println("Selection out of range. Try again: ");
                input.nextLine();
                ret = input.nextInt();
            }

            if (ret == 1) {
                // TODO: enter profile management
            } else if (ret == 2) {
                // TODO: enter dashboard
            } else if (ret == 3){
                // TODO: enter routines
            } else {
                // TODO: enter classes
            }
        }
    }
    public static void trainerCredScreen(Scanner input){
        int ret = -1;
        while (ret != 0) {
            System.out.print("\n\nEnter Selection:\n");
            System.out.println("(1) Register");
            System.out.println("(2) Log In");
            System.out.println("(0) EXIT");
            System.out.println("Enter Your Selection: ");
            ret = input.nextInt();
            input.nextLine();
            if (ret == 0) break;

            while (ret < 0 || ret > 2) {
                System.out.println("Selection out of range. Try again: ");
                input.nextLine();
                ret = input.nextInt();
            }

            if (ret == 1) {
                registerTrainer(input);
            } else {
                trainerView(input);
            }
        }
    }
    public static void registerTrainer(Scanner input){
        String fn,ln,email,sex;
        int height,weight,age;
        System.out.println("Enter first name:");
        fn = input.nextLine();
        System.out.println("Enter last name:");
        ln = input.nextLine();
        System.out.println("Enter email:");
        email = input.nextLine();
        try{
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO Trainers(FName, LName, Email) VALUES ('" +
                    fn + "', '" + ln + "', '" + email + "')");
            System.out.println("Registration Successful");
            connection.close();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void trainerView(Scanner input){
        String email = " ";
        HashSet<String> trainerEmails = new HashSet<>();

        try{
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT Email FROM Members;");
            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next()){
                trainerEmails.add(resultSet.getString("Email"));
            }
            connection.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        // TODO: user login validation
        System.out.print("Enter email or type 'exit' to leave menu:");
        email = input.nextLine();
        System.out.println();

        while (!trainerEmails.contains(email) || !(email.equals("exit"))){
            if (email.equals("exit")){
                return;
            }
            System.out.println("invalid email:");
            System.out.print("Enter email:");
            email = input.nextLine();
            System.out.println();
        }

        try{
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT TrainerID FROM Trainers WHERE Email='" + email + "';");
            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next()){
                trainerID = resultSet.getInt("TrainerID");
            }
            connection.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        int ret = -1;
        while (ret != 0) {
            System.out.print("\n\nSelect option:\n");
            System.out.println("(1) Manage Schedule");
            System.out.println("(2) Member Profile Viewing");
            System.out.println("(2) View All Members");  // TODO: extra function
            System.out.println("(0) EXIT");
            System.out.println("Enter Your Selection: ");
            ret = input.nextInt();
            input.nextLine();
            if (ret == 0) break;

            while (ret < 0 || ret > 2) {
                System.out.println("Selection out of range. Try again: ");
                input.nextLine();
                ret = input.nextInt();
            }

            if (ret == 1) {
                // TODO: enter schedule management
            } else if (ret == 2){
                viewMemberProfile(input);
            } else {
                viewAllProfiles();
            }
        }
    }

    // TODO: Extra functionality
    public static void viewAllProfiles(){
        try{
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT * FROM Members WHERE MemberID IN (SELECT MemberID FROM Takes WHERE TrainerID="+trainerID+");");
            ResultSet resultSet = statement.getResultSet();
            System.out.println("Displaying all members taking your courses:");
            while(resultSet.next()){
                System.out.print(resultSet.getInt("MemberID") + "\t");
                System.out.print(resultSet.getString("FName") + "\t");
                System.out.print(resultSet.getString("LName") + "\t");
                System.out.print(resultSet.getString("Email") + "\t");
                System.out.print(resultSet.getInt("GoalWeightKGs") + "\t");
                System.out.print(resultSet.getString("GoalDeadline") + "\t");
                System.out.print(resultSet.getInt("HeightCM") + "\t");
                System.out.print(resultSet.getInt("WeightKGs") + "\t");
                System.out.print(resultSet.getInt("Age") + "\t");
                System.out.println(resultSet.getString("Sex"));
            }
            connection.close();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static void viewMemberProfile(Scanner input){
        String first = " ";
        String last = " ";
        System.out.print("Enter First Name:");
        first = input.nextLine();
        System.out.println();
        System.out.print("Enter Last Name:");
        last = input.nextLine();
        System.out.println();

        try{
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT * FROM Members WHERE FName='" + first + "' AND LName='" + last + "';");
            ResultSet resultSet = statement.getResultSet();

            while(resultSet.next()){
                System.out.print(resultSet.getInt("MemberID") + "\t");
                System.out.print(resultSet.getString("FName") + "\t");
                System.out.print(resultSet.getString("LName") + "\t");
                System.out.print(resultSet.getString("Email") + "\t");
                System.out.print(resultSet.getInt("GoalWeightKGs") + "\t");
                System.out.print(resultSet.getString("GoalDeadline") + "\t");
                System.out.print(resultSet.getInt("HeightCM") + "\t");
                System.out.print(resultSet.getInt("WeightKGs") + "\t");
                System.out.print(resultSet.getInt("Age")+ "\t");
                System.out.println(resultSet.getString("Sex"));
            }
            connection.close();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    //TODO: admin screen
    public static void adminCredScreen(Scanner input){

    }

}


//    public static void getAllStudents(){
//        try{
//            // creates the connection to DB
//            Class.forName("org.postgresql.Driver");
//            connection = DriverManager.getConnection(url, user, password);
//            // creates query statement and send request to DB
//            Statement statement = connection.createStatement();
//            statement.executeQuery("SELECT * FROM students");
//            ResultSet resultSet = statement.getResultSet();
//            // loop through results to print all students to terminal
//            while(resultSet.next()){
//                System.out.print(resultSet.getInt("student_id") + "\t");
//                System.out.print(resultSet.getString("first_name") + "\t" );
//                System.out.print(resultSet.getString("last_name") + "\t");
//                System.out.print(resultSet.getString("email") + "\t");
//                System.out.println(resultSet.getString("enrollment_date"));
//            }
//            // closes connection to DB
//            connection.close();
//
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }
//    public static void addStudent(String first, String last, String email, String date){
//        try{
//            // creates the connection to DB
//            Class.forName("org.postgresql.Driver");
//            connection = DriverManager.getConnection(url, user, password);
//            // creates query statement and send request to DB
//            Statement statement = connection.createStatement();
//            statement.executeUpdate("INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES ('" +
//                    first + "', '" + last + "', '" + email + "', '" + date +"')" );
//            // closes connection to DB
//            connection.close();
//
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }
//    public static void deleteStudent(String id){
//        try{
//            // creates the connection to DB
//            Class.forName("org.postgresql.Driver");
//            connection = DriverManager.getConnection(url, user, password);
//            // creates query statement and send request to DB
//            Statement statement = connection.createStatement();
//            statement.executeUpdate("DELETE FROM students WHERE student_id = " + id );
//            // closes connection to DB
//            connection.close();
//
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }
//    public static void updateStudentEmail(String id, String email){
//        try{
//            // creates the connection to DB
//            Class.forName("org.postgresql.Driver");
//            connection = DriverManager.getConnection(url, user, password);
//            Statement statement = connection.createStatement();
//            statement.executeUpdate("UPDATE students SET email = '" + email + "' WHERE student_id = " + id );
//            // closes connection to DB
//            connection.close();
//
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }
