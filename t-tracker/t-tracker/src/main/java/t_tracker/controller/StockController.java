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

import t_tracker.model.Stock;
import t_tracker.model.StockDTO;
import t_tracker.service.StockService;

@RestController
@RequestMapping("/stock")
public class StockController {
    
    @Autowired
    private StockService stockService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Stock>> getLabsStock(HttpServletRequest request) {

        return new ResponseEntity<>(stockService.getStock(), HttpStatus.OK);
    }

    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<List<Stock>> addLabStock(@RequestBody StockDTO stockDto, HttpServletRequest request) {
        Stock stock = new Stock(stockDto.getProduct(), stockDto.getQuantity());

        return new ResponseEntity<>(stockService.addStock(stock.getProduct(), stock.getQuantity()), HttpStatus.OK);
    }

    @PostMapping(value = "/remove", consumes = "application/json")
    public ResponseEntity<List<Stock>> removeLabStock(@RequestBody StockDTO stockDto, HttpServletRequest request) {
        Stock stock = new Stock(stockDto.getProduct(), stockDto.getQuantity());

        return new ResponseEntity<>(stockService.removeStock(stock.getProduct(), stock.getQuantity()), HttpStatus.OK);
    }

}
