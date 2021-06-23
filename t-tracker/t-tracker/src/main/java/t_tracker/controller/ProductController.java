package t_tracker.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import t_tracker.model.Product;
import t_tracker.model.ProductDTO;
import t_tracker.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Product> registerNewProduct(@RequestBody ProductDTO productDto, HttpServletRequest request) throws ResponseStatusException {
        Product product = new Product(productDto.getName(), productDto.getPrice(), productDto.getType(), productDto.getDescription(), productDto.getFoto());
        return new ResponseEntity<>(productService.registerProduct(product), HttpStatus.OK);
    }
    
    @GetMapping(value = "/{prodId}")
    public ResponseEntity<Product> getProductInfo(@PathVariable(value = "prodId") int prodId, HttpServletRequest request)
            throws ResponseStatusException {

        return new ResponseEntity<>(productService.getProduct(prodId), HttpStatus.OK);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<Product>> getAllProducts(HttpServletRequest request)
            throws ResponseStatusException {

        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }
}
