package t_tracker.repository;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import t_tracker.model.Product;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product("Nasal Test", 19.99, "spray", "Spray test for covid!");
        entityManager.persistAndFlush(testProduct);

    }

    @Test
    void whenFindByValidId_thenReturnProduct() {
        Optional<Product> productFound = productRepository.findById(testProduct.getId());

        assertThat(productFound.isPresent(), is(true));
        assertThat(productFound.get(), is(testProduct));
    }

    @Test
    void whenFindByInvalidId_thenReturnNull() {
        int invalidId = 9999;
        Optional<Product> productFound = productRepository.findById(invalidId);

        assertThat(productFound.isPresent(), is(false));
    }

    @Test
    void whenFindByValidNameAndPriceAndType_thenReturnProduct() {
        Optional<Product> productFound = productRepository.findByNameAndPriceAndType(testProduct.getName(),
                testProduct.getPrice(), testProduct.getType());

        assertThat(productFound.isPresent(), is(true));
        assertThat(productFound.get(), is(testProduct));
    }

    @Test
    void whenFindByInvalidNameAndValidPriceAndType_thenReturnNull() {
        String invalidName = "Invalid Name";
        Optional<Product> productFound = productRepository.findByNameAndPriceAndType(invalidName,
                testProduct.getPrice(), testProduct.getType());

        assertThat(productFound.isPresent(), is(false));
    }

    @Test
    void whenFindByValidNameAndInvalidPriceAndType_thenReturnNull() {
        Double invalidPrice = 99999.9999;
        Optional<Product> productFound = productRepository.findByNameAndPriceAndType(testProduct.getName(),
                invalidPrice, testProduct.getType());

        assertThat(productFound.isPresent(), is(false));
    }

    @Test
    void whenFindByInvalidNameAndValidPriceAndInvalidType_thenReturnNull() {
        String invalidType = "InvalidType";
        Optional<Product> productFound = productRepository.findByNameAndPriceAndType(testProduct.getName(),
                testProduct.getPrice(), invalidType);

        assertThat(productFound.isPresent(), is(false));
    }

}
