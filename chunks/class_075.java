class AdminService {
        // Master data: IFSC and bank codes
        private Map<String, String> ifscToBankCode = new HashMap<>();
        private Map<String, String> bankCodeToName = new HashMap<>();

        // Batch management
        private Map<String, List<BatchCheque>> batches = new HashMap<>();
        // Stuck transactions (for demo, just a list of cheque numbers)
        private Set<String> stuckTransactions = new HashSet<>();

        // --- Master Data Management ---
        public void addOrUpdateIFSC(String ifsc, String bankCode) {
            ifscToBankCode.put(ifsc, bankCode);
            System.out.println("IFSC " + ifsc + " mapped to bank code " + bankCode);
        }

        public void addOrUpdateBankCode(String bankCode, String bankName) {
            bankCodeToName.put(bankCode, bankName);
            System.out.println("Bank code " + bankCode + " mapped to bank name " + bankName);
        }

        public void displayIFSCs() {
            System.out.println("\n--- IFSC to Bank Code Mapping ---");
            if (ifscToBankCode.isEmpty()) {
                System.out.println("No IFSC records.");
            } else {
                ifscToBankCode.forEach((ifsc, code) -> System.out.println("IFSC: " + ifsc + " -> Bank Code: " + code));
            }
        }

        public void displayBankCodes() {
            System.out.println("\n--- Bank Code to Name Mapping ---");
            if (bankCodeToName.isEmpty()) {
                System.out.println("No bank code records.");
            } else {
                bankCodeToName.forEach((code, name) -> System.out.println("Bank Code: " + code + " -> Name: " + name));
            }
        }

        // --- Batch Management ---
        public void createBatch(String batchId, List<BatchCheque> cheques) {
            batches.put(batchId, new ArrayList<>(cheques));
            System.out.println("Batch " + batchId + " created with " + cheques.size() + " cheques.");
        }

        public void displayBatches() {
            System.out.println("\n--- Batch List ---");
            if (batches.isEmpty()) {
                System.out.println("No batches available.");
            } else {
                batches.forEach((batchId, cheques) -> {
                    System.out.println("Batch ID: " + batchId + " | Cheques: " + cheques.size());
                });
            }
        }

        public void displayBatchDetails(String batchId) {
            List<BatchCheque> cheques = batches.get(batchId);
            if (cheques == null) {
                System.out.println("Batch not found.");
                return;
            }
            System.out.println("Batch " + batchId + " details:");
            for (BatchCheque cheque : cheques) {
                System.out.printf("Account: %s | Cheque: %s | Amount: %.2f | Currency: %s\n",
                    cheque.accountNumber, cheque.chequeNumber, cheque.amount, cheque.currency);
            }
        }

        // --- Stuck Transaction Management ---
        public void markTransactionStuck(String chequeNumber) {
            stuckTransactions.add(chequeNumber);
            System.out.println("Cheque " + chequeNumber + " marked as stuck.");
        }

        public void resetStuckTransaction(String chequeNumber) {
            if (stuckTransactions.remove(chequeNumber)) {
                System.out.println("Cheque " + chequeNumber + " reset (removed from stuck list).");
            } else {
                System.out.println("Cheque " + chequeNumber + " was not marked as stuck.");
            }
        }

        public void displayStuckTransactions() {
            System.out.println("\n--- Stuck Transactions ---");
            if (stuckTransactions.isEmpty()) {
                System.out.println("No stuck transactions.");
            } else {
                stuckTransactions.forEach(cheque -> System.out.println("Cheque: " + cheque));
            }
        }
    }
}



/**
 * Represents a user of the system (employee or account holder).
 */
static
class
