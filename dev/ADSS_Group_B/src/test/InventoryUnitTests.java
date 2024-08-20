package test;

import DomainLayer.Inventory.CallBack;
import ServiceLayer.Inventory.*;
import ServiceLayer.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class InventoryUnitTests {

    private InventoryService inventoryService;

    // Mock callback for testing
    private CallBack callBack1 = new CallBack() {
        @Override
        public void call(String msg) {
            // TEST uneccasery
        }
    };
    private CallBack callBack2 = new CallBack() {
        @Override
        public void call(String msg) {
            // TEST uneccasery
        }
    };

    @Before
    public void setUp() throws ClassNotFoundException {
        inventoryService = new InventoryService(callBack1, callBack2);
    }
    @Test
    public void testAddProduct_Successful() {
        Response response = inventoryService.addProduct(123, 10, "Product1", "Shelf", "Manufacturer", 10.0, 20.0, new String[]{"Category"}, 5);
        assertNull(response.getErrorMsg());
        inventoryService.deleteAll();
    }

    @Test
    public void testAddProduct_DuplicateProduct() {
        inventoryService.addProduct(123, 10, "Product1", "Shelf", "Manufacturer", 10.0, 20.0, new String[]{"Category"}, 5);
        Response response = inventoryService.addProduct(123, 10, "Product1", "Shelf", "Manufacturer", 10.0, 20.0, new String[]{"Category"}, 5);
        assertNotNull(response.getErrorMsg());
        inventoryService.deleteAll();

    }
    @Test
    public void testAddItem_Successful() {
        // Assuming product exists in the category
        inventoryService.addProduct(123, 10, "Product1", "Shelf", "Manufacturer", 10.0, 20.0, new String[]{"Category1","Category2","Category3"}, 5);
        Response response = inventoryService.addItem(123,  456, new Date(), true);
        assertNull(response.getErrorMsg());
        inventoryService.deleteAll();

    }

    @Test
    public void testAddItem_ProductNotExists() {
        Response response = inventoryService.addItem(123, 456, new Date(), true);
        assertNotNull(response.getErrorMsg());
        inventoryService.deleteAll();

    }
    @Test
    public void testAddItem_ItemAlreadyExists() {
        inventoryService.addProduct(123, 10, "Product1", "Shelf", "Manufacturer", 10.0, 20.0, new String[]{"Category"}, 5);
        inventoryService.addItem(123,  456, new Date(), true);
        Response response = inventoryService.addItem(123,  456, new Date(), true);
        assertNotNull(response.getErrorMsg());
        inventoryService.deleteAll();

    }
    @Test
    public void testRemoveItem_Successful() {
        inventoryService.addProduct(123, 10, "Product1", "Shelf", "Manufacturer", 10.0, 20.0, new String[]{"Category1","Category2","Category3"}, 5);
        inventoryService.addItem(123,  456, new Date(), true);
        Response response = inventoryService.removeItem(123, 456);
        assertNull(response.getErrorMsg());
        inventoryService.deleteAll();
    }

    @Test
    public void testRemoveItem_ItemNotExists() {
        Response response = inventoryService.removeItem(123, 456);
        assertNotNull(response.getErrorMsg());
        inventoryService.deleteAll();

    }


    @Test
    public void testMoveItemToShelf_Successful() {
        inventoryService.addProduct(123, 10, "Product1", "Storage", "Manufacturer", 10.0, 20.0, new String[]{"Category1","Category2","Category3"}, 5);
        inventoryService.addItem(123,  456, new Date(), true);
        Response response = inventoryService.moveItemToShelf(123, 1);
        assertNull(response.getErrorMsg());
        inventoryService.deleteAll();

    }

    @Test
    public void testMoveItemToShelf_ItemNotExists() {
        Response response = inventoryService.moveItemToShelf(123, 1);
        assertNotNull(response.getErrorMsg());
        inventoryService.deleteAll();

    }
    @Test
    public void testMoveItemToShelf_InvalidAmount() {
        inventoryService.addProduct(123, 10, "Product1", "Storage", "Manufacturer", 10.0, 20.0, new String[]{"Category"}, 5);
        inventoryService.addItem(123, 456, new Date(), true);
        Response response = inventoryService.moveItemToShelf(123, 3);
        assertNotNull(response.getErrorMsg());
        inventoryService.deleteAll();

    }

    @Test
    public void testReportDefective_Successful() {
        inventoryService.addProduct(123, 10, "Product1", "Storage", "Manufacturer", 10.0, 20.0, new String[]{"Category1","Category2","Category3"}, 5);
        inventoryService.addItem(123,  456, new Date(), false);
        Response response = inventoryService.reportDefective(123, 1);
        assertNull(response.getErrorMsg());
        inventoryService.deleteAll();

    }

    @Test
    public void testReportDefective_ItemNotExists() {
        Response response = inventoryService.reportDefective(123, 2);
        assertNotNull(response.getErrorMsg());
        inventoryService.deleteAll();

    }
    @Test
    public void testReportDefective_InvalidAmount() {
        inventoryService.addProduct(123, 10, "Product1", "Storage", "Manufacturer", 10.0, 20.0, new String[]{"Category1","Category2","Category3"}, 5);
        inventoryService.addItem(123,  456, new Date(), false);
        Response response = inventoryService.reportDefective(123, -1);
        assertNotNull(response.getErrorMsg());
        inventoryService.deleteAll();

    }
    ///// added part 2 tests
    @Test
    public void testRemoveProduct_Successful() {
        inventoryService.addProduct(123, 10, "Product1", "Shelf", "Manufacturer", 10.0, 20.0, new String[]{"Category1","Category2","Category3"}, 5);
        Response response = inventoryService.removeProduct(123, new String[]{"Category1","Category2","Category3"});
        assertNull(response.getErrorMsg());
        inventoryService.deleteAll();
    }

    @Test
    public void testRemoveProduct_NotExists() {
        Response response = inventoryService.removeProduct(123, new String[]{"Category1","Category2","Category3"});
        assertNotNull(response.getErrorMsg());
        inventoryService.deleteAll();
    }

    @Test
    public void testMoveItemToStorage_Successful() {
        inventoryService.addProduct(123, 10, "Product1", "Shelf", "Manufacturer", 10.0, 20.0, new String[]{"Category1","Category2","Category3"}, 5);
        inventoryService.addItem(123, 456, new Date(), false);
        Response response = inventoryService.moveItemToStorage(123, 1);
        assertNull(response.getErrorMsg());
        inventoryService.deleteAll();
    }

    @Test
    public void testMoveItemToStorage_ItemNotExists() {
        Response response = inventoryService.moveItemToStorage(123, 1);
        assertNotNull(response.getErrorMsg());
        inventoryService.deleteAll();
    }

    @Test
    public void testMoveItemToStorage_InvalidAmount() {
        inventoryService.addProduct(123, 10, "Product1", "Shelf", "Manufacturer", 10.0, 20.0, new String[]{"Category1","Category2","Category3"}, 5);
        inventoryService.addItem(123, 456, new Date(), false);
        Response response = inventoryService.moveItemToStorage(123, 3);
        assertNotNull(response.getErrorMsg());
        inventoryService.deleteAll();
    }

    @Test
    public void testMakeReport_Successful() {
        List<Integer> prodMakats = new LinkedList<>();
        List<String[]> catNames = new LinkedList<>();
        catNames.add(new String[]{"Category1","Category2","Category3"});
        prodMakats.add(123);
        inventoryService.addProduct(123, 10, "Product1", "Shelf", "Manufacturer", 10.0, 20.0, new String[]{"Category1","Category2","Category3"}, 5);
        Response response = inventoryService.makeReport(catNames, prodMakats);
        assertNull(response.getErrorMsg());
        inventoryService.deleteAll();
    }

    @Test
    public void testAddDiscountFromSupplier_Successful() {
        List<String[]> catNames = new LinkedList<>();
        catNames.add(new String[]{"Category1","Category2","Category3"});
        inventoryService.addProduct(123, 10, "Product1", "Shelf", "Manufacturer", 10.0, 20.0, new String[]{"Category1","Category2","Category3"}, 5);
        Response response = inventoryService.addDiscountFromSupplier(null, catNames, 10);
        assertNull(response.getErrorMsg());
        inventoryService.deleteAll();
    }

    @Test
    public void testAddDiscountFromStore_Successful() {
        List<Integer> prodMakats = new LinkedList<>();
        prodMakats.add(123);
        inventoryService.addProduct(123, 10, "Product1", "Shelf", "Manufacturer", 10.0, 20.0, new String[]{"Category1","Category2","Category3"}, 5);
        Response response = inventoryService.addDiscountFromStore(prodMakats, null, 10);
        assertNull(response.getErrorMsg());
        inventoryService.deleteAll();
    }

    @Test
    public void testRemoveDiscountFromSupplier_Successful() {
        List<String[]> catNames = new LinkedList<>();
        catNames.add(new String[]{"Category1","Category2","Category3"});
        inventoryService.addProduct(123, 10, "Product1", "Shelf", "Manufacturer", 10.0, 20.0, new String[]{"Category1","Category2","Category3"}, 5);
        inventoryService.addDiscountFromSupplier(null, catNames, 10);
        Response response = inventoryService.removeDiscountFromSupplier(1);  // Assuming orderId is 1
        assertNull(response.getErrorMsg());
        inventoryService.deleteAll();
    }

    @Test
    public void testRemoveDiscountFromStore_Successful() {
        List<Integer> prodMakats = new LinkedList<>();
        prodMakats.add(123);
        inventoryService.addProduct(123, 10, "Product1", "Shelf", "Manufacturer", 10.0, 20.0, new String[]{"Category1","Category2","Category3"}, 5);
        inventoryService.addDiscountFromStore(prodMakats, null, 10);
        Response response = inventoryService.removeDiscountFromStore(1);  // Assuming orderId is 1
        assertNull(response.getErrorMsg());
        inventoryService.deleteAll();
    }

    @Test
    public void testGetAllProducts_Successful() {
        inventoryService.addProduct(123, 10, "Product1", "Shelf", "Manufacturer", 10.0, 20.0, new String[]{"Category1","Category2","Category3"}, 5);
        Response response = inventoryService.getAllProducts();
        assertNull(response.getErrorMsg());
        assertNotNull(response.getReturnValue());
        assertTrue(((String) response.getReturnValue()).length() > 0);
        inventoryService.deleteAll();
    }

    @Test
    public void testGetAllProducts_NoProducts() {
        Response response = inventoryService.getAllProducts();
        assertNotNull(response.getErrorMsg());
        assertNull(response.getReturnValue());
    }

    @Test
    public void testGetAllItems_Successful() {
        inventoryService.addProduct(123, 10, "Product1", "Shelf", "Manufacturer", 10.0, 20.0, new String[]{"Category1","Category2","Category3"}, 5);
        inventoryService.addItem(123, 456, new Date(), true);
        Response response = inventoryService.getAllItems(123);
        assertNull(response.getErrorMsg());
        assertNotNull(response.getReturnValue());
        inventoryService.deleteAll();
    }

    @Test
    public void testGetAllItems_NoItems() {
        inventoryService.addProduct(123, 10, "Product1", "Shelf", "Manufacturer", 10.0, 20.0, new String[]{"Category"}, 5);
        Response response = inventoryService.getAllItems(123);
        assertNotNull(response.getErrorMsg());
        inventoryService.deleteAll();
    }

}