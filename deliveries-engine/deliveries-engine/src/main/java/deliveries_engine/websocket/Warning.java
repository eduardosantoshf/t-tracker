package deliveries_engine.websocket;

public class Warning {
    private String username;

    public Warning(String username){
        this.username=username;
    }
    public String getUsername(){
        return this.username;
    }
}
