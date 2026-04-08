class ChequeProcessor {
        private CurrencyExchangeService currencyExchangeService;
        private SignatureVerificationService signatureVerificationService;
        private CoreBankingSystemUpdater coreBankingSystemUpdater;
        private ChequeHistoryManager chequeHistoryManager;
        private FraudDetectionService fraudDetectionService;

        public ChequeProcessor(CurrencyExchangeService currencyExchangeService,
                               SignatureVerificationService signatureVerificationService,
                               CoreBankingSystemUpdater coreBankingSystemUpdater,
                               ChequeHistoryManager chequeHistoryManager,
                               FraudDetectionService fraudDetectionService) {
            this.currencyExchangeService = currencyExchangeService;
            this.signatureVerificationService = signatureVerificationService;
            this.coreBankingSystemUpdater = coreBankingSystemUpdater;
            this.chequeHistoryManager = chequeHistoryManager;
            this.fraudDetectionService = fraudDetectionService;
        }

        public void processCheque(String accountNumber, String chequeNumber, String currency, double amount, String signature) {
            System.out.println("Processing cheque...");

            // Step 1: Verify signature
            if (!signatureVerificationService.verifySignature(accountNumber, signature)) {
                System.out.println("Signature verification failed. Cheque processing aborted.");
                return;
            }

            // Step 2: Fraud detection
            if (fraudDetectionService.isFraudulentCheque(accountNumber, chequeNumber, amount)) {
                System.out.println("Fraudulent cheque detected. Cheque processing aborted.");
                return;
            }

            // Step 3: Get detailed exchange rate information if currency is not local
            double amountInLocalCurrency = amount;
            if (!"USD".equalsIgnoreCase(currency)) { // USD is the base currency
                Map<String, Double> detailedRates = currencyExchangeService.getDetailedExchangeRates(currency);

                if (detailedRates.isEmpty()) {
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

            System.out.println("Cheque processed successfully.");
        }
    }

    /**
     * Enhanced Currency Exchange Service
     * Supports multiple currencies with detailed exchange rate calculations
     * and dynamic fetching of rates from external sources.
     */
    static
class
