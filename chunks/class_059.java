class CurrencyExchangeService {
        private Map<String, CurrencyRate> exchangeRateCache = new HashMap<>();
        private static final String BASE_CURRENCY = "USD";
        private static final long CACHE_EXPIRY_MINUTES = 60; // Cache expiry time in minutes
        private static final String API_KEY = "demo"; // Replace with your actual API key for production

        // Fallback exchange rates in case API is unavailable
        private static final Map<String, Double> FALLBACK_RATES = new HashMap<String, Double>() {{
            put("EUR", 1.1);
            put("GBP", 1.3);
            put("INR", 0.013);
            put("JPY", 0.0091);
            put("CAD", 0.74);
            put("AUD", 0.67);
            put("CHF", 1.12);
            put("CNY", 0.14);
            put("HKD", 0.13);
            put("NZD", 0.62);
        }};

        /**
         * Get the exchange rate for a specific currency
         * @param currency The currency code (e.g., EUR, GBP)
         * @return The exchange rate relative to the base currency (USD)
         */
        public double getExchangeRate(String currency) {
            System.out.println("Fetching exchange rate for currency: " + currency);

            // Standardize currency code
            String currencyCode = currency.toUpperCase();

            // Return 1.0 if it's the base currency
            if (BASE_CURRENCY.equals(currencyCode)) {
                return 1.0;
            }

            // Check cache first
            if (isCacheValid(currencyCode)) {
                CurrencyRate cachedRate = exchangeRateCache.get(currencyCode);
                System.out.println("Using cached rate: " + cachedRate.getRate() + " (Last updated: " +
                        cachedRate.getLastUpdated().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME) + ")");
                return cachedRate.getRate();
            }

            // Try to fetch from external API
            try {
                double apiRate = fetchRateFromAPI(currencyCode);
                if (apiRate > 0) {
                    // Cache the new rate
                    exchangeRateCache.put(currencyCode, new CurrencyRate(apiRate, java.time.LocalDateTime.now()));
                    return apiRate;
                }
            } catch (Exception e) {
                System.out.println("Error fetching exchange rate from API: " + e.getMessage());
                // Continue to fallback rates
            }

            // Use fallback rates if API fetch failed
            Double fallbackRate = FALLBACK_RATES.get(currencyCode);
            if (fallbackRate != null) {
                System.out.println("Using fallback rate for " + currencyCode + ": " + fallbackRate);
                // Cache the fallback rate
                exchangeRateCache.put(currencyCode, new CurrencyRate(fallbackRate, java.time.LocalDateTime.now()));
                return fallbackRate;
            }

            System.out.println("No exchange rate available for currency: " + currencyCode);
            return 0.0;
        }

        /**
         * Convert an amount from one currency to another
         * @param amount The amount to convert
         * @param fromCurrency The source currency
         * @param toCurrency The target currency
         * @return The converted amount
         */
        public double convertCurrency(double amount, String fromCurrency, String toCurrency) {
            double fromRate = getExchangeRate(fromCurrency);
            double toRate = getExchangeRate(toCurrency);

            if (fromRate <= 0 || toRate <= 0) {
                System.out.println("Cannot convert: invalid exchange rates");
                return 0.0;
            }

            // Convert to base currency first, then to target currency
            double amountInBaseCurrency = amount * fromRate;
            double convertedAmount = amountInBaseCurrency / toRate;

            System.out.println(String.format("Converted %.2f %s to %.2f %s",
                    amount, fromCurrency.toUpperCase(), convertedAmount, toCurrency.toUpperCase()));

            return convertedAmount;
        }

        /**
         * Get detailed exchange rate information including buy/sell rates and fees
         * @param currency The currency code
         * @return A map containing detailed rate information
         */
        public Map<String, Double> getDetailedExchangeRates(String currency) {
            String currencyCode = currency.toUpperCase();
            double baseRate = getExchangeRate(currencyCode);

            if (baseRate <= 0) {
                return Collections.emptyMap();
            }

            Map<String, Double> detailedRates = new HashMap<>();
            detailedRates.put("mid", baseRate);

            // Calculate buy rate (slightly lower than mid rate)
            double buyRate = baseRate * 0.99;
            detailedRates.put("buy", buyRate);

            // Calculate sell rate (slightly higher than mid rate)
            double sellRate = baseRate * 1.01;
            detailedRates.put("sell", sellRate);

            // Calculate fees
            double fee = baseRate * 0.005; // 0.5% fee
            detailedRates.put("fee", fee);

            return detailedRates;
        }

        /**
         * Get a list of all supported currencies
         * @return A list of supported currency codes
         */
        public List<String> getSupportedCurrencies() {
            List<String> currencies = new ArrayList<>();
            currencies.add(BASE_CURRENCY);
            currencies.addAll(FALLBACK_RATES.keySet());

            // Sort alphabetically
            Collections.sort(currencies);
            return currencies;
        }

        /**
         * Check if the cached rate is still valid
         * @param currency The currency code
         * @return true if the cache is valid, false otherwise
         */
        private boolean isCacheValid(String currency) {
            if (!exchangeRateCache.containsKey(currency)) {
                return false;
            }

            CurrencyRate cachedRate = exchangeRateCache.get(currency);
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            java.time.LocalDateTime expiryTime = cachedRate.getLastUpdated().plusMinutes(CACHE_EXPIRY_MINUTES);

            return now.isBefore(expiryTime);
        }

        /**
         * Fetch exchange rate from an external API
         * @param currency The currency code
         * @return The exchange rate
         * @throws Exception If there's an error fetching the rate
         */
        private double fetchRateFromAPI(String currency) throws Exception {
            // Using Open Exchange Rates API as an example
            // In a real application, you would use a proper API key
            String apiUrl = "https://open.er-api.com/v6/latest/" + BASE_CURRENCY + "?apikey=" + API_KEY;

            try {
                java.net.URL url = new java.net.URL(apiUrl);
                java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int status = connection.getResponseCode();
                if (status != 200) {
                    throw new Exception("API returned status code: " + status);
                }

                java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse JSON response
                org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
                org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) parser.parse(response.toString());
                org.json.simple.JSONObject rates = (org.json.simple.JSONObject) jsonObject.get("rates");

                if (rates != null && rates.containsKey(currency)) {
                    double rate = ((Number) rates.get(currency)).doubleValue();
                    System.out.println("Fetched rate from API for " + currency + ": " + rate);
                    return rate;
                } else {
                    throw new Exception("Currency not found in API response");
                }
            } catch (Exception e) {
                System.out.println("API fetch failed: " + e.getMessage());
                throw e;
            }
        }

        /**
         * Clear the exchange rate cache
         */
        public void clearCache() {
            exchangeRateCache.clear();
            System.out.println("Exchange rate cache cleared");
        }
    }

        /**
     * Enhanced Currency Exchange Service V1
     * Supports multiple currencies with detailed exchange rate calculations
     * and dynamic fetching of rates from external sources.
     */
    // Inefficient and non-best-practice version of CurrencyExchangeServiceV2
    static
class
