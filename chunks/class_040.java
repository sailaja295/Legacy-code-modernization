class or accessible here
                        ChequePrintingService chequePrintingService = new ChequePrintingService();
                        handleChequePrinting(scanner, chequePrintingService);
                        break;
    
                    case 8:  
                        System.out.println("Logging out and exiting...");  
                        scanner.close();  
                        return;  
    
                    case 9:
                        exceptionReportManager.displayExceptions();
                        break;
    
                    case 10:
                        chequeStatusManager.displayAllStatuses();
                        break;
    
                    case 11:
                        System.out.print("Enter account number: ");
                        String cancelAccount = scanner.nextLine();
                        System.out.print("Enter cheque number: ");
                        String cancelCheque = scanner.nextLine();
                        chequeProcessor.cancelCheque(cancelAccount, cancelCheque);
                        break;

                    case 12:
                        System.out.print("Enter account number: ");
                        String firAccount = scanner.nextLine();
                        System.out.print("Enter cheque number: ");
                        String firCheque = scanner.nextLine();
                        System.out.print("Enter FIR/Complaint Number: ");
                        String firNumber = scanner.nextLine();
                        System.out.print("Enter Police Station: ");
                        String policeStation = scanner.nextLine();
                        System.out.print("Enter FIR/Complaint Date (YYYY-MM-DD): ");
                        String firDateStr = scanner.nextLine();
                        Date firDate;
                        try {
                            firDate = new SimpleDateFormat("yyyy-MM-dd").parse(firDateStr);
                        } catch (Exception e) {
                            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
                            break;
                        }
                        System.out.print("Enter Remarks: ");
                        String firRemarks = scanner.nextLine();
                        exceptionReportManager.recordFIRDetails(firAccount, firCheque, firNumber, policeStation, firDate, firRemarks);
                        break;

                    case 13:
                        System.out.println("\n--- Admin: Edit IFSC/Bank Codes ---");
                        System.out.println("1. Add/Update IFSC");
                        System.out.println("2. Add/Update Bank Code");
                        System.out.println("3. View IFSCs");
                        System.out.println("4. View Bank Codes");
                        System.out.println("5. Return");
                        System.out.print("Enter your choice: ");
                        int adminChoice = scanner.nextInt();
                        scanner.nextLine();
                        switch (adminChoice) {
                            case 1:
                                System.out.print("Enter IFSC: ");
                                String ifsc = scanner.nextLine();
                                System.out.print("Enter Bank Code: ");
                                String bankCode = scanner.nextLine();
                                adminService.addOrUpdateIFSC(ifsc, bankCode);
                                break;
                            case 2:
                                System.out.print("Enter Bank Code: ");
                                String code = scanner.nextLine();
                                System.out.print("Enter Bank Name: ");
                                String name = scanner.nextLine();
                                adminService.addOrUpdateBankCode(code, name);
                                break;
                            case 3:
                                adminService.displayIFSCs();
                                break;
                            case 4:
                                adminService.displayBankCodes();
                                break;
                            default:
                                break;
                        }
                        break;

                    case 14:
                        System.out.println("\n--- Admin: Manage Batches ---");
                        System.out.println("1. Create Batch");
                        System.out.println("2. View Batches");
                        System.out.println("3. View Batch Details");
                        System.out.println("4. Return");
                        System.out.print("Enter your choice: ");
                        int batchChoice = scanner.nextInt();
                        scanner.nextLine();
                        switch (batchChoice) {
                            case 1:
                                System.out.print("Enter Batch ID: ");
                                String batchId = scanner.nextLine();
                                System.out.print("Enter number of cheques in batch: ");
                                int numCheques = scanner.nextInt();
                                scanner.nextLine();
                                List<BatchCheque> batchCheques = new ArrayList<>();
                                for (int i = 0; i < numCheques; i++) {
                                    System.out.println("Enter details for Cheque #" + (i + 1) + ":");
                                    System.out.print("Account number: ");
                                    String acc = scanner.nextLine();
                                    System.out.print("Cheque number: ");
                                    String chq = scanner.nextLine();
                                    System.out.print("Currency: ");
                                    String curr = scanner.nextLine();
                                    System.out.print("Amount: ");
                                    double amt = scanner.nextDouble();
                                    scanner.nextLine();
                                    System.out.print("Signature: ");
                                    String sig = scanner.nextLine();
                                    batchCheques.add(new BatchCheque(acc, chq, curr, amt, sig));
                                }
                                adminService.createBatch(batchId, batchCheques);
                                break;
                            case 2:
                                adminService.displayBatches();
                                break;
                            case 3:
                                System.out.print("Enter Batch ID: ");
                                String viewBatchId = scanner.nextLine();
                                adminService.displayBatchDetails(viewBatchId);
                                break;
                            default:
                                break;
                        }
                        break;

                    case 15:
                        System.out.println("\n--- Admin: Reset Stuck Transactions ---");
                        System.out.println("1. Mark Cheque as Stuck");
                        System.out.println("2. Reset Stuck Cheque");
                        System.out.println("3. View Stuck Transactions");
                        System.out.println("4. Return");
                        System.out.print("Enter your choice: ");
                        int stuckChoice = scanner.nextInt();
                        scanner.nextLine();
                        switch (stuckChoice) {
                            case 1:
                                System.out.print("Enter Cheque Number to mark as stuck: ");
                                String stuckChq = scanner.nextLine();
                                adminService.markTransactionStuck(stuckChq);
                                break;
                            case 2:
                                System.out.print("Enter Cheque Number to reset: ");
                                String resetChq = scanner.nextLine();
                                adminService.resetStuckTransaction(resetChq);
                                break;
                            case 3:
                                adminService.displayStuckTransactions();
                                break;
                            default:
                                break;
                        }
                        break;
    
                    default:  
                        System.out.println("Invalid choice. Please try again.");  
                }
            }
        } catch (Exception ex) {
            Logger.error("Fatal error in main: " + ex.getMessage());
            ex.printStackTrace();
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
            try {
                System.out.println("\n--- Login ---");
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine(); // In a real app, use a secure way to read password

                User user = userService.authenticate(username, password);
                if (user != null) {
                    Logger.info("User logged in: " + username);
                    System.out.println("Welcome, " + user.getUsername() + " (" + user.getRole() + ")!");
                    return user; // Login successful
                } else {
                    Logger.warn("Failed login attempt for user: " + username);
                    System.out.println("Invalid username or password. Attempt " + attempt + " of " + MAX_ATTEMPTS);
                }
            } catch (Exception ex) {
                Logger.error("Error during login: " + ex.getMessage());
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
        try {
            System.out.println("\n--- Batch Cheque Processing ---");
            System.out.print("Enter the number of cheques in the batch: ");
            int batchSize = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            List<BatchCheque> chequesToProcess = new ArrayList<>();

            for (int i = 0; i < batchSize; i++) {
                try {
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
                } catch (Exception ex) {
                    Logger.error("Error collecting cheque batch input: " + ex.getMessage());
                    scanner.nextLine(); // Clear buffer
                }
            }

            System.out.println("\nProcessing batch...");
            chequesToProcess.forEach(cheque -> {
                try {
                    chequeProcessor.processCheque(cheque.accountNumber, cheque.chequeNumber, cheque.currency, cheque.amount, cheque.signature);
                } catch (Exception ex) {
                    Logger.error("Error processing cheque in batch: " + ex.getMessage());
                }
            });
        } catch (Exception ex) {
            Logger.error("Batch processing error: " + ex.getMessage());
        }
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

    /**
     * Handles the cheque printing simulation.
     * @param scanner The scanner for user input
     * @param printingService The cheque printing service
     */
    private static void handleChequePrinting(Scanner scanner, ChequePrintingService printingService) {
        System.out.println("\n--- Simulate Cheque Printing ---");

        System.out.print("Enter Payee Name: ");
        String payeeName = scanner.nextLine();

        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter Date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        Date chequeDate;
        try {
            chequeDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (java.text.ParseException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD. Using current date.");
            chequeDate = new Date();
        }

        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();

        System.out.print("Enter Cheque Number: ");
        String chequeNumber = scanner.nextLine();

        // You can make bankName configurable or a constant
        String bankName = "Global Trust Bank";

        printingService.printCheque(payeeName, amount, chequeDate, accountNumber, chequeNumber, bankName);
    }

    /**
     * Handles the process of "scanning", encrypting, signing, and "sending" a cheque image.
     * @param scanner Input scanner
     * @param imageHandler Service to handle image "upload"
     * @param cryptoService Service for encryption and signing
     * @param clearinghouseService Service to "send" to clearinghouse
     * @param currentUser The currently logged-in user
     */
    private static void handleChequeImageSubmission(Scanner scanner,
                                                    ChequeImageHandler imageHandler,
                                                    CryptographyService cryptoService,
                                                    ClearinghouseService clearinghouseService,
                                                    User currentUser) {
        System.out.println("\n--- Cheque Image Submission ---");
        System.out.print("Enter Account Number for the cheque: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter Cheque Number: ");
        String chequeNumber = scanner.nextLine();
        System.out.print("Enter path to cheque image file (e.g., /path/to/cheque.jpg): ");
        String imagePath = scanner.nextLine();

        // 1. Simulate scanning and uploading image
        byte[] imageData = imageHandler.loadImageData(imagePath);
        if (imageData == null) {
            System.out.println("Failed to load image data. Aborting submission.");
            return;
        }
        System.out.println("Cheque image \"uploaded\" successfully from: " + imagePath);

        // 2. Encrypt the image data
        // In a real system, key management would be crucial.
        String encryptionKey = "a-very-secure-encryption-key"; // Placeholder
        byte[] encryptedImageData = cryptoService.encryptData(imageData, encryptionKey);
        System.out.println("Image data encrypted.");

        // 3. Sign the encrypted image data
        String privateKey = currentUser.getUsername() + "-private-key"; // Placeholder
        String digitalSignature = cryptoService.signData(encryptedImageData, privateKey);
        System.out.println("Encrypted image data signed. Signature: " + digitalSignature.substring(0, 10) + "..."); // Show partial signature

        // 4. Send to clearinghouse
        clearinghouseService.submitToClearinghouse(accountNumber, chequeNumber, encryptedImageData, digitalSignature);
    }

    // --- Existing Inner Classes (User, UserService, BatchCheque) ---
    // ... (Keep existing inner classes as they are)
    static
class
