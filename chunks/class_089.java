class FraudDetection {
        private static final double ABNORMAL_AMOUNT_THRESHOLD = 10000.0;
        private static final double SUSPICIOUS_ACTIVITY_MULTIPLIER = 10.0;
        private static final double AMOUNT_VARIANCE_THRESHOLD = 0.05;

        private Map<String, Set<String>> chequeRegistry = new HashMap<>();
        private Map<String, Double> accountActivity = new HashMap<>();
        private Map<String, List<TransactionRecord>> accountTransactionHistory = new HashMap<>();
        private Map<String, AccountProfile> accountProfiles = new HashMap<>();

        public boolean isDuplicateCheque(String accountId, String chequeNumber) {
            if (!chequeRegistry.containsKey(accountId)) {
                chequeRegistry.put(accountId, new HashSet<>());
            }
            Set<String> processedCheques = chequeRegistry.get(accountId);
            if (processedCheques.contains(chequeNumber)) {
                return true;
            }
            processedCheques.add(chequeNumber);
            return false;
        }

        public boolean isAbnormalAmount(double amount) {
            return amount > ABNORMAL_AMOUNT_THRESHOLD;
        }

        public boolean isSuspiciousActivity(String accountId, double amount) {
            double totalActivity = accountActivity.containsKey(accountId) ?
                    accountActivity.get(accountId) : 0.0;
            totalActivity += amount;
            accountActivity.put(accountId, totalActivity);
            recordTransaction(accountId, amount);
            updateAccountProfile(accountId, amount);
            boolean exceedsThreshold = totalActivity > ABNORMAL_AMOUNT_THRESHOLD * SUSPICIOUS_ACTIVITY_MULTIPLIER;
            boolean abnormalBehavior = isAbnormalBehavior(accountId, amount);
            return exceedsThreshold || abnormalBehavior;
        }

        private void recordTransaction(String accountId, double amount) {
            if (!accountTransactionHistory.containsKey(accountId)) {
                accountTransactionHistory.put(accountId, new ArrayList<>());
            }
            List<TransactionRecord> history = accountTransactionHistory.get(accountId);
            history.add(new TransactionRecord(amount, java.time.LocalDateTime.now()));
            java.time.LocalDateTime cutoff = java.time.LocalDateTime.now().minusDays(90);
            accountTransactionHistory.put(accountId, history.stream()
                    .filter(record -> record.timestamp.isAfter(cutoff))
                    .toList());
        }

        private void updateAccountProfile(String accountId, double amount) {
            if (!accountProfiles.containsKey(accountId)) {
                accountProfiles.put(accountId, new AccountProfile());
            }
            AccountProfile profile = accountProfiles.get(accountId);
            profile.updateWithTransaction(amount);
        }

        private boolean isAbnormalBehavior(String accountId, double amount) {
            if (!accountProfiles.containsKey(accountId)) {
                return false;
            }
            AccountProfile profile = accountProfiles.get(accountId);
            if (profile.transactionCount >= 5) {
                double avgAmount = profile.totalAmount / profile.transactionCount;
                double variance = Math.abs(amount - avgAmount) / avgAmount;
                return variance > AMOUNT_VARIANCE_THRESHOLD && amount > avgAmount;
            }
            return false;
        }

        private static
class
