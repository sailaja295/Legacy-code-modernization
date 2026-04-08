class AccountProfile {
            private double totalAmount = 0.0;
            private int transactionCount = 0;
            private double maxAmount = 0.0;
            private double minAmount = Double.MAX_VALUE;

            public void updateWithTransaction(double amount) {
                totalAmount += amount;
                transactionCount++;
                maxAmount = Math.max(maxAmount, amount);
                minAmount = Math.min(minAmount, amount);
            }
        }
    }
}
