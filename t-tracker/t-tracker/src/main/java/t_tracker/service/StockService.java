package t_tracker.service;

import java.util.List;

import t_tracker.model.Product;
import t_tracker.model.Stock;

public interface StockService {

    List<Stock> getStock();

    List<Stock> addStock(Product productToAdd, int quantityToAdd);

    List<Stock> removeStock(Product productToRemove, int quantityToRemove);
    
}
