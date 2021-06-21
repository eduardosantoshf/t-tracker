package t_tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import t_tracker.model.Coordinates;
import t_tracker.model.Lab;
import t_tracker.repository.CoordinatesRepository;
import t_tracker.repository.LabRepository;

@Component
public class DataLoader implements ApplicationRunner {

    private LabRepository labRepository;
    private CoordinatesRepository coordinatesRepository;
    private String labInfo = "{\"name\":\"CT-TrackerDeliveries" + java.time.LocalDateTime.now() + "\",\"ownerName\":\"TqsG101\",\"latitude\":\"1.0\",\"longitude\":\"2.0\"}";

    @Autowired
    public DataLoader(LabRepository labRepository, CoordinatesRepository coordinatesRepository) {
        this.labRepository = labRepository;
        this.coordinatesRepository = coordinatesRepository;
    }

    public void run(ApplicationArguments args) {
        if (labRepository.findAll().isEmpty())
            return;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        Coordinates labCoord = new Coordinates(1.0, 2.0);

        HttpEntity<String> requestContent = new HttpEntity<>(labInfo, httpHeaders);
        try {

            ResponseEntity<Lab> response = restTemplate.postForEntity("http://localhost:8080/store", requestContent,
                    Lab.class);

            if (response.getStatusCode() != HttpStatus.OK)
                return;

            Lab newLab = new Lab(response.getBody().getId(), response.getBody().getToken(), "CT-TrackerDeliveries",
                    labCoord);

            coordinatesRepository.save(labCoord);
            labRepository.save(newLab);
        } catch (Exception e) {}

    }
}
