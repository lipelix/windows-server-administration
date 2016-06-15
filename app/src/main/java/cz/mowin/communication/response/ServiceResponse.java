package cz.mowin.communication.response;

/**
 * Model class for service item. Response from server api is deserialized to this class.
 * @author Libor Vachal
 */
public class ServiceResponse {

    private String DisplayName;
    private String Name;
    private int StartType;
    private int Status;

    /**
     * Get string of service status
     * @return Running, Stopped or Unknown
     */
    public String getStatus() {
        if (Status == 4)
            return "Running";
        else if (Status == 1)
            return "Stopped";
        else
            return "Unknown";
    }

    /**
     * Get string of service start type
     * @return Automatic, Manual, Disabled or Unknown
     */
    public String getStartType() {
        if (StartType == 2)
            return "Automatic";
        else if (StartType == 3)
            return "Manual";
        else if (StartType == 4)
            return "Disabled";
        else
            return "Unknown";
    }

    @Override
    public String toString() {
        return "DisplayName: " + DisplayName + '\n' +
                "Name: " + Name + '\n' +
                "StartType: " + getStartType() + '\n' +
                "Status: " + getStatus() + '\n';

    }
}
