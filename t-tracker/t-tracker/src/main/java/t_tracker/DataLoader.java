package t_tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import t_tracker.model.Coordinates;
import t_tracker.model.Lab;
import t_tracker.model.Product;
import t_tracker.repository.CoordinatesRepository;
import t_tracker.repository.LabRepository;
import t_tracker.repository.ProductRepository;

@Component
public class DataLoader implements ApplicationRunner {

    private LabRepository labRepository;
    private CoordinatesRepository coordinatesRepository;
    private ProductRepository productRepository;
    private String labInfo = "{\"name\":\"CT-TrackerDeliveries" + java.time.LocalDateTime.now() + "\",\"ownerName\":\"TqsG101\",\"latitude\":\"1.0\",\"longitude\":\"2.0\"}";

    @Autowired
    public DataLoader(LabRepository labRepository, CoordinatesRepository coordinatesRepository, ProductRepository productRepository) {
        this.labRepository = labRepository;
        this.coordinatesRepository = coordinatesRepository;
        this.productRepository = productRepository;
    }

    public void run(ApplicationArguments args) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        Coordinates labCoord = new Coordinates(1.0, 2.0);

        HttpEntity<String> requestContent = new HttpEntity<>(labInfo, httpHeaders);
        try {

            ResponseEntity<Lab> response = restTemplate.postForEntity("http://backend-engine:8080/store", requestContent,
                    Lab.class);

            /*if (response.getBody() == null) {
                return;
            }*/

            System.out.println("entrou");

            Lab newLab = new Lab(response.getBody().getId(), response.getBody().getToken(), "CT-TrackerDeliveries",
                    labCoord);

            coordinatesRepository.save(labCoord);
            labRepository.save(newLab);
        } catch (Exception e) {
            System.out.println(e);
            return;
        }

        productRepository.save(new Product("Split Test", 49.0, "Molecular", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus venenatis consectetur nibh et tincidunt. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; In ac turpis neque. produto 1.", "assets/images/product/split.png"));
        productRepository.save(new Product("Throat Test", 129.0, "Molecular", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus venenatis consectetur nibh et tincidunt. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; In ac turpis neque. produto 2.", "assets/images/product/throat.png"));
        productRepository.save(new Product("Nose Test",89.0, "Molecular", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus venenatis consectetur nibh et tincidunt. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; In ac turpis neque. produto 3.", "assets/images/product/nose.png"));
        productRepository.save(new Product("Nose Test", 59.0, "Antigen", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus venenatis consectetur nibh et tincidunt. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; In ac turpis neque. produto 4.", "assets/images/product/nose.png"));
        productRepository.save(new Product("Throat Test", 59.0, "Antigen", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus venenatis consectetur nibh et tincidunt. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; In ac turpis neque. produto 5.", "assets/images/product/throat.png"));
        productRepository.save(new Product("Take Blood", 79.0, "Antibody", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus venenatis consectetur nibh et tincidunt. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; In ac turpis neque. produto 6.", "assets/images/product/tirarsange.png"));
        productRepository.save(new Product("Take Blood",89.0, "Antibody", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus venenatis consectetur nibh et tincidunt. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; In ac turpis neque. produto 7.", "assets/images/product/fingerblood.png"));
    }
}
