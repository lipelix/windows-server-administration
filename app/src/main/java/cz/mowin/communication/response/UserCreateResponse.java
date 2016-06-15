package cz.mowin.communication.response;

/**
 * Model class for user create. Response from server api is deserialized to this class.
 * @author Libor Vachal
 */
public class UserCreateResponse {

    /**
     * User is enabled
     */
    private String Enabled;

    /**
     * Logon name in Active Directory
     */
    private String SamAccountName;

    /**
     * Name in LDAP comma separated format
     */
    private String DistinguishedName;

    /**
     * Username in Active Directory
     */
    private String Name;

    /**
     * Date of creation
     */
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
