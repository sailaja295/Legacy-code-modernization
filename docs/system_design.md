# System Overview

The legacy system currently supports financial and banking operations, including cheque processing, currency exchange, user authentication, cheque management, fraud detection, and related tasks. The goal of the modernization project is to re-engineer the monolithic Java-based implementation into a Python-based microservices architecture, leveraging **FastAPI**.

FastAPI offers a lightweight, efficient framework for building RESTful APIs, with built-in features for data validation, asynchronous programming, and comprehensive OpenAPI documentation generation. The new architecture will use **Python 3.10+** and prioritize modularity, loose coupling, and asynchronous execution. Communication between microservices will be facilitated through **RESTful APIs** or **message queues** such as RabbitMQ or Kafka, depending on workflow dynamics.

Enhanced functionalities such as caching, end-to-end secure communication via encryption, user role-based authentication using OAuth2 and JWTs, and highly scalable database integrations will be incorporated to future-proof the system.

---

# Main Components

Below is an enhanced breakdown of the main system components, encapsulating their responsibilities and methods.

1. **EmailNotificationService**  
   - Manages system-generated email communications such as transaction updates or exception notifications.  
   - **Methods**:  
     - `sendEmail(String to, String subject, String body)`

2. **ChequePrintingService**  
   - Handles formatting and rendering of printable cheques.  
   - **Methods**:  
     - `printCheque(String payeeName, double amount, Date date, String accountNumber, String chequeNumber, String bankName)`

3. **User**  
   - Models application users with roles for fine-grained access control.  
   - **Methods**:  
     - `getUsername()`, `getPassword()`, `getRole()`

4. **UserService**  
   - Manages user registration, credential verification, and role-based information.  
   - **Methods**:  
     - `registerUser(String username, String password, String role)`  
     - `authenticate(String username, String password)`

5. **ChequeStatusManager**  
   - Maintains and retrieves statuses for cheque processing.  
   - **Methods**:  
     - `setStatus(String accountNumber, String chequeNumber, String status)`  
     - `getStatus(String accountNumber, String chequeNumber)`  
     - `displayAllStatuses()`

6. **ChequeImageHandler**  
   - Processes and loads cheque images for downstream operations (e.g., fraud detection, encryption).  
   - **Methods**:  
     - `loadImageData(String filePath)`

7. **CryptographyService**  
   - Provides encryption and cryptographic utilities such as digital signatures.  
   - **Methods**:  
     - `encryptData(byte[] data, String key)`  
     - `signData(byte[] data, String privateKey)`

8. **ClearinghouseService**  
   - Transmits cheque metadata (e.g., encrypted cheque images) to external clearinghouse services.  
   - **Methods**:  
     - `submitToClearinghouse(String accountNumber, String chequeNumber, byte[] encryptedImageData, String digitalSignature)`

9. **SignatureVerificationService**  
   - Verifies user-registered signatures for cheques and updates them as needed.  
   - **Methods**:  
     - `verifySignature(String accountNumber, String signature)`  
     - `updateSignature(String accountNumber, String newSignature)`

10. **ChequeProcessor**  
    - Central service for performing full-cycle cheque operations, including validations, fraud checks, and processing.  
    - **Methods**:  
      - `processCheque(String accountNumber, String chequeNumber, String currency, double amount, String signature)`  
      - `cancelCheque(String accountNumber, String chequeNumber)`

11. **CurrencyExchangeService**  
    - Handles exchange rate retrieval and currency conversion operations, with caching mechanisms.  
    - **Methods**:  
      - `getExchangeRate(String currency)`  
      - `convertCurrency(double amount, String fromCurrency, String toCurrency)`  
      - `getDetailedExchangeRates(String currency)`  
      - `getSupportedCurrencies()`  
      - `isCacheValid(String currency)`  
      - `fetchRateFromAPI(String currency)`  
      - `clearCache()`

12. **CurrencyRate**  
    - Encapsulates exchange rate information with metadata such as timestamps.  
    - **Methods**:  
      - `getRate()`, `getLastUpdated()`

13. **ChequeTransaction**  
    - Represents a specific cheque transaction, including the amount and transaction date.  
    - **Methods**:  
      - `getAmount()`, `getDate()`

14. **TransactionRecord**  
    - Captures generic transaction data, enabling integration with the core banking system and audit trails.  

15. **AccountProfile**  
    - Tracks aggregated account data and recent transaction histories.  
    - **Methods**:  
      - `updateWithTransaction(double amount)`

16. **AdminService**  
    - Offers administrative controls, including management of system configurations like IFSC codes, bank codes, cheque batches, and stuck transactions.  
    - **Methods**:  
      - `addOrUpdateIFSC(String ifsc, String bankCode)`  
      - `addOrUpdateBankCode(String bankCode, String bankName)`  
      - `displayIFSCs()`  
      - `displayBankCodes()`  
      - `createBatch(String batchId, List<BatchCheque> cheques)`  
      - `displayBatches()`  
      - `displayBatchDetails(String batchId)`  
      - `markTransactionStuck(String chequeNumber)`  
      - `resetStuckTransaction(String chequeNumber)`  
      - `displayStuckTransactions()`

---

# Workflows & Data Flows

### 1. Cheque Processing Workflow  
   a. **Process Initiation**: The client submits a cheque for processing (`POST /cheques/process`) with the data payload including `accountNumber`, `chequeNumber`, `amount`, `currency`, and `signature`.  
   b. **Orchestration by ChequeProcessor**:  
      - Retrieves the cheque’s current status (`ChequeStatusManager.getStatus`).  
      - Validates the signature (`SignatureVerificationService.verifySignature`).  
      - Converts the currency as needed (`CurrencyExchangeService.convertCurrency`).  
      - Runs fraud detection checks (`FraudDetectionService.isFraudulentCheque`).  
      - Submits encrypted cheque data to the clearinghouse (`ClearinghouseService.submitToClearinghouse`).  
      - Updates the processing status (`ChequeStatusManager.setStatus`) and notifies stakeholders (`EmailNotificationService.sendEmail`).  

### 2. User Authentication Workflow  
   a. **Login Process**: Users submit credentials (`POST /users/authenticate`).  
   b. **Authentication Validation**:  
      - The system validates credentials through `UserService`.  
      - Upon success, returns a time-bound and role-specific JWT to the user.

### 3. Admin Batch Management  
   a. **Batch Creation**: Admins invoke `AdminService.createBatch` to organize cheques into processable groups (`POST /admin/batches`).  
   b. **Batch Operations**: Display all batches (`GET /admin/batches`) or fetch details of a specific batch (`GET /admin/batches/{batchId}`).  
   c. **Error Handling**: Stuck transactions are flagged (`AdminService.markTransactionStuck`) and later reset (`AdminService.resetStuckTransaction`).  

---

# Business Rules (Inferred)

1. Validating cheques includes signature authentication, fraud analysis, and status verification.  
2. Only administrators manage operational data such as IFSC, batch configurations, and stuck transactions.  
3. Exchange rate caches refresh periodically post-expiry or forced invalidation.

---

# Security Considerations

1. Authentication via OAuth2 and signed JWT tokens with user roles to manage access rights.  
2. Secure sensitive information (e.g., passwords and signatures) using hashing and encryption.  
3. Ensure all APIs enforce HTTPS for transport encryption, with strict input validations.  

---

# Risks & Migration Notes

### Risks  
1. Database translation issues during migration may lead to data inconsistencies.  
2. Integration failures could disrupt external API interactions (e.g., clearinghouse systems).  
3. Network latencies introduced by microservices’ distributed architecture may impact performance.  

### Migration Notes  
1. Implement robust database migration frameworks. During migration, rehash stored passwords.  
2. Perform extensive integration tests for external interfaces to ensure seamless interaction.

---

# Traceability Matrix

| **Class**                     | **Method**                          | **Mapped Workflow/Operation**                                   |
|-------------------------------|-------------------------------------|---------------------------------------------------------------|
| EmailNotificationService      | sendEmail                          | Cheque Status Notification                                    |
| ChequePrintingService         | printCheque                        | Not utilized in current workflows                             |
| UserService                   | registerUser, authenticate          | User Registration and Authentication                         |
| ChequeStatusManager           | setStatus, getStatus, displayAllStatuses | Cheque Status Management                                       |
| ChequeProcessor               | processCheque, cancelCheque         | Orchestrates Cheque Processing and Cancellations             |
| CryptographyService           | encryptData, signData               | Encryption for secure communications                         |
| CurrencyExchangeService       | getExchangeRate, convertCurrency    | Currency Conversion                                           |
| ClearinghouseService          | submitToClearinghouse              | Connections with clearinghouse systems                       |
| SignatureVerificationService  | verifySignature                    | Signature Validation Operations                              |