// package t_tracker.service;

// import java.util.List;
// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.stereotype.Service;
// import org.springframework.web.server.ResponseStatusException;

// import t_tracker.model.Lab;
// import t_tracker.model.Product;
// import t_tracker.model.Stock;
// import t_tracker.repository.CoordinatesRepository;
// import t_tracker.repository.LabRepository;
// import t_tracker.repository.StockRepository;
// import t_tracker.repository.ProductRepository;

// @Service
// public class LabServiceImpl implements LabService {

//     @Autowired
//     LabRepository labRepository;

//     @Autowired
//     CoordinatesRepository coordRepository;

//     @Autowired
//     StockRepository stockRepository;

//     @Autowired
//     ProductRepository productRepository;

//     final static String LAB_NOT_FOUND = "Lab not found.";

//     @Override
//     public List<Stock> getLabStock() {
//         List<Lab> labFound = labRepository.findAll();

//         System.out.println(labFound);

//         if (labFound.isEmpty())
//             throw new ResponseStatusException(HttpStatus.NOT_FOUND, LAB_NOT_FOUND);

//         return labFound.get(0).getStocks();
//     }

//     @Override
//     public List<Stock> addStockToLab(Stock stockToAdd) {
//         List<Lab> labFound = labRepository.findAll();

//         System.out.println(labFound);

//         if (labFound.isEmpty())
//             throw new ResponseStatusException(HttpStatus.NOT_FOUND, LAB_NOT_FOUND);

//         Lab actualLab = labFound.get(0);

//         List<Stock> allStocks = actualLab.getStocks();

//         Boolean labHasStock = false;

//         for (Stock stock : allStocks)
//             if (stockToAdd.getProduct().equals(stock.getProduct())) {
//                 stock.addQuantity(stockToAdd.getQuantity());
//                 stockRepository.save(stock);
//                 labHasStock = true;
//                 break;
//             }

//         if (!labHasStock) {
//             Product actualProduct;
//             Optional<Product> productFound = productRepository.findByNameAndPriceAndType(
//                     stockToAdd.getProduct().getName(), stockToAdd.getProduct().getPrice(),
//                     stockToAdd.getProduct().getType());

//             if (productFound.isPresent())
//                 actualProduct = productFound.get();
//             else
//                 actualProduct = productRepository.save(stockToAdd.getProduct());

//             stockToAdd.setProduct(actualProduct);

//             stockToAdd.setLab(actualLab);

//             actualLab.addStock(stockRepository.save(stockToAdd));
//         }

//         labRepository.save(actualLab);

//         return actualLab.getStocks();
//     }

//     @Override
//     public List<Stock> removeStockFromLab(Stock stockToRemove) {
//         List<Lab> labFound = labRepository.findAll();

//         if (labFound.isEmpty())
//             throw new ResponseStatusException(HttpStatus.NOT_FOUND, LAB_NOT_FOUND);

//         Lab actualLab = labFound.get(0);

//         List<Stock> allStocks = actualLab.getStocks();

//         for (Stock stock : allStocks)
//             if (stockToRemove.getProduct().equals(stock.getProduct())) {
//                 stock.removeQuantity(stockToRemove.getQuantity());
//                 stockRepository.save(stock);
//                 break;
//             }

//         labRepository.save(actualLab);

//         return actualLab.getStocks();
//     }

// }
