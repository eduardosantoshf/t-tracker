package t_tracker.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import t_tracker.model.Product;
import t_tracker.repository.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTests {

    @Mock(lenient = true)
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product validTestProduct1, validTestProduct2;
    private int invalidId = 9999;
    private int validId = 1;

    @BeforeEach
    void setUp() {
        validTestProduct1 = new Product("Blood Test", 4.99, "blood", "Quick and easy covid test.");
        validTestProduct2 = new Product("Blood Test 2", 9.99, "blood", "Quick and easy covid test but more expensive.");

        Mockito.when(productRepository.save(validTestProduct1)).thenReturn(validTestProduct1);
        Mockito.when(productRepository.save(validTestProduct2)).thenThrow( ResponseStatusException.class );
        Mockito.when(productRepository.findAll())
                .thenReturn(new ArrayList<>(Arrays.asList(validTestProduct1, validTestProduct2)));
        Mockito.when(productRepository.findById(validId)).thenReturn(Optional.of(validTestProduct1));
        Mockito.when(productRepository.findById(invalidId)).thenReturn(Optional.ofNullable(null));

    }

    @Test
    void whenRegisterValidProduct_thenCreateProduct() {
        Product productStored = productService.registerProduct(validTestProduct1);

        assertThat(productStored, is(validTestProduct1));

    }

    @Test
    void whenRegisterInvalidProduct_thenThrow409() {
        ResponseStatusException thrownException = assertThrows(ResponseStatusException.class, () -> productService.registerProduct(validTestProduct2));

        assertThat( thrownException.getStatus(), is(HttpStatus.CONFLICT) );
        assertThat( thrownException.getReason(), is("Failed to register product.") );

    }

    @Test
    void whenGetAllProducts_thenReturnList() {
        List<Product> productsFound = productService.getAllProducts();

        assertThat(productsFound.size(), is(2));
        assertThat(productsFound.contains(validTestProduct1), is(true));
        assertThat(productsFound.contains(validTestProduct2), is(true));

        verifyFindAllIsCalledOnce();

    }

    @Test
    void whenGetProductByValidId_thenReturnValidProduct() {
        Product productFound = productService.getProduct(validId);

        assertThat(productFound, is(validTestProduct1));

        verifyFindByIdIsCalledOnce(validId);

    }

    @Test
    void whenGetProductByInvalidId_thenReturn404NotFound() {
        ResponseStatusException thrownException = assertThrows(ResponseStatusException.class,
                () -> productService.getProduct(invalidId));

        assertThat(thrownException.getStatus(), is(HttpStatus.NOT_FOUND));
        assertThat(thrownException.getReason(), is("Product not found."));

        verifyFindByIdIsCalledOnce(invalidId);

    }

    private void verifyFindAllIsCalledOnce() {
        Mockito.verify(productRepository, VerificationModeFactory.times(1)).findAll();
    }

    private void verifyFindByIdIsCalledOnce(int prodId) {
        Mockito.verify(productRepository, VerificationModeFactory.times(1)).findById(prodId);
    }

}
