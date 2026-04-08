class CreateApplication {
    public static void main(String[] args) {
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

        // Set up dependencies
        fraudDetectionService.setHistoryManager(chequeHistoryManager);

        System.out.println("System initialized successfully.");

        // --- Login Process ---
        authenticatedUser = performLogin(scanner, userService);
        if (authenticatedUser == null) {
            System.out.println("Maximum login attempts reached. Exiting.");
            return; // Exit if login fails after retries
        }

        // Initialize cheque processor
        ChequeProcessor chequeProcessor = new ChequeProcessor(currencyExchangeService, signatureVerificationService,
                coreBankingSystemUpdater, chequeHistoryManager, fraudDetectionService);
        
        System.out.println("System initialized successfully.");
  
        while (authenticatedUser != null) { // Continue loop only if authenticated
            System.out.println("\n--- Cheque Processing System ---");  
            System.out.println("1. Process a Single Cheque");  
            System.out.println("2. Process Multiple Cheques (Batch)");
            System.out.println("3. View Cheque History");  
            System.out.println("4. Currency Exchange Information");  
            System.out.println("5. Generate Cheque Reports");
            System.out.println("6. Exit");  
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
                    System.out.println("Exiting...");  
                    scanner.close();  
                    return;  
  
                default:  
                    System.out.println("Invalid choice. Please try again.");  
            }
        }
    }

    /**
     * Handles the user login process.
     * @param scanner The scanner for user input
     * @param userService The user service for authentication
     * @return The authenticated User object, or null if login fails after retries
     */
    private static User performLogin(Scanner scanner, UserService userService) {
        final int MAX_ATTEMPTS = 3;
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            System.out.println("\n--- Login ---");
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine(); // In a real app, use a secure way to read password

            User user = userService.authenticate(username, password);
            if (user != null) {
                System.out.println("Welcome, " + user.getUsername() + " (" + user.getRole() + ")!");
                return user; // Login successful
            } else {
                System.out.println("Invalid username or password. Attempt " + attempt + " of " + MAX_ATTEMPTS);
            }
        }
        return null; // Login failed after max attempts
    }

    /**
     * Handles processing multiple cheques in a batch.
     * Prompts the user for the number of cheques and their details.
     * @param scanner The scanner for user input
     * @param chequeProcessor The cheque processor service
     */
    private static void processChequeBatch(Scanner scanner, ChequeProcessor chequeProcessor) {
        System.out.println("\n--- Batch Cheque Processing ---");
        System.out.print("Enter the number of cheques in the batch: ");
        int batchSize = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        List<BatchCheque> chequesToProcess = new ArrayList<>();

        for (int i = 0; i < batchSize; i++) {
            System.out.println("\nEnter details for Cheque #" + (i + 1) + ":");
            System.out.print("Account number: ");
            String accountNumber = scanner.nextLine();
            System.out.print("Cheque number: ");
            String chequeNumber = scanner.nextLine();
            System.out.print("Currency (e.g., USD, EUR, GBP): ");
            String currency = scanner.nextLine();
            System.out.print("Amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
            System.out.print("Signature: ");
            String signature = scanner.nextLine();

            chequesToProcess.add(new BatchCheque(accountNumber, chequeNumber, currency, amount, signature));
        }

        System.out.println("\nProcessing batch...");
        chequesToProcess.forEach(cheque -> chequeProcessor.processCheque(cheque.accountNumber, cheque.chequeNumber, cheque.currency, cheque.amount, cheque.signature));
    }
    
    /**
     * Display the currency exchange menu and handle user interactions
     * @param scanner The scanner for user input
     * @param currencyExchangeService The currency exchange service
     */
    private static void displayCurrencyExchangeMenu(Scanner scanner, CurrencyExchangeService currencyExchangeService) {
        while (true) {
            System.out.println("\n--- Currency Exchange Menu ---");
            System.out.println("1. View Supported Currencies");
            System.out.println("2. Get Exchange Rate");
            System.out.println("3. Get Detailed Exchange Rate Information");
            System.out.println("4. Convert Currency");
            System.out.println("5. Return to Main Menu");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    // Display supported currencies
                    List<String> supportedCurrencies = currencyExchangeService.getSupportedCurrencies();
                    System.out.println("\nSupported Currencies:");
                    for (String currencyCode : supportedCurrencies) {
                        System.out.println("- " + currencyCode);
                    }
                    break;
                    
                case 2:
                    // Get exchange rate
                    System.out.print("Enter currency code: ");
                    String currencyCode = scanner.nextLine().toUpperCase();
                    double rate = currencyExchangeService.getExchangeRate(currencyCode);
                    
                    if (rate > 0) {
                        System.out.println("Exchange rate for " + currencyCode + ": " + rate);
                    } else {
                        System.out.println("Currency not supported or exchange rate unavailable.");
                    }
                    break;
                    
                case 3:
                    // Get detailed exchange rate information
                    System.out.print("Enter currency code: ");
                    String detailCurrencyCode = scanner.nextLine().toUpperCase();
                    Map<String, Double> detailedRates = currencyExchangeService.getDetailedExchangeRates(detailCurrencyCode);
                    
                    if (!detailedRates.isEmpty()) {
                        System.out.println("\nDetailed Exchange Rate Information for " + detailCurrencyCode + ":");
                        System.out.println("Mid Rate: " + detailedRates.get("mid"));
                        System.out.println("Buy Rate: " + detailedRates.get("buy"));
                        System.out.println("Sell Rate: " + detailedRates.get("sell"));
                        System.out.println("Fee Rate: " + detailedRates.get("fee"));
                    } else {
                        System.out.println("Currency not supported or exchange rate unavailable.");
                    }
                    break;
                    
                case 4:
                    // Convert currency
                    System.out.print("Enter amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    
                    System.out.print("Enter source currency code: ");
                    String fromCurrency = scanner.nextLine().toUpperCase();
                    
                    System.out.print("Enter target currency code: ");
                    String toCurrency = scanner.nextLine().toUpperCase();
                    
                    double convertedAmount = currencyExchangeService.convertCurrency(amount, fromCurrency, toCurrency);
                    
                    if (convertedAmount > 0) {
                        System.out.printf("%.2f %s = %.2f %s\n", amount, fromCurrency, convertedAmount, toCurrency);
                    } else {
                        System.out.println("Currency conversion failed. Please check the currency codes.");
                    }
                    break;
                    
                case 5:
                    // Return to main menu
                    return;
                    
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Handles the report generation menu and logic.
     * @param scanner The scanner for user input
     * @param chequeHistoryManager The cheque history manager
     */
    private static void handleReportGeneration(Scanner scanner, ChequeHistoryManager chequeHistoryManager) {
        System.out.println("\n--- Generate Cheque Reports ---");
        System.out.println("1. Daily Report (Today)");
        System.out.println("2. Weekly Report (Last 7 Days)");
        System.out.println("3. Monthly Report (Last 30 Days)");
        System.out.println("4. Custom Date Range Report");
        System.out.println("5. Return to Main Menu");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        LocalDate endDate = LocalDate.now();
        LocalDate startDate;
        String reportNamePrefix;

        switch (choice) {
            case 1: // Daily
                startDate = endDate;
                reportNamePrefix = "daily_report_";
                break;
            case 2: // Weekly
                startDate = endDate.minusDays(6); // Last 7 days including today
                reportNamePrefix = "weekly_report_";
                break;
            case 3: // Monthly
                startDate = endDate.minusDays(29); // Last 30 days including today
                reportNamePrefix = "monthly_report_";
                break;
            case 4: // Custom
                System.out.print("Enter start date (YYYY-MM-DD): ");
                String startDateStr = scanner.nextLine();
                System.out.print("Enter end date (YYYY-MM-DD): ");
                String endDateStr = scanner.nextLine();
                try {
                    startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
                    endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (Exception e) {
                    System.out.println("Invalid date format. Please use YYYY-MM-DD.");
                    return;
                }
                if (startDate.isAfter(endDate)) {
                    System.out.println("Start date cannot be after end date.");
                    return;
                }
                reportNamePrefix = "custom_report_";
                break;
            case 5:
                return; // Return to main menu
            default:
                System.out.println("Invalid choice. Please try again.");
                return;
        }

        List<ChequeHistoryManager.ChequeRecord> records = chequeHistoryManager.getAllChequeRecordsInPeriod(startDate, endDate);

        if (records.isEmpty()) {
            System.out.println("No cheque records found for the selected period.");
            return;
        }

        String csvData = chequeHistoryManager.generateChequeReportCSV(records);
        String fileName = reportNamePrefix + startDate.format(DateTimeFormatter.ISO_LOCAL_DATE) +
                          "_to_" + endDate.format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(csvData);
            System.out.println("Report generated successfully: " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing report to file: " + e.getMessage());
        }
    }


    // --- Existing Inner Classes (User, UserService, BatchCheque) ---
    // ... (Keep existing inner classes as they are)

    /**
     * Service for verifying signatures on cheques.
     * This is a simplified implementation for demonstration purposes.
     */
    static
class
