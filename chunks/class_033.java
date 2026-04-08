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
     * Core fraud detection
class
