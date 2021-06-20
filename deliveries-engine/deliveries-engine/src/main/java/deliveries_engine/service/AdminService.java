package deliveries_engine.service;

import deliveries_engine.model.Stats;
import org.json.JSONException;
import org.json.JSONObject;

public interface AdminService {
    Stats getStats();
}
