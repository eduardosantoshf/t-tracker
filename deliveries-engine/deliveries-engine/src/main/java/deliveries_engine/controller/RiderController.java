package deliveries_engine.controller;


import deliveries_engine.model.Delivery;
import deliveries_engine.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import deliveries_engine.model.Rider;
import deliveries_engine.service.RiderService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rider")
public class RiderController {

    @Autowired
    private RiderService riderService;

    @PostMapping(value = "/signup", consumes = "application/json")
    public Rider registerRider(@RequestBody Rider rider) throws Exception {
        System.out.println(rider);
        return riderService.registerRider(rider);
    }


    @GetMapping(value = "/location/{location}", produces = "application/json")
    public Rider updateLocation(@PathVariable(value = "location") String location){

        Rider rider = new Rider("ole", "ola", "ola", "ola", 3, "ola", "ola", "ola", 0);

        //System.out.println(rider.toString());
        return rider;
        //return "ole";
    }

    @PostMapping(value = "/status/{status}")
    public void updateStatus(@PathVariable(value = "status") int status){
    }


    
}
