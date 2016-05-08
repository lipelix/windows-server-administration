package cz.mowin.communication.response;

public class UserResponse {

    private String GivenName;
    private String Surname;
    private String Enabled;
    private String SamAccountName;
    private String DistinguishedName;
    private String Name;
    private String AccountExpirationDate;
    private String DisplayName;
    private String EmailAddress;
    private String Created;
    private String LastLogon;
    private String PasswordLastSet;

    @Override
    public String toString() {
        return "SamAccountName: " + SamAccountName + '\n' +
                "Name: " + Name + '\n' +
                "GivenName: " + GivenName + '\n' +
                "Surname: " + Surname + '\n' +
                "DisplayName: " + DisplayName + '\n' +
                "EmailAddress: " + EmailAddress + "\n\n" +
                "Enabled: " + Enabled + '\n' +
                "AccountExpiration: " + AccountExpirationDate + '\n' +
                "Created: " + Created + '\n' +
                "LastLogon: " + LastLogon + '\n' +
                "PasswordLastSet: " + PasswordLastSet + '\n';

    }
}
