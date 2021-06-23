package deliveries_engine.service;

import deliveries_engine.model.Delivery;
import deliveries_engine.model.Rider;
import deliveries_engine.model.Stats;
import deliveries_engine.model.Store;
import deliveries_engine.repository.DeliveryRepository;
import deliveries_engine.repository.RiderRepository;
import deliveries_engine.repository.StoreRepository;
import deliveries_engine.repository.UserRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServiceImp implements AdminService{

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;


    @Override
    public Stats getStats() {

        List<Rider> riderList = riderRepository.findAll();
        List<Store> storeList = storeRepository.findAll();
        List<Delivery> deliveryList = deliveryRepository.findAll();

        Stats stats = new Stats();

        stats.setRiders(riderList);
        stats.setStores(storeList);
        stats.setDeliveries(deliveryList);

        System.out.println(stats);

        return stats;
    }
}
