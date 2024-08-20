//package test;
//import DataAccessLayer.HR.ControllerClasses.WeeklyRoleConstraintsController;
//import Role;
//import DomainLayer.HR.WeeklyRoleConstraints;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import test.mockClasses.WeeklyRoleConstraintsControllerMock;
//
//import java.util.Random;
//
//public class WeeklyRoleConstraintsTest {
//    private static WeeklyRoleConstraintsController weeklyRoleConstraintsController;
//
//    @BeforeAll
//    static void beforeAll()
//    {
//        try {
//            weeklyRoleConstraintsController = new WeeklyRoleConstraintsControllerMock();
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Test
//    void builder_properUse_noException()
//    {
//        Role[][][] arr1 = new Role[7][2][1];
//        Role[][][] arr2 = new Role[7][2][2];
//        Role[][][] arr3 = new Role[7][2][5];
//        Random random = new Random();
//        Role[] rolesValues = Role.values();
//        for(int i=0;i<7;i++)
//        {
//            for(int j=0;j<2;j++)
//            {
//                arr1[i][j][0] = rolesValues[random.nextInt(rolesValues.length)];
//                arr2[i][j][0] = rolesValues[random.nextInt(rolesValues.length)];
//                arr2[i][j][1] = rolesValues[random.nextInt(rolesValues.length)];
//                arr3[i][j][0] = rolesValues[random.nextInt(rolesValues.length)];
//                arr3[i][j][1] = rolesValues[random.nextInt(rolesValues.length)];
//                arr3[i][j][2] = rolesValues[random.nextInt(rolesValues.length)];
//                arr3[i][j][3] = rolesValues[random.nextInt(rolesValues.length)];
//                arr3[i][j][4] = rolesValues[random.nextInt(rolesValues.length)];
//            }
//        }
//        Assertions.assertDoesNotThrow(() -> new WeeklyRoleConstraints(arr1,1,1,weeklyRoleConstraintsController,false));
//        Assertions.assertDoesNotThrow(() -> new WeeklyRoleConstraints(arr2,1,weeklyRoleConstraintsController,false));
//        Assertions.assertDoesNotThrow(() -> new WeeklyRoleConstraints(arr3,1,weeklyRoleConstraintsController,false));
//    }
//    @Test
//    void builder_wrongDaysSize_exception()
//    {
//        Role[][][] arr1 = new Role[1][2][1];
//        Role[][][] arr2 = new Role[5][2][1];
//        Role[][][] arr3 = new Role[8][2][0];
//        Random random = new Random();
//        Role[] rolesValues = Role.values();
//        arr1[0][0][0] = rolesValues[random.nextInt(rolesValues.length)];
//        arr1[0][1][0] = rolesValues[random.nextInt(rolesValues.length)];
//        for(int i =0;i<5;i++)
//        {
//            for (int j=0;j<2;j++)
//            {
//                arr2[i][j][0] = rolesValues[random.nextInt(rolesValues.length)];
//            }
//        }
//        Assertions.assertThrows(Exception.class,() -> new WeeklyRoleConstraints(arr1,1,1,weeklyRoleConstraintsController,false));
//        Assertions.assertThrows(Exception.class,() -> new WeeklyRoleConstraints(arr2,1,1,weeklyRoleConstraintsController,false));
//        Assertions.assertThrows(Exception.class,() -> new WeeklyRoleConstraints(arr3,1,1,weeklyRoleConstraintsController,false));
//    }
//    @Test
//    void builder_WrongShiftsSize_exception()
//    {
//        Role[][][] arr1 = new Role[7][0][0];
//        Role[][][] arr2 = new Role[7][1][2];
//        Role[][][] arr3 = new Role[7][3][1];
//        Random random = new Random();
//        Role[] rolesValues = Role.values();
//        for(int i =0;i<7;i++)
//        {
//            arr2[i][0][0] =  rolesValues[random.nextInt(rolesValues.length)];
//            arr2[i][0][1] =  rolesValues[random.nextInt(rolesValues.length)];
//            for (int j=0;j<3;j++)
//            {
//                arr3[i][j][0] = rolesValues[random.nextInt(rolesValues.length)];
//            }
//        }
//        Assertions.assertThrows(Exception.class,() -> new WeeklyRoleConstraints(arr1,1,1,weeklyRoleConstraintsController,false));
//        Assertions.assertThrows(Exception.class,() -> new WeeklyRoleConstraints(arr2,1,1,weeklyRoleConstraintsController,false));
//        Assertions.assertThrows(Exception.class,() -> new WeeklyRoleConstraints(arr3,1,1,weeklyRoleConstraintsController,false));
//    }
//    @Test
//    void builder_nullSomewhereInArray_exception()
//    {
//        Role[][][] arr1 = null;
//        Role[][][] arr2 = new Role[7][][];
//        Role[][][] arr3 = new Role[7][2][];
//        Role[][][] arr4 = new Role[7][2][1];
//        Random random = new Random();
//        Role[] rolesValues = Role.values();
//        for(int i =0;i<7;i++)
//        {
//            arr3[i][0] = new Role[1];
//            arr3[i][1] = new Role[1];
//            arr3[i][0][0] = rolesValues[random.nextInt(rolesValues.length)];
//            arr3[i][1][0] = rolesValues[random.nextInt(rolesValues.length)];
//            arr4[i][0][0] = rolesValues[random.nextInt(rolesValues.length)];
//            arr4[i][1][0] = rolesValues[random.nextInt(rolesValues.length)];
//        }
//        arr3[6][1] = null;
//        arr4[6][1][0] = null;
//        Assertions.assertThrows(Exception.class,() -> new WeeklyRoleConstraints(arr1,1,1,weeklyRoleConstraintsController,false));
//        Assertions.assertThrows(Exception.class,() -> new WeeklyRoleConstraints(arr2,1,1,weeklyRoleConstraintsController,false));
//        Assertions.assertThrows(Exception.class,() -> new WeeklyRoleConstraints(arr3,1,1,weeklyRoleConstraintsController,false));
//        Assertions.assertThrows(Exception.class,() -> new WeeklyRoleConstraints(arr4,1,1,weeklyRoleConstraintsController,false));
//    }
//    @Test
//    void builder_containsEmptyRoleArray_noException()
//    {
//        Role[][][] arr1 = new Role[7][2][0];
//        Role[][][] arr2 = new Role[7][2][];
//        Random random = new Random();
//        Role[] rolesValues = Role.values();
//        for(int i =0;i<7;i++)
//        {
//            for (int j=0;j<2;j++)
//            {
//                int ranSize = random.nextInt(6);
//                arr2[i][j] = new Role[ranSize];
//                for(int k=0;k<ranSize;k++)
//                {
//                    arr2[i][j][k] = rolesValues[random.nextInt(rolesValues.length)];
//                }
//            }
//        }
//        arr2[6][1] = new Role[0];
//        Assertions.assertDoesNotThrow(() -> new WeeklyRoleConstraints(arr1,1,1,weeklyRoleConstraintsController,false));
//        Assertions.assertDoesNotThrow(() -> new WeeklyRoleConstraints(arr2,1,1,weeklyRoleConstraintsController,false));
//
//
//    }
//    @Test
//    void getDefaultConstraints_containsManagerStoreKeeperCashierAtEachShift()
//    {
//        WeeklyRoleConstraints defaultConstraints = WeeklyRoleConstraints.getDefaultConstraints();
//        int rolesAmount = Role.values().length;
//        int[] shouldBeArr = new int[rolesAmount];
//        for(int i =0;i<rolesAmount;i++)
//        {
//            shouldBeArr[i] = 1;
//        }
//        for(int i =0;i<7;i++)
//        {
//            for(int j=0;j<2;j++)
//            {
//                try {
//                    Role[] roles = defaultConstraints.getConstraints(i,j);
//                    int[] counter = new int[rolesAmount];
//                    for(Role r : roles)
//                    {
//                        counter[r.ordinal()] ++;
//                    }
//                    Assertions.assertArrayEquals(shouldBeArr,counter);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }
//    }
//    @Test
//    void getDefaultConstraints_changeGivenValue_defaultDoesntChange()
//    {
//        Role[] roles = Role.values();
//        WeeklyRoleConstraints defaultConstraints = WeeklyRoleConstraints.getDefaultConstraints();
//        try {
//            Role[] constraints1 =  defaultConstraints.getConstraints(0,1);
//            Role[] constraints2 =  defaultConstraints.getConstraints(0,1);
//            Assertions.assertNotEquals(constraints1,constraints2);
//            Random random = new Random();
//            Role r = roles[random.nextInt(roles.length)];
//            while(r.equals(constraints1[0]))
//            {
//                r = roles[random.nextInt(roles.length)];
//            }
//            constraints1[0] = r;
//            constraints2 =  defaultConstraints.getConstraints(0,1);
//            Assertions.assertNotEquals(constraints1[0],constraints2[0]);
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//    @Test
//    void getConstraints_returnsExpectedConstraints()
//    {
//        Role[][][] arr1 = new Role[7][2][2];
//        Random random = new Random();
//        Role[] rolesValues = Role.values();
//        for(int i=0;i<7;i++)
//        {
//            for(int j=0;j<2;j++)
//            {
//                arr1[i][j][0] = rolesValues[random.nextInt(rolesValues.length)];
//                arr1[i][j][1] = rolesValues[random.nextInt(rolesValues.length)];
//            }
//        }
//        try {
//            WeeklyRoleConstraints weeklyRoleConstraints = new WeeklyRoleConstraints(arr1,1,1,weeklyRoleConstraintsController,false);
//            for(int i=0;i<7;i++)
//            {
//                for(int j=0;j<2;j++)
//                {
//                    Role[] r = weeklyRoleConstraints.getConstraints(i,j);
//                    Assertions.assertEquals(arr1[i][j][0],r[0]);
//                    Assertions.assertEquals(arr1[i][j][1],r[1]);
//                }
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//
//}
