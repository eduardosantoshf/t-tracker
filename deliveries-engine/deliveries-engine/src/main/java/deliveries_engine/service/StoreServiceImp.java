package deliveries_engine.service;

import deliveries_engine.exception.ErrorWarning;
import deliveries_engine.model.Rider;
import deliveries_engine.model.Store;
import deliveries_engine.repository.RiderRepository;
import deliveries_engine.repository.StoreRepository;
import deliveries_engine.websocket.WarningController;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;

@Service
public class StoreServiceImp implements StoreService{

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    public WarningController warningController = new WarningController();

    public Store registerStore(Store store) throws Exception {

        Optional<Store> potentialStore = storeRepository.findByName(store.getName());

        if (potentialStore.isPresent()){
            throw new Exception("Store is already registered");
        }
        else{
            String token = JwtTokenService.generateToken(store.getName(), new DefaultClaims());
            store.setToken(token);
        }

        return storeRepository.save(store);
    }

    @Override
    public Rider getClosestRider(double latitude, double longitude, String token, int storeId) throws Exception {

        Optional<Store> store = storeRepository.findById(storeId);

        if(!store.isPresent()){
            throw new Exception("Invalid Store Id");
        }

        if(!token.equals(store.get().getToken())){
            throw new Exception("Invalid Store token");
        }

        HashMap<Rider, Double> map = new HashMap<>();

        List<Rider> riders = riderRepository.findAll();

        if (riders.size() == 0){
            throw new Exception("No riders available");
        }

        for(Rider r: riders){
            double riderLat = r.getLatitude();
            double riderLong = r.getLongitude();

            // The math module contains a function
            // named toRadians which converts from
            // degrees to radians.
            double lat1 = Math.toRadians(riderLat);
            double lat2 = Math.toRadians(latitude);
            double lon1 = Math.toRadians(riderLong);
            double lon2 = Math.toRadians(longitude);

            // Haversine formula
            double dlon = lon2 - lon1;
            double dlat = lat2 - lat1;
            double a = Math.pow(Math.sin(dlat / 2), 2)
                    + Math.cos(lat1) * Math.cos(lat2)
                    * Math.pow(Math.sin(dlon / 2),2);

            double c = 2 * Math.asin(Math.sqrt(a));

            // Radius of earth in kilometers. Use 3956
            // for miles
            double radius = 6371;

            // calculate the result
            double finalResult = c * radius;

            map.put(r, finalResult);
        }

        Rider responseRider = map.keySet().iterator().next();
        double minDistance = map.get(responseRider);
        for(Map.Entry<Rider, Double> entry: map.entrySet()){
            if(entry.getValue() < minDistance || entry.getKey().getStatus() == 1){
                responseRider = entry.getKey();
                minDistance = entry.getValue();
            }
        }

        JSONObject json = new JSONObject();
        json.put("latitude", latitude);
        json.put("longitude", longitude);

        warningController.send(json.toString());

        return responseRider;
    }
}
