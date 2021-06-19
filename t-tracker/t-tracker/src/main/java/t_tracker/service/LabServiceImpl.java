package t_tracker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import t_tracker.model.Lab;
import t_tracker.model.Product;
import t_tracker.model.Stock;
import t_tracker.repository.CoordinatesRepository;
import t_tracker.repository.LabRepository;
import t_tracker.repository.StockRepository;
import t_tracker.repository.ProductRepository;

@Service
public class LabServiceImpl implements LabService {

    @Autowired
    LabRepository labRepository;

    @Autowired
    CoordinatesRepository coordRepository;

    @Autowired
    StockRepository stockRepository;

    @Autowired
    ProductRepository productRepository;

    @Override
    public Lab registerLab(Lab lab) {
        coordRepository.save(lab.getLocation());
        return labRepository.save(lab);
    }

    @Override
    public Lab getLabById(int id) {
        Optional<Lab> labFound = labRepository.findById(id);

        if (labFound.isPresent())
            return labFound.get();

        return null;
    }

    @Override
    public List<Lab> getAllLabs() {
        return labRepository.findAll();
    }

    @Override
    public List<Stock> getLabStock(int id) {
        Optional<Lab> labFound = labRepository.findById(id);

        if (!labFound.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lab not found.");

        return labFound.get().getStocks();
    }

    @Override
    public List<Stock> addStockToLab(int labId, Stock stockToAdd) {
        Optional<Lab> labFound = labRepository.findById(labId);

        if (!labFound.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lab not found.");

        Lab actualLab = labFound.get();

        List<Stock> allStocks = actualLab.getStocks();

        Boolean labHasStock = false;

        for (Stock stock : allStocks)
            if (stockToAdd.getProduct().equals(stock.getProduct())) {
                stock.addQuantity(stockToAdd.getQuantity());
                stockRepository.save(stock);
                labHasStock = true;
                break;
            }

        if (!labHasStock) {
            Product actualProduct;
            Optional<Product> productFound = productRepository.findByNameAndPriceAndType(
                    stockToAdd.getProduct().getName(), stockToAdd.getProduct().getPrice(),
                    stockToAdd.getProduct().getType());

            if (productFound.isPresent())
                actualProduct = productFound.get();
            else
                actualProduct = productRepository.save(stockToAdd.getProduct());
            
            stockToAdd.setProduct(actualProduct);

            stockToAdd.setLab(actualLab);

            actualLab.addStock(stockRepository.save(stockToAdd));
        }

        labRepository.save(actualLab);

        return actualLab.getStocks();
    }

    @Override
    public List<Stock> removeStockFromLab(int labId, Stock stockToRemove) {
        Optional<Lab> labFound = labRepository.findById(labId);

        if (!labFound.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lab not found.");

        Lab actualLab = labFound.get();

        List<Stock> allStocks = actualLab.getStocks();

        for (Stock stock : allStocks)
            if (stockToRemove.getProduct().equals(stock.getProduct())) {
                stock.removeQuantity(stockToRemove.getQuantity());
                stockRepository.save(stock);
                break;
            }

        labRepository.save(actualLab);

        return actualLab.getStocks();
    }

}
