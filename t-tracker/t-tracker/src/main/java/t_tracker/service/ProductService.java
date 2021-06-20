package t_tracker.service;

import java.util.List;

import t_tracker.model.Product;

public interface ProductService {

    Product registerProduct(Product product);
    List<Product> getAllProducts();
    Product getProduct(int id);
    
}
