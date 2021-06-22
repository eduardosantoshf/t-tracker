// package t_tracker.service;

// import static org.hamcrest.MatcherAssert.assertThat;
// import static org.hamcrest.core.Is.is;
// import static org.junit.jupiter.api.Assertions.assertThrows;

// import java.util.Arrays;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.Mockito;
// import org.mockito.internal.verification.VerificationModeFactory;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.http.HttpStatus;
// import org.springframework.web.server.ResponseStatusException;

// import t_tracker.model.Lab;
// import t_tracker.model.Product;
// import t_tracker.model.Stock;
// import t_tracker.model.Coordinates;
// import t_tracker.repository.*;

// @ExtendWith(MockitoExtension.class)
// class LabServiceUnitTests {

//     @Mock(lenient = true)
//     private LabRepository labRepository;

//     @Mock(lenient = true)
//     private CoordinatesRepository coordRepository;

//     @Mock(lenient = true)
//     private StockRepository stockRepository;

//     @Mock(lenient = true)
//     private ProductRepository productRepository;

//     @InjectMocks
//     private LabServiceImpl labService;

//     private Lab testLab;
//     private Product testProduct1, testProduct2;
//     private Stock testStock1, testStock2;

//     @BeforeEach
//     void setUp() {
//         testLab = new Lab(1, "labtoken", "Public Test Lab", new Coordinates(44.5566, 44.6655));
//         testProduct1 = new Product("Very Accurate Test", 99.99, "goodtype", "Bad test.");
//         testProduct2 = new Product("Not So Accurate Test", 9.99, "badtype", "Bad test.");
//         testStock1 = new Stock(testProduct1, 10);
//         testStock2 = new Stock(testProduct2, 5);
//         testLab.setStocks(new ArrayList<>(Arrays.asList(testStock1, testStock2)));

//         Mockito.when(labRepository.save(testLab)).thenReturn(testLab);
//         Mockito.when(labRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(testLab)));

//         Mockito.when(productRepository.findByNameAndPriceAndType(testProduct1.getName(), testProduct1.getPrice(),
//                 testProduct1.getType())).thenReturn(Optional.of(testProduct1));
//         Mockito.when(productRepository.findByNameAndPriceAndType(testProduct2.getName(), testProduct2.getPrice(),
//                 testProduct2.getType())).thenReturn(Optional.ofNullable(null));

//         Mockito.when(productRepository.save(testStock1.getProduct())).thenReturn(testStock1.getProduct());
//         Mockito.when(productRepository.save(testStock2.getProduct())).thenReturn(testStock2.getProduct());
//     }

//     @Test
//     void whenGetLabStock_thenReturnLabStock() {
//         List<Stock> stockFound = labService.getLabStock();

//         assertThat(stockFound.size(), is(testLab.getStocks().size()));
//         assertThat(stockFound.contains(testStock1), is(true));
//         assertThat(stockFound.contains(testStock2), is(true));

//         verifyFindAllIsCalledOnce();

//     }

//     @Test
//     void whenGetLabStockWithNoLab_thenThrow404LabNotFound() {
//         Mockito.when(labRepository.findAll()).thenReturn(new ArrayList<>());
//         ResponseStatusException exceptionThrown = assertThrows(ResponseStatusException.class,
//                 () -> labService.getLabStock());

//         assertThat(exceptionThrown.getStatus(), is(HttpStatus.NOT_FOUND));
//         assertThat(exceptionThrown.getReason(), is("Lab not found."));

//         verifyFindAllIsCalledOnce();

//     }

//     @Test
//     void whenAddExistingStock_thenSumQuantityToStock() {
//         Stock stockToAdd = new Stock(testProduct2, 1);

//         List<Stock> resultingStock = labService.addStockToLab(testStock2);

//         List<Stock> expectedStock = testLab.getStocks();
//         for (Stock s : expectedStock)
//             if (s.getProduct().equals(testStock2.getProduct())) {
//                 s.setQuantity(s.getQuantity() + testStock2.getQuantity());
//                 break;
//             }

//         assertThat(resultingStock.size(), is(2));
//         assertThat(resultingStock, is(expectedStock));
//         assertThat(resultingStock, is(testLab.getStocks()));

//         verifyFindAllIsCalledOnce();

//     }

//     @Test
//     void whenAddNewStockTo_thenAppendStockTo() {
//         Stock stockToAdd = new Stock(new Product("Brand New Test", 49.99, "deluxe", "Deluxe test."), 3);

//         List<Stock> expectedStock = new ArrayList<>(testLab.getStocks());
//         expectedStock.add(stockToAdd);

//         Mockito.when(productRepository.save(stockToAdd.getProduct())).thenReturn(stockToAdd.getProduct());
//         Mockito.when(stockRepository.save(stockToAdd)).thenReturn(stockToAdd);

//         List<Stock> resultingStock = labService.addStockToLab(stockToAdd);

//         assertThat(resultingStock.size(), is(3));
//         assertThat(resultingStock, is(expectedStock));
//         assertThat(resultingStock, is(testLab.getStocks()));

//         verifyFindAllIsCalledOnce();

//     }

//     @Test
//     void whenAddNewStockWithNoLab_thenThrow404LabNotFound() {
//         Mockito.when(labRepository.findAll()).thenReturn(new ArrayList<>());
//         Stock stockToAdd = new Stock(new Product("Brand New Test", 49.99, "deluxe", "Deluxe test."), 3);

//         List<Stock> expectedStock = new ArrayList<>(testLab.getStocks());

//         ResponseStatusException throwException = assertThrows(ResponseStatusException.class,
//                 () -> labService.addStockToLab(stockToAdd));

//         assertThat(throwException.getStatus(), is(HttpStatus.NOT_FOUND));
//         assertThat(throwException.getReason(), is("Lab not found."));
//         assertThat(testLab.getStocks(), is(expectedStock));

//         verifyFindAllIsCalledOnce();

//     }

//     @Test
//     void whenRemoveValidStockFromLab_thenReturnResultingStock() {
//         Stock stockToRemove = new Stock(testProduct1, 1);

//         List<Stock> resultingStock = labService.removeStockFromLab(stockToRemove);

//         List<Stock> expectedStock = testLab.getStocks();
//         for (Stock s : expectedStock)
//             if (s.getProduct().equals(stockToRemove.getProduct())) {
//                 s.setQuantity(s.getQuantity() + stockToRemove.getQuantity());
//                 break;
//             }

//         assertThat(resultingStock.size(), is(2));
//         assertThat(resultingStock, is(expectedStock));
//         assertThat(resultingStock, is(testLab.getStocks()));

//         verifyFindAllIsCalledOnce();
//     }

//     @Test
//     void whenRemoveStockWithNoLab_thenReturn404() {
//         Mockito.when(labRepository.findAll()).thenReturn(new ArrayList<>());
//         Stock stockToRemove = new Stock(new Product("Brand New Test", 29.99, "deluxe", "Deluxe test."), 1);

//         List<Stock> expectedStock = new ArrayList<>(testLab.getStocks());

//         ResponseStatusException throwException = assertThrows(ResponseStatusException.class,
//                 () -> labService.removeStockFromLab(stockToRemove));

//         assertThat(throwException.getStatus(), is(HttpStatus.NOT_FOUND));
//         assertThat(throwException.getReason(), is("Lab not found."));
//         assertThat(testLab.getStocks(), is(expectedStock));

//         verifyFindAllIsCalledOnce();
//     }

//     private void verifyFindAllIsCalledOnce() {
//         Mockito.verify(labRepository, VerificationModeFactory.times(1)).findAll();
//     }

// }
