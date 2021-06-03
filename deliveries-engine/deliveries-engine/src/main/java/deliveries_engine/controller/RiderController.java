package deliveries_engine.controller;


import org.springframework.web.bind.annotation.*;

import deliveries_engine.model.Rider;

@RestController
@RequestMapping("/rider")
public class RiderController {

    @PostMapping(value = "/signup", produces = "application/json")
    public void registerRider(@RequestBody Rider rider){
    }


    @PostMapping(value = "/location/{location}")
    public void updateLocation(@PathVariable(value = "location") String location){
    }

    @PostMapping(value = "/status/{status}")
    public void updateStatus(@PathVariable(value = "status") int status){
    }


    
}
