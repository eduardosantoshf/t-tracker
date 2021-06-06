package deliveries_engine.service;

import deliveries_engine.model.Rider;
import org.springframework.stereotype.Component;


public interface RiderService {

    Rider registerRider(Rider rider) throws Exception;

}
