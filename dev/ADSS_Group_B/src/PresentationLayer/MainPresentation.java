package PresentationLayer;

import GlobalClasses.Role;
import GlobalClasses.EmployeeToSend;
import ServiceLayer.HR.WrapperService;
import ServiceLayer.Response;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The MainPresentation class serves as the entry point for our HR management system.
 * It provides a console interface for employees and HR managers to interact with the system.
 *
 * The system is designed to support two types of users: Regular Employees and HR Managers.
 *
 * - Regular Employees:
 *   Regular employees can log in to the system using their employee ID and password.
 *   Once logged in, they have access to the following functionalities:
 *   1. Submit Availability: Employees can submit their availability for work shifts for a given week.
 *   2. View Work Schedule: Employees can view their assigned work schedule for a specific date.
 *   3. Logout: Employees can log out of the system.
 *
 *  - HR Managers:
 *
 *   HR Managers have additional privileges compared to regular employees. They can perform all the tasks
 *   available to regular employees and in addition they have access to a range of management functionalities, including:
 *   1. Edit Employees: HR Managers can add new employees, update existing employee information, and get information about employees.
 *   2. Manage Availability: HR Managers can view the availability of all employees in a specific branch for a given week.
 *   3. Manage Roles: HR Managers can assign or remove roles for employees.
 *   4. Post Work Schedule: HR Managers can create and post work schedules for employees in a specific branch.
 *   5. Set Weekly Role Constraints: HR Managers can set constraints on the number of employees required for each role for each shift in a week.
 *   6. Restrict Employee Access: HR Managers can restrict an employee's access to the system.
 *   7. Reinstate Employee Access: HR Managers can reinstate an employee's access to the system.
 *   8. Get All Employees in Branch: HR Managers can view all employees in a specific branch.
 *   9. Restore Weekly Role Constraints to Default: HR Managers can restore the role constraints for a week to their default values.
 *   10. Get Weekly Role Constraints: HR Managers can retrieve the role constraints for a specific week.
 *   11. Logout:  log out of the system.
 *
 * The MainPresentation class uses a Scanner object to read input from the console and interacts with the
 * WrapperService class, which acts as a facade to the underlying service layer, ensuring a clear separation
 * of concerns and make the code more manageable .
 */
public class MainPresentation {
    private static final Scanner scanner = new Scanner(System.in);
    private static final WrapperService wrapperService;

    static {
        try {
            wrapperService = new WrapperService();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean loggedIn = false;
    private static int currentEmployeeId = -1;
    private static boolean isHRManager = false;
    private static boolean laodDataUsed=false;

    public static void main(String[] args) {
        while (true) {
            if (!loggedIn) {
                System.out.println("1. Login");
                System.out.println("2. Exit");
                if(!laodDataUsed){
                    System.out.println("3. Load Testing Data");
                }
                System.out.print("Choose an option: ");
                int option = scanner.nextInt();
                scanner.nextLine();

                try {
                    switch (option) {
                        case 1 -> login();
                        case 2 -> {
                            System.out.println("Exiting the program! Goodbye!");
                            return;
                        }
                        case 3->{
                            if(!laodDataUsed){
                                loadTestingData();
                                laodDataUsed=true;
                            }else{
                                System.out.println("Invalid option. Please try again.");
                            }
                        }
                        default -> System.out.println("Invalid option. Please try again.");
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else {
                boolean shouldContinue = true;
                while (loggedIn && shouldContinue) {

                    System.out.println("1.HR Management");
                    System.out.println("2.Inventory Management");
                    System.out.println("3.Logout");

                    System.out.print("Choose an option:");
                    int option=scanner.nextInt();
                    scanner.nextLine();
                    switch (option){
                        case 1->{
                            if(isHRManager){
                                hrManagerMenu();
                            }else{
                                regularEmployeeMenu();
                            }
                        }
                        case 2->{
                            if(isHRManager){
                                InventoryCLI.MainManagerMenu();
                            }else {
                                InventoryCLI.MainEmployeeMenu();
                            }
                        }
                        case 3->{
                            try{
                                logout();
                            }catch (Exception e){
                                System.out.println("Exeption "+e.getMessage());
                            }

                        }
                        default -> System.out.println("Invalid option.Please try again.");
                    }

                }
            }
        }
    }

    private static void loadTestingData() throws Exception {
        InventoryCLI.loadInitialData();
        wrapperService.loadDataFromDB();
        System.out.println("Loaded data successfully");
    }


    /**
     * Handles the login process for employees.
     * @throws Exception if there is an error during login.
     */

    private static void login() throws Exception {
        System.out.print("Enter employee ID: ");
        int id = getIntInput();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        Response response = wrapperService.employeeService.login(id, password);
        if (!response.errorOccurred()) {
            loggedIn = true;
            currentEmployeeId = id;
            Response empResponse = wrapperService.employeeService.getEmployee(id);
            if (!empResponse.errorOccurred()) {
                EmployeeToSend  employee = (EmployeeToSend) empResponse.returnValue;
                isHRManager = employee.isHRManager;;
            } else {
                System.out.println(empResponse.errorMsg);
            }
        } else {
            System.out.println(response.errorMsg);
        }
    }


    /**
     * Handles the logout process for employees.
     * @return true to continue in the inner loop, false to break out to the login menu.
     * @throws Exception if there is an error during logout.
     */
    private static boolean logout() throws Exception {
        if (!loggedIn) {
            System.out.println("You must be logged in to log out.");
            return true;
        }
        Response response = wrapperService.employeeService.logout(currentEmployeeId);
        if (!response.errorOccurred()) {
            loggedIn = false;
            currentEmployeeId = -1;
            System.out.println(response.returnValue);
            return false; // Return false to break the inner loop and go back to the login/exit menu
        }
        System.out.println(response.errorMsg);
        return true; // Continue in the inner loop
    }


    /**
     * Displays the HR manager menu and handles menu options.
     */
    private static void hrManagerMenu() {
        System.out.println("1. Edit employees");
        System.out.println("2. Get Info About Existing Availability");
        System.out.println("3. Manage Roles");
        System.out.println("4. Post work Schedule");
        System.out.println("5. Set Weekly Role Constraints");
        System.out.println("6. Restrict Employee Access");
        System.out.println("7. Reinstate Employee Access");
        System.out.println("8. Get All Employees in Branch");
        System.out.println("9. Restore weekly Role Constraints to Default");
        System.out.println("10. Get Weekly Role Constraints");
        System.out.println("11. ChangePassword ");
        System.out.println("12. Back to Main Menu ");

        int option = scanner.nextInt();
        scanner.nextLine();
        try{
            switch (option){
                case 1 -> editEmployees();
                case 2 -> getAvailabilityInformation();
                case 3 -> manageRoles();
                case 4 -> postWorkSchedule();
                case 5-> setWeeklyRoleConstraints();
                case 6->restrictEmployeeAccess();
                case 7->reinstateEmployeeAcess();
                case 8->getAllEmployeesInBranch();
                case 9->restoreWeeklyRoleConstraintsToDefault();
                case 10->getWeeklyRoleConstraints();
                case 11->changePassword();
                case 12-> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");

            }
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
    /**
     * Displays the regular employee menu and handles menu options !.
     */
    private static void regularEmployeeMenu() {

        System.out.println("1.Submit Availibiltity");
        System.out.println("2. View Work Schedule");
        System.out.println("3. Change Password");
        System.out.println("4. Back to Main Menu");
        System.out.print("Choose an option: ");

        int option = scanner.nextInt();
        scanner.nextLine();

        try{
            switch (option){
                case 1->submitAvailability();
                case 2->viewWorkSchedule();
                case 3->changePassword();
                case 4-> {

                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }


        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }


    /**
     * Allows an employee to submit their availability for the upcoming week .
     * @throws Exception if there is an error during the process
     */
    private static void submitAvailability() throws Exception {
        System.out.print("Enter Date (yyyyMMdd): ");
        int date = scanner.nextInt();
        scanner.nextLine();
        Response response = wrapperService.employeeService.canChangeAvailability(date);
        if (response.errorOccurred()) {
            System.out.println(response.errorMsg);
            return;
        }
        boolean canSubmit = (Boolean) response.returnValue;
        if (!canSubmit) {
            System.out.println("You cannot submit availability for the selected week.");
            return;
        }
        boolean[][] availabilityArray = new boolean[7][2];
        for (int i = 0; i < 7; i++) {
            availabilityArray[i][0] = getBooleanInput("Can work on day " + (i + 1) + " in the morning (true/false): ");
            availabilityArray[i][1] = getBooleanInput("Can work on day " + (i + 1) + " in the evening (true/false): ");
        }

        response = wrapperService.employeeService.setAvailability(currentEmployeeId, date, availabilityArray);
        if (response.errorOccurred()) {
            System.out.println(response.errorMsg);
        } else {
            System.out.println(response.returnValue);
        }
    }

    private static boolean getBooleanInput(String prompt){
        while(true){
            System.out.println(prompt);
            String input=scanner.nextLine().trim().toLowerCase();
            if (input.equals("true") || input.equals("false")) {
                return Boolean.parseBoolean(input);
            } else {
                System.out.println("Not valid input. Please try again.");
            }
        }
    }

    /**
     * Retrieves an integer input from the user, ensuring it is valid.
     * @return the valid integer input.
     */

    private static int getIntInput(){
        while(true){
            try{
                return Integer.parseInt(scanner.nextLine().trim());

            }catch (NumberFormatException e){
                System.out.println("Not a valid input .Please enter a valid number .");
            }
        }
    }

    /**
     * Displays the menu options for editing employees (Available for hr manager role)
     * @throws Exception if there is an error during the process
     */

    private static void editEmployees() throws Exception {
        System.out.println("1. Get Information about Employees");
        System.out.println("2. Add New Employee");
        System.out.println("3. Add Driver New Employee");
        System.out.println("4. Update Employee's Information");
        System.out.print("Choose an option: ");
        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1 -> getEmployeeInformation();
            case 2 -> addEmployee();
            case 3->addDriverEmployee();
            case 4 -> updateEmployeeInformation();
            default -> System.out.println("Invalid option. Please try again.");
        }
    }

    private static void addDriverEmployee() throws Exception {
        System.out.print("Enter Driver Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Driver ID: ");
        int id = getIntInput();
        System.out.print("Enter Branch ID: ");
        int branchId = getIntInput();
        System.out.print("Enter Terms of Employment: ");
        String termsOfEmployment = scanner.nextLine();
        System.out.print("Enter Start Date (yyyyMMdd): ");
        int startDate = getIntInput();
        System.out.print("Enter Bank Code: ");
        int bankCode = getIntInput();
        System.out.print("Enter Bank Branch Code: ");
        int bankBranchCode = getIntInput();
        System.out.print("Enter Bank Account: ");
        int bankAccount = getIntInput();
        System.out.print("Enter Hourly Rate: ");
        int hourlyRate = getIntInput();
        System.out.print("Enter Monthly Rate: ");
        int monthlyRate = getIntInput();

        EmployeeToSend newDriver = new EmployeeToSend(id, branchId, termsOfEmployment, name, startDate, bankCode, bankBranchCode, bankAccount, hourlyRate, monthlyRate, new Role[0]);
        Response response = wrapperService.hrManagerService.addDriver(currentEmployeeId, newDriver, password);
        if (response.errorOccurred()) {
            System.out.println(response.errorMsg);
        } else {
            System.out.println(response.returnValue);
        }
    }


    /**
     * Retrieves and displays information of given employeee
     * @throws Exception if there is an error during process
     */
    private static void getEmployeeInformation() throws Exception {
        System.out.print("Enter Employee ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        Response response = wrapperService.hrManagerService.getEmployee(1, id); // Assuming managerId is 0 for simplicity
        if (!response.errorOccurred()) {

            EmployeeToSend employee = (EmployeeToSend) response.returnValue;
            System.out.println("Employee ID: " + employee.id);
            System.out.println("Employee Name:" + employee.name);
            System.out.println("Branch: " + employee.branchId);
            System.out.println("Terms of Employment: " + employee.termsOfEmployment);
            System.out.println("Start Date: " + employee.startDate);

        } else {
            System.out.println(response.errorMsg);
        }
    }

    /**
     * adds a new employee to the system
     * @throws Exception if there is an error during the process
     */

    private static void addEmployee() throws Exception {

        System.out.print("Enter Employee Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Employee ID: ");
        int id = getIntInput();
        System.out.print("Enter Branch ID: ");
        int branchId = getIntInput();
        System.out.print("Enter Terms of Employment: ");
        String termsOfEmployment = scanner.nextLine();
        System.out.print("Enter start date (yyyyMMdd): ");
        int startDate = getIntInput();
        System.out.print("Enter Bank Code: ");
        int bankCode = getIntInput();
        System.out.print("Enter Bank Branch Code: ");
        int bankBranchCode = getIntInput();
        System.out.print("Enter Bank Account: ");
        int bankAccount = getIntInput();
        System.out.print("Enter Hourly Rate: ");
        int hourlyRate = getIntInput();
        System.out.print("Enter Monthly Rate: ");
        int monthlyRate = getIntInput();
        List<Role> rolesList = new ArrayList<>();

        // Notice that the system read one role each time until your done
        while (true) {
            System.out.print("Enter Role (CASHIER, STOREKEEPER, MANAGER) or 'done' to finish: ");
            String roleStr = scanner.nextLine();
            if (roleStr.equalsIgnoreCase("done")) {
                break;
            }
            try {
                Role role = Role.valueOf(roleStr.toUpperCase());
                if(!rolesList.contains(role)){
                     rolesList.add(role);
                }else{
                    System.out.println("Role already added.Please enter a different role !");
                }

            } catch (IllegalArgumentException e) {
                System.out.println("Invalid role. Please try again.");
            }
        }

        Role[] roles = rolesList.toArray(new Role[0]);
        EmployeeToSend newEmployee = new EmployeeToSend(id, branchId, termsOfEmployment, name, startDate, bankCode, bankBranchCode, bankAccount, hourlyRate, monthlyRate, roles);
        Response response = wrapperService.hrManagerService.addEmployee(currentEmployeeId, newEmployee, password);
        if (response.errorOccurred()) {
            System.out.println(response.errorMsg);
        } else {
            System.out.println(response.returnValue);
        }
    }

    /**
     * Updates the information of an existing employee.
     * @throws Exception if there is an error during the process.
     */



    private static void updateEmployeeInformation() throws Exception {
        System.out.print("Enter Employee ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Response response = wrapperService.hrManagerService.getEmployee(currentEmployeeId, id);
        if (!response.errorOccurred()) {
            EmployeeToSend employee = (EmployeeToSend) response.returnValue;
            System.out.println("Current information: ");
            System.out.println("Name: " + employee.name);
            System.out.println("Branch ID: " + employee.branchId);
            System.out.println("Terms of Employment: " + employee.termsOfEmployment);
            System.out.println("Start Date: " + employee.startDate);
            System.out.println("Bank Code: " + employee.bankCode);
            System.out.println("Bank Branch Code: " + employee.bankBranchCode);
            System.out.println("Bank Account: " + employee.bankAccount);
            System.out.println("Hourly Rate: " + employee.hourlyRate);
            System.out.println("Monthly Rate: " + employee.monthlyRate);

            System.out.print("Enter new Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter new Branch ID: ");
            int branchId = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter new Terms of Employment: ");
            String termsOfEmployment = scanner.nextLine();
            System.out.print("Enter new Start Date: ");
            int startDate = scanner.nextInt();
            System.out.print("Enter new Bank Code: ");
            int bankCode = scanner.nextInt();
            System.out.print("Enter new Bank Branch Code: ");
            int bankBranchCode = scanner.nextInt();
            System.out.print("Enter new Bank Account: ");
            int bankAccount = scanner.nextInt();
            System.out.print("Enter new Hourly Rate: ");
            int hourlyRate = scanner.nextInt();
            System.out.print("Enter new Monthly Rate: ");
            int monthlyRate = scanner.nextInt();
            scanner.nextLine();
            Role[] roles = employee.roles;
            EmployeeToSend updatedEmployee = new EmployeeToSend(id, branchId, termsOfEmployment, name, startDate, bankCode, bankBranchCode, bankAccount, hourlyRate, monthlyRate, roles);
            Response updateResponse = wrapperService.hrManagerService.updateEmployee(currentEmployeeId, updatedEmployee);
            if (updateResponse.errorOccurred()) {
                System.out.println(updateResponse.errorMsg);
            } else {
                System.out.println(updateResponse.returnValue);
            }
        } else {
            System.out.println(response.errorMsg);
        }
    }



    /**
     * Retrieves and displays information about employee availability for a specific branch and date.
     * @throws Exception if there is an error during the process.
     */

    private static void getAvailabilityInformation() throws Exception {
        System.out.print("Enter Branch ID: ");
        int branchId = getIntInput();
        System.out.print("Enter Date (yyyyMMdd): ");
        int date =  getIntInput();
        Response response = wrapperService.hrManagerService.getEmployeesAvailability(currentEmployeeId, date, branchId);
        if (!response.errorOccurred()) {
            List<String>[][] availability = (List<String>[][]) response.returnValue;
            displayAvailability(availability);
        } else {
            System.out.println(response.errorMsg);
        }
    }


    /**
     * Displays the availability of employees for each day and shift.
     * @param availability a 2D list containing the availability information for each day and shift.
     */

    private static void displayAvailability(List<String>[][] availability) {
        String[] days={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        String[] shifts={"Morning","Evening"};
        for (int i = 0; i < 7; i++) {
            for (int j=0;j<2;j++){
                System.out.println(days[i]+" "+shifts[j]+":");
                if(availability[i][j].isEmpty()){
                    System.out.println("NO AVAILABILITY");

                }else{
                    for(String employee:availability[i][j]){
                        System.out.println(" "+employee);
                    }
                }

            }
        }
    }

    /**
     * Retrieves and displays the work schedule for the current employee for a specific date.
     * @throws Exception if there is an error during the process.
     */


    private static void viewWorkSchedule() throws Exception {
        System.out.print("Enter Date (yyyyMMdd): ");
        int date =  getIntInput();
        Response response = wrapperService.employeeService.getWorkSchedule(currentEmployeeId, date);
        if (response.errorOccurred()) {
            System.out.println(response.errorMsg);
        } else {
            String[][][] scheduleArr=(String [][][]) response.returnValue;
            displayWorkSchedule(scheduleArr);
        }
    }

    /**
     * Displays the work schedule for the week.
     * @param scheduleArr a 3D array containing the work schedule for each day and shift.
     */

    private static void displayWorkSchedule(String[][][] scheduleArr) {
       String[] days={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
       String[] shifts={"Morning","Evening"};
       for (int i = 0; i < 7; i++) {
           for(int j=0;j<2;j++){
               System.out.println(days[i]+" "+shifts[j]+":");
               if(scheduleArr[i][j].length==0){
                   System.out.println("No employees scheduled");
               }else{
                   for(String employee:scheduleArr[i][j]){
                       System.out.println("Employee name"+employee);
                   }
               }
           }
       }
    }

    /**
     * Displays the menu options for managing roles and handles the selected option.
     * @throws Exception if there is an error during the process.
     */
    private static void manageRoles() throws Exception{
        System.out.println("1. Assign Role to Employee");
        System.out.println("2. Remove Role from Employee");
        System.out.print("Choose an option: ");
        int option = scanner.nextInt();
        scanner.nextLine();
        switch (option){
            case 1-> assignRole();
            case 2->removeRole();
            default -> System.out.println("Invalid option. Please try again ! ");
        }
    }

    /**
     * Assigns a role to an employee.
     * @throws Exception if there is an error during the process.
     */
    private static void assignRole() throws Exception{
        System.out.println("Enter Employee ID: ");
        int employeeId =  getIntInput();

        Role role=null;
        while(role==null) {
            System.out.print("Enter Role:(CASHIER,STOREKEEPER,MANAGER) ");
            String roleStr = scanner.nextLine();


            try {
                role = Role.valueOf(roleStr.toUpperCase());
            }catch (IllegalArgumentException e){
                System.out.println("Invalid role, please try again ");
            }
        }
        Response response=wrapperService.hrManagerService.assignRole(currentEmployeeId,employeeId,
                role);

        if(response.errorOccurred()){
            System.out.println(response.errorMsg);
        }
        else{
            System.out.println(response.returnValue);

        }


    }
    /**
     * Removes a role from an employee.
     * @throws Exception if there is an error during the process.
     */
    private static void removeRole() throws Exception {
        System.out.print("Enter Employee ID: ");
        int employeeId = getIntInput();  // No extra scanner.nextLine()

        Role role = null;
        while (role == null) {
            System.out.print("Enter Role (CASHIER, STOREKEEPER, MANAGER): ");
            String roleStr = scanner.nextLine();
            try {
                role = Role.valueOf(roleStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid role. Please try again.");
            }
        }

        Response response = wrapperService.hrManagerService.removeRole(currentEmployeeId, employeeId, role);
        if (response.errorOccurred()) {
            System.out.println(response.errorMsg);
        } else {
            System.out.println(response.returnValue);
        }
    }

    /**
     * Posts the work schedule for a specific branch and date.
     * @throws Exception if there is an error during the process.
     */
    private static void postWorkSchedule() throws Exception {
        System.out.print("Enter Branch ID: ");
        int branchId = getIntInput();

        System.out.print("Enter Date (yyyyMMdd): ");
        int date = getIntInput();

        int[][][] employeesIdsArray = new int[7][2][];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                while (true) {
                    try {
                        System.out.println("Enter Employee IDs for " + getDayString(i) + " " + getShiftString(j) + " shift (comma separated or 'empty' for no employees): ");
                        String input = scanner.nextLine();
                        if (input.equalsIgnoreCase("empty")) {
                            employeesIdsArray[i][j] = new int[0]; // Set the shift to be empty
                        } else {
                            String[] employeeIds = input.split(",");
                            employeesIdsArray[i][j] = new int[employeeIds.length];
                            for (int k = 0; k < employeeIds.length; k++) {
                                employeesIdsArray[i][j][k] = Integer.parseInt(employeeIds[k].trim());
                            }
                        }
                        break;
                    } catch (Exception e) {
                        System.out.println("Invalid input. Please try again.");
                    }
                }
            }
        }

        Response response = wrapperService.hrManagerService.postWorkSchedule(currentEmployeeId, branchId, date, employeesIdsArray);
        if (response.errorOccurred()) {
            System.out.println(response.errorMsg);
        } else {
            System.out.println("Work schedule posted successfully.");
        }
    }



    /**
     * Converts a day index to its corresponding day name.
     * @param day the day index (0=Sunday, 1=Monday, ..., 6=Saturday)
     * @return the corresponding day name.
     */

    private static String getDayString(int day) {
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        return days[day];
    }
    /**
     * Converts a shift index to its corresponding shift name.
     * @param shift the shift index (0=Morning, 1=Evening)
     * @return the corresponding shift name.
     */
    private static String getShiftString(int shift) {
        String[] shifts = {"Morning", "Evening"};
        return shifts[shift];
    }


    /**
     * Sets the weekly role constraints for a specific branch and date.
     * @throws Exception if there is an error during the process.
     */
    private static void setWeeklyRoleConstraints() throws Exception {
        System.out.print("Enter Branch ID: ");
        int branchId = getIntInput();

        System.out.print("Enter Date (yyyyMMdd): ");
        int date = getIntInput();

        Role[][][] constraintsArray = new Role[7][2][];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                while (true) {
                    try {
                        System.out.print("Enter roles for " + getDayString(i) + " " + getShiftString(j) + " shift (comma separated, e.g. CASHIER,MANAGER): ");
                        String roles = scanner.nextLine();
                        String[] roleArray = roles.split(",");
                        constraintsArray[i][j] = new Role[roleArray.length];
                        for (int k = 0; k < roleArray.length; k++) {
                            constraintsArray[i][j][k] = Role.valueOf(roleArray[k].trim().toUpperCase());
                        }
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid role. Please try again.");
                    }
                }
            }
        }

        Response response = wrapperService.hrManagerService.setWeeklyRoleConstraints(currentEmployeeId, branchId, date, constraintsArray);
        if (response.errorOccurred()) {
            System.out.println(response.errorMsg);
        } else {
            System.out.println("Weekly role constraints set successfully.");
        }
    }

    /**
     * Restricts access for a specific employee.
     * @throws Exception if there is an error during the process.
     */

    private static void restrictEmployeeAccess() throws Exception{
        System.out.println("Enter Employee ID");
        int employeeId=getIntInput();
        Response response=wrapperService.hrManagerService.removeAccess(currentEmployeeId,employeeId);
        if(response.errorOccurred()){
            System.out.println(response.errorMsg);
        }
        else{
            System.out.println(response.returnValue);
        }
    }


    /**
     * Reinstates access for a specific employee.
     * @throws Exception if there is an error during the process.
     */
    private static void reinstateEmployeeAcess() throws Exception{
        System.out.print("Enter Employee ID");
        int employeeId=getIntInput();
        Response response=wrapperService.hrManagerService.reinstateAccess(currentEmployeeId,employeeId);
        if(response.errorOccurred()){
            System.out.println(response.errorMsg);
        }else{
            System.out.println(response.returnValue);
        }
    }

    /**
     * Retrieves and displays all employees in a specific branch.
     * @throws Exception if there is an error during the process.
     */
    private static void getAllEmployeesInBranch() throws Exception {
        System.out.print("Enter Branch ID: ");
        int branchId = getIntInput();
        Response response = wrapperService.hrManagerService.getAllEmployeesInBranch(currentEmployeeId, branchId);
        if (response.errorOccurred()) {
            System.out.println(response.errorMsg);
        } else {
            EmployeeToSend[] employees = (EmployeeToSend[]) response.returnValue;
            if(employees.length==0){
                System.out.println("No employess in the given branch.");
            }else{
                for (EmployeeToSend employee : employees) {
                    System.out.println("Employee ID: " + employee.id);
                    System.out.println("Employee Name: " + employee.name);
                    System.out.println("Branch: " + employee.branchId);
                    System.out.println("Terms of Employment: " + employee.termsOfEmployment);
                    System.out.println("Start Date: " + employee.startDate);
                    System.out.println("Roles: " + getRolesAsString(employee.roles));
                    System.out.println();
                }
            }
        }
    }


    private static String getRolesAsString(Role[] roles){
        StringBuilder rolesStr = new StringBuilder();
        for (Role role : roles) {
            if (rolesStr.length() > 0) {
                rolesStr.append(", ");
            }
            rolesStr.append(role.toString());
        }
        return rolesStr.toString();

    }

    /**
     * Restores the weekly role constraints to their default values for a specific branch and date.
     * @throws Exception if  there is an error during the process.
     */
    private static void restoreWeeklyRoleConstraintsToDefault() throws Exception {
        System.out.print("Enter Branch ID: ");
        int branchId = getIntInput();
        System.out.print("Enter Date (yyyyMMdd): ");
        int date = getIntInput();

        Response response = wrapperService.hrManagerService.restoreWeeklyRoleConstraintsToDefault(currentEmployeeId, branchId, date);
        if (response.errorOccurred()) {
            System.out.println(response.errorMsg);
        } else {
            System.out.println("Weekly role constraints restored to default successfully.");
        }
    }
    /**
     * Retrieves and displays the weekly role constraints for a specific branch and date.
     * @throws Exception if there is an error during the process.
     */
    private static void getWeeklyRoleConstraints() throws Exception {
        System.out.print("Enter Branch ID: ");
        int branchId = getIntInput();

        System.out.print("Enter Date (yyyyMMdd): ");
        int date = getIntInput();

        Response response = wrapperService.hrManagerService.getWeeklyRoleConstraints(currentEmployeeId, branchId, date);
        if (response.errorOccurred()) {
            System.out.println(response.errorMsg);
        } else {
            Role[][][] constraints = (Role[][][]) response.returnValue;
            displayWeeklyRoleConstraints(constraints);
        }
    }

    /**
     * Displays the weekly role constraints.
     * @param constraints a 3D array containing the role constraints for each day and shift.
     */
    private static void displayWeeklyRoleConstraints(Role[][][] constraints) {
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        String[] shifts = {"Morning", "Evening"};

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.println(days[i] + " " + shifts[j] + " Constraints:");
                for (Role role : constraints[i][j]) {
                    System.out.println(role);
                }
            }
        }
    }

    /**
     * Change the password of current Employee user
     * @throws Exception if theres an error during the process
     */
    private static void changePassword() throws Exception {
        System.out.print("Enter your old password: ");
        String oldPassword = scanner.nextLine();
        System.out.print("Enter your new password: ");
        String newPassword = scanner.nextLine();

        Response response = wrapperService.employeeService.changePassword(currentEmployeeId, oldPassword, newPassword);
        if (response.errorOccurred()) {
            System.out.println(response.errorMsg);
        } else {
            System.out.println("Password changed successfully.");
        }
    }
}





