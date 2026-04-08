class SignatureVerificationService {
        private Map<String, String> accountSignatures = new HashMap<>();

        public SignatureVerificationService() {
            // Initialize with some sample signatures for testing
            accountSignatures.put("1001", "John Doe");
            accountSignatures.put("1002", "Jane Smith");
            accountSignatures.put("1003", "Robert Johnson");
        }

        /**
         * Verifies if the provided signature matches the one on file for the account.
         *
         * @param accountNumber The account number
         * @param signature The signature to verify
         * @return true if the signature is valid, false otherwise
         */
        public boolean verifySignature(String accountNumber, String signature) {
            System.out.println("Verifying signature for account: " + accountNumber);

            // If we don't have a signature on file, accept any signature (for demo purposes)
            if (!accountSignatures.containsKey(accountNumber)) {
                System.out.println("No signature on file for account: " + accountNumber + ". Accepting new signature.");
                accountSignatures.put(accountNumber, signature);
                return true;
            }

            // Compare the provided signature with the one on file
            String storedSignature = accountSignatures.get(accountNumber);
            boolean isValid = storedSignature.equals(signature);

            if (isValid) {
                System.out.println("Signature verified successfully.");
            } else {
                System.out.println("Signature verification failed.");
            }

            return isValid;
        }

        /**
         * Updates the signature on file for an account.
         *
         * @param accountNumber The account number
         * @param newSignature The new signature
         */
        public void updateSignature(String accountNumber, String newSignature) {
            accountSignatures.put(accountNumber, newSignature);
            System.out.println("Signature updated for account: " + accountNumber);
        }
    }

    /**
     * Module for processing cheques with signature verification, fraud detection,
     * currency conversion, and core banking system update.
     */
    static
class
