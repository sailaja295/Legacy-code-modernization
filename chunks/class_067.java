class ChequeTransaction {
            private double amount;
            private java.time.LocalDate date;

            public ChequeTransaction(double amount, java.time.LocalDate date) {
                this.amount = amount;
                this.date = date;
            }

            public double getAmount() {
                return amount;
            }

            public java.time.LocalDate getDate() {
                return date;
            }
        }
    }

        /**
     * Service for detecting fraudulent cheque activities version3.
     * Implements various fraud detection mechanisms and uses ChequeHistoryManager version3.
     */
    static
class
