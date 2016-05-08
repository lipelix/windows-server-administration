package cz.mowin.communication.response;

import java.text.SimpleDateFormat;
import java.util.Date;

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
