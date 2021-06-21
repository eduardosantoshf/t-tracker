package t_tracker.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import t_tracker.model.Lab;
import t_tracker.model.Stock;
import t_tracker.service.LabService;

@RestController
@RequestMapping("/lab")
public class LabController {

    @Autowired
    private LabService labService;

    // @PostMapping(consumes = "application/json")
    // public ResponseEntity<?> registerLab(@RequestBody Lab lab, HttpServletRequest
    // request) throws Exception {
    // Lab registeredLab;

    // try {
    // registeredLab = labService.registerLab(lab);
    // } catch (Exception e) {
    // return new ResponseEntity<>(null, HttpStatus.CONFLICT);
    // }

    // return new ResponseEntity<>(registeredLab, HttpStatus.CREATED);
    // }

    // @GetMapping(value = "/{labId}")
    // public ResponseEntity<?> getLabById(@PathVariable(value = "labId") int labId,
    // HttpServletRequest request)
    // throws Exception {
    // Lab labFound;

    // try {
    // labFound = labService.getLabById(labId);
    // } catch (ResponseStatusException e) {
    // return new ResponseEntity<>(e.getReason(), e.getStatus());
    // }

    // return new ResponseEntity<>(labFound, HttpStatus.OK);
    // }

    // @GetMapping(value = "/all")
    // public ResponseEntity<?> getAllLabs(HttpServletRequest request) throws
    // Exception {
    // return new ResponseEntity<>(labService.getAllLabs(), HttpStatus.OK);
    // }

    @GetMapping(value = "/stock")
    public ResponseEntity<?> getLabsStock(HttpServletRequest request) throws Exception {
        List<Stock> labStock;

        try {
            labStock = labService.getLabStock();
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatus());
        }

        return new ResponseEntity<>(labStock, HttpStatus.OK);
    }

    @PostMapping(value = "/stock/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> addLabStock(@RequestBody Stock stock, HttpServletRequest request) throws Exception {
        List<Stock> labStockAfterAdd;

        try {
            labStockAfterAdd = labService.addStockToLab(stock);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatus());
        }
        System.out.println(labStockAfterAdd);
        return new ResponseEntity<>(labStockAfterAdd, HttpStatus.OK);
    }

    @PostMapping(value = "/stock/remove", consumes = "application/json")
    public ResponseEntity<?> removeLabStock(@RequestBody Stock stock, HttpServletRequest request) throws Exception {
        List<Stock> labStockAfterRemoval;

        try {
            labStockAfterRemoval = labService.removeStockFromLab(stock);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatus());
        }

        return new ResponseEntity<>(labStockAfterRemoval, HttpStatus.OK);
    }

}
