class ChequeApplication {

    public static void main(String[] args) {
        try {
            User authenticatedUser = null;
            Scanner scanner = new Scanner(System.in);

            System.out.println("Starting Cheque Processing System with Enhanced Fraud Detection...");
            
            // Initialize services
            CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService();
            SignatureVerificationService signatureVerificationService = new SignatureVerificationService();
            CoreBankingSystemUpdater coreBankingSystemUpdater = new CoreBankingSystemUpdater();
            UserService userService = new UserService(); // Initialize UserService
            ChequeHistoryManager chequeHistoryManager = new ChequeHistoryManager();
            FraudDetectionService fraudDetectionService = new FraudDetectionService();
            ExceptionReportManager exceptionReportManager = new ExceptionReportManager();
            ChequeStatusManager chequeStatusManager = new ChequeStatusManager();
            EmailNotificationService emailNotificationService = new EmailNotificationService();

            // Set up dependencies
            fraudDetectionService.setHistoryManager(chequeHistoryManager);

            // Initialize new services for image processing
            ChequeImageHandler imageHandler = new ChequeImageHandler();
            CryptographyService cryptoService = new CryptographyService();
            ClearinghouseService clearinghouseService = new ClearinghouseService();
            System.out.println("System initialized successfully.");

            // --- Login Process ---
            authenticatedUser = performLogin(scanner, userService);
            if (authenticatedUser == null) {
                System.out.println("Maximum login attempts reached. Exiting.");
                return; // Exit if login fails after retries
            }

            // Initialize cheque processor
            ChequeProcessor chequeProcessor = new ChequeProcessor(currencyExchangeService, signatureVerificationService,
                    coreBankingSystemUpdater, chequeHistoryManager, fraudDetectionService, exceptionReportManager, chequeStatusManager, emailNotificationService);
            
            System.out.println("System initialized successfully.");
    
            while (authenticatedUser != null) { // Continue loop only if authenticated
                System.out.println("\n--- Cheque Processing System ---");  
                System.out.println("1. Process a Single Cheque");  
                System.out.println("2. Process Multiple Cheques (Batch)");
                System.out.println("3. View Cheque History");  
                System.out.println("4. Currency Exchange Information");  
                System.out.println("5. Generate Cheque Reports");
                System.out.println("6. Scan, Encrypt, and Send Cheque Image"); // New Option
                System.out.println("7. Simulate Cheque Printing");
                System.out.println("8. Exit");  
                System.out.println("9. View Cheque Exception Report"); // New menu option
                System.out.println("10. View All Cheque Statuses"); // New menu option
                System.out.println("11. Cancel a Cheque"); // New menu option
                System.out.println("12. Record FIR/Legal Complaint for Bounced Cheque"); // New menu option
                System.out.println("13. Admin: Edit IFSC/Bank Codes");
                System.out.println("14. Admin: Manage Batches");
                System.out.println("15. Admin: Reset Stuck Transactions");
                System.out.print("Enter your choice: ");  
                int choice = scanner.nextInt();  
                scanner.nextLine(); // Consume newline  
    
                switch (choice) {  
                    case 1:  
                        System.out.println("Enter account number:");  
                        String accountNumber = scanner.nextLine();  
    
                        System.out.println("Enter cheque number:");  
                        String chequeNumber = scanner.nextLine();  
    
                        System.out.println("Enter currency (e.g., USD, EUR, GBP):");  
                        String currency = scanner.nextLine();  
    
                        System.out.println("Enter amount:");  
                        double amount = scanner.nextDouble();  
                        scanner.nextLine(); // Consume newline  
    
                        System.out.println("Enter signature:");  
                        String signature = scanner.nextLine();  
    
                        // Process the cheque  
                        chequeProcessor.processCheque(accountNumber, chequeNumber, currency, amount, signature);  
                        break;  
    
                    case 2:
                        // Process multiple cheques in a batch
                        processChequeBatch(scanner, chequeProcessor);
                        break;
    
                    case 3:  
                        System.out.println("Enter account number to view cheque history:");  
                        String historyAccountNumber = scanner.nextLine();  
                        chequeHistoryManager.displayChequeHistory(historyAccountNumber);  
                        break;  
    
                    case 4:
                        displayCurrencyExchangeMenu(scanner, currencyExchangeService);
                        break;
                        
                    case 5:
                        handleReportGeneration(scanner, chequeHistoryManager);
                        break;
    
                    case 6:
                        handleChequeImageSubmission(scanner, imageHandler, cryptoService, clearinghouseService, authenticatedUser);
                        break;
    
                    case 7:
                        // Assumes ChequePrintingService is an inner static
class
