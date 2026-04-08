enum AlertLevel {
            LOW,
            MEDIUM,
            HIGH,
            CRITICAL
        }

        public FraudDetectionService() {
            this.fraudDetection = new FraudDetection();
            this.recentTransactions = new HashMap<>();
        }

        public void setHistoryManager(ChequeHistoryManager historyManager) {
            this.historyManager = historyManager;
        }

        public boolean isFraudulentCheque(String accountId, String chequeNumber, double amount) {
            boolean isDuplicate = checkDuplicateCheque(accountId, chequeNumber);
            boolean isAbnormal = checkAbnormalAmount(amount);
            boolean isSuspicious = checkSuspiciousActivity(accountId, amount);
            boolean isVelocityFraud = checkVelocityFraud(accountId, amount);
            boolean isPatternFraud = checkPatternFraud(accountId, amount);

            boolean isHistoricalDuplicate = false;
            boolean isUnusualFrequency = false;
            boolean isSimilarToRecent = false;

            if (historyManager != null) {
                isHistoricalDuplicate = checkHistoricalDuplicate(accountId, chequeNumber);
                isUnusualFrequency = checkUnusualFrequency(accountId);
                isSimilarToRecent = checkSimilarToRecent(accountId, amount);
            }

            logFraudChecks(accountId, chequeNumber, amount, isDuplicate, isAbnormal,
                    isSuspicious, isVelocityFraud, isPatternFraud,
                    isHistoricalDuplicate, isUnusualFrequency, isSimilarToRecent);

            AlertLevel alertLevel = determineAlertLevel(isDuplicate, isAbnormal,
                    isSuspicious, isVelocityFraud, isPatternFraud,
                    isHistoricalDuplicate, isUnusualFrequency, isSimilarToRecent);

            System.out.println("Fraud Alert Level: " + alertLevel);

            return isDuplicate || isAbnormal || isSuspicious || isVelocityFraud || isPatternFraud ||
                    isHistoricalDuplicate || isUnusualFrequency || isSimilarToRecent;
        }

        private boolean checkDuplicateCheque(String accountId, String chequeNumber) {
            return fraudDetection.isDuplicateCheque(accountId, chequeNumber);
        }

        private boolean checkAbnormalAmount(double amount) {
            return fraudDetection.isAbnormalAmount(amount);
        }

        private boolean checkSuspiciousActivity(String accountId, double amount) {
            return fraudDetection.isSuspiciousActivity(accountId, amount);
        }

        private boolean checkVelocityFraud(String accountId, double amount) {
            if (!recentTransactions.containsKey(accountId)) {
                recentTransactions.put(accountId, new ArrayList<>());
            }
            ChequeTransaction currentTransaction = new ChequeTransaction(amount, java.time.LocalDate.now());
            List<ChequeTransaction> transactions = recentTransactions.get(accountId);
            transactions.add(currentTransaction);

            java.time.LocalDate cutoffDate = java.time.LocalDate.now().minusDays(VELOCITY_CHECK_DAYS);
            long recentCount = transactions.stream()
                    .filter(t -> !t.getDate().isBefore(cutoffDate))
                    .count();

            // Clean up old transactions
            recentTransactions.put(accountId, transactions.stream()
                    .filter(t -> !t.getDate().isBefore(cutoffDate))
                    .toList());

            return recentCount > VELOCITY_THRESHOLD;
        }

        private boolean checkPatternFraud(String accountId, double amount) {
            if (!recentTransactions.containsKey(accountId)) {
                return false;
            }
            List<ChequeTransaction> transactions = recentTransactions.get(accountId);
            if (transactions.size() < 3) {
                return false;
            }
            List<Double> amounts = transactions.stream()
                    .map(ChequeTransaction::getAmount)
                    .toList();
            double similarCount = 0;
            for (Double pastAmount : amounts) {
                double similarity = 1.0 - Math.abs(pastAmount - amount) / Math.max(pastAmount, amount);
                if (similarity > PATTERN_THRESHOLD) {
                    similarCount++;
                }
            }
            return similarCount >= 3;
        }

        private boolean checkHistoricalDuplicate(String accountId, String chequeNumber) {
            List<String> historicalCheques = historyManager.getChequeNumbers(accountId);
            return historicalCheques.contains(chequeNumber);
        }

        private boolean checkUnusualFrequency(String accountId) {
            int totalCheques = historyManager.getTotalChequeCount(accountId);
            int recentCheques = historyManager.getRecentChequeCount(accountId);
            if (totalCheques < 10) {
                return false;
            }
            double avgMonthlyFrequency = totalCheques / 3.0;
            return recentCheques > avgMonthlyFrequency * UNUSUAL_FREQUENCY_THRESHOLD;
        }

        private boolean checkSimilarToRecent(String accountId, double amount) {
            return historyManager.hasSimilarRecentCheque(accountId, amount, SIMILAR_AMOUNT_THRESHOLD);
        }

        private AlertLevel determineAlertLevel(boolean isDuplicate, boolean isAbnormal,
                                               boolean isSuspicious, boolean isVelocityFraud,
                                               boolean isPatternFraud, boolean isHistoricalDuplicate,
                                               boolean isUnusualFrequency, boolean isSimilarToRecent) {
            int fraudCount = 0;
            if (isDuplicate || isHistoricalDuplicate) fraudCount += 3;
            if (isAbnormal) fraudCount += 2;
            if (isSuspicious) fraudCount += 2;
            if (isVelocityFraud) fraudCount += 2;
            if (isPatternFraud) fraudCount += 2;
            if (isUnusualFrequency) fraudCount += 1;
            if (isSimilarToRecent) fraudCount += 1;

            if (fraudCount >= 5 || isDuplicate || isHistoricalDuplicate) {
                return AlertLevel.CRITICAL;
            } else if (fraudCount >= 3) {
                return AlertLevel.HIGH;
            } else if (fraudCount >= 2) {
                return AlertLevel.MEDIUM;
            } else {
                return AlertLevel.LOW;
            }
        }

        private void logFraudChecks(String accountId, String chequeNumber, double amount,
                                    boolean isDuplicate, boolean isAbnormal, boolean isSuspicious,
                                    boolean isVelocityFraud, boolean isPatternFraud,
                                    boolean isHistoricalDuplicate, boolean isUnusualFrequency,
                                    boolean isSimilarToRecent) {
            System.out.println("\n===== FRAUD CHECK REPORT =====");
            System.out.println("Account: " + accountId + ", Cheque: " + chequeNumber + ", Amount: " + amount);

            System.out.println("\n--- Basic Checks ---");
            System.out.println("Duplicate Check: " + formatCheckResult(isDuplicate));
            System.out.println("Abnormal Amount Check: " + formatCheckResult(isAbnormal));
            System.out.println("Suspicious Activity Check: " + formatCheckResult(isSuspicious));
            System.out.println("Velocity Check: " + formatCheckResult(isVelocityFraud));
            System.out.println("Pattern Analysis: " + formatCheckResult(isPatternFraud));

            if (historyManager != null) {
                System.out.println("\n--- Advanced Checks ---");
                System.out.println("Historical Duplicate Check: " + formatCheckResult(isHistoricalDuplicate));
                System.out.println("Unusual Frequency Check: " + formatCheckResult(isUnusualFrequency));
                System.out.println("Similar Recent Amount Check: " + formatCheckResult(isSimilarToRecent));
            }

            boolean anyFraudDetected = isDuplicate || isAbnormal || isSuspicious ||
                    isVelocityFraud || isPatternFraud ||
                    isHistoricalDuplicate || isUnusualFrequency || isSimilarToRecent;

            System.out.println("\n--- Summary ---");
            if (anyFraudDetected) {
                System.out.println("⚠️ FRAUD ALERT: Potential fraud detected!");
            } else {
                System.out.println("✓ No fraud detected.");
            }
            System.out.println("=============================\n");
        }

        private String formatCheckResult(boolean failed) {
            return failed ? "FAILED ⚠️" : "Passed ✓";
        }

        private static
class
