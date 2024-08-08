import java.util.List;

class User {private String firstName;
    private String lastName;
    private String email;
    private List<String> roles;
    private List<String> phones;

    public User(String firstName, String lastName, String email, List<String> roles, List<String> phones) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
        this.phones = phones;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public List<String> getPhones() {
        return phones;
    }

    @Override
    public String toString() {
        return "User{" +
               "firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", email='" + email + '\'' +
               ", roles=" + roles +
               ", phones=" + phones +
               '}';
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }
}