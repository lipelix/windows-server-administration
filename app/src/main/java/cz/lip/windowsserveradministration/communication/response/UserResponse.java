package cz.lip.windowsserveradministration.communication.response;

import java.sql.Date;

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
    private String LastLogonDate;
    private String PasswordLastSet;

    @Override
    public String toString() {
        return "GivenName: " + GivenName + '\n' +
                "Surname: " + Surname + '\n' +
                "Enabled: " + Enabled + '\n' +
                "SamAccountName: " + SamAccountName + '\n' +
                "DistinguishedName: " + DistinguishedName + '\n' +
                "Name: " + Name + '\n' +
                "AccountExpirationDate: " + AccountExpirationDate + '\n' +
                "DisplayName: " + DisplayName + '\n' +
                "EmailAddress: " + EmailAddress + '\n' +
                "Created: " + Created + '\n' +
                "LastLogonDate: " + LastLogonDate + '\n' +
                "PasswordLastSet: " + PasswordLastSet + '\n';

    }
}
