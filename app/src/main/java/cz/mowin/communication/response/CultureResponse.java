package cz.mowin.communication.response;

public class CultureResponse {

    private String name;
    private String displayName;
    private String englishName;

    public CultureResponse(){}

    @Override
    public String toString() {
        return "name: " + name + '\n' +
                "displayName: " + displayName + '\n' +
                "englishName: " + englishName + '\n';
    }
}
