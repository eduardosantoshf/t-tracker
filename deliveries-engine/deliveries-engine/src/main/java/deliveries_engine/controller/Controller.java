package deliveries_engine.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

    @GetMapping(value = "/stats", produces = "application/json")
    public void getStats(){
        
    }
    
}
