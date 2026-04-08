class EmailNotificationService {
    public void sendEmail(String to, String subject, String body) {
        // In a real system, integrate with JavaMail or an SMTP server.
        // For simulation, just print to console.
        System.out.println("\n--- EMAIL NOTIFICATION ---");
        System.out.println("To: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("Body:\n" + body);
        System.out.println("--------------------------\n");
    }
        }
        
    /**
     * Service for simulating cheque printing.
     */
    static
class
