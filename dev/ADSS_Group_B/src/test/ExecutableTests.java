package test;

import DataAccessLayer.HR.ControllerClasses.*;
import GlobalClasses.DateHelper;
import GlobalClasses.Role;
import GlobalClasses.EmployeeToSend;
import ServiceLayer.HR.WrapperService;
import ServiceLayer.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.mockClasses.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class ExecutableTests {


    private WrapperService wrapperService;
    private int hrManagerId;
    private int employeeId;
    private int branchId;
    private int branchIdSc2;
    private String hrManagerPassword;
    private DateHelper dateHelper;

    private static RoleController roleController;
    private static WorkScheduleController workScheduleController;
    private static WeeklyRoleConstraintsController weeklyRoleConstraintsController;
    private static EmployeeController employeeController;
    private static EmployeeAvailabilityController employeeAvailabilityController;

    @BeforeAll
    public static void beforeAll()
    {
        try {
            roleController = new RoleControllerMock();
            workScheduleController = new WorkScheduleControllerMock();
            weeklyRoleConstraintsController = new WeeklyRoleConstraintsControllerMock();
            employeeController = new EmployeeControllerMock();
            employeeAvailabilityController = new EmployeeAvailabilityControllerMock();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setUp() throws Exception {
        wrapperService = new WrapperService();
        wrapperService.hrManagerService.initialData();
        wrapperService = new WrapperService();
        wrapperService.loadDataFromDB();
        hrManagerId = 1; // hr ID
        employeeId =2; // emp ID
        branchId = 1;
        branchIdSc2=0;
        hrManagerPassword = "hrManager"; // HR manager password
        dateHelper = new DateHelper();
    }

    @Test
    public void testScenario() throws Exception {
        // Step 1: Login as hr manager

        Response loginResponse = wrapperService.employeeService.login(hrManagerId, hrManagerPassword);
        assertFalse(loginResponse.errorOccurred(), "Login failed: " + loginResponse.errorMsg);

        // Step 2: Add availability for all employees in the branch of week

        int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int date;

        if (today <= Calendar.TUESDAY) {
            // Closest week
            date = dateHelper.getNextWeekDate();
        } else {
            // Second closest
            date = dateHelper.getNextWeekDate();
            Calendar cal = Calendar.getInstance();
            cal.set(Integer.parseInt(String.valueOf(date).substring(0, 4)),
                    Integer.parseInt(String.valueOf(date).substring(4, 6)) - 1,
                    Integer.parseInt(String.valueOf(date).substring(6, 8)));
            cal.add(Calendar.DATE, 7);
            date = Integer.parseInt(String.format("%04d%02d%02d",
                    cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH)));
        }

        Response employeesResponse = wrapperService.hrManagerService.getAllEmployeesInBranch(hrManagerId, branchId);
        assertFalse(employeesResponse.errorOccurred(), "Failed to get employees: " + employeesResponse.errorMsg);

        EmployeeToSend[] employees = (EmployeeToSend[]) employeesResponse.returnValue;


        for (EmployeeToSend employee : employees) {

                Response employeeLoginResponse = wrapperService.employeeService.login(employee.id, "abc" + employee.id); // Assuming password is part of EmployeeToSend
                assertFalse(employeeLoginResponse.errorOccurred(), "Employee login failed: " + employeeLoginResponse.errorMsg);

                boolean[][] availability = new boolean[7][2];
                for (int i = 0; i < 7; i++) {
                    availability[i][0] = true;
                    availability[i][1] = false;
                }


                Response setAvailabilityResponse = wrapperService.employeeService.setAvailability(employee.id, date, availability);
                assertFalse(setAvailabilityResponse.errorOccurred(), "Set availability failed for employee " + employee.id + ": " + setAvailabilityResponse.errorMsg);


        }

        Role[][][] weeklyConstraints=new Role[7][2][];
        for(int i=0;i<7;i++){
            weeklyConstraints[i][0]=new Role[]{Role.CASHIER,Role.STOREKEEPER};
            weeklyConstraints[i][1]=new Role[]{};
        }
        Response setWeeklyConstraints=wrapperService.hrManagerService.setWeeklyRoleConstraints(hrManagerId,1,date,weeklyConstraints);
        assertFalse(setWeeklyConstraints.errorOccurred(),"Set weekly constraints failed");



        int[][][] employeesIdsArray = new int[7][2][employees.length];
        for (int i = 0; i < 7; i++) {
          List<Integer> morningShiftsIds= Stream.of(employees).map(employee->employee.id).collect(Collectors.toList());
          employeesIdsArray[i][0]=morningShiftsIds.stream().mapToInt(Integer::intValue).toArray();
          employeesIdsArray[i][1]=new int[0];
        }

        Response postWorkScheduleResponse = wrapperService.hrManagerService.postWorkSchedule(hrManagerId, branchId, date, employeesIdsArray);
        assertFalse(postWorkScheduleResponse.errorOccurred(), "Post work schedule failed: " + postWorkScheduleResponse.errorMsg);




    }

    @Test
    public void testScenario2() throws Exception {

        // Step 1: Login as hr manager
        Response loginResponse = wrapperService.employeeService.login(hrManagerId, hrManagerPassword);
        assertFalse(loginResponse.errorOccurred(), "Login failed: " + loginResponse.errorMsg);


        int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int date;
        if (today <= Calendar.TUESDAY) {
            // Closest week
            date = dateHelper.getNextWeekDate();
        } else {
            // Second closest
            date = dateHelper.getNextWeekDate();
            Calendar cal = Calendar.getInstance();
            cal.set(Integer.parseInt(String.valueOf(date).substring(0, 4)),
                    Integer.parseInt(String.valueOf(date).substring(4, 6)) - 1,
                    Integer.parseInt(String.valueOf(date).substring(6, 8)));
            cal.add(Calendar.DATE, 7);
            date = Integer.parseInt(String.format("%04d%02d%02d",
                    cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH)));
        }

        // Step 3: Retrieve all employees in branch 3
        Response employeesResponse = wrapperService.hrManagerService.getAllEmployeesInBranch(hrManagerId, 3);
        assertFalse(employeesResponse.errorOccurred(), "Failed to get employees: " + employeesResponse.errorMsg);

        EmployeeToSend[] employees = (EmployeeToSend[]) employeesResponse.returnValue;
        System.out.println("Total employees retrieved: " + employees.length);



        for (EmployeeToSend employee : employees) {
            Response employeeLoginResponse = wrapperService.employeeService.login(employee.id, "abc" + employee.id); // Assuming password is part of EmployeeToSend
            assertFalse(employeeLoginResponse.errorOccurred(), "Employee login failed: " + employeeLoginResponse.errorMsg);

            boolean[][] availability = new boolean[7][2];
            for (int i = 0; i < 7; i++) {
                availability[i][0] = true; // Morning
                availability[i][1] = true; // Evening
            }

            Response setAvailabilityResponse = wrapperService.employeeService.setAvailability(employee.id, date, availability);
            assertFalse(setAvailabilityResponse.errorOccurred(), "Set availability failed for employee " + employee.id + ": " + setAvailabilityResponse.errorMsg);
        }

        Role[][][] weeklyConstraints = new Role[7][2][];
        for (int i = 0; i < 7; i++) {
            weeklyConstraints[i][0] = new Role[]{Role.CASHIER, Role.STOREKEEPER, Role.MANAGER};
            weeklyConstraints[i][1] = new Role[]{Role.CASHIER, Role.STOREKEEPER};
        }
        Response setWeeklyConstraints = wrapperService.hrManagerService.setWeeklyRoleConstraints(hrManagerId, 3, date, weeklyConstraints);
        assertFalse(setWeeklyConstraints.errorOccurred(), "Set weekly constraints for branch 3 failed");

        List<EmployeeToSend> cashiers = Stream.of(employees).filter(employee -> Arrays.asList(employee.roles).contains(Role.CASHIER)).collect(Collectors.toList());
        List<EmployeeToSend> storekeepers = Stream.of(employees).filter(employee -> Arrays.asList(employee.roles).contains(Role.STOREKEEPER)).collect(Collectors.toList());
        List<EmployeeToSend> managers = Stream.of(employees).filter(employee -> Arrays.asList(employee.roles).contains(Role.MANAGER)).collect(Collectors.toList());

        System.out.println("Cashiers: " + cashiers.size());
        System.out.println("Storekeepers: " + storekeepers.size());
        System.out.println("Managers: " + managers.size());

        int[][][] employeesIdsArray = new int[7][2][];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                List<Integer> shiftEmployees = new ArrayList<>();
                if (j == 0) { // Morning shift
                    if (!cashiers.isEmpty()) shiftEmployees.add(cashiers.get(i % cashiers.size()).id);
                    if (!storekeepers.isEmpty()) shiftEmployees.add(storekeepers.get(i % storekeepers.size()).id);
                    if (!managers.isEmpty()) shiftEmployees.add(managers.get(i % managers.size()).id);
                } else { // Evening shift
                    if (!cashiers.isEmpty()) shiftEmployees.add(cashiers.get((i + 1) % cashiers.size()).id);
                    if (!storekeepers.isEmpty()) shiftEmployees.add(storekeepers.get((i + 1) % storekeepers.size()).id);
                }

                employeesIdsArray[i][j] = shiftEmployees.stream().mapToInt(Integer::intValue).toArray();
            }
        }

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.println("Day " + i + " Shift " + j + " Constraints: " + Arrays.toString(weeklyConstraints[i][j]));
                System.out.println("Assigned Employees: " + Arrays.toString(employeesIdsArray[i][j]));
            }
        }

        Response postWorkScheduleResponse = wrapperService.hrManagerService.postWorkSchedule(hrManagerId, 3, date, employeesIdsArray);
        assertFalse(postWorkScheduleResponse.errorOccurred(), "Post work schedule failed: " + postWorkScheduleResponse.errorMsg);
    }


    @Test
    public void testScenario3() throws Exception {
            // Step 1: Login as hr manager
            Response loginResponse = wrapperService.employeeService.login(hrManagerId, hrManagerPassword);
            assertFalse(loginResponse.errorOccurred(), "Login failed: " + loginResponse.errorMsg);

            // Step 2: Determine the date for the schedule
            int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            int date;
            if (today <= Calendar.TUESDAY) {
                // Closest week
                date = dateHelper.getNextWeekDate();
            } else {
                // Second closest
                date = dateHelper.getNextWeekDate();
                Calendar cal = Calendar.getInstance();
                cal.set(Integer.parseInt(String.valueOf(date).substring(0, 4)),
                        Integer.parseInt(String.valueOf(date).substring(4, 6)) - 1,
                        Integer.parseInt(String.valueOf(date).substring(6, 8)));
                cal.add(Calendar.DATE, 7);
                date = Integer.parseInt(String.format("%04d%02d%02d",
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH)));
            }

            // Step 3: Retrieve all employees in branch 1
            Response employeesResponse = wrapperService.hrManagerService.getAllEmployeesInBranch(hrManagerId, 1);
            assertFalse(employeesResponse.errorOccurred(), "Failed to get employees: " + employeesResponse.errorMsg);

            EmployeeToSend[] employees = (EmployeeToSend[]) employeesResponse.returnValue;
            System.out.println("Total employees retrieved: " + employees.length);

            // Step 4: Set availability for each employee for the entire week
            for (EmployeeToSend employee : employees) {
                Response employeeLoginResponse = wrapperService.employeeService.login(employee.id, "abc" + employee.id); // Assuming password is part of EmployeeToSend
                assertFalse(employeeLoginResponse.errorOccurred(), "Employee login failed: " + employeeLoginResponse.errorMsg);

                boolean[][] availability = new boolean[7][2];
                for (int i = 0; i < 7; i++) {
                    availability[i][0] = true;
                    availability[i][1] = true;
                }

                Response setAvailabilityResponse = wrapperService.employeeService.setAvailability(employee.id, date, availability);
                assertFalse(setAvailabilityResponse.errorOccurred(), "Set availability failed for employee " + employee.id + ": " + setAvailabilityResponse.errorMsg);
            }

            Role[][][] weeklyConstraints = new Role[7][2][];
            for (int i = 0; i < 7; i++) {
                if (i == 0) {
                    weeklyConstraints[i][0] = new Role[]{Role.CASHIER, Role.STOREKEEPER, Role.MANAGER}; // Morning constraints for day 0
                    weeklyConstraints[i][1] = new Role[]{Role.CASHIER, Role.STOREKEEPER}; // Evening constraints for day 0
                } else {
                    weeklyConstraints[i][0] = new Role[]{};
                    weeklyConstraints[i][1] = new Role[]{};
                }
            }
            Response setWeeklyConstraints = wrapperService.hrManagerService.setWeeklyRoleConstraints(hrManagerId, 1, date, weeklyConstraints);
            assertFalse(setWeeklyConstraints.errorOccurred(), "Set weekly constraints for branch 1 failed");

            // Step 6: Create the work schedule
            List<EmployeeToSend> cashiers = Stream.of(employees).filter(employee -> Arrays.asList(employee.roles).contains(Role.CASHIER)).collect(Collectors.toList());
            List<EmployeeToSend> storekeepers = Stream.of(employees).filter(employee -> Arrays.asList(employee.roles).contains(Role.STOREKEEPER)).collect(Collectors.toList());
            List<EmployeeToSend> managers = Stream.of(employees).filter(employee -> Arrays.asList(employee.roles).contains(Role.MANAGER)).collect(Collectors.toList());

            int[][][] employeesIdsArray = new int[7][2][];
            Set<Integer> assignedEmployees=new HashSet<>();

            for (int i=0;i<7;i++){
                for(int j=0;j<2;j++){
                    List<Integer> shiftEmployees=new ArrayList<>();
                    if(i==0){
                        if(j==0){
                            for(EmployeeToSend cashier:cashiers){
                                if(!assignedEmployees.contains(cashier.id)){
                                    shiftEmployees.add(cashier.id);
                                    assignedEmployees.add(cashier.id);
                                    break;
                                }
                            }
                            for(EmployeeToSend storeKeeper:storekeepers){
                                if(!assignedEmployees.contains(storeKeeper.id)){
                                    shiftEmployees.add(storeKeeper.id);
                                    assignedEmployees.add(storeKeeper.id);
                                    break;
                                }
                            }
                            for(EmployeeToSend manager:managers){
                                if(!assignedEmployees.contains(manager.id)){
                                    shiftEmployees.add(manager.id);
                                    assignedEmployees.add(manager.id);
                                    break;
                                }
                            }

                        }else{
                            for (EmployeeToSend cashier : cashiers) {
                                if (!assignedEmployees.contains(cashier.id)) {
                                    shiftEmployees.add(cashier.id);
                                    assignedEmployees.add(cashier.id);
                                    break;
                                }
                            }
                            for (EmployeeToSend storekeeper : storekeepers) {
                                if (!assignedEmployees.contains(storekeeper.id)) {
                                    shiftEmployees.add(storekeeper.id);
                                    assignedEmployees.add(storekeeper.id);
                                    break;
                                }
                            }
                        }
                    }
                    employeesIdsArray[i][j]=shiftEmployees.stream().mapToInt(Integer::intValue).toArray();


                }
            }
//            for (int i = 0; i < 7; i++) {
//                for (int j = 0; j < 2; j++) {
//                    List<Integer> shiftEmployees = new ArrayList<>();
//
//                    // Add required roles to the shift for day 0
//                    if (i == 0) {
//                        if (j == 0) { // Morning shift
//                            if (!cashiers.isEmpty()) shiftEmployees.add(cashiers.get(0).id);
//                            if (!storekeepers.isEmpty()) shiftEmployees.add(storekeepers.get(0).id);
//                            if (!managers.isEmpty()) shiftEmployees.add(managers.get(0).id);
//                        } else { // Evening shift
//                            if (!cashiers.isEmpty()) shiftEmployees.add(cashiers.get(1).id);
//                            if (!storekeepers.isEmpty()) shiftEmployees.add(storekeepers.get(1).id);
//                        }
//                    }
//
//                    employeesIdsArray[i][j] = shiftEmployees.stream().mapToInt(Integer::intValue).toArray();
//                }
//            }

            Response postWorkScheduleResponse = wrapperService.hrManagerService.postWorkSchedule(hrManagerId, 1, date, employeesIdsArray);
            assertFalse(postWorkScheduleResponse.errorOccurred(), "Post work schedule failed: " + postWorkScheduleResponse.errorMsg);
        }



}








