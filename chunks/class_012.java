class FIRDetails {
            String firNumber;
            String policeStation;
            Date firDate;
            String remarks;

            FIRDetails(String firNumber, String policeStation, Date firDate, String remarks) {
                this.firNumber = firNumber;
                this.policeStation = policeStation;
                this.firDate = firDate;
                this.remarks = remarks;
            }
        }

        private List<ExceptionRecord> exceptions = new ArrayList<>();

        public void reportException(String accountNumber, String chequeNumber, String type, String details) {
            exceptions.add(new ExceptionRecord(accountNumber, chequeNumber, type, details, new Date()));
            System.out.println("Exception reported: " + type + " for Cheque " + chequeNumber + " (" + details + ")");
        }

        // New: Record FIR/legal complaint details for a bounced cheque
        public boolean recordFIRDetails(String accountNumber, String chequeNumber, String firNumber, String policeStation, Date firDate, String remarks) {
            for (ExceptionRecord ex : exceptions) {
                if (ex.accountNumber.equals(accountNumber)
                        && ex.chequeNumber.equals(chequeNumber)
                        && "Bounced".equalsIgnoreCase(ex.type)) {
                    ex.firDetails = new FIRDetails(firNumber, policeStation, firDate, remarks);
                    System.out.println("FIR/legal complaint details recorded for bounced cheque " + chequeNumber);
                    return true;
                }
            }
            System.out.println("No bounced cheque exception found for the given account and cheque number.");
            return false;
        }

        public void displayExceptions() {
            if (exceptions.isEmpty()) {
                System.out.println("No cheque exceptions reported.");
                return;
            }
            System.out.println("\n--- Cheque Exception Report ---");
            for (ExceptionRecord ex : exceptions) {
                System.out.printf("Date: %s | Account: %s | Cheque: %s | Type: %s | Details: %s\n",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm").format(ex.date),
                    ex.accountNumber, ex.chequeNumber, ex.type, ex.details);
                // Show FIR/legal complaint details if present
                if ("Bounced".equalsIgnoreCase(ex.type) && ex.firDetails != null) {
                    System.out.printf("   FIR No: %s | Police Station: %s | FIR Date: %s | Remarks: %s\n",
                        ex.firDetails.firNumber,
                        ex.firDetails.policeStation,
                        new SimpleDateFormat("yyyy-MM-dd").format(ex.firDetails.firDate),
                        ex.firDetails.remarks);
                }
            }
        }
    }

    /**
     * Enum to represent cheque status.
     */
enum
