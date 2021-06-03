package deliveries_engine.controller;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rider")
public class RiderController {

    @PostMapping(value = "/{location}")
    public void updateLocation(@PathVariable(value = "location") String location){


    }


    
}
