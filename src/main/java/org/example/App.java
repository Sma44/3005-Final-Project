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
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        // calls function display menu
        showMenu(in);

        //close the scanner
        in.close();
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
            } else if (ret == 3){
                adminCredScreen(input);
            }
        } while (ret != 0);
    }

    // _________________MEMBER FUNCTIONS_____________________________
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
            } else if (ret == 2){
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
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            // adds entry to members table
            statement.executeUpdate("INSERT INTO Members(FName, LName, Email, GoalWeightKGs, GoalDeadline, HeightCM, WeightKGs, Age, Sex) VALUES ('" +
                    fn + "', '" + ln + "', '" + email + "', 100, '2000-01-01', " + height + ", " + weight + ", " + age + ", '" + sex + "');");
            // gets memberID
            statement.executeQuery("SELECT MemberID FROM Members WHERE Email='"+ email + "';");
            ResultSet resultset = statement.getResultSet();
            while (resultset.next()){
                memberID = resultset.getInt("MemberID");
            }
            // creates entry for billing
            statement.executeUpdate("INSERT INTO Billings(MemberID, AmountDue) VALUES (" + memberID + ", 150);");

            System.out.println("Registration Successful");
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

        int ret;
        do {
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
                memberProfileManagement(input);
            } else if (ret == 2) {
                memberDashboard();
            } else if (ret == 3){
                memberManageRoutines(input);
            } else if (ret == 4) {
                memberManageClasses(input);
            }
        } while (ret != 0);
    }
    public static void memberProfileManagement(Scanner input){
        int ret;
        do {
            System.out.print("\n\nSelect option:\n");
            System.out.println("(1) Update Personal Information/Health Metrics");
            System.out.println("(2) Update Fitness Goals");
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

            if (ret == 1){
                int age,weight,height;
                System.out.print("Enter New Age: ");
                age = input.nextInt();
                input.nextLine();
                System.out.print("\nEnter New Weight in KGs: ");
                weight = input.nextInt();
                input.nextLine();
                System.out.print("\nEnter New Height in CMs: ");
                height = input.nextInt();
                input.nextLine();

                try{
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(url, user, password);
                    Statement statement = connection.createStatement();

                    statement.executeUpdate("UPDATE Members SET Age="+ age +" WHERE MemberID="+memberID+";");
                    statement.executeUpdate("UPDATE Members SET WeightKGs="+ weight +" WHERE MemberID="+memberID+";");
                    statement.executeUpdate("UPDATE Members SET Height="+ height +" WHERE MemberID="+memberID+";");
                    System.out.println("Update successful");
                    connection.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            else if (ret == 2){
                int goalWeight;
                String goalDeadline;
                System.out.print("\nEnter New Goal Weight in KGs: ");
                goalWeight = input.nextInt();
                input.nextLine();
                System.out.print("Enter New Goal Deadline (Format: YYYY-MM-DD): ");
                goalDeadline = input.nextLine();

                try{
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(url, user, password);
                    Statement statement = connection.createStatement();

                    statement.executeUpdate("UPDATE Members SET GoalWeightKGs="+ goalWeight +" WHERE MemberID="+memberID+";");
                    statement.executeUpdate("UPDATE Members SET GoalDeadline='"+ goalDeadline +"' WHERE MemberID="+memberID+";");
                    System.out.println("Update successful");
                    connection.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        } while (ret != 0);
    }
    public static void memberDashboard(){
        int height;
        int weight = 0;
        int goalWeight = 1;
        try{
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();

            statement.executeQuery("SELECT RoutineDescription, DurationMins FROM Routines WHERE MemberID=" + memberID + ";");
            ResultSet resultSet = statement.getResultSet();
            System.out.println("Routines: ");
            System.out.println("Description --|-- Duration (Minutes)");
            while(resultSet.next()){
                System.out.print(resultSet.getInt("RoutineDescription") + "\t");
                System.out.println(resultSet.getInt("DurationMins"));
            }

            statement.executeQuery("SELECT FName, LName, GoalWeightKGs, GoalDeadline, HeightCM, WeightKGs, Age, Sex FROM Members WHERE MemberID="
                    + memberID + ";");
            resultSet = statement.getResultSet();
            System.out.println("Health Statistics:");
            System.out.println("First Name --|-- Last Name --|-- Goal Weight --|-- Goal Deadline --|-- Height --|-- Weight --|-- Age --|-- Sex --|-- BMI");
            while(resultSet.next()){
                System.out.print(resultSet.getString("FName") + "\t");
                System.out.print(resultSet.getString("LName") + "\t");
                goalWeight = resultSet.getInt("GoalWeightKGs");
                System.out.print(goalWeight + "\t");
                System.out.print(resultSet.getString("GoalDeadline") + "\t");
                height = resultSet.getInt("heightCM");
                System.out.print(height + "\t");
                weight = resultSet.getInt("WeightKGs");
                System.out.print(weight + "\t");
                System.out.print(resultSet.getInt("Age") + "\t");
                System.out.print(resultSet.getString("Sex") + "\t");
                System.out.println(weight / (height * height));
            }

            System.out.println("Fitness Achievements:");
            if (goalWeight == weight){
                System.out.println("Weight Milestone Reached: " + weight + "KGs");
            }

            connection.close();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static void memberManageRoutines(Scanner input){
        int ret;
        do {
            System.out.print("\n\nSelect option:\n");
            System.out.println("(1) Add Routine");
            System.out.println("(2) Delete Routine");
            System.out.println("Enter Your Selection: ");
            ret = input.nextInt();
            input.nextLine();
            if (ret == 0) break;

            while (ret < 0 || ret > 2) {
                System.out.println("Selection out of range. Try again: ");
                input.nextLine();
                ret = input.nextInt();
            }

            int duration, routineID;
            String description;
            if(ret == 1){
                System.out.println("Enter Routine Description: ");
                duration = input.nextInt();
                input.nextLine();
                System.out.println("Enter Routine Duration: ");
                description = input.nextLine();

                try{
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(url, user, password);
                    Statement statement = connection.createStatement();

                    statement.executeUpdate("INSERT INTO Routines (MemberID, RoutineDescription, DurationMins) VALUES ("
                            + memberID + ", '" + description + "', " + duration + ");");

                    System.out.println("Insertion Successful");
                    connection.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if (ret == 2){
                System.out.println("Enter Routine ID: ");
                routineID = input.nextInt();
                input.nextLine();

                try{
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(url, user, password);
                    Statement statement = connection.createStatement();

                    statement.executeUpdate("DELETE FROM Routines WHERE RoutineID=" + routineID + ";");

                    System.out.println("Deletion Successful");
                    connection.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }

        } while (ret != 0);

    }
    // TODO: routines
    public static void memberManageClasses(Scanner input) {
        int ret;
        do {
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


        } while (ret != 0);
    }


    // _______________TRAINER FUNCTIONS_______________________________
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
        String fn,ln,email;
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
            // adds trainer to table
            statement.executeUpdate("INSERT INTO Trainers(FName, LName, Email) VALUES ('" +
                    fn + "', '" + ln + "', '" + email + "');");
            // gets trainer id
            statement.executeQuery("SELECT TrainerID FROM Trainers WHERE Email='" + email + "';");
            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next()){
                trainerID = resultSet.getInt("TrainerID");
            }
            // creates availability entry in table
            statement.executeUpdate("INSERT INTO TrainerAvailability (TrainerID,StartTime,EndTime) VALUES (" + trainerID +
                    ", '00:00', '23:59') WHERE TrainerID="+ trainerID +";");

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

        int ret;
        do {
            System.out.print("\n\nSelect option:\n");
            System.out.println("(1) Manage Schedule");
            System.out.println("(2) Member Profile Viewing");
            System.out.println("(3) View All Members"); // EXTRA FUNCTION
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
                manageTrainerSchedule(input);
            } else if (ret == 2){
                viewMemberProfile(input);
            } else {
                viewAllProfiles();
            }
        } while (ret != 0);
    }
    public static void manageTrainerSchedule(Scanner input){
        String start,end;
        System.out.println("Enter New Shift Start Time (HH:MM):");
        start = input.nextLine();
        System.out.println("Enter New Shift End Time (HH:MM):");
        end = input.nextLine();

        try{
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE TrainerAvailability SET StartTime='" + start + "' WHERE TrainerID="+ trainerID +";");
            statement.executeUpdate("UPDATE TrainerAvailability SET EndTime='" + end + "' WHERE TrainerID="+ trainerID +";");
            System.out.println("Update Successful");
            connection.close();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    // EXTRA FUNCTIONALITY
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


    // ______________ADMIN FUNCTIONS__________________________________
    //TODO: admin functions
    public static void adminCredScreen(Scanner input){}

}