package deliveries_engine.service;

import deliveries_engine.exception.ErrorWarning;
import deliveries_engine.model.Store;
import deliveries_engine.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StoreServiceImp implements StoreService{

    @Autowired
    private StoreRepository storeRepository;

    public Store registerStore(Store store) throws ErrorWarning {

        Optional<Store> potentialStore = storeRepository.findByName(store.getName());

        if (potentialStore.isPresent()){
            throw new ErrorWarning("Store is already registered");
        }

        return storeRepository.save(store);
    }
}
