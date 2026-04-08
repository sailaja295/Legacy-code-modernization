class CurrencyRate {
        private double rate;
        private java.time.LocalDateTime lastUpdated;

        public CurrencyRate(double rate, java.time.LocalDateTime lastUpdated) {
            this.rate = rate;
            this.lastUpdated = lastUpdated;
        }

        public double getRate() {
            return rate;
        }

        public java.time.LocalDateTime getLastUpdated() {
            return lastUpdated;
        }
    }

    /**
     * Service for detecting fraudulent cheque activities.
     * Implements various fraud detection mechanisms and uses ChequeHistoryManager.
     */
    static
class
