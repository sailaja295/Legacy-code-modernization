class ChequePrintingService {
        public void printCheque(String payeeName, double amount, Date date, String accountNumber, String chequeNumber, String bankName) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US); // Assuming USD

            System.out.println("\n+----------------------------------------------------------------------+");
            System.out.printf("| %-50s Date: %-10s |\n", bankName.toUpperCase(), dateFormat.format(date));
            System.out.println("|                                                                      |");
            System.out.printf("| Pay To: %-30s Amount: %-15s |\n", payeeName, currencyFormatter.format(amount));
            // Amount in words is complex, for simulation, we can skip or put a placeholder
            System.out.printf("| Amount in Words: %-45s |\n", "*(Numeric Amount Above)*");
            System.out.println("|                                                                      |");
            System.out.println("|                                                                      |");
            System.out.printf("| Account No: %-25s Cheque No: %-15s |\n", accountNumber, chequeNumber);
            System.out.println("|                                                                      |");
            System.out.println("|                                                 Signature:           |");
            System.out.println("|                                                 -------------------- |");
            System.out.println("+----------------------------------------------------------------------+");
            System.out.println("Cheque simulation printed successfully.\n");
        }
    }
    /**
 * Represents a user of the system (employee or account holder).
 */
    static
class
