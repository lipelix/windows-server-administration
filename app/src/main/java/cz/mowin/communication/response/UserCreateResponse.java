package cz.mowin.communication.response;

public class UserCreateResponse {

    private String Enabled;
    private String SamAccountName;
    private String DistinguishedName;
    private String Name;
    private String Created;

    @Override
    public String toString() {
        return "Enabled: " + Enabled + '\n' +
                "SamAccountName: " + SamAccountName + '\n' +
                "DistinguishedName: " + DistinguishedName + '\n' +
                "Name: " + Name + '\n' +
                "Created: " + Created + '\n';

    }
}
