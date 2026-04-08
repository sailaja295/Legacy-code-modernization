class ChequeProcessor {
        private CurrencyExchangeService currencyExchangeService;
        private SignatureVerificationService signatureVerificationService;
        private CoreBankingSystemUpdater coreBankingSystemUpdater;
        private ChequeHistoryManager chequeHistoryManager;
        private FraudDetectionService fraudDetectionService;
        private ExceptionReportManager exceptionReportManager;
        private ChequeStatusManager chequeStatusManager;
        private EmailNotificationService emailNotificationService;

        public ChequeProcessor(CurrencyExchangeService currencyExchangeService,
                               SignatureVerificationService signatureVerificationService,
                               CoreBankingSystemUpdater coreBankingSystemUpdater,
                               ChequeHistoryManager chequeHistoryManager,
                               FraudDetectionService fraudDetectionService,
                               ExceptionReportManager exceptionReportManager,
                               ChequeStatusManager chequeStatusManager,
                               EmailNotificationService emailNotificationService) {
            this.currencyExchangeService = currencyExchangeService;
            this.signatureVerificationService = signatureVerificationService;
            this.coreBankingSystemUpdater = coreBankingSystemUpdater;
            this.chequeHistoryManager = chequeHistoryManager;
            this.fraudDetectionService = fraudDetectionService;
            this.exceptionReportManager = exceptionReportManager;
            this.chequeStatusManager = chequeStatusManager;
            this.emailNotificationService = emailNotificationService;
        }

        public void processCheque(String accountNumber, String chequeNumber, String currency, double amount, String signature) {
            try {
                // Mark as issued if not already tracked
                if (chequeStatusManager.getStatus(accountNumber, chequeNumber) == null) {
                    chequeStatusManager.setStatus(accountNumber, chequeNumber, ChequeStatus.ISSUED);
                }

                Logger.info("Processing cheque: " + chequeNumber + " for account: " + accountNumber);

                System.out.println("Processing cheque...");

                // Step 1: Verify signature
                if (!signatureVerificationService.verifySignature(accountNumber, signature)) {
                    exceptionReportManager.reportException(accountNumber, chequeNumber, "Altered", "Signature mismatch");
                    Logger.warn("Signature verification failed for cheque: " + chequeNumber);
                    System.out.println("Signature verification failed. Cheque processing aborted.");
                    // Send email notification
                    emailNotificationService.sendEmail(
                        accountNumber + "@bank.com",
                        "Cheque Validation Failure",
                        "Cheque " + chequeNumber + " for account " + accountNumber + " failed signature verification."
                    );
                    return;
                }

                // Step 2: Fraud detection
                if (fraudDetectionService.isFraudulentCheque(accountNumber, chequeNumber, amount)) {
                    exceptionReportManager.reportException(accountNumber, chequeNumber, "Duplicate", "Fraudulent or duplicate cheque detected");
                    Logger.warn("Fraudulent cheque detected: " + chequeNumber);
                    System.out.println("Fraudulent cheque detected. Cheque processing aborted.");
                    // Send email notification
                    emailNotificationService.sendEmail(
                        accountNumber + "@bank.com",
                        "Fraud Detection Alert",
                        "Potential fraud detected for cheque " + chequeNumber + " on account " + accountNumber + "."
                    );
                    return;
                }

                // Simulate bounced cheque (for demo, if amount > 50000)
                if (amount > 50000) {
                    exceptionReportManager.reportException(accountNumber, chequeNumber, "Bounced", "Insufficient funds (simulated)");
                    Logger.warn("Cheque bounced due to high amount: " + chequeNumber);
                    System.out.println("Cheque bounced due to insufficient funds. Cheque processing aborted.");
                    // Send email notification
                    emailNotificationService.sendEmail(
                        accountNumber + "@bank.com",
                        "Cheque Bounced Notification",
                        "Cheque " + chequeNumber + " for account " + accountNumber + " has bounced due to insufficient funds."
                    );
                    return;
                }

                // Simulate delayed cheque (for demo, if cheque number ends with '9')
                if (chequeNumber.endsWith("9")) {
                    exceptionReportManager.reportException(accountNumber, chequeNumber, "Delayed", "Cheque processing delayed (simulated)");
                    Logger.info("Cheque processing delayed for cheque: " + chequeNumber);
                    System.out.println("Cheque processing delayed (simulated).");
                    // Optional: send notification for delayed cheques if desired
                }

                // Step 3: Get detailed exchange rate information if currency is not local
                double amountInLocalCurrency = amount;
                if (!"USD".equalsIgnoreCase(currency)) { // USD is the base currency
                    Map<String, Double> detailedRates = currencyExchangeService.getDetailedExchangeRates(currency);

                    if (detailedRates.isEmpty()) {
                        Logger.error("Exchange rate unavailable for currency: " + currency);
                        System.out.println("Failed to fetch exchange rate. Cheque processing aborted.");
                        return;
                    }

                    // Use the buy rate for incoming transactions
                    double buyRate = detailedRates.get("buy");
                    double fee = detailedRates.get("fee");

                    // Step 4: Convert amount to local currency with detailed calculations
                    amountInLocalCurrency = amount * buyRate;
                    double feeAmount = amount * fee;

                    System.out.println("Currency: " + currency.toUpperCase());
                    System.out.println("Original amount: " + amount);
                    System.out.println("Exchange rate (buy): " + buyRate);
                    System.out.println("Fee rate: " + fee);
                    System.out.println("Fee amount: " + feeAmount);
                    System.out.println("Amount in local currency (before fees): " + amountInLocalCurrency);

                    // Apply fee
                    amountInLocalCurrency -= feeAmount;
                    System.out.println("Final amount in local currency (USD): " + amountInLocalCurrency);
                } else {
                    System.out.println("Processing in local currency (USD): " + amountInLocalCurrency);
                }

                // Step 5: Update core banking system
                coreBankingSystemUpdater.updateCoreBankingSystem(accountNumber, amountInLocalCurrency);

                // Step 6: Record cheque history
                chequeHistoryManager.recordCheque(accountNumber, chequeNumber, currency, amount, new java.util.Date());

                // If cheque is processed successfully:
                chequeStatusManager.setStatus(accountNumber, chequeNumber, ChequeStatus.PROCESSED);
                Logger.info("Cheque processed successfully: " + chequeNumber);
                System.out.println("Cheque processed successfully.");
            } catch (Exception ex) {
                Logger.error("Error processing cheque " + chequeNumber + ": " + ex.getMessage());
                exceptionReportManager.reportException(accountNumber, chequeNumber, "ProcessingError", ex.getMessage());
                System.out.println("An error occurred during cheque processing. Please check logs.");
                // Send email notification for processing error
                emailNotificationService.sendEmail(
                    accountNumber + "@bank.com",
                    "Cheque Processing Error",
                    "An error occurred while processing cheque " + chequeNumber + " for account " + accountNumber + ": " + ex.getMessage()
                );
            }
        }

        // Add a method to cancel a cheque
        public void cancelCheque(String accountNumber, String chequeNumber) {
            try {
                chequeStatusManager.setStatus(accountNumber, chequeNumber, ChequeStatus.CANCELED);
                Logger.info("Cheque canceled: " + chequeNumber + " for account: " + accountNumber);
                System.out.println("Cheque " + chequeNumber + " for account " + accountNumber + " has been canceled.");
            } catch (Exception ex) {
                Logger.error("Error canceling cheque " + chequeNumber + ": " + ex.getMessage());
                System.out.println("An error occurred while canceling the cheque.");
            }
        }
    }

    /**
     * Enhanced Currency Exchange Service
     * Supports multiple currencies with detailed exchange rate calculations
     * and dynamic fetching of rates from external sources.
     */
    static
class
