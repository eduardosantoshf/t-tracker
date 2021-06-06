package deliveries_engine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import deliveries_engine.model.Rider;
import deliveries_engine.service.RiderService;

@RestController
@RequestMapping("/rider")
public class RiderController {

    @Autowired
    private RiderService riderService;

    @PostMapping(value = "/signup", consumes = "application/json")
    public Rider registerRider(@RequestBody Rider rider) throws Exception {
        return riderService.registerRider(rider);
    }


    @GetMapping(value = "/location/{location}", produces = "application/json")
    public Rider updateLocation(@PathVariable(value = "location") String location){
        return null;
    }

    @PostMapping(value = "/status/{status}")
    public void updateStatus(@PathVariable(value = "status") int status){
    }


    
}
