package deliveries_engine.controller;

import deliveries_engine.exception.ErrorWarning;
import deliveries_engine.repository.RiderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import deliveries_engine.model.Rider;
import deliveries_engine.service.RiderService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/rider")
public class RiderController {

    @Autowired
    private RiderService riderService;

    @Autowired
    private RiderRepository riderRepository;

    @PostMapping(value = "/signup", consumes = "application/json")
    public Rider registerRider(@RequestBody Rider rider, HttpServletRequest request) throws Exception {
        return riderService.registerRider(rider);
    }


    @PostMapping(value = "/location/{latitude}/{longitude}", produces = "application/json")
    public Rider updateLocation(@PathVariable(value = "latitude") double latitude, @PathVariable(value = "longitude") double longitude, HttpServletRequest request) throws ErrorWarning {
        Principal principal = request.getUserPrincipal();
        Optional<Rider> opt = riderRepository.findByUsername(principal.getName());
        Rider rider = new Rider();
        if (opt.isPresent())
            rider = opt.get();
        return riderService.updateLocation(latitude,longitude, rider);
    }

    @PostMapping(value = "/status/{status}")
    public void updateStatus(@PathVariable(value = "status") int status){
    }


    
}
