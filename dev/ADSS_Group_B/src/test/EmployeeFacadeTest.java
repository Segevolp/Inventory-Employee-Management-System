
package test;
import DataAccessLayer.HR.ControllerClasses.EmployeeAvailabilityController;
import DataAccessLayer.HR.ControllerClasses.EmployeeController;
import DataAccessLayer.HR.ControllerClasses.RoleController;
import DomainLayer.HR.Employee;
import DomainLayer.HR.EmployeeFacade;
import DomainLayer.HR.WorkSchedule;
import GlobalClasses.DateHelper;
import DomainLayer.HR.EmployeeAvailability;
import GlobalClasses.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.mockClasses.EmployeeAvailabilityControllerMock;
import test.mockClasses.EmployeeControllerMock;
import test.mockClasses.RoleControllerMock;

import java.util.*;

public class EmployeeFacadeTest {
    //For employee
    private static String password;
    private static int employeeId;
    private static int branchId;
    private static String name;
    private static Role[] roles;
    private static String termsOfEmployment;
    private static int startDate;
    private static int bankCode;
    private static int bankBranchCode;
    private static int bankAccount;
    private static int hourlyRate;
    private static int monthlyRate;
    private static DateHelper dateHelper;
    private static EmployeeController employeeController;
    private static RoleController roleController;
    private static EmployeeAvailabilityController employeeAvailabilityController;

    //For facade
    private EmployeeFacade employeeFacade;
    private Employee employee;

    @BeforeAll
    static void init()
    {
        dateHelper = new DateHelper();
        password = "abcd1234";
        employeeId = 20;
        branchId = 5;
        name = "john doe";
        roles = new Role[]{Role.CASHIER,Role.STOREKEEPER};
        termsOfEmployment="you work, I pay.";
        startDate = 20240606;
        bankCode = 10;
        bankBranchCode = 53;
        bankAccount = 80811;
        hourlyRate = 50;
        monthlyRate = 8000;
        try {
            roleController = new RoleControllerMock();
            employeeController = new EmployeeControllerMock();
            employeeAvailabilityController = new EmployeeAvailabilityControllerMock();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    @BeforeEach
    void init_each()
    {
        int maxBranchId = 9; // 0 .. 9
        HashMap<Integer, Employee> employeesDict = new HashMap<>();
        HashMap<Integer, WorkSchedule>[] workScheduleHashMap = new HashMap[maxBranchId+1];
        HashMap<Integer,HashMap<Employee, EmployeeAvailability>>[] employeesAvailabilityDict = new HashMap[maxBranchId+1];
        for(int i =0;i<=maxBranchId;i++)
        {
            workScheduleHashMap[i] = new HashMap<>();
            employeesAvailabilityDict[i] = new HashMap<>();
        }
        try {
            employee = new Employee(password,employeeId,branchId,name,roles,termsOfEmployment,startDate,bankCode,
                    bankBranchCode,bankAccount,hourlyRate,monthlyRate,employeeController,roleController);
        } catch (Exception e) {
            throw new RuntimeException(e);
        };
        employeesDict.put(employeeId,employee);
        try {
            employeeFacade = new EmployeeFacade(employeesDict,workScheduleHashMap,employeesAvailabilityDict,employeeAvailabilityController);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void login_employeeDoesntExists_exception()
    {
        Assertions.assertThrows(Exception.class,() -> employeeFacade.login(employeeId+5,password));
        Assertions.assertThrows(Exception.class,() -> employeeFacade.login(employeeId-1,password));
        Assertions.assertThrows(Exception.class,() -> employeeFacade.login(-1,password));
        Assertions.assertThrows(Exception.class,() -> employeeFacade.login(1000000,password));
    }
    @Test
    void login_properUse_userLoggedInAfter()
    {
        Assertions.assertTrue(() -> {
            try {
                boolean test1 = !employee.loggedIn();
                employeeFacade.login(employeeId,password);
                boolean test2 = employee.loggedIn();
                return test1&&test2;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    void login_wrongPassword_exception()
    {
        Assertions.assertThrows(Exception.class,() -> employeeFacade.login(employeeId,password+"k"));
        Assertions.assertThrows(Exception.class,() -> employeeFacade.login(employeeId,null));
        Assertions.assertThrows(Exception.class,() -> employeeFacade.login(employeeId,""));
    }
    @Test
    void login_revokedAccessEmployee_exception()
    {
        Assertions.assertThrows(Exception.class,() -> {employee.setHasAccess(false);
            employeeFacade.login(employeeId, password);
        });

    }
    @Test
    void logout_employeeDoesntExists_exception()
    {
        Assertions.assertThrows(Exception.class,() -> employeeFacade.logout(employeeId+5));
        Assertions.assertThrows(Exception.class,() -> employeeFacade.logout(employeeId-1));
        Assertions.assertThrows(Exception.class,() -> employeeFacade.logout(-1));
        Assertions.assertThrows(Exception.class,() -> employeeFacade.logout(1000000));
    }
    @Test
    void logout_properUse_userLoggedOutAfter()
    {
        Assertions.assertTrue(() -> {
            try {
                employeeFacade.login(employeeId,password);
                boolean test1 = employee.loggedIn();
                employeeFacade.logout(employeeId);
                boolean test2 = !employee.loggedIn();
                return test1&&test2;

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    void logout_revokedAccessEmployee_exception()
    {
        Assertions.assertThrows(Exception.class,() -> {
            employeeFacade.login(employeeId, password);
        employee.setHasAccess(false);
        employeeFacade.logout(employeeId);
        });
    }


    @Test
    void addOrChangeAvailability_properUse1_noException()
    {
        Assertions.assertDoesNotThrow(() ->{
            int nextWeekDate = dateHelper.getNextWeekDate();
            if(dateHelper.getDayOfWeek()<=2)
            {
                addOrChangeAvailability_properUseHelper(nextWeekDate);
            }
        });
    }
    @Test
    void addOrChangeAvailability_properUse2_noException()
    {
        Assertions.assertDoesNotThrow(() ->{
            int nextNextWeekDate = dateHelper.getNextWeekDate(dateHelper.getNextWeekDate());
            addOrChangeAvailability_properUseHelper(nextNextWeekDate);
        });
    }
    private void addOrChangeAvailability_properUseHelper(int date) throws Exception {
        {
            employeeFacade.login(employeeId, password);
            boolean[][] tempArr1 = new boolean[7][2];
            boolean[][] tempArr2 = new boolean[7][2];
            boolean[][] tempArr3 = new boolean[7][2];
//            Random random = new Random();
//            for(int i =0;i<7;i++)
//            {
//                for(int j=0;j<2;j++)
//                {
//                    tempArr1[i][j] = random.nextBoolean();
//                    tempArr2[i][j] = random.nextBoolean();
//                }
//            }
//            EmployeeAvailability employeeAvailability1 = new EmployeeAvailability(tempArr1,1,1,employeeAvailabilityController,false);
//            EmployeeAvailability employeeAvailability2 = new EmployeeAvailability(tempArr2,1,1,employeeAvailabilityController,false);
//            EmployeeAvailability employeeAvailability3 = new EmployeeAvailability(tempArr3,1,1,employeeAvailabilityController,false);
            employeeFacade.addOrChangeAvailability(employeeId,date,tempArr1);
            employeeFacade.addOrChangeAvailability(employeeId,date,tempArr2);
            employeeFacade.addOrChangeAvailability(employeeId,date,tempArr3);
        }
    }
    @Test
    void addOrChangeAvailability_employeeDoesntExists_exception()
    {
        Assertions.assertThrows(Exception.class,() ->
        {
            int date = dateHelper.getNextWeekDate(dateHelper.getNextWeekDate());
            boolean[][] tempArr1 = new boolean[7][2];
           // EmployeeAvailability employeeAvailability1 = new EmployeeAvailability(tempArr1);
            employeeFacade.addOrChangeAvailability(employeeId+5,date,tempArr1);
        });
        Assertions.assertThrows(Exception.class,() ->
        {
            int date = dateHelper.getNextWeekDate(dateHelper.getNextWeekDate());
            boolean[][] tempArr1 = new boolean[7][2];
            //EmployeeAvailability employeeAvailability1 = new EmployeeAvailability(tempArr1);
            employeeFacade.addOrChangeAvailability(-1,date,tempArr1);
        });
    }
    @Test
    void addOrChangeAvailability_invalidDate_exception()
    {
        Assertions.assertThrows(Exception.class,() ->
        { //cant change because its already past tuesday
            int nextWeekDate = dateHelper.getNextWeekDate();
            if(dateHelper.getDayOfWeek()>2)
            {
                addOrChangeAvailability_invalidDateHelper(nextWeekDate);
            }
            else
            {
                throw new Exception("Irrelevant test");
            }
        });
        Assertions.assertThrows(Exception.class,() ->
        {
            int nextNextNextWeekDate = dateHelper.getNextWeekDate(dateHelper.getNextWeekDate(dateHelper.getNextWeekDate()));
            addOrChangeAvailability_invalidDateHelper(nextNextNextWeekDate);
        });
    }
    private void addOrChangeAvailability_invalidDateHelper(int date) throws Exception {
        boolean[][] tempArr1 = new boolean[7][2];
      //  EmployeeAvailability employeeAvailability1 = new EmployeeAvailability(tempArr1);
        employeeFacade.addOrChangeAvailability(employeeId,date,tempArr1);
    }
    @Test
    void addOrChangeAvailability_revokedAccessEmployee_exception()
    {
        Assertions.assertThrows(Exception.class,() ->
        {
            employeeFacade.login(employeeId, password);
            int date = dateHelper.getNextWeekDate(dateHelper.getNextWeekDate());
            boolean[][] tempArr1 = new boolean[7][2];

          //  EmployeeAvailability employeeAvailability1 = new EmployeeAvailability(tempArr1);
            employee.setHasAccess(false);
            employeeFacade.addOrChangeAvailability(employeeId,date,tempArr1);
        });
    }

    @Test
    void getWorkSchedule_employeeDoesntExists_exception()
    {
        Assertions.assertThrows(Exception.class,() -> employeeFacade.getWorkSchedule(employeeId+22,dateHelper.getNextWeekDate(dateHelper.getNextWeekDate())));
        Assertions.assertThrows(Exception.class,() -> employeeFacade.getWorkSchedule(employeeId-7,dateHelper.getNextWeekDate(dateHelper.getNextWeekDate())));
        Assertions.assertThrows(Exception.class,() -> employeeFacade.getWorkSchedule(-1,dateHelper.getNextWeekDate(dateHelper.getNextWeekDate())));
        Assertions.assertThrows(Exception.class,() -> employeeFacade.getWorkSchedule(315115261,dateHelper.getNextWeekDate(dateHelper.getNextWeekDate())));
    }
    @Test
    void getWorkSchedule_invalidDate_exception()
    {
        Assertions.assertThrows(Exception.class,() -> employeeFacade.getWorkSchedule(employeeId,-8));
        Assertions.assertThrows(Exception.class,() -> employeeFacade.getWorkSchedule(employeeId,10));
        Assertions.assertThrows(Exception.class,() -> employeeFacade.getWorkSchedule(employeeId,48484848));
        Assertions.assertThrows(Exception.class,() -> employeeFacade.getWorkSchedule(employeeId,2040505666));

    }
    @Test
    void getWorkSchedule_scheduleNotPostedForDate_exception()
    {
        Assertions.assertThrows(Exception.class,() -> employeeFacade.getWorkSchedule(employeeId,20240505));

    }


}

