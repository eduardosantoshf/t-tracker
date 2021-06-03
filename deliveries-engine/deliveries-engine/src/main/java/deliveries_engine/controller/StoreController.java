package deliveries_engine.controller;

import org.springframework.web.bind.annotation.*;

import deliveries_engine.model.Delivery;
import deliveries_engine.model.Rider;
import deliveries_engine.model.Store;

@RestController
@RequestMapping("/store")
public class StoreController {

    @PostMapping(consumes = "application/json")
    public void registerStore(@RequestBody Store store){
    }

    @GetMapping(value = "/driver/location/{location}", produces = "application/json")
    public Rider givenLocationGetDriver(@PathVariable(name = "location") String location){
        return null;
    }

    @PostMapping(value = "/driver/rating", consumes = "application/json")
    public void updateRating(@RequestBody int rating, @RequestBody String comment){
    }

    @PutMapping(value = "/update", consumes = "applcation/json")
    public void updateStore(@RequestBody Store store){
    }

    @PostMapping(value = "/order", consumes = "application/json")
    public void order(@RequestBody Delivery delivery){
    }
    
}
