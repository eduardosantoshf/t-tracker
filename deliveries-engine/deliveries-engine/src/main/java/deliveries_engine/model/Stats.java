package deliveries_engine.model;

import java.util.ArrayList;
import java.util.List;

public class Stats {

    private List<Rider> riders;
    private List<Store> stores;
    private List<Delivery> deliveries;

    public Stats() {
        this.riders = new ArrayList<>();
        this.stores = new ArrayList<>();
        this.deliveries = new ArrayList<>();
    }

    public List<Rider> getRiders() {
        return riders;
    }

    public void setRiders(List<Rider> riders) {
        this.riders = riders;
    }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

    @Override
    public String toString() {
        return "Stats{" +
                "riders=" + riders +
                ", stores=" + stores +
                ", deliveries=" + deliveries +
                '}';
    }
}
