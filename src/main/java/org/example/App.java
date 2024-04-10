package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class App 
{
    // various DB connection utilities global for easy access
    static String url = "jdbc:postgresql://localhost:5432/Student";
    static String user = "postgres";
    static String password = "Admin";
    static Connection connection;

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

            // do while loop prompts user for program function calls
            boolean flag = true;
            do{
                int userChoice;
                String fn,ln,email,date,id;
                // calls function display menu and to return user choice
                userChoice = showMenu(in);

                switch (userChoice){
                    case 0:
                        // exit case
                        flag = false;
                        break;
                    case 1:
                        // calls function to print all student records to the terminal
                        getAllStudents();
                        break;
                    case 2:
                        // prompts user to enter the nessessary values to add a student
                        System.out.println("Enter first name, last name, email, and date:");
                        fn = in.nextLine();
                        ln = in.nextLine();
                        email = in.nextLine();
                        date = in.nextLine();
                        // calls function to add student
                        addStudent(fn,ln,email,date);
                        break;
                    case 3:
                        // prompts user to enter the nessessary values to update email of student
                        System.out.println("Enter student id, and email:");
                        id = in.nextLine();
                        email = in.nextLine();
                        // calls function to update student email
                        updateStudentEmail(id,email);
                        break;
                    case 4:
                        // prompts user to enter the nessessary values to delete a student
                        System.out.println("Enter student id:");
                        id = in.nextLine();
                        // calls a function to delete student
                        deleteStudent(id);
                        break;
                    default:
                        System.out.println("Error, userChoice undefined");
                        break;
                }


            }while (flag);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    // function prints menu to terminal
    public static int showMenu(Scanner input){
        int ret;
        System.out.print("\n\n");
        System.out.println("(1) Get All Students");
        System.out.println("(2) Add Student");
        System.out.println("(3) Update Student Email");
        System.out.println("(4) Delete Student");
        System.out.println("(0) EXIT");
        System.out.println("Enter Your Selection: ");
        ret = input.nextInt();
        input.nextLine();

        // checks for valid input and for exit clause
        if (ret == 0) return ret;

        while (ret < 1 || ret > 4){
            System.out.println("Selection out of range. Try again: ");
            input.nextLine();
            ret = input.nextInt();
        }
        // returns user choice
        return ret;
    }

    // function sends a query to the DB to get all students
    public static void getAllStudents(){
        try{
            // creates the connection to DB
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            // creates query statement and send request to DB
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT * FROM students");
            ResultSet resultSet = statement.getResultSet();
            // loop through results to print all students to terminal
            while(resultSet.next()){
                System.out.print(resultSet.getInt("student_id") + "\t");
                System.out.print(resultSet.getString("first_name") + "\t" );
                System.out.print(resultSet.getString("last_name") + "\t");
                System.out.print(resultSet.getString("email") + "\t");
                System.out.println(resultSet.getString("enrollment_date"));
            }
            // closes connection to DB
            connection.close();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    // function adds a new student into the DB with the given values
    public static void addStudent(String first, String last, String email, String date){
        try{
            // creates the connection to DB
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            // creates query statement and send request to DB
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES ('" +
                    first + "', '" + last + "', '" + email + "', '" + date +"')" );
            // closes connection to DB
            connection.close();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    // function deletes student with matching ID
    public static void deleteStudent(String id){
        try{
            // creates the connection to DB
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            // creates query statement and send request to DB
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM students WHERE student_id = " + id );
            // closes connection to DB
            connection.close();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    // function updates student email with the matching ID
    public static void updateStudentEmail(String id, String email){
        try{
            // creates the connection to DB
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE students SET email = '" + email + "' WHERE student_id = " + id );
            // closes connection to DB
            connection.close();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
