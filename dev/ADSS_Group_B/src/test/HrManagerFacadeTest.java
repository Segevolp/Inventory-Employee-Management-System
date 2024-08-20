
package test;
import DataAccessLayer.HR.ControllerClasses.EmployeeController;
import DataAccessLayer.HR.ControllerClasses.RoleController;
import DataAccessLayer.HR.ControllerClasses.WeeklyRoleConstraintsController;
import DataAccessLayer.HR.ControllerClasses.WorkScheduleController;
import DomainLayer.HR.*;
import GlobalClasses.EmployeeToSend;
import GlobalClasses.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.mockClasses.EmployeeControllerMock;
import test.mockClasses.RoleControllerMock;
import test.mockClasses.WeeklyRoleConstraintsControllerMock;
import test.mockClasses.WorkScheduleControllerMock;

import java.util.HashMap;
import java.util.List;



public class HrManagerFacadeTest {

    //For employee
    private static String password;
    private static int employee1Id;
    private static int employee2Id;
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
    private static int managerId;

    private static HRManagerFacade facade;
    private static Employee manager;
    private static Employee dummyEmployee1;
    private static Employee dummyEmployee2;

    private static RoleController roleController;
    private static WorkScheduleController workScheduleController;
    private static WeeklyRoleConstraintsController weeklyRoleConstraintsController;
    private static EmployeeController employeeController;

    @BeforeAll
    static void init() {
        password = "abcd1234";
        employee1Id = 20;
        employee2Id = 30;
        managerId = 1233;
        branchId = 5;
        name = "john doe";
        roles = new Role[]{Role.CASHIER, Role.STOREKEEPER};
        termsOfEmployment = "you work, I pay.";
        startDate = 20240607;
        bankCode = 10;
        bankBranchCode = 53;
        bankAccount = 80811;
        hourlyRate = 50;
        monthlyRate = 8000;
        try {
            roleController = new RoleControllerMock();
            workScheduleController = new WorkScheduleControllerMock();
            weeklyRoleConstraintsController = new WeeklyRoleConstraintsControllerMock();
            employeeController = new EmployeeControllerMock();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @BeforeEach
    void init_each() {
        int maxBranchId = 9;
        HashMap<Integer, Employee> employeesDict = new HashMap<>();
        HashMap<Integer, WorkSchedule>[] workScheduleHashMap = new HashMap[maxBranchId + 1];
        HashMap<Integer, HashMap<Employee, EmployeeAvailability>>[] employeesAvailabilityDict = new HashMap[maxBranchId + 1];
        HashMap<Integer, WeeklyRoleConstraints>[] weeklyRoleConstraintsDict = new HashMap[maxBranchId + 1];
        try {
            manager = new Employee(password, managerId, branchId, name, roles, termsOfEmployment, startDate, bankCode,
                    bankBranchCode, bankAccount, hourlyRate, monthlyRate,employeeController,roleController);
            dummyEmployee1 = new Employee(password, employee1Id, branchId, name, roles, termsOfEmployment, startDate, bankCode,
                    bankBranchCode, bankAccount, hourlyRate, monthlyRate,employeeController,roleController);
            dummyEmployee2 = new Employee(password, employee2Id, branchId, name, roles, termsOfEmployment, startDate, bankCode,
                    bankBranchCode, bankAccount, hourlyRate, monthlyRate,employeeController,roleController);
            manager.setHRManager(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        employeesDict.put(managerId, manager);
        try {
            facade = new HRManagerFacade(employeesDict, workScheduleHashMap, employeesAvailabilityDict, weeklyRoleConstraintsDict, maxBranchId,
                    roleController,workScheduleController,weeklyRoleConstraintsController,employeeController);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void addEmployee_wrongManagerId_exception() {
        Assertions.assertThrows(Exception.class, () -> facade.addEmployee(managerId + 123, new EmployeeToSend(dummyEmployee1),"abcd"));
        Assertions.assertThrows(Exception.class, () -> {
            facade.addEmployee(managerId,new EmployeeToSend(dummyEmployee1),"abcd");
            facade.addEmployee(employee1Id, new EmployeeToSend(dummyEmployee2),"abcd");
        });
    }

    @Test
    void addEmployee_invalidBranchId_Exception() {
        Assertions.assertThrows(Exception.class, () -> facade.addEmployee(managerId,
                new EmployeeToSend(employee1Id, -1,  termsOfEmployment,name, startDate, bankCode,
                        bankBranchCode, bankAccount, hourlyRate, monthlyRate, roles),"abcd"));
        Assertions.assertThrows(Exception.class, () -> facade.addEmployee(managerId,
                new EmployeeToSend(employee1Id, 10,  termsOfEmployment,name, startDate, bankCode,
                        bankBranchCode, bankAccount, hourlyRate, monthlyRate, roles),"abcd"));
        Assertions.assertThrows(Exception.class, () -> facade.addEmployee(managerId,
                new EmployeeToSend(employee1Id,100000,  termsOfEmployment,name, startDate, bankCode,
                        bankBranchCode, bankAccount, hourlyRate, monthlyRate, roles),"abcd"));
    }

    @Test
    void addEmployee_properUse_noException() {
        Assertions.assertDoesNotThrow(() -> {
            facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee1),"abcd");
            facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee2),"abcd");
        });
    }

    @Test
    void addEmployee_idAlreadyExists_exception() {
        Assertions.assertThrows(Exception.class, () -> {
            facade.addEmployee(managerId,
                   new EmployeeToSend(dummyEmployee1),"abcd");
            facade.addEmployee(managerId,
                    new EmployeeToSend(dummyEmployee1),"abcd");
        });
    }

    @Test
    void getEmployee_wrongManagerId_exception() {
        Assertions.assertThrows(Exception.class, () ->
        {
            facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee1),"abcd");
            facade.getEmployee(-1, employee1Id);
        });
        Assertions.assertThrows(Exception.class, () ->
        {
            facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee1),"abcd");
            facade.getEmployee(managerId + 123, employee1Id);
        });
    }

    @Test
    void getEmployee_properUse_employee() {
        Assertions.assertTrue(() ->
        {
            try {
                facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee1),"abcd");
                facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee2),"abcd");
                Employee e1 = facade.getEmployee(managerId, employee1Id);
                Employee e2 = facade.getEmployee(managerId, employee2Id);
                return e1 != null && e2 != null && e1.getId() == employee1Id && e2.getId() == employee2Id;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void getEmployee_noEmployeeWithThisId_null() {
        Assertions.assertTrue(() ->
        {
            try {
                Employee e1 = facade.getEmployee(managerId, employee1Id);
                Employee e2 = facade.getEmployee(managerId, employee2Id);
                return e1 == null && e2 == null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void getAllEmployeesIds_wrongManagerId_exception() {
        Assertions.assertThrows(Exception.class, () ->
        {
            facade.getAllEmployeesIds(managerId + 123);
        });
        Assertions.assertThrows(Exception.class, () ->
        {
            facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee1),"abcd");
            facade.getAllEmployeesIds(employee1Id);
        });
    }

    @Test
    void getAllEmployeesIds_properUse_listOfIds() {
        Assertions.assertTrue(() ->
        {
            try {
                List<Integer> temp = facade.getAllEmployeesIds(managerId);
                boolean test1 = temp.size() == 1 && temp.contains(managerId);
                facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee1),"abcd");
                temp = facade.getAllEmployeesIds(managerId);
                boolean test2 = temp.size() == 2 && temp.contains(managerId) && temp.contains(employee1Id);
                facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee2),"abcd");
                temp = facade.getAllEmployeesIds(managerId);
                boolean test3 = temp.size() == 3 && temp.contains(managerId) && temp.contains(employee1Id)
                        && temp.contains(employee2Id);
                return test1 && test2 && test3;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void assignRole_wrongManagerId_exception() {
        Assertions.assertThrows(Exception.class, () ->
        {
            facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee1),"abcd");
            facade.assignRole(managerId + 123, employee1Id, Role.MANAGER);
        });
        Assertions.assertThrows(Exception.class, () ->
        {
            facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee1),"abcd");
            facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee2),"abcd");
            facade.assignRole(employee1Id, employee2Id, Role.MANAGER);
        });

    }

    @Test
    void assignRole_properUse_assignRoleToEmployee() {
        Assertions.assertTrue(() ->
        {
            try {
                facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee1),"abcd");
                facade.assignRole(managerId, employee1Id, Role.MANAGER);
                facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee2),"abcd");
                facade.assignRole(managerId, employee2Id, Role.MANAGER);
                return facade.getEmployee(managerId, employee1Id).hasRole(Role.MANAGER) &&
                        facade.getEmployee(managerId, employee2Id).hasRole(Role.MANAGER);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void assignRole_alreadyHasRole_exception() {
        Assertions.assertThrows(Exception.class, () ->
        {
            facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee1),"abcd");
            facade.assignRole(managerId, employee1Id, Role.MANAGER);
            facade.assignRole(managerId, employee1Id, Role.MANAGER);
        });
        Assertions.assertThrows(Exception.class, () ->
        {
            facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee1),"abcd");
            facade.assignRole(managerId, employee1Id, Role.STOREKEEPER);

        });
    }

    @Test
    void assignRole_noEmployeesWithThisId_exception() {
        Assertions.assertThrows(Exception.class, () ->
        {
            facade.assignRole(managerId, employee1Id, Role.MANAGER);
        });
    }

    @Test
    void removeRole_wrongManagerId_exception() {
        Assertions.assertThrows(Exception.class, () ->
        {
            facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee1),"abcd");
            facade.removeRole(managerId + 123, employee1Id, Role.STOREKEEPER);
        });
        Assertions.assertThrows(Exception.class, () ->
        {
            facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee1),"abcd");
            facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee2),"abcd");
            facade.assignRole(employee1Id, employee2Id, Role.STOREKEEPER);
        });
    }

    @Test
    void removeRole_properUse_removeRoleFromEmployeeRoles() {
        Assertions.assertTrue(() ->
        {
            try {
                facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee1),"abcd");
                facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee2),"abcd");
                boolean test1 = facade.getEmployee(managerId, employee1Id).hasRole(Role.STOREKEEPER) &&
                        facade.getEmployee(managerId, employee2Id).hasRole(Role.CASHIER);
                facade.removeRole(managerId, employee1Id, Role.STOREKEEPER);
                facade.removeRole(managerId, employee2Id, Role.CASHIER);
                boolean test2 = !(facade.getEmployee(managerId, employee1Id).hasRole(Role.STOREKEEPER) &&
                        facade.getEmployee(managerId, employee2Id).hasRole(Role.CASHIER));
                return test1 && test2;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void removeRole_noEmployeesWithThisId_exception() {
        Assertions.assertThrows(Exception.class, () ->
        {
            facade.removeRole(managerId, employee1Id, Role.STOREKEEPER);
        });
    }

    @Test
    void removeRole_employeeDoesntHasRole_exception() {
        Assertions.assertThrows(Exception.class, () ->
        {
            facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee1),"abcd");
            facade.removeRole(managerId, employee1Id, Role.STOREKEEPER);
            facade.removeRole(managerId, employee1Id, Role.STOREKEEPER);
        });
        Assertions.assertThrows(Exception.class, () ->
        {
            facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee1),"abcd");
            facade.removeRole(managerId, employee1Id, Role.MANAGER);
        });
    }

    @Test
    void removeAccess_wrongManagerId_exception() {
        Assertions.assertThrows(Exception.class, () ->
        {
            facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee1),"abcd");
            facade.removeAccess(managerId + 123, employee1Id);
        });
        Assertions.assertThrows(Exception.class, () ->
        {
            facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee1),"abcd");
            facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee2),"abcd");
            facade.removeAccess(employee1Id, employee2Id);
        });
    }

    @Test
    void removeAccess_properUse_employeeLoseAccess() {
        Assertions.assertTrue(() ->
        {
            try {

                facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee1),"abcd");
                facade.removeAccess(managerId, employee1Id);
                return !facade.getEmployee(managerId, dummyEmployee1.getId()).getHasAccess();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

    @Test
    void removeAccess_alreadyRemovedAccess_exception() {
        Assertions.assertThrows(Exception.class, () ->
        {
            facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee1),"abcd");
            facade.removeAccess(managerId, employee1Id);
            facade.removeAccess(managerId, employee1Id);
        });
    }

    @Test
    void reinstateAccess_wrongManagerId_exception() {
        Assertions.assertThrows(Exception.class, () ->
        {
            facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee1),"abcd");
            facade.reinstateAccess(managerId + 123, employee1Id);
        });
        Assertions.assertThrows(Exception.class, () ->
        {
            facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee1),"abcd");
            facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee2),"abcd");
            facade.reinstateAccess(employee1Id, employee2Id);
        });
    }

    @Test
    void reinstateAccess_properUse_employeeGetsAccessBack() {
        Assertions.assertTrue(() ->
        {
            try {
                facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee1),"abcd");
                facade.removeAccess(managerId, employee1Id);
                boolean f = !facade.getEmployee(managerId, dummyEmployee1.getId()).getHasAccess();
                facade.reinstateAccess(managerId, employee1Id);
                return f && facade.getEmployee(managerId, dummyEmployee1.getId()).getHasAccess();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void reinstateAccess_alreadyHasAccess_exception() {
        Assertions.assertThrows(Exception.class, () ->
        {
            facade.addEmployee(managerId, new EmployeeToSend(dummyEmployee1),"abcd");
            facade.reinstateAccess(managerId, employee1Id);
        });
    }

    @Test
    void getEmployeesAvailability_wrongManagerId_exception() {
        Assertions.assertThrows(Exception.class, () ->
        {
            facade.getEmployeesAvailability(managerId + 84, 20240505, branchId);

        });
        Assertions.assertThrows(Exception.class, () ->
        {
            facade.getEmployeesAvailability(managerId - 52, 20240505, branchId);

        });
        Assertions.assertThrows(Exception.class, () ->
        {
            facade.getEmployeesAvailability(-20, 20240505, branchId);

        });
    }

    @Test
    void getEmployeesAvailability_invalidDate_exception() {
        Assertions.assertThrows(Exception.class, () ->
        {
            facade.getEmployeesAvailability(managerId, 2024, branchId);

        });
        Assertions.assertThrows(Exception.class, () ->
        {
            facade.getEmployeesAvailability(managerId, 222222222, branchId);

        });
        Assertions.assertThrows(Exception.class, () ->
        {
            facade.getEmployeesAvailability(managerId, -8, branchId);

        });
    }

    @Test
    void getEmployeesAvailability_invalidBranchId_exception() {
        Assertions.assertThrows(Exception.class, () ->
        {
            facade.getEmployeesAvailability(managerId, 20240505, -1);

        });
    }

}
