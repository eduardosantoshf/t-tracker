package t_tracker.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import net.bytebuddy.agent.VirtualMachine.ForHotSpot.Connection.Response;
import t_tracker.model.Lab;
import t_tracker.model.Product;
import t_tracker.model.Stock;
import t_tracker.model.Coordinates;
import t_tracker.repository.*;

@ExtendWith(MockitoExtension.class)
public class LabServiceUnitTests {

    @Mock(lenient = true)
    private LabRepository labRepository;

    @Mock(lenient = true)
    private CoordinatesRepository coordRepository;

    @Mock(lenient = true)
    private StockRepository stockRepository;

    @Mock(lenient = true)
    private ProductRepository productRepository;

    @InjectMocks
    private LabServiceImpl labService;

    private Lab testLab;
    private Product testProduct1, testProduct2;
    private Stock testStock1, testStock2;

    private int invalidLabId = 99999;

    @BeforeEach
    void setUp() {
        testLab = new Lab("Public Test Lab", new Coordinates(44.5566, 44.6655));
        testProduct1 = new Product("Very Accurate Test", 99.99, "goodtype", "Bad test.");
        testProduct2 = new Product("Not So Accurate Test", 9.99, "badtype", "Bad test.");
        testStock1 = new Stock(testProduct1, 10);
        testStock2 = new Stock(testProduct2, 5);
        testLab.setStocks(new ArrayList<>(Arrays.asList(testStock1, testStock2)));

        Mockito.when(labRepository.save(testLab)).thenReturn(testLab);
        Mockito.when(labRepository.findById(testLab.getId())).thenReturn(Optional.of(testLab));
        Mockito.when(labRepository.findById(invalidLabId)).thenReturn(Optional.ofNullable(null));
        
        Mockito.when(productRepository.findByNameAndPriceAndType(testProduct1.getName(), testProduct1.getPrice(),
                testProduct1.getType())).thenReturn(Optional.of(testProduct1));
        Mockito.when(productRepository.findByNameAndPriceAndType(testProduct2.getName(), testProduct2.getPrice(),
                testProduct2.getType())).thenReturn(Optional.of(testProduct2));
        
        Mockito.when(productRepository.save(testStock1.getProduct())).thenReturn(testStock1.getProduct());
        Mockito.when(productRepository.save(testStock2.getProduct())).thenReturn(testStock2.getProduct());
    }

    @Test
    void whenRegisterValidLab_thenReturnValidLab() {
        Lab labRegistered = labService.registerLab(testLab);
        assertThat(labRegistered, is(testLab));

    }

    @Test
    void whenGetLabByValidId_thenReturnLab() {
        Lab labRegistered = labService.getLabById(testLab.getId());
        assertThat(labRegistered, is(testLab));

        verifyFindByIdIsCalledOnce(testLab.getId());
    }

    @Test
    void whenGetLabByInvalidId_thenThrow404LabNotFound() {
        ResponseStatusException exceptionThrown = assertThrows(ResponseStatusException.class,
                () -> labService.getLabById(invalidLabId));

        assertThat(exceptionThrown.getStatus(), is(HttpStatus.NOT_FOUND));
        assertThat(exceptionThrown.getReason(), is("Lab not found."));

        verifyFindByIdIsCalledOnce(invalidLabId);
    }

    @Test
    void whenGetAllLabs_thenReturnAllLabs() {
        Mockito.when(labRepository.findAll()).thenReturn(new ArrayList<Lab>(Arrays.asList(testLab)));

        List<Lab> labsFound = labService.getAllLabs();

        assertThat(labsFound.size(), is(1));
        assertThat(labsFound.contains(testLab), is(true));

        verifyFindAllIsCalledOnce();
    }

    @Test
    void whenGetAllLabsEmpty_thenReturnEmptyList() {
        Mockito.when(labRepository.findAll()).thenReturn(new ArrayList<Lab>());

        List<Lab> labsFound = labService.getAllLabs();

        assertThat(labsFound.size(), is(0));

        verifyFindAllIsCalledOnce();
    }

    @Test
    void whenGetLabStock_thenReturnLabStock() {
        List<Stock> stockFound = labService.getLabStock(testLab.getId());

        assertThat(stockFound.size(), is(testLab.getStocks().size()));
        assertThat(stockFound.contains(testStock1), is(true));
        assertThat(stockFound.contains(testStock2), is(true));

        verifyFindByIdIsCalledOnce(testLab.getId());

    }

    @Test
    void whenGetLabStockWithInvalidId_thenThrow404LabNotFound() {
        ResponseStatusException exceptionThrown = assertThrows(ResponseStatusException.class,
                () -> labService.getLabStock(invalidLabId));

        assertThat(exceptionThrown.getStatus(), is(HttpStatus.NOT_FOUND));
        assertThat(exceptionThrown.getReason(), is("Lab not found."));

        verifyFindByIdIsCalledOnce(invalidLabId);

    }

    @Test
    void whenAddExistingStockToValidLab_thenSumQuantityToLabStock() {
        Stock stockToAdd = new Stock(testProduct1, 1);

        List<Stock> resultingStock = labService.addStockToLab(testLab.getId(), stockToAdd);

        List<Stock> expectedStock = testLab.getStocks();
        for (Stock s : expectedStock)
            if (s.getProduct().equals(stockToAdd.getProduct())) {
                s.setQuantity(s.getQuantity() + stockToAdd.getQuantity());
                break;
            }

        assertThat(resultingStock.size(), is(2));
        assertThat(resultingStock, is(expectedStock));
        assertThat(resultingStock, is(testLab.getStocks()));

    }

    @Test
    void whenAddNewStockToValidLab_thenAppendStockToLab() {
        Stock stockToAdd = new Stock(new Product("Brand New Test", 49.99, "deluxe", "Deluxe test."), 3);

        List<Stock> expectedStock = new ArrayList<>(testLab.getStocks());
        expectedStock.add(stockToAdd);

        Mockito.when(productRepository.save(stockToAdd.getProduct())).thenReturn(stockToAdd.getProduct());
        Mockito.when(stockRepository.save(stockToAdd)).thenReturn(stockToAdd);

        List<Stock> resultingStock = labService.addStockToLab(testLab.getId(), stockToAdd);

        System.out.println(resultingStock);
        assertThat(resultingStock.size(), is(3));
        assertThat(resultingStock, is(expectedStock));
        assertThat(resultingStock, is(testLab.getStocks()));

    }

    @Test
    void whenAddNewStockToInvalidLab_thenThrow404LabNotFound() {
        Stock stockToAdd = new Stock(new Product("Brand New Test", 49.99, "deluxe", "Deluxe test."), 3);
        int invalidLabId = 99999;

        List<Stock> expectedStock = new ArrayList<>(testLab.getStocks());

        ResponseStatusException throwException = assertThrows(ResponseStatusException.class, () -> labService.addStockToLab(invalidLabId, stockToAdd));

        assertThat( throwException.getStatus(), is(HttpStatus.NOT_FOUND) );
        assertThat( throwException.getReason(), is("Lab not found.") );
        assertThat( testLab.getStocks(), is(expectedStock) );

    }

    private void verifyFindByIdIsCalledOnce(int id) {
        Mockito.verify(labRepository, VerificationModeFactory.times(1)).findById(id);
    }

    private void verifyFindAllIsCalledOnce() {
        Mockito.verify(labRepository, VerificationModeFactory.times(1)).findAll();
    }

}
