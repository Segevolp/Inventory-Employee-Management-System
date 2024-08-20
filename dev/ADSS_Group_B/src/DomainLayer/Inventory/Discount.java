package DomainLayer.Inventory;

import DataAccessLayer.Inventory.ControllerClasses.DiscountController;
import DataAccessLayer.Inventory.DTOClasses.DiscountDTO;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class Discount {
    private int id;
    private List<Category> cats;
    private List<Product> products;
    private double discountPrecentage;

    private boolean fromSupplier;

    private DiscountDTO dto;

    //////////// Constructors ////////////
    public Discount(int id, List<Category> cats, List<Product> products, double discountPrecentage, boolean fromSupplier, DiscountController dc, boolean fromDB) throws Exception{
        this.id = id;
        this.cats = cats;
        this.products = products;
        this.discountPrecentage = discountPrecentage;
        this.fromSupplier = fromSupplier;

        List<String> catNames = new LinkedList<>();
        List<Integer> makats = new LinkedList<>();

        this.dto = new DiscountDTO(id,catNames,makats,discountPrecentage,fromSupplier,dc,fromDB);
        if(!fromDB)
            dto.persist();
    }


    //////////// Getters ////////////
    public List<Category> getCat(){
        return this.cats;
    }
    public List<Product> getProduct(){
        return this.products;
    }

    public double getDiscountPrecentage(){
        return this.discountPrecentage;
    }
    

    public List<Product> getProductsOnDiscount(){
        if(cats == null){
            return this.products;
        }
        List<Product> productsTemp = new LinkedList<>();
        for(Category cat: cats){
            List<Product> temp2 = cat.getCategoryProducts();
            productsTemp.addAll(temp2);
        }
        return productsTemp;
    }
    public String toString(){
        if(cats == null){
            return "Discount on products: \n" + getProducts() + " with precentage: " + discountPrecentage + " from supplier:" +fromSupplier;
        }
        return "Discount on categories: " + getCategoryNames() + " with precentage: " + discountPrecentage + " from supplier:" +fromSupplier;

    }
    private String getProducts(){
        StringBuilder str = new StringBuilder();
        for (Product product: products){
            str.append("name: ").append(product.getName()).append(" makat: ").append(product.getMakat()).append("\n");
        }
        return str.toString();
    }
    private String getCategoryNames(){
        StringBuilder str = new StringBuilder();
        for (Category cat: cats){
            str.append("name: ").append(cat.getCategoryName()).append("\n");
        }
        return str.toString();
    }
    public void deleteDTO() throws Exception{
        dto.deleteDiscount();
    }
}
