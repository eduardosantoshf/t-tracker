package deliveries_engine.service;

import deliveries_engine.exception.ErrorWarning;
import deliveries_engine.model.Delivery;
import deliveries_engine.model.Rider;

import java.util.List;

public interface RiderService {

    Rider registerRider(Rider rider) throws Exception;
    Rider updateLocation(double latitude, double longitude, Rider rider) throws Exception;

    Rider updateStatus(int status, Rider rider) throws Exception;

    List<Delivery> getDeliveries(Rider rider);
}
