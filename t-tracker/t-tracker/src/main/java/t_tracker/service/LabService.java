package t_tracker.service;

import java.util.List;

import t_tracker.model.Lab;
import t_tracker.model.Stock;

public interface LabService {

    // Lab registerLab(Lab lab);

    // Lab getLabById(int id);

    // List<Lab> getAllLabs();

    List<Stock> getLabStock();

    List<Stock> addStockToLab(Stock stock);

    List<Stock> removeStockFromLab(Stock stock);

}
