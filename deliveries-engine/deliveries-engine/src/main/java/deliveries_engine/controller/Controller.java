package deliveries_engine.controller;

import deliveries_engine.model.Stats;
import deliveries_engine.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("/admin")
public class Controller {

    @Autowired
    private AdminService adminService;


    @GetMapping(value = "/stats", produces = "application/json")
    public Stats getStats(HttpServletRequest request) throws Exception {

        Principal principal = request.getUserPrincipal();

        System.out.println(principal.getName());


        if(principal.getName().equals("admin")){
            System.out.println("vai entrar");
            Stats totalStats = adminService.getStats();

            return totalStats;
        }
        else {
            throw new Exception("Forbidden: not an admin");
        }
    }
    
}
