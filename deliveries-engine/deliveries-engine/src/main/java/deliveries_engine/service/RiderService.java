package deliveries_engine.service;

import deliveries_engine.exception.ErrorWarning;
import deliveries_engine.model.Rider;

public interface RiderService {

    Rider registerRider(Rider rider) throws ErrorWarning;

}
