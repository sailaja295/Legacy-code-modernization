enum Level { INFO, WARN, ERROR, DEBUG }

        public static void log(Level level, String message) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            System.out.printf("[%s] [%s] %s%n", timestamp, level, message);
        }

        public static void info(String message) { log(Level.INFO, message); }
        public static void warn(String message) { log(Level.WARN, message); }
        public static void error(String message) { log(Level.ERROR, message); }
        public static void debug(String message) { log(Level.DEBUG, message); }
        }

    /**
     * Simulates handling of cheque images.
     */
    static
class
