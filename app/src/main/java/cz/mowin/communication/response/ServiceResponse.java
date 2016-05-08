package cz.mowin.communication.response;

public class ServiceResponse {

    private String DisplayName;
    private String Name;
    private int StartType;
    private int Status;

    public String getStatus() {
        if (Status == 4)
            return "Running";
        else if (Status == 1)
            return "Stopped";
        else
            return "Unknown";
    }

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
