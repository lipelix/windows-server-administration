package cz.lip.windowsserveradministration.communication.response;

/**
 * Created by Libor on 17.4.2016.
 */
public class DiskResponse {

    public String Name;
    public Double Free;
    public Double Used;
    public Double Total;

    @Override
    public String toString() {
        return "name: " + Name + '\n' +
                "free: " + Free + '\n' +
                "used: " + Used + '\n' +
                "total: " + Total + '\n';

    }
}
