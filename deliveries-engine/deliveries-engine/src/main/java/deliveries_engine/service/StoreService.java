package deliveries_engine.service;

import deliveries_engine.exception.ErrorWarning;
import deliveries_engine.model.Delivery;
import deliveries_engine.model.Rider;
import deliveries_engine.model.Store;

import java.util.List;

public interface StoreService {
    Store registerStore(Store store) throws Exception;
    Rider getClosestRider(Delivery delivery, double Latitude, double Longitude, String token, int storeId) throws Exception;
    List<Integer> updateRatings(int rating, String token, int storeId, int riderId) throws Exception;
    List<String> updateComments(String comment, String token, int storeId, int riderId) throws Exception;
    List<Integer> getRatings(String token, int storeId, int riderId) throws Exception;
    List<String> getComments(String token, int storeId, int riderId) throws Exception;


}
