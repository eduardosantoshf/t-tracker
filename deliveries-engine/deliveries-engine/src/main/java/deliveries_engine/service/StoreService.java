package deliveries_engine.service;

import deliveries_engine.model.Rider;
import deliveries_engine.model.Store;

public interface StoreService {
    Store registerStore(Store store) throws Exception;
    Rider getClosestRider(double Latitude, double Longitude);
}
