class ClearinghouseService {
        /**
         * Simulates submitting cheque image data and signature to a clearinghouse.
         * @param accountNumber The account number.
         * @param chequeNumber The cheque number.
         * @param encryptedImageData The encrypted image data.
         * @param digitalSignature The digital signature.
         */
        public void submitToClearinghouse(String accountNumber, String chequeNumber, byte[] encryptedImageData, String digitalSignature) {
            System.out.println("\n--- Submitting to Clearinghouse ---");
            System.out.println("Account: " + accountNumber + ", Cheque: " + chequeNumber);
            System.out.println("Encrypted Data Length: " + encryptedImageData.length + " bytes");
            System.out.println("Digital Signature: " + digitalSignature.substring(0, Math.min(digitalSignature.length(), 20)) + "..."); // Show partial signature
            System.out.println("Simulating sending via SFTP/REST API...");
            System.out.println("Submission to clearinghouse successful (simulated).");
            System.out.println("---------------------------------");
        }
    }


    /**
     * Service for verifying signatures on cheques.
     * This is a simplified implementation for demonstration purposes.
     */
    static
class
