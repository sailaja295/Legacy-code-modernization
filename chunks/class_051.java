class ChequeStatusManager {
        // Key: accountNumber + ":" + chequeNumber, Value: ChequeStatus
        private Map<String, ChequeStatus> chequeStatusMap = new HashMap<>();

        public void setStatus(String accountNumber, String chequeNumber, ChequeStatus status) {
            chequeStatusMap.put(accountNumber + ":" + chequeNumber, status);
            System.out.println("Status of cheque " + chequeNumber + " for account " + accountNumber + " set to " + status);
        }

        public ChequeStatus getStatus(String accountNumber, String chequeNumber) {
            return chequeStatusMap.getOrDefault(accountNumber + ":" + chequeNumber, null);
        }

        public void displayAllStatuses() {
            if (chequeStatusMap.isEmpty()) {
                System.out.println("No cheque statuses recorded.");
                return;
            }
            System.out.println("\n--- Cheque Statuses ---");
            for (Map.Entry<String, ChequeStatus> entry : chequeStatusMap.entrySet()) {
                String[] parts = entry.getKey().split(":");
                System.out.printf("Account: %s | Cheque: %s | Status: %s\n", parts[0], parts[1], entry.getValue());
            }
        }
    }

    /**
     * Simple Logger utility for error/info/debug logging.
     */
    static
class
