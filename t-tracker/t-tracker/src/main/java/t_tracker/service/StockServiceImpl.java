package t_tracker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import t_tracker.model.Product;
import t_tracker.model.Stock;
import t_tracker.repository.CoordinatesRepository;
import t_tracker.repository.StockRepository;
import t_tracker.repository.ProductRepository;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    CoordinatesRepository coordRepository;

    @Autowired
    StockRepository stockRepository;

    @Autowired
    ProductRepository productRepository;

    @Override
    public List<Stock> getStock() {
        return stockRepository.findAll();
    }

    @Override
    public List<Stock> addStock(Product productToAdd, int quantityToAdd) {
        List<Stock> allStocks = stockRepository.findAll();

        Boolean stockExists = false;

        for (Stock stock : allStocks)
            if (productToAdd.equals(stock.getProduct())) {
                stock.addQuantity(quantityToAdd);
                stockRepository.save(stock);
                stockExists = true;
                break;
            }

        if (!stockExists) {
            Product actualProduct;

            Optional<Product> productFound = productRepository.findByNameAndPriceAndType(
                    productToAdd.getName(), productToAdd.getPrice(),
                    productToAdd.getType());

            System.out.println(allStocks);
            System.out.println("product found:");
            System.out.println(productFound);
            System.out.println(productFound.isPresent());
            if (productFound.isPresent())
                actualProduct = productFound.get();
            else
                actualProduct = productRepository.save(productToAdd);

            Stock stockToAdd = new Stock(actualProduct, quantityToAdd);
            
            stockRepository.save(stockToAdd);
            allStocks.add(stockToAdd);
        }

        return allStocks;
    }

    @Override
    public List<Stock> removeStock(Product productToRemove, int quantityToRemove) {
        List<Stock> allStocks = stockRepository.findAll();

        for (Stock stock : allStocks)
            if (productToRemove.equals(stock.getProduct())) {
                if (stock.getQuantity() < quantityToRemove)
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Not enough stock to process request.");
                stock.removeQuantity(quantityToRemove);
                stockRepository.save(stock);
                return allStocks;
            }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not Found.");
    }

}
