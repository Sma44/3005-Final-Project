package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.util.HashSet;

// TODO: add order by statement to queries
public class App 
{
    static String url = "jdbc:postgresql://localhost:5432/FitnessApp";
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
            // choice validation
            while (ret < 0 || ret > 3) {
                System.out.println("Selection out of range. Try again: ");
                input.nextLine();
                ret = input.nextInt();
            }
            // calls chosen user view
            if (ret == 1) {
                memberCredScreen(input);
            } else if (ret == 2) {
                trainerCredScreen(input);
            } else if (ret == 3){
                adminCredScreen(input);
            }
        } while (ret != 0);
    }

    // _________________MEMBER USER FUNCTIONS_____________________________
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
            // choice validation
            while (ret < 0 || ret > 2) {
                System.out.println("Selection out of range. Try again: ");
                input.nextLine();
                ret = input.nextInt();
            }

            // switches to log in or register pages
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
            statement.executeUpdate("INSERT INTO Billings(MemberID, AmountDue) VALUES (" + memberID + ", 250);");

            System.out.println("Registration Successful");
            connection.close();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static void memberView(Scanner input){
        String email;
        HashSet<String> memberEmails = new HashSet<>();

        // adds list of emails in table into hashset for authentication
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

        // login validation
        System.out.print("Enter email or type 'exit' to leave menu:");
        email = input.nextLine();
        System.out.println();
        while (!memberEmails.contains(email)){
            if (email.equals("exit")){
                return;
            }
            System.out.println("invalid email:");
            System.out.print("Enter email:");
            email = input.nextLine();
            System.out.println();
        }

        // gets member ID
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
            // choice validation
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
            // choice validation
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
                    statement.executeUpdate("UPDATE Members SET HeightCM="+ height +" WHERE MemberID="+memberID+";");
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
                System.out.print(resultSet.getString("RoutineDescription") + "\t");
                System.out.println(resultSet.getInt("DurationMins"));
            }

            statement.executeQuery("SELECT FName, LName, GoalWeightKGs, GoalDeadline, HeightCM, WeightKGs, Age, Sex FROM Members WHERE MemberID="
                    + memberID + ";");
            resultSet = statement.getResultSet();
            System.out.println("Health Statistics:");
            System.out.println("First | Last | Goal | Deadline | Height | Weight | Age | Sex | BMI");
            while(resultSet.next()){
                System.out.print(resultSet.getString("FName") + "\t");
                System.out.print(resultSet.getString("LName") + "\t");
                goalWeight = resultSet.getInt("GoalWeightKGs");
                System.out.print(goalWeight + "\t");
                System.out.print(resultSet.getString("GoalDeadline") + "\t");
                height = resultSet.getInt("heightCM");
                System.out.print(height + "\t" + "\t");
                weight = resultSet.getInt("WeightKGs");
                System.out.print(weight + "\t" + "\t" + "\t");
                System.out.print(resultSet.getInt("Age") + "\t");
                System.out.print(resultSet.getString("Sex") + "\t");
                System.out.println(weight / ((height/(float)100) * (height/(float)100)));
            }

            System.out.println("Fitness Achievements:");
            if (goalWeight == weight){
                System.out.println("Weight Milestone Reached: " + weight + "KGs");
            }else{
                System.out.println("No Achievements");
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

            int duration, routineID;
            String description;
            if(ret == 1){
                System.out.println("Enter Routine Description: ");
                description = input.nextLine();
                System.out.println("Enter Routine Duration in Minutes: ");
                duration = input.nextInt();
                input.nextLine();

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

                try{
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(url, user, password);
                    Statement statement = connection.createStatement();

                    statement.executeQuery("SELECT * FROM Routines WHERE MemberID="+ memberID + " ORDER BY RoutineID DESC;");
                    ResultSet resultSet = statement.getResultSet();
                    System.out.println("Routines:");
                    System.out.println("Routine ID --|-- Member ID --|-- Description --|-- Duration");
                    while(resultSet.next()){
                        System.out.print(resultSet.getInt("RoutineID") + "\t");
                        System.out.print(resultSet.getInt("MemberID") + "\t");
                        System.out.print(resultSet.getString("RoutineDescription") + "\t");
                        System.out.println(resultSet.getInt("DurationMins"));
                    }

                    connection.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }

                System.out.println();
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
    public static void memberManageClasses(Scanner input) {
        int ret;
        do {
            System.out.print("\n\nSelect option:\n");
            System.out.println("(1) Schedule a Class");
            System.out.println("(2) Cancel a Class");

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
                // display available classes
                try{
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(url, user, password);
                    Statement statement = connection.createStatement();

                    // prints available classes
                    statement.executeQuery("SELECT ClassID, ClassType, ClassDescription, StartTime, EndTime FROM Classes WHERE Available=True;");
                    ResultSet resultSet = statement.getResultSet();

                    System.out.println("Available Classes: ");
                    System.out.println("Class ID --|-- Class Type --|-- Description --|-- Start Time --|-- End Time");
                    while (resultSet.next()){
                        System.out.print(resultSet.getInt("ClassID") + "\t");
                        System.out.print(resultSet.getString("ClassType") + "\t");
                        System.out.print(resultSet.getString("ClassDescription") + "\t");
                        System.out.print(resultSet.getString("StartTime") + "\t");
                        System.out.println(resultSet.getString("EndTime"));
                    }
                    int id;
                    System.out.println("Enter Selection through class ID:");
                    id = input.nextInt();
                    input.nextLine();

                    // check if type is personal
                    String type = "";
                    statement.executeQuery("SELECT ClassType FROM Classes WHERE ClassID=" + id + ";");
                    resultSet = statement.getResultSet();
                    while (resultSet.next()){
                        type = resultSet.getString("ClassType");
                    }

                    if (type.equals("Personal")){
                        statement.executeUpdate("UPDATE Classes SET Available=False WHERE ClassID=" + id + ";");
                    }
                    // update takes table
                    statement.executeUpdate("INSERT INTO Takes (MemberID, ClassID) VALUES (" + memberID + ", " + id + ");");

                    // adds to billing
                    int amount = 250;
                    statement.executeQuery("SELECT AmountDue FROM Billings WHERE MemberID=" + memberID + ";");
                    while (resultSet.next()) {
                        amount = resultSet.getInt("AmountDue");
                    }
                    statement.executeUpdate("UPDATE Billings SET AmountDue= "+ (amount + 100) + " WHERE MemberID=" + memberID + ";");

                    System.out.println("Sign Up Successful");
                    connection.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            else if (ret == 2){
                // display taken classes from takes table
                try{
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(url, user, password);
                    Statement statement = connection.createStatement();

                    // prints available classes for deletion
                    statement.executeQuery("SELECT ClassID, ClassType, ClassDescription, StartTime, EndTime FROM Classes " +
                            "WHERE ClassID IN (SELECT ClassID FROM Takes WHERE MemberID="+ memberID + ");");
                    ResultSet resultSet = statement.getResultSet();

                    System.out.println("Available Classes: ");
                    System.out.println("Class ID --|-- Class Type --|-- Description --|-- Start Time --|-- End Time");
                    while (resultSet.next()){
                        System.out.print(resultSet.getInt("ClassID") + "\t");
                        System.out.print(resultSet.getString("ClassType") + "\t");
                        System.out.print(resultSet.getString("ClassDescription") + "\t");
                        System.out.print(resultSet.getString("StartTime") + "\t");
                        System.out.println(resultSet.getString("EndTime"));
                    }
                    int id;
                    System.out.println("Enter Selection through class ID:");
                    id = input.nextInt();
                    input.nextLine();

                    // check if type is personal
                    String type = "";
                    statement.executeQuery("SELECT ClassType FROM Classes WHERE ClassID=" + id + ";");
                    resultSet = statement.getResultSet();
                    while (resultSet.next()){
                        type = resultSet.getString("ClassType");
                    }

                    if (type.equals("Personal")){
                        statement.executeUpdate("UPDATE Classes SET Available=True WHERE ClassID=" + id + ";");
                    }

                    statement.executeUpdate("DELETE FROM Takes WHERE MemberID=" + memberID + " AND ClassID=" + id + ";");

                    // subtract from billing
                    int amount = 250;
                    statement.executeQuery("SELECT AmountDue FROM Billings WHERE MemberID=" + memberID + ";");
                    while (resultSet.next()) {
                        amount = resultSet.getInt("AmountDue");
                    }
                    statement.executeUpdate("UPDATE Billings SET AmountDue= "+ (amount - 100) + ";");

                    System.out.println("Remove Successful");
                    connection.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
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
            statement.executeUpdate("INSERT INTO Trainers (FName, LName, Email) VALUES ('" +
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
        String email;
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

        while (!trainerEmails.contains(email)){
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
            } else if (ret == 3){
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
    } // EXTRA FUNCTIONALITY
    public static void viewMemberProfile(Scanner input){
        String first;
        String last;
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
    public static void adminCredScreen(Scanner input){
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
                registerAdmin(input);
            } else {
                adminView(input);
            }
        }
    }
    public static void registerAdmin(Scanner input){
        String email;
        System.out.println("Enter email:");
        email = input.nextLine();

        try{
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            // adds admin to table
            statement.executeUpdate("INSERT INTO Admins (Email) VALUES ('" + email + "');");
            // gets admin id
            statement.executeQuery("SELECT AdminID FROM Admins WHERE Email='" + email + "';");
            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next()){
                adminID = resultSet.getInt("AdminID");
            }

            System.out.println("Registration Successful");
            connection.close();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static void adminView(Scanner input){
        String email;
        HashSet<String> adminEmails = new HashSet<>();

        try{
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT Email FROM Admins;");
            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next()){
                adminEmails.add(resultSet.getString("Email"));
            }
            connection.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        System.out.println("Enter email or type 'exit' to leave menu:");
        email = input.nextLine();
        System.out.println();

        while (!adminEmails.contains(email)){
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
            statement.executeQuery("SELECT AdminID FROM Admins WHERE Email='" + email + "';");
            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next()){
                adminID = resultSet.getInt("AdminID");
            }
            connection.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        int ret;
        do {
            System.out.print("\n\nSelect option:\n");
            System.out.println("(1) Room Management");
            System.out.println("(2) Equipment Maintenance Monitoring");
            System.out.println("(3) Class Schedule Updating");
            System.out.println("(4) Billing and Payment Processing");
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

            if (ret == 1){
                int c;
                System.out.print("\n\nSelect option:\n");
                System.out.println("(1) Add Room");
                System.out.println("(2) Remove Room");
                System.out.println("(0) EXIT");
                System.out.println("Enter Your Selection: ");
                c = input.nextInt();
                input.nextLine();

                if (c == 1){
                    String name;
                    System.out.println("Enter Room Name:");
                    name = input.nextLine();

                    try{
                        Class.forName("org.postgresql.Driver");
                        connection = DriverManager.getConnection(url, user, password);
                        Statement statement = connection.createStatement();
                        statement.executeUpdate("INSERT INTO Rooms (RoomName) VALUES ('"+name+"');");
                        System.out.println("Room Creation Successful");
                        connection.close();
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                else if (c == 2){
                    // print rooms currently available
                    try{
                        Class.forName("org.postgresql.Driver");
                        connection = DriverManager.getConnection(url, user, password);
                        Statement statement = connection.createStatement();
                        statement.executeQuery("SELECT * FROM Rooms;");
                        ResultSet resultSet = statement.getResultSet();
                        System.out.println("Rooms:");
                        System.out.println("Room ID --|-- Room Name");
                        while(resultSet.next()){
                            System.out.print(resultSet.getInt("RoomID")+"\t");
                            System.out.println(resultSet.getString("RoomName"));
                        }
                        connection.close();
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }

                    int id;
                    System.out.println("Enter Room ID:");
                    id = input.nextInt();
                    input.nextLine();

                    try{
                        Class.forName("org.postgresql.Driver");
                        connection = DriverManager.getConnection(url, user, password);
                        Statement statement = connection.createStatement();
                        statement.executeUpdate("DELETE FROM Rooms WHERE RoomID="+id+";");
                        System.out.println("Room Deletion Successful");
                        connection.close();
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            }
            else if (ret == 2){
                try{
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(url, user, password);
                    Statement statement = connection.createStatement();

                    System.out.println("Equipment:");
                    System.out.println("Name --|-- Description --|-- Condition");
                    statement.executeQuery("SELECT EquipmentName, EquipmentDescription, Condition FROM Equipment");
                    ResultSet resultSet = statement.getResultSet();
                    while(resultSet.next()){
                        System.out.print(resultSet.getString("EquipmentName") + "\t");
                        System.out.println(resultSet.getString("EquipmentDescription"));
                        System.out.println(resultSet.getString("Condition"));
                    }

                    int c;
                    System.out.print("\n\nSelect option:\n");
                    System.out.println("(1) Add Equipment");
                    System.out.println("(2) Remove Equipment");
                    System.out.println("(0) EXIT");
                    System.out.println("Enter Your Selection: ");
                    c = input.nextInt();
                    input.nextLine();
                    while (c < 0 || c > 2) {
                        System.out.println("Selection out of range. Try again: ");
                        input.nextLine();
                        c = input.nextInt();
                    }

                    String name,des,cond;
                    int id;
                    if (c == 1){
                        System.out.println("Enter Name:");
                        name = input.nextLine();
                        System.out.println("Enter Description:");
                        des = input.nextLine();
                        System.out.println("Enter Condition:");
                        cond = input.nextLine();

                        // add equipment to table
                        statement.executeUpdate("INSERT INTO Equipment (EquipmentName, EquipmentDescription, Condition) VALUES ('"
                                + name + "', '" + des + "', '" + cond + "');");

                        System.out.println("Addition Successful");
                    }else if (c == 2){
                        System.out.println("Enter Equipment ID:");
                        id = input.nextInt();

                        // delete equipment from table
                        statement.executeUpdate("DELETE FROM Equipment WHERE EquipmentID=" + id + ";");

                        System.out.println("Deletion Successful");
                    }
                    connection.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            else if (ret == 3){
                int c;
                System.out.print("\n\nSelect option:\n");
                System.out.println("(1) Add Class");
                System.out.println("(2) Remove Class");
                System.out.println("(0) EXIT");
                System.out.println("Enter Your Selection: ");
                c = input.nextInt();
                input.nextLine();

                if (c==1){
                    int roomId, tID;
                    String classType,des,start,end;
                    System.out.println("Enter room ID:");
                    roomId = input.nextInt();
                    input.nextLine();
                    System.out.println("Enter Trainer ID:");
                    tID = input.nextInt();
                    input.nextLine();
                    System.out.println("Enter class type:");
                    classType = input.nextLine();
                    System.out.println("Enter class Description:");
                    des = input.nextLine();
                    System.out.println("Enter Start Time (Format: HH:MM):");
                    start = input.nextLine();
                    System.out.println("Enter End Time (Format: HH:MM):");
                    end = input.nextLine();

                    try{
                        Class.forName("org.postgresql.Driver");
                        connection = DriverManager.getConnection(url, user, password);
                        Statement statement = connection.createStatement();
                        statement.executeUpdate("INSERT INTO Classes (RoomID, TrainerID, ClassType, ClassDescription, StartTime, EndTime, Available) VALUES ("
                                + roomId + ", " + tID + ", '" + classType + "', '" + des + "', '" + start + "', '" + end + "', True);");

                        System.out.println("Class Added Successfully");
                        connection.close();
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                else if (c==2){
                    System.out.println("Classes:");
                    try{
                        Class.forName("org.postgresql.Driver");
                        connection = DriverManager.getConnection(url, user, password);
                        Statement statement = connection.createStatement();
                        statement.executeQuery("SELECT * FROM Classes;");
                        ResultSet resultSet = statement.getResultSet();
                        while(resultSet.next()){
                            System.out.print(resultSet.getInt("ClassID") + "\t");
                            System.out.print(resultSet.getInt("RoomID") + "\t");
                            System.out.print(resultSet.getInt("TrainerID") + "\t");
                            System.out.print(resultSet.getString("ClassType") + "\t");
                            System.out.print(resultSet.getString("StartTime") + "\t");
                            System.out.print(resultSet.getString("EndTime") + "\t");
                            System.out.println(resultSet.getString("Available"));
                        }
                        connection.close();
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }

                    int cId;
                    System.out.println("Enter Class ID:");
                    cId = input.nextInt();
                    input.nextLine();

                    try{
                        Class.forName("org.postgresql.Driver");
                        connection = DriverManager.getConnection(url, user, password);
                        Statement statement = connection.createStatement();
                        statement.executeUpdate("DELETE FROM Classes WHERE ClassID=" + cId + ";");

                        System.out.println("Class Deleted Successfully");
                        connection.close();
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            }
            else if (ret == 4){
                try{
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(url, user, password);
                    Statement statement = connection.createStatement();

                    System.out.println("Member Dues:");
                    System.out.println("Member ID | Amount Due");
                    statement.executeQuery("SELECT * FROM Billings ORDER BY MemberID DESC");
                    ResultSet resultSet = statement.getResultSet();
                    while(resultSet.next()){
                        System.out.print(resultSet.getInt("MemberID") + "\t");
                        System.out.println(resultSet.getInt("AmountDue"));
                    }
                    System.out.println("End of Billings");
                    connection.close();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        } while (ret != 0);
    }
}