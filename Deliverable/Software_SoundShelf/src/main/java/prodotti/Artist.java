package prodotti;

public class Artist {

    private String firstName;
    private String lastName;  
    private String stageName;  

    public Artist(String firstName, String lastName, String stageName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.stageName = stageName;
    }

    public Artist() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    @Override
    public String toString() {
        return "Artist [firstName=" + firstName + ", lastName=" + lastName + ", stageName=" + stageName + "]";
    }
}

