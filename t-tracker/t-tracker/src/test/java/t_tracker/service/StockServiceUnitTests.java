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
import t_tracker.model.Stock;
import t_tracker.repository.*;

@ExtendWith(MockitoExtension.class)
class StockServiceUnitTests {

    @Mock(lenient = true)
    private CoordinatesRepository coordRepository;

    @Mock(lenient = true)
    private StockRepository stockRepository;

    @Mock(lenient = true)
    private ProductRepository productRepository;

    @InjectMocks
    private StockServiceImpl stockService;

    private Product testProduct1, testProduct2, testProduct3;
    private Stock testStock1, testStock2;

    @BeforeEach
    void setUp() {
        testProduct1 = new Product("Very Accurate Test", 99.99, "goodtype", "Bad test.");
        testProduct2 = new Product("Not So Accurate Test", 9.99, "badtype", "Bad test.");
        testProduct3 = new Product("Test test", 4.99, "unknowntype", "It should work.");
        testStock1 = new Stock(testProduct1, 10);
        testStock2 = new Stock(testProduct2, 5);

        Mockito.when(stockRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(testStock1, testStock2)));

        Mockito.when(productRepository.findByNameAndPriceAndType(testProduct1.getName(), testProduct1.getPrice(),
                testProduct1.getType())).thenReturn(Optional.of(testProduct1));

        Mockito.when(productRepository.findByNameAndPriceAndType(testProduct2.getName(), testProduct2.getPrice(),
                testProduct2.getType())).thenReturn(Optional.ofNullable(null));
        Mockito.when(productRepository.findByNameAndPriceAndType(testProduct3.getName(), testProduct3.getPrice(),
                testProduct3.getType())).thenReturn(Optional.of(testProduct3));

        Mockito.when(productRepository.save(testProduct1)).thenReturn(testProduct1);
        Mockito.when(productRepository.save(testProduct2)).thenReturn(testProduct2);
    }

    @Test
    void whenGetStock_thenReturnStock() {
        List<Stock> stockFound = stockService.getStock();

        assertThat(stockFound.size(), is(2));
        assertThat(stockFound.contains(testStock1), is(true));
        assertThat(stockFound.contains(testStock2), is(true));

        verifyFindAllIsCalledOnce();

    }

    @Test
    void whenAddExistingStock_thenSumQuantityToStock() {
        Stock stockToAdd = new Stock(testProduct1, 1);

        List<Stock> resultingStock = stockService.addStock(stockToAdd.getProduct(), stockToAdd.getQuantity());

        testStock1.setQuantity(testStock1.getQuantity() + 1);

        List<Stock> expectedStock = new ArrayList<>(Arrays.asList(testStock1, testStock2));

        assertThat(resultingStock.size(), is(2));
        assertThat(resultingStock, is(expectedStock));

        verifyFindAllIsCalledOnce();

    }

    @Test
    void whenAddStockWithUnknowProduct_thenCreateProductAndAddStock() {
        Stock stockToAdd = new Stock(new Product("Brand New Test", 49.99, "deluxe", "Deluxe test."), 3);

        List<Stock> expectedStock = new ArrayList<>();
        expectedStock.add(testStock1);
        expectedStock.add(testStock2);
        expectedStock.add(stockToAdd);

        Mockito.when(productRepository.save(stockToAdd.getProduct())).thenReturn(stockToAdd.getProduct());
        Mockito.when(stockRepository.save(stockToAdd)).thenReturn(stockToAdd);

        List<Stock> resultingStock = stockService.addStock(stockToAdd.getProduct(), stockToAdd.getQuantity());

        assertThat(resultingStock.size(), is(3));
        assertThat(resultingStock, is(expectedStock));

        verifyFindAllIsCalledOnce();

    }

    @Test
    void whenAddNewStockWithKnownProduct_thenCreateStock() {
        Stock stockToAdd = new Stock(testProduct3, 3);

        List<Stock> expectedStock = new ArrayList<>();
        expectedStock.add(testStock1);
        expectedStock.add(testStock2);
        expectedStock.add(stockToAdd);

        Mockito.when(productRepository.save(stockToAdd.getProduct())).thenReturn(stockToAdd.getProduct());
        Mockito.when(stockRepository.save(stockToAdd)).thenReturn(stockToAdd);

        List<Stock> resultingStock = stockService.addStock(stockToAdd.getProduct(), stockToAdd.getQuantity());

        assertThat(resultingStock.size(), is(3));
        assertThat(resultingStock, is(expectedStock));

        verifyFindAllIsCalledOnce();

    }

    @Test
    void whenRemoveValidStockFromLab_thenReturnResultingStock() {
        Stock stockToRemove = new Stock(testProduct1, 1);

        List<Stock> resultingStock = stockService.removeStock(stockToRemove.getProduct(), stockToRemove.getQuantity());

        List<Stock> expectedStock = new ArrayList<>();
        testStock1.setQuantity(testStock1.getQuantity() - 1);
        expectedStock.add(testStock1);
        expectedStock.add(testStock2);

        assertThat(resultingStock.size(), is(2));
        assertThat(resultingStock, is(expectedStock));

        verifyFindAllIsCalledOnce();
    }

    @Test
    void whenRemoveStockWithNotEnoughQuantity_thenReturn409() {
        Stock stockToRemove = new Stock(testProduct1, 50);

        Product prod = stockToRemove.getProduct();
        int quant = stockToRemove.getQuantity();

        ResponseStatusException exceptionThrown = assertThrows(ResponseStatusException.class,
                () -> stockService.removeStock(prod, quant));

        assertThat(exceptionThrown.getStatus(), is(HttpStatus.CONFLICT));
        assertThat(exceptionThrown.getReason(), is("Not enough stock to process request."));

        verifyFindAllIsCalledOnce();
    }

    @Test
    void whenRemoveStockWithUnknownProduct_thenReturn404() {
        Stock stockToRemove = new Stock(new Product("Whatever", 1.1, "whatever", "whatever"), 50);

        Product prod = stockToRemove.getProduct();
        int quant = stockToRemove.getQuantity();

        ResponseStatusException exceptionThrown = assertThrows(ResponseStatusException.class,
                () -> stockService.removeStock(prod, quant));

        assertThat(exceptionThrown.getStatus(), is(HttpStatus.NOT_FOUND));
        assertThat(exceptionThrown.getReason(), is("Product not Found."));

        verifyFindAllIsCalledOnce();
    }

    private void verifyFindAllIsCalledOnce() {
        Mockito.verify(stockRepository, VerificationModeFactory.times(1)).findAll();
    }

}
