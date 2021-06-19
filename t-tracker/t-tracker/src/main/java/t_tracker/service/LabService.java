package t_tracker.service;

import java.util.List;

import t_tracker.model.Lab;
import t_tracker.model.Stock;

public interface LabService {

    Lab registerLab(Lab lab);

    Lab getLabById(int id);

    List<Lab> getAllLabs();

    List<Stock> getLabStock(int id);

    List<Stock> addStockToLab(int labId, Stock stock);

    List<Stock> removeStockFromLab(int labId, Stock stock);

}
