package test;
import DataAccessLayer.HR.ControllerClasses.EmployeeAvailabilityController;
import DataAccessLayer.HR.ControllerClasses.EmployeeController;
import DataAccessLayer.HR.ControllerClasses.RoleController;
import DataAccessLayer.HR.ControllerClasses.WorkScheduleController;
import DataAccessLayer.HR.DTOClasses.EmployeeAvailabilityDTO;
import DataAccessLayer.HR.DTOClasses.EmployeeDTO;
import DataAccessLayer.HR.DTOClasses.RoleDTO;
import DataAccessLayer.HR.DTOClasses.WorkScheduleDTO;
import GlobalClasses.DateHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;


public class DBTests {

    private static EmployeeController employeeController;
    private static RoleController roleController;
    private static EmployeeAvailabilityController employeeAvailability;
    private static WorkScheduleController workScheduleController;
    private static DateHelper dateHelper;
    private static int date;



    @BeforeAll
    public static void beforeAll(){
        try {
            employeeController=new EmployeeController();
            roleController=new RoleController();
            workScheduleController=new WorkScheduleController();
            employeeAvailability = new EmployeeAvailabilityController();
            dateHelper=new DateHelper();
            int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);


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

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    @BeforeEach
    public void beforeEach()
    {
        try {
            employeeController.deleteAllHr();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void insertEmployee_ProperUse_insertedToDB()
    {
        try{
            EmployeeDTO employee=new EmployeeDTO(28612,true,"password",false,true,1,
                    "Full-time","Jessica simpson",20230101,123,456,789,50,5000,employeeController,true);

            employee.persist();
            List<EmployeeDTO> employees=employeeController.getAll();
            Assertions.assertFalse(employees.isEmpty());
            EmployeeDTO retrived=employees.get(0);
            Assertions.assertEquals("Jessica simpson",retrived.getName());
        }catch (Exception e) {
            Assertions.fail("Execption was thrown: " + e.getMessage());
        }



    }

    @Test
    void updateEmployeeFields_ProperUse_updatedInDB() {
        try{
            EmployeeDTO employee = new EmployeeDTO(2, true, "password", false, true, 1,
                    "Full-time", "adam parker", 20230101, 123, 456, 789, 50, 5000, employeeController, false);
            employee.persist();
            employee.updateName("Jane parker");
            employee.updatePassword("newPassword");
            List<EmployeeDTO> employeeDTOS=employeeController.getAll();
            EmployeeDTO updated=employeeDTOS.stream().filter(e->e.getId()==2).findFirst().orElse(null);
            Assertions.assertEquals("Jane parker",updated.getName());
            Assertions.assertEquals("newPassword",updated.getPassword());
        }catch(Exception e){
            Assertions.fail("Exception was thrown: " + e.getMessage());
        }

    }

    @Test
    void insertRole_noEmployeeInDB_false()
    {

        RoleDTO role = new RoleDTO(3, 0, roleController, false);
        Assertions.assertThrows(Exception.class, role::persist);
    }




    @Test
    void insertRole_alreadyInsertedSameRoleForEmployee_false()
    {
        try{
            EmployeeDTO employee=new EmployeeDTO(4,true,"password",false,true,1,"Full-time","Alice",20230101,123,456,789,50,5000,employeeController,false);
            employee.persist();
            RoleDTO role1=new RoleDTO(4,0,roleController,false);
            role1.persist();
            RoleDTO role2=new RoleDTO(4,0,roleController,false);
            Assertions.assertThrows(Exception.class,role2::persist);
        }catch (Exception e){
            Assertions.fail("Execption was thrown: "+e.getMessage());

        }
    }


    @Test
    void insertAvailability_ProperUse_insertedToDB()
    {
        try{

            EmployeeDTO employee=new EmployeeDTO(1,true,"password",false,true,1,
                    "Full-time","Jessica simpson",20230101,123,456,789,50,5000,employeeController,true);
            employee.persist();
            boolean[][] availabilityArray=new boolean[7][2];

            EmployeeAvailabilityDTO availability=new EmployeeAvailabilityDTO(1,date,availabilityArray,employeeAvailability,false);

            availability.persist();
            List<EmployeeAvailabilityDTO> availabilities=employeeAvailability.getAll();
            Assertions.assertFalse(availabilities.isEmpty());
            EmployeeAvailabilityDTO retrieved=availabilities.get(0);
            Assertions.assertEquals(1,retrieved.getEmployeeId());
            Assertions.assertEquals(date,retrieved.getDate());


        }
        catch (SQLException e)
        {
            Assertions.fail("SQLExecption was thrown: "+e.getMessage());
        }
        catch (Exception e){
            Assertions.fail("Execption was thrown "+e.getMessage());
        }


    }
    @Test
    void insertAvailability_noEmployeeInDB_false()
    {
        boolean[][] availabilityArray=new boolean[7][2];
        EmployeeAvailabilityDTO availabilty=new EmployeeAvailabilityDTO(999,date,availabilityArray,employeeAvailability,false);
        Assertions.assertThrows(Exception.class,availabilty::persist);

    }


    @Test
    void insertAvailability_alreadyExistingAvailabilityInDB_false()
    {
        try{

            EmployeeDTO employee=new EmployeeDTO(1,true,"password",false,true,1,
                    "Full-time","Jessica simpson",20230101,123,456,789,50,5000,employeeController,true);
            employee.persist();
            boolean[][] availabilityArray=new boolean[7][2];
            EmployeeAvailabilityDTO availability1=new EmployeeAvailabilityDTO(1,date,availabilityArray ,employeeAvailability,false);
            availability1.persist();
            EmployeeAvailabilityDTO availability2=new EmployeeAvailabilityDTO(1, date, availabilityArray, employeeAvailability, false);
            Assertions.assertThrows(Exception.class,availability2::persist);

        }catch (Exception e){
            Assertions.fail("Execption was thrown: "+e.getMessage());

        }

    }
    @Test
    void deleteExistingAvailability_properUse_deletedFromDB()
    {
        try {
            EmployeeDTO employee=new EmployeeDTO(1,true,"password",false,true,1,
                    "Full-time","Jessica simpson",20230101,123,456,789,50,5000,employeeController,true);
            employee.persist();
            boolean[][] availabilityArray = new boolean[7][2];
            EmployeeAvailabilityDTO availability = new EmployeeAvailabilityDTO(1, date, availabilityArray, employeeAvailability, false);
            availability.persist();
            availability.deleteAvailability();
            List<EmployeeAvailabilityDTO> availabilities = employeeAvailability.getAll();
            Assertions.assertTrue(availabilities.isEmpty());

        }catch (SQLException e){
            Assertions.fail("SQLExecption was thrown: "+e.getMessage());
        }catch (Exception e){
            Assertions.fail("Execption was thrown: "+e.getMessage());
        }

    }
    @Test
    void insertWorkSchedule_properUse_insertedToDB()
    {

        try{
            EmployeeDTO[] employeeDTOs = new EmployeeDTO[28];
            for(int i =0;i<28;i++)
            {
                employeeDTOs[i] = new EmployeeDTO(i+1,true,"abcd",false,false,
                        1,"abc","Jon" + (i+1),20240606,40,50,60,
                        60,3000,employeeController,false);
                employeeDTOs[i].persist();
            }
            int[][][] employeeIds=new int[7][2][2];
            for(int i =0;i<7;i++)
            {
                for(int j=0;j<2;j++)
                {
                    for(int k=0;k<2;k++)
                    {
                        employeeIds[i][j][k] = employeeDTOs[i*4+j*2+k].getId();
                    }
                }
            }

            WorkScheduleDTO workSchedule=new WorkScheduleDTO(1,date,employeeIds,workScheduleController,false);
            workSchedule.persist();
            List<WorkScheduleDTO> workSchedules=workScheduleController.getAll();
            Assertions.assertFalse(workSchedules.isEmpty());
            WorkScheduleDTO retrieved=workSchedules.get(0);
            Assertions.assertEquals(1,retrieved.getBranchId());
            Assertions.assertEquals(date,retrieved.getDate());
        }catch (SQLException e){
            Assertions.fail("SQLExecption was thrown: "+e.getMessage());
        }
        catch (Exception e){
            Assertions.fail("Execption was thrown: "+e.getMessage());
        }


    }
    @Test
    void insertWorkSchedule_alreadyExistingWorkScheduleInDB_false()
    {
        try{
            EmployeeDTO[] employeeDTOs = new EmployeeDTO[28];
            for(int i =0;i<28;i++)
            {
                employeeDTOs[i] = new EmployeeDTO(i+1,true,"abcd",false,false,
                        5,"abc","Jon" + (i+1),20240606,40,50,60,
                        60,3000,employeeController,false);
                employeeDTOs[i].persist();
            }
            int[][][] employeeIds=new int[7][2][2];
            for(int i =0;i<7;i++)
            {
                for(int j=0;j<2;j++)
                {
                    for(int k=0;k<2;k++)
                    {
                        employeeIds[i][j][k] = employeeDTOs[i*4+j*2+k].getId();
                    }
                }
            }
            WorkScheduleDTO workSchedule1=new WorkScheduleDTO(1,date,employeeIds,workScheduleController,false);
            workSchedule1.persist();
            WorkScheduleDTO workSchedule2=new WorkScheduleDTO(1,date,employeeIds,workScheduleController,false);
            Assertions.assertThrows(Exception.class,workSchedule2::persist);

        }catch (Exception e){
            Assertions.fail("Exception was thrown: "+e.getMessage());
        }



    }



}
