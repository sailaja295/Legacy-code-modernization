class UserService {
        private Map<String, User> users = new HashMap<>();

        public UserService() {
            // Add some sample users for demonstration
            registerUser("employee1", "password123", "EMPLOYEE");
            registerUser("account1001", "chequeuser", "ACCOUNT_HOLDER");
            registerUser("account1002", "securepass", "ACCOUNT_HOLDER");
        }

        /**
         * Registers a new user.
         * @param username The username
         * @param password The password
         * @param role The user's role
         */
        public void registerUser(String username, String password, String role) {
            users.put(username, new User(username, password, role));
            System.out.println("User registered: " + username + " (" + role + ")");
        }

        /**
         * Authenticates a user based on username and password.
         * @param username The username
         * @param password The password
         * @return The authenticated User object if successful, null otherwise
         */
        public User authenticate(String username, String password) {
            User user = users.get(username);
            if (user != null && user.getPassword().equals(password)) {
                System.out.println("Authentication successful for user: " + username);
                return user;
            }
            System.out.println("Authentication failed for user: " + username);
            return null;
        }
    }

    /**
     * Represents a single cheque transaction for batch processing.
     */
class
