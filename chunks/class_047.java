class ExceptionRecord {
            String accountNumber, chequeNumber, type, details;
            Date date;
            FIRDetails firDetails; // New: FIR/legal complaint details

            ExceptionRecord(String accountNumber, String chequeNumber, String type, String details, Date date) {
                this.accountNumber = accountNumber;
                this.chequeNumber = chequeNumber;
                this.type = type;
                this.details = details;
                this.date = date;
                this.firDetails = null;
            }
        }

    // New: FIR/legal complaint details
class
