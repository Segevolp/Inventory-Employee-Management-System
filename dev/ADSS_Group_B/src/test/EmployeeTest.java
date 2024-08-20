
package test;
import DataAccessLayer.HR.ControllerClasses.EmployeeController;
import DataAccessLayer.HR.ControllerClasses.RoleController;
import DomainLayer.HR.Employee;
import GlobalClasses.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Arrays;

public class EmployeeTest {
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
    private static EmployeeController employeeController = null;
    private static RoleController roleController = null;

    @BeforeAll
    static void init()
    {
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
    }



    @Test
    void builder_properUse_noException() {
        Assertions.assertDoesNotThrow(() -> new Employee(password,employeeId,branchId,name,roles,termsOfEmployment,startDate,bankCode,
                bankBranchCode,bankAccount,hourlyRate,monthlyRate,employeeController,roleController));
    }


    @Test
    void builder_invalidBankDetails_Exception()
    {
        Assertions.assertThrows(Exception.class,() -> new Employee(password,employeeId,branchId,name,roles,termsOfEmployment,startDate,-1,
                bankBranchCode,bankAccount,hourlyRate,monthlyRate,employeeController,roleController));
        Assertions.assertThrows(Exception.class,() -> new Employee(password,employeeId,branchId,name,roles,termsOfEmployment,startDate,bankCode,
                -1,bankAccount,hourlyRate,monthlyRate,employeeController,roleController));
        Assertions.assertThrows(Exception.class,() -> new Employee(password,employeeId,branchId,name,roles,termsOfEmployment,startDate,bankCode,
                bankBranchCode,-1,hourlyRate,monthlyRate,employeeController,roleController));
    }


    @Test
    void login_properUse_true()
    {
        Assertions.assertTrue(() -> {
            try {
                return (new Employee(password,employeeId,branchId,name,roles,termsOfEmployment,startDate,bankCode,
                        bankBranchCode,bankAccount,hourlyRate,monthlyRate,employeeController,roleController)).login(password);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    void login_wrongPassword_false()
    {
        Assertions.assertFalse(() -> {
            try {
                return (new Employee(password,employeeId,branchId,name,roles,termsOfEmployment,startDate,bankCode,
                        bankBranchCode,bankAccount,hourlyRate,monthlyRate,employeeController,roleController)).login(password+"k");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    void login_alreadyLoggedIn_Exception()
    {
        Assertions.assertThrows(Exception.class,() -> {
            Employee employee = new Employee(password,employeeId,branchId,name,roles,termsOfEmployment,startDate,bankCode,
                        bankBranchCode,bankAccount,hourlyRate,monthlyRate,employeeController,roleController);
            employee.login(password);
            employee.login(password);
        });
    }
    @Test
    void logout_properUse_noException()
    {
        Assertions.assertDoesNotThrow(() -> {
            Employee employee = new Employee(password,employeeId,branchId,name,roles,termsOfEmployment,startDate,bankCode,
                    bankBranchCode,bankAccount,hourlyRate,monthlyRate,employeeController,roleController);
            employee.login(password);
            employee.logout();
        });
    }
    @Test
    void logout_notLoggedIn_Exception()
    {
        Assertions.assertThrows(Exception.class,() -> {
            Employee employee = new Employee(password,employeeId,branchId,name,roles,termsOfEmployment,startDate,bankCode,
                    bankBranchCode,bankAccount,hourlyRate,monthlyRate,employeeController,roleController);
            employee.logout();
        });
    }
    @Test
    void hasRole_dontHasRole_false()
    {
        Assertions.assertTrue(() -> {
            try {
                Role[] r = new Role[]{Role.CASHIER};
                Employee employee = new Employee(password,employeeId,branchId,name,r,termsOfEmployment,startDate,bankCode,
                        bankBranchCode,bankAccount,hourlyRate,monthlyRate,employeeController,roleController);
                Role[] temp = employee.getRoles();
                return Arrays.stream(temp).noneMatch((x) -> x.equals(Role.MANAGER) || x.equals(Role.STOREKEEPER));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        Assertions.assertTrue(() -> {
            try {
                Role[] r = new Role[]{Role.CASHIER,Role.MANAGER};
                Employee employee = new Employee(password,employeeId,branchId,name,r,termsOfEmployment,startDate,bankCode,
                        bankBranchCode,bankAccount,hourlyRate,monthlyRate,employeeController,roleController);
                Role[] temp = employee.getRoles();
                return Arrays.stream(temp).noneMatch((x) -> x.equals(Role.STOREKEEPER));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    void hasRole_hasRole_true()
    {
        Assertions.assertTrue(() -> {
            try {
                Role[] r = new Role[]{Role.CASHIER};
                Employee employee = new Employee(password,employeeId,branchId,name,r,termsOfEmployment,startDate,bankCode,
                        bankBranchCode,bankAccount,hourlyRate,monthlyRate,employeeController,roleController);
                Role[] temp = employee.getRoles();
                return Arrays.stream(temp).filter((x) -> x.equals(Role.CASHIER)).count()==1;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        Assertions.assertTrue(() -> {
            try {
                Role[] r = new Role[]{Role.CASHIER,Role.MANAGER};
                Employee employee = new Employee(password,employeeId,branchId,name,r,termsOfEmployment,startDate,bankCode,
                        bankBranchCode,bankAccount,hourlyRate,monthlyRate,employeeController,roleController);
                Role[] temp = employee.getRoles();
                return Arrays.stream(temp).filter((x) -> x.equals(Role.CASHIER)).count()==1&&
                        Arrays.stream(temp).filter((x) -> x.equals(Role.MANAGER)).count()==1;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    void addRole_properUse_hasRoleAfter()
    {
        Assertions.assertTrue(() -> {
            try {
                Role[] r = new Role[]{};
                Employee employee = new Employee(password,employeeId,branchId,name,r,termsOfEmployment,startDate,bankCode,
                        bankBranchCode,bankAccount,hourlyRate,monthlyRate,employeeController,roleController);
                Role[] temp = employee.getRoles();
                boolean test1= temp.length == 0;
                employee.addRole(Role.STOREKEEPER);
                temp = employee.getRoles();
                boolean test2 = temp.length==1&&temp[0].equals(Role.STOREKEEPER);
                employee.addRole(Role.MANAGER);
                temp = employee.getRoles();
                boolean test3 = temp.length==2&& ((temp[0].equals(Role.STOREKEEPER)&&temp[1].equals(Role.MANAGER))
                        ||(temp[0].equals(Role.MANAGER)&&temp[1].equals(Role.STOREKEEPER)));
                return test1&&test2&&test3;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    void addRole_alreadyHasRole_Exception()
    {
        Assertions.assertThrows(Exception.class, () -> {
            try {
                Role[] r = new Role[]{};
                Employee employee = new Employee(password,employeeId,branchId,name,r,termsOfEmployment,startDate,bankCode,
                        bankBranchCode,bankAccount,hourlyRate,monthlyRate,employeeController,roleController);
                employee.addRole(Role.CASHIER);
                employee.addRole(Role.CASHIER);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        Assertions.assertThrows(Exception.class, () -> {
            try {
                Role[] r = new Role[]{Role.CASHIER};
                Employee employee = new Employee(password,employeeId,branchId,name,r,termsOfEmployment,startDate,bankCode,
                        bankBranchCode,bankAccount,hourlyRate,monthlyRate,employeeController,roleController);
                employee.addRole(Role.CASHIER);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    void removeRole_properUse_removedRole()
    {
        Assertions.assertTrue(() -> {
            try {
                Role[] r = new Role[]{Role.CASHIER,Role.MANAGER,Role.STOREKEEPER};
                Employee employee = new Employee(password,employeeId,branchId,name,r,termsOfEmployment,startDate,bankCode,
                        bankBranchCode,bankAccount,hourlyRate,monthlyRate,employeeController,roleController);
                boolean test1 = employee.getRoles().length==3;
                employee.removeRole(Role.CASHIER);
                Role[] temp = employee.getRoles();
                boolean test2 = temp.length==2&& ((temp[0].equals(Role.STOREKEEPER)&&temp[1].equals(Role.MANAGER))
                        ||(temp[0].equals(Role.MANAGER)&&temp[1].equals(Role.STOREKEEPER)));
                employee.removeRole(Role.MANAGER);
                temp = employee.getRoles();
                boolean test3 = temp.length==1 && temp[0].equals(Role.STOREKEEPER);
                employee.removeRole(Role.STOREKEEPER);
                boolean test4 = employee.getRoles().length==0;
                return test1&&test2&&test3&&test4;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }
    @Test
    void removeRole_doesntHasRole_Exception()
    {
        Assertions.assertThrows(Exception.class, () -> {
            try {
                Role[] r = new Role[]{Role.CASHIER};
                Employee employee = new Employee(password,employeeId,branchId,name,r,termsOfEmployment,startDate,bankCode,
                        bankBranchCode,bankAccount,hourlyRate,monthlyRate,employeeController,roleController);
                employee.removeRole(Role.CASHIER);
                employee.removeRole(Role.CASHIER);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        Assertions.assertThrows(Exception.class, () -> {
            try {
                Role[] r = new Role[]{Role.CASHIER};
                Employee employee = new Employee(password,employeeId,branchId,name,r,termsOfEmployment,startDate,bankCode,
                        bankBranchCode,bankAccount,hourlyRate,monthlyRate,employeeController,roleController);
                employee.removeRole(Role.MANAGER);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        Assertions.assertThrows(Exception.class, () -> {
            try {
                Role[] r = new Role[]{Role.CASHIER};
                Employee employee = new Employee(password,employeeId,branchId,name,r,termsOfEmployment,startDate,bankCode,
                        bankBranchCode,bankAccount,hourlyRate,monthlyRate,employeeController,roleController);
                employee.removeRole(Role.STOREKEEPER);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }


}

