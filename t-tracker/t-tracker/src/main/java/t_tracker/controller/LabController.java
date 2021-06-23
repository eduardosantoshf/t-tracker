package t_tracker.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import t_tracker.model.Stock;
import t_tracker.service.LabService;

@RestController
@RequestMapping("/lab")
public class LabController {

    @Autowired
    private LabService labService;

    @GetMapping(value = "/stock")
    public ResponseEntity<?> getLabsStock(HttpServletRequest request) throws ResponseStatusException {
        List<Stock> labStock;

        try {
            labStock = labService.getLabStock();
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatus());
        }

        return new ResponseEntity<>(labStock, HttpStatus.OK);
    }

    @PostMapping(value = "/stock/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> addLabStock(@RequestBody Stock stock, HttpServletRequest request) throws ResponseStatusException {
        List<Stock> labStockAfterAdd;

        try {
            labStockAfterAdd = labService.addStockToLab(stock);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatus());
        }

        return new ResponseEntity<>(labStockAfterAdd, HttpStatus.OK);
    }

    @PostMapping(value = "/stock/remove", consumes = "application/json")
    public ResponseEntity<?> removeLabStock(@RequestBody Stock stock, HttpServletRequest request) throws ResponseStatusException {
        List<Stock> labStockAfterRemoval;

        try {
            labStockAfterRemoval = labService.removeStockFromLab(stock);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatus());
        }

        return new ResponseEntity<>(labStockAfterRemoval, HttpStatus.OK);
    }

}
