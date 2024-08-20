//package test;
//import DomainLayer.HR.EmployeeAvailability;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import java.util.Random;
//
//public class EmployeeAvailabilityTest {
//
//
//    @Test
//    void builder_properUse_noException()
//    {
//        boolean[][] arr1 = new boolean[7][2];
//        boolean[][] arr2 = new boolean[7][2];
//        boolean[][] arr3 = new boolean[7][2];
//        Random random = new Random();
//        for(int i =0;i<7;i++)
//        {
//            for(int j = 0;j<2;j++)
//            {
//                arr1[i][j] = random.nextBoolean();
//                arr2[i][j] = random.nextBoolean();
//                arr3[i][j] = random.nextBoolean();
//            }
//        }
//        Assertions.assertDoesNotThrow(() -> new EmployeeAvailability(arr1));
//        Assertions.assertDoesNotThrow(() -> new EmployeeAvailability(arr2));
//        Assertions.assertDoesNotThrow(() -> new EmployeeAvailability(arr3));
//    }
//    @Test
//    void builder_wrongDaysSize_exception()
//    {
//        boolean[][] arr1 = new boolean[6][2];
//        boolean[][] arr2 = new boolean[1][2];
//        boolean[][] arr3 = new boolean[8][2];
//        boolean[][] arr4 = new boolean[10][2];
//        boolean[][] arr5 = new boolean[20][2];
//        Assertions.assertThrows(Exception.class,() -> new EmployeeAvailability(arr1));
//        Assertions.assertThrows(Exception.class,() -> new EmployeeAvailability(arr2));
//        Assertions.assertThrows(Exception.class,() -> new EmployeeAvailability(arr3));
//        Assertions.assertThrows(Exception.class,() -> new EmployeeAvailability(arr4));
//        Assertions.assertThrows(Exception.class,() -> new EmployeeAvailability(arr5));
//
//    }
//    @Test
//    void builder_WrongShiftsSize_exception()
//    {
//        boolean[][] arr1 = new boolean[7][3];
//        boolean[][] arr2 = new boolean[7][1];
//        boolean[][] arr3 = new boolean[7][0];
//        boolean[][] arr4 = new boolean[7][];
//        for(int i =0;i<6;i++)
//        {
//            arr4[i] = new boolean[2];
//        }
//        arr4[6] = new boolean[3];
//        Assertions.assertThrows(Exception.class,() -> new EmployeeAvailability(arr1));
//        Assertions.assertThrows(Exception.class,() -> new EmployeeAvailability(arr2));
//        Assertions.assertThrows(Exception.class,() -> new EmployeeAvailability(arr3));
//        Assertions.assertThrows(Exception.class,() -> new EmployeeAvailability(arr4));
//    }
//    @Test
//    void builder_nullSomewhereInArray_exception()
//    {
//        boolean[][] arr1 = null;
//        boolean[][] arr2 = new boolean[7][];
//        boolean[][] arr3 = new boolean[7][];
//        for(int i =0;i<6;i++)
//        {
//            arr3[i] = new boolean[2];
//        }
//        Assertions.assertThrows(Exception.class,() -> new EmployeeAvailability(arr1));
//        Assertions.assertThrows(Exception.class,() -> new EmployeeAvailability(arr2));
//        Assertions.assertThrows(Exception.class,() -> new EmployeeAvailability(arr3));
//    }
//}
