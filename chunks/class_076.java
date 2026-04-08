class User {
    private String username;
    private String password; // In a real app, this would be hashed
    private String role; // e.g., "EMPLOYEE", "ACCOUNT_HOLDER"

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}

    /**
     * Service for managing users and handling authentication.
     */
static
class
