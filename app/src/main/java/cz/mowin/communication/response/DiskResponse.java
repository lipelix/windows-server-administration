package cz.mowin.communication.response;

/**
 * Model class for drive item. Response from server api is deserialized to this class.
 * @author Libor Vachal
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
