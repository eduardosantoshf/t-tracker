package t_tracker.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import t_tracker.model.Client;

@RestController
@RequestMapping("/client")
public class ClientController {

    @PostMapping(value = "/signup", consumes = "application/json")
    public ResponseEntity<Client> registerClient(@RequestBody Client client, HttpServletRequest request) throws Exception {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
