import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class RestaurantConsole {

    public static void main (String[] args) throws IOException, SQLException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Database db = new Database();

        /*

            1. List All Restaurants
            2. Find the Business Hours of a Restaurant
            3. Create a Regular Account
            4. Cancel a Reservation
            5. View All Reviews of a Restaurant
            6. Exit

         */

        boolean active = true;

        while (active) {

            // Prints Menu
            consolePrintMainMenu();

            // Reads Input
            String input = br.readLine();

            switch (input) {
                case "1": {

                    // Setup Query for Restaurants
                    String q = "Select restaurantId, name, phoneNumber from Restaurant;";

                    ArrayList<ArrayList<String>> data = db.dbQuery(q);

                    // Format and Prints Data
                    System.out.println(padRight("Restaurant ID", 30)
                            + padRight("Restaurant Name", 30)
                            + padRight("Phone Number", 10));
                    System.out.println(padRight("", 73).replaceAll(" ", "-"));
                    for (ArrayList<String> row : data) {
                        System.out.print(padRight(row.get(0), 30));
                        System.out.print(padRight(row.get(1), 30));
                        System.out.print(padRight(row.get(2), 10));
                        System.out.println();
                    }

                    // Pause to allow reading
                    System.out.println("Press Enter to Continue");
                    br.readLine();

                    break;
                }
                case "2": {

                    // Query
                    String q = "Select restaurantId, name from Restaurant;";

                    ArrayList<ArrayList<String>> data = db.dbQuery(q);
                    ArrayList<String> rIdList = new ArrayList<>();

                    // If the query is empty, print
                    if (data.isEmpty()) {
                        System.out.println("There are no Restaurants Available");
                        break;
                    }

                    // Formats and Print's Data
                    System.out.println(padRight("Restaurant ID", 30)
                            + padRight("Restaurant Name", 30));
                    System.out.println(padRight("", 60).replaceAll(" ", "-"));
                    for (ArrayList<String> row : data) {
                        rIdList.add(row.get(0));

                        System.out.print(padRight(row.get(0), 30));
                        System.out.print(padRight(row.get(1), 30));
                    }

                    // Reads and Parses Input
                    System.out.println("\nPlease Enter the Restaurant ID to Check Business Hours");
                    String rIdInput = "";
                    while (true) {
                        rIdInput = br.readLine();

                        if (!rIdList.contains(rIdInput)) {
                            System.out.println("Please Input a Valid Restaurant ID");
                        } else {
                            break;
                        }
                    }

                    // Secondary Query
                    q = "Select day, openTime, closeTime from BusinessHour where restaurantId = '" + rIdInput + "';";

                    data = db.dbQuery(q);

                    // Format and Prints Data
                    System.out.println(padRight("Day", 11)
                            + padRight("Opening Time", 17)
                            + padRight("Closing Time", 17));
                    System.out.println(padRight("", 37).replaceAll(" ", "-"));

                    for (ArrayList<String> row : data) {

                        switch (row.get(0)) {

                            case "1": {
                                System.out.print(padRight("Monday", 11));
                                break;
                            }
                            case "2": {
                                System.out.print(padRight("Tuesday", 11));
                                break;
                            }
                            case "3": {
                                System.out.print(padRight("Wednesday", 11));
                                break;
                            }
                            case "4": {
                                System.out.print(padRight("Thursday", 11));
                                break;
                            }
                            case "5": {
                                System.out.print(padRight("Friday", 11));
                                break;
                            }
                            case "6": {
                                System.out.print(padRight("Saturday", 11));
                                break;
                            }
                            case "7": {
                                System.out.print(padRight("Sunday", 11));
                                break;
                            }
                            default: {
                                continue;
                            }

                        }

                        System.out.print(padRight(row.get(1).substring(0, row.get(1).length()-3), 17));
                        System.out.print(padRight(row.get(2).substring(0, row.get(2).length()-3), 17));
                        System.out.println();
                    }

                    // Pause to allow reading
                    System.out.println("\nPress Enter to Continue");
                    br.readLine();

                    break;
                }
                case "3": {

                    // Query
                    String q = "Select email, username, phoneNumber from RegularAccount " +
                            "UNION " +
                            "Select email, username, phoneNumber from BusinessAccount";

                    ArrayList<ArrayList<String>> data = db.dbQuery(q);
                    ArrayList<String> emails = new ArrayList<>();
                    ArrayList<String> usernames = new ArrayList<>();
                    ArrayList<String> phoneNumbers = new ArrayList<>();

                    // Holds a list to check for duplicates in application instead of database
                    for (ArrayList<String> row : data) {

                        emails.add(row.get(0));
                        usernames.add(row.get(1));
                        phoneNumbers.add(row.get(2));

                    }

                    // Inputs
                    System.out.println("Please Enter Your Email");
                    String email = br.readLine();

                    while (emails.contains(email)) {
                        System.out.println("There is an account associated with this email. Please enter a different email.");
                        email = br.readLine();
                    }

                    System.out.println("Please Enter a Username");
                    String username = br.readLine();

                    while (usernames.contains(username)) {
                        System.out.println("This username is taken. Please enter a different username.");
                        username = br.readLine();
                    }

                    System.out.println("Please Create a Secure Password");
                    String pswd = br.readLine();

                    System.out.println("Please Enter Your Phone Number");
                    String phone = br.readLine();

                    while (phone.length() != 10 || phoneNumbers.contains(phone)) {

                        if (phoneNumbers.contains(phone)) {
                            System.out.println("This phoneNumber is associated with a different account. Please enter a different number");

                        } else if (phone.length() != 10) {
                            System.out.println("Please enter a valid phone number");
                        }

                        phone = br.readLine();
                    }

                    // Insert Data
                    q = "Insert into RegularAccount(email, username, password, phoneNumber) Values('"
                            + email + "', '" + username + "', '" + pswd + "', '" + phone + "');";

                    db.dbInsertOrUpdate(q);

                    System.out.println("Account created successfully");

                    // Pause to allow reading
                    System.out.println("Press Enter to Continue");
                    br.readLine();

                    break;
                }
                case "4": {

                    // Query
                    String q = "Select email from RegularAccount;";

                    ArrayList<ArrayList<String>> data = db.dbQuery(q);
                    ArrayList<String> emails = new ArrayList<>();

                    // If Data is empty, go back to main menu
                    if (data.isEmpty()) {
                        System.out.println("There are no Users");
                        break;
                    }

                    // Format and Print Data
                    System.out.println(padRight("Email", 31));
                    System.out.println(padRight("", 31).replaceAll(" ", "-"));
                    for (ArrayList<String> row : data) {
                        emails.add(row.get(0));

                        System.out.print(padRight(row.get(0), 30));
                        System.out.println();
                    }

                    // Allow Input
                    System.out.println("\nPlease Enter the Email from the Reservation");
                    String emailInput = "";
                    while (true) {
                        emailInput = br.readLine();

                        if (!emails.contains(emailInput)) {
                            System.out.println("Please Input a Valid Email");
                        } else {
                            break;
                        }
                    }

                    // Secondary Query
                    q = "Select reserve.restaurantId, rest.name, reserve.numberOfPeople, reserve.date from Reservation as reserve " +
                            "Inner Join Restaurant as rest on rest.restaurantId = reserve.restaurantId " +
                            "where reserve.accountEmail = '" + emailInput + "' and isValid = 1";

                    data = db.dbQuery(q);

                    // If Data is empty, go back to main menu
                    if (data.isEmpty()) {
                        System.out.println("There are no Reservations");
                        break;
                    }

                    // Format and Print Data
                    System.out.println(padRight("ID", 6)
                            + padRight("Restaurant ID", 31)
                            + padRight("Restaurant Name", 31)
                            + padRight("Guests", 7)
                            + padRight("Date and Time", 21));
                    System.out.println(padRight("", 96).replaceAll(" ", "-"));
                    for (int i = 0; i < data.size(); i++) {

                        System.out.print(padRight(Integer.toString(i+1), 6));
                        System.out.print(padRight(data.get(i).get(0), 31));
                        System.out.print(padRight(data.get(i).get(1), 31));
                        System.out.print(padRight(data.get(i).get(2), 7));
                        System.out.print(padRight(data.get(i).get(3), 21));
                        System.out.println();

                    }

                    // Allow Input for Reservation ID
                    System.out.println("\nPlease Enter the ID of the Reservation");
                    int reservationIdInput = 0;
                    while (true) {

                        try {
                            reservationIdInput = Integer.parseInt(br.readLine())-1;
                        } catch (Exception e) {
                            System.out.println("Please Enter the Number ID");
                            continue;
                        }

                        if (!(reservationIdInput >= 0 && reservationIdInput < data.size())) {
                            System.out.println("Please Enter a Valid ID");
                        } else {
                            break;
                        }
                    }

                    // Update information in Database
                    q = "Update Reservation " +
                            "Set isValid = 0 " +
                            "where accountEmail = '" + emailInput + "' and date = '" + data.get(reservationIdInput).get(3) + "';";

                    db.dbInsertOrUpdate(q);

                    System.out.println("Reservation Cancelled Successfully");

                    // Allow Pause for Reading
                    System.out.println("Press Enter to Continue");
                    br.readLine();

                    break;
                }
                case "5": {

                    // Query
                    String q = "Select restaurantId, name from Restaurant;";

                    ArrayList<ArrayList<String>> data = db.dbQuery(q);
                    ArrayList<String> rIdList = new ArrayList<>();

                    // If Data is empty, return to main menu
                    if (data.isEmpty()) {
                        System.out.println("There are no Restaurants Available");
                        break;
                    }

                    // Format and Print Data
                    System.out.println(padRight("Restaurant ID", 30)
                            + padRight("Restaurant Name", 30));
                    System.out.println(padRight("", 60).replaceAll(" ", "-"));
                    for (ArrayList<String> row : data) {
                        rIdList.add(row.get(0));

                        System.out.print(padRight(row.get(0), 30));
                        System.out.print(padRight(row.get(1), 30));
                        System.out.println();
                    }

                    // Allow input of restaurantId
                    System.out.println("\nPlease Enter the Restaurant ID of the Reviews you Wish to View");
                    String rIdInput = "";
                    while (true) {
                        rIdInput = br.readLine();

                        if (!rIdList.contains(rIdInput)) {
                            System.out.println("Please Input a Valid Restaurant ID");
                        } else {
                            break;
                        }
                    }

                    // Secondary Query
                    q = "select reviewId, rating, comment, postedAt, accountEmail from Review where restaurantId = '" + rIdInput + "';";

                    data = db.dbQuery(q);

                    // Format and Print Data
                    System.out.println(padRight("Review ID", 30)
                            + padRight("Rating", 10)
                            + padRight("Comment", 200)
                            + padRight("Posted At", 21)
                            + padRight("Posted By", 30));
                    System.out.println(padRight("", 339).replaceAll(" ", "-"));
                    for (ArrayList<String> row : data) {
                        System.out.print(padRight(row.get(0), 30));
                        System.out.print(padRight(row.get(1), 10));
                        System.out.print(padRight(row.get(2), 200));
                        System.out.print(padRight(row.get(3), 21));
                        System.out.print(padRight(row.get(4), 30));
                        System.out.println();
                    }

                    // Allow pause for reading
                    System.out.println("\nPress Enter to Continue");
                    br.readLine();

                    break;
                }
                case "6": {
                    // Breaks out of loop
                    System.out.println("Exiting");
                    active = false;
                    break;
                }
                default: {
                    System.out.println("Invalid Input, Please Select a Valid Number");
                }
            }

        }

        // Closes database connection
        db.dbClose();

    }

    // Prints the main menu
    private static void consolePrintMainMenu() {
        System.out.println(
                "Restaurant Service Main Menu" +
                        "\n\t1. List All Restaurants" +
                        "\n\t2. Find the Business Hours of a Restaurant" +
                        "\n\t3. Create a Regular Account" +
                        "\n\t4. Cancel a Reservation" +
                        "\n\t5. View All Reviews of a Restaurant" +
                        "\n\t6. Exit" +
                        "\nPlease Enter your Option:"
        );
    }

    // Pads Strings with spaces
    private static String padRight(String s, int size) {
        return String.format("%-" + size + "s", s);
    }
}
