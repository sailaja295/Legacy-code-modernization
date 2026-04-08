class BatchCheque {
        String accountNumber;
        String chequeNumber;
        String currency;
        double amount;
        String signature;

        public BatchCheque(String accountNumber, String chequeNumber, String currency, double amount, String signature) {
            this.accountNumber = accountNumber;
            this.chequeNumber = chequeNumber;
            this.currency = currency;
            this.amount = amount;
            this.signature = signature;
        }
    }

    /**
     * Manages exception reports for cheques (bounced, duplicate, altered, delayed).
     * Now supports recording FIR/legal complaint details for bounced cheques.
     */
    static
class
