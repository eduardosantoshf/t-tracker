package t_tracker.repository;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import t_tracker.model.Product;
import t_tracker.model.Stock;

@DataJpaTest
class StockRepositoryTests {
    
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StockRepository stockRepository;

    private Stock testStock1, testStock2;
    private Product testProduct1, testProduct2;

    @BeforeEach
    void setUp() {
        testProduct1 = new Product("Nasal Test", 19.99, "spray", "Spray test for covid!");
        testProduct2 = new Product("Eye Test", 4.99, "lenses", "A lense to test for covid!");
        entityManager.persist(testProduct1);
        entityManager.persist(testProduct2);
        entityManager.flush();
        
        testStock1 = new Stock(testProduct1, 10);
        testStock2 = new Stock(testProduct2, 50);
        entityManager.persist(testStock1);
        entityManager.persist(testStock2);
        entityManager.flush();

    }

    @Test
    void whenFindAllStocks_thenReturnList() {
        List<Stock> stocksFound = stockRepository.findAll();

        assertThat(stocksFound.size(), is(2));
        assertThat(stocksFound.contains(testStock1), is(true));
        assertThat(stocksFound.contains(testStock2), is(true));
    }

}
