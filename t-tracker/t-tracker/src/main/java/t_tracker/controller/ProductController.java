package t_tracker.controller;

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
import t_tracker.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> registerNewProduct(@RequestBody Product product, HttpServletRequest request) throws ResponseStatusException {
        Product productInfo;

        try {
            productInfo = productService.registerProduct(product);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatus());
        }

        return new ResponseEntity<>(productInfo, HttpStatus.OK);
    }
    
    @GetMapping(value = "/{prodId}")
    public ResponseEntity<?> getProductInfo(@PathVariable(value = "prodId") int prodId, HttpServletRequest request)
            throws ResponseStatusException {
        Product productInfo;

        try {
            productInfo = productService.getProduct(prodId);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatus());
        }

        return new ResponseEntity<>(productInfo, HttpStatus.OK);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllProducts(HttpServletRequest request)
            throws ResponseStatusException {

        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }
}