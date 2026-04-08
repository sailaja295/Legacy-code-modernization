class FraudDetectionService {
        private FraudDetection fraudDetection;
        private ChequeHistoryManager historyManager;
        private Map<String, List<ChequeTransaction>> recentTransactions;

        // Fraud detection thresholds
        private static final int VELOCITY_CHECK_DAYS = 7;
        private static final int VELOCITY_THRESHOLD = 5;
        private static final double PATTERN_THRESHOLD = 0.95; // 95% similarity threshold
        private static final double SIMILAR_AMOUNT_THRESHOLD = 0.90; // 90% similarity threshold
        private static final int UNUSUAL_FREQUENCY_THRESHOLD = 3; // 3x normal frequency

        // Fraud alert levels
public
enum
public
enum
