package t_tracker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import t_tracker.model.Product;
import t_tracker.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public Product registerProduct(Product product) {
        Product registeredProduct;

        try {
            registeredProduct = productRepository.save(product);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Failed to register product.");
        }

        return registeredProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(int id) {
        Optional<Product> productFound = productRepository.findById(id);

        if (!productFound.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found.");

        return productFound.get();
    }

}
