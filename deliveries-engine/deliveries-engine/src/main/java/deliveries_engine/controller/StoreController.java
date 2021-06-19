package deliveries_engine.controller;

import deliveries_engine.exception.ErrorWarning;
import deliveries_engine.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import deliveries_engine.model.Delivery;
import deliveries_engine.model.Rider;
import deliveries_engine.model.Store;

import java.util.List;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    StoreService storeService;

    @PostMapping(consumes = "application/json")
    public Store registerStore(@RequestBody Store store) throws Exception {
        return storeService.registerStore(store);
    }

    @GetMapping(value = "/driver/location/{location}", produces = "application/json")
    public Rider givenLocationGetDriver(@PathVariable(name = "location") String location){
        return null;
    }

    @PostMapping(value = "/driver/rating/{storeId}/{riderId}", consumes = "application/json")
    public List<Integer> updateRatings(@RequestBody com.fasterxml.jackson.databind.JsonNode rating,
                                       @RequestHeader(name = "Authorization") String token,
                                       @PathVariable(name = "storeId") int storeId, @PathVariable int riderId) throws Exception {

        String strRating = rating.get("rating").toString();

        return storeService.updateRatings(Integer.valueOf(strRating), token, storeId, riderId);
    }

    @GetMapping(value = "/driver/rating/{storeId}/{riderId}")
    public List<Integer> getRatings(@RequestHeader(name = "Authorization") String token,
                                       @PathVariable(name = "storeId") int storeId, @PathVariable int riderId) throws Exception {


        return storeService.getRatings( token, storeId, riderId);
    }

    @PostMapping(value = "/driver/comment/{storeId}/{riderId}", consumes = "application/json")
    public List<String> updateComments(@RequestBody com.fasterxml.jackson.databind.JsonNode comment, @RequestHeader(name = "Authorization") String token, @PathVariable(name = "storeId") int storeId, @PathVariable int riderId) throws Exception {

        return storeService.updateComments(comment.get("comment").toString().replaceAll("\"", ""), token, storeId, riderId);
    }

    @GetMapping(value = "/driver/comment/{storeId}/{riderId}")
    public List<String> getComments(@RequestHeader(name = "Authorization") String token,
                                    @PathVariable(name = "storeId") int storeId, @PathVariable int riderId) throws Exception {

        return storeService.getComments(token, storeId, riderId);
    }

    @PutMapping(value = "/update", consumes = "applcation/json")
    public void updateStore(@RequestBody Store store){
    }

    @PostMapping(value = "/order/{storeId}", consumes = "application/json", produces = "application/json")
    public Rider order(@RequestBody Delivery delivery, @RequestHeader(name = "Authorization") String token, @PathVariable(name = "storeId") int storeId) throws Exception {
        return storeService.getClosestRider(delivery, delivery.getDeliveryLatitude(), delivery.getDeliveryLongitude(), token, storeId);
    }
}
