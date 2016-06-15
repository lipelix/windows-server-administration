package cz.mowin.communication.response;

/**
 * Model class for process item. Response from server api is deserialized to this class.
 * @author Libor Vachal
 */
public class ProcessResponse {

    private int Id;
    private String Name;
    private double CPU;
    private double CPUPercent;
    private String Description;
    private double WS;
    private double PeakWorkingSet;
    private String StartTime;
    private boolean Responding;

    @Override
    public String toString() {
        return "Id: " + Id + '\n' +
                "Name: " + Name + '\n' +
                "Responding: " + Responding + '\n' +
                "StartTime: " + StartTime + '\n' +
                "Description: " + Description + "\n\n" +

                "CPU Overtime Usage [%]: " + CPUPercent + '\n' +
                "CPU Time [s]: " + CPU + '\n' +
                "WorkingSet [MB]: " + WS + '\n' +
                "WorkingSet Peak [MB]: " + PeakWorkingSet + '\n';

    }
}
