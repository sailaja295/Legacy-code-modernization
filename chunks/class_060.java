class CurrencyExchangeServiceV2 {
        private Map<String, Double> cache = new HashMap<>();
        private static final String BASE = "USD";
        private static final String KEY = "demo";
        // Fallback rates
        private static final Map<String, Double> RATES = new HashMap<String, Double>();
        static {
            RATES.put("EUR", 1.1);
            RATES.put("GBP", 1.3);
            RATES.put("INR", 0.013);
            RATES.put("JPY", 0.0091);
            RATES.put("CAD", 0.74);
            RATES.put("AUD", 0.67);
            RATES.put("CHF", 1.12);
            RATES.put("CNY", 0.14);
            RATES.put("HKD", 0.13);
            RATES.put("NZD", 0.62);
        }

        public double getExchangeRateV2(String currency) {
            String c = currency;
            if (c == null) return 0.0;
            c = c.toUpperCase();
            if (c.equals(BASE)) return 1.0;
            if (cache.containsKey(c)) return cache.get(c);
            double rate = 0.0;
            try {
                rate = fetchRateFromAPIV2(c);
                cache.put(c, rate);
                return rate;
            } catch (Exception e) {
                // ignore
            }
            if (RATES.containsKey(c)) {
                cache.put(c, RATES.get(c));
                return RATES.get(c);
            }
            return 0.0;
        }

        public double convertCurrencyV2(double amount, String from, String to) {
            double r1 = getExchangeRateV2(from);
            double r2 = getExchangeRateV2(to);
            if (r1 == 0.0 || r2 == 0.0) return 0.0;
            return (amount * r1) / r2;
        }

        public Map<String, Double> getDetailedExchangeRatesV2(String currency) {
            Map<String, Double> m = new HashMap<>();
            double r = getExchangeRateV2(currency);
            if (r == 0.0) return m;
            m.put("mid", r);
            m.put("buy", r - 0.01 * r);
            m.put("sell", r + 0.01 * r);
            m.put("fee", r * 0.005);
            return m;
        }

        public List<String> getSupportedCurrenciesV2() {
            List<String> l = new ArrayList<>();
            l.add(BASE);
            for (String k : RATES.keySet()) l.add(k);
            return l;
        }

        private double fetchRateFromAPIV2(String currency) throws Exception {
            String url = "https://open.er-api.com/v6/latest/" + BASE + "?apikey=" + KEY;
            java.net.URL u = new java.net.URL(url);
            java.net.HttpURLConnection c = (java.net.HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            int s = c.getResponseCode();
            if (s != 200) throw new Exception("bad");
            java.io.BufferedReader r = new java.io.BufferedReader(new java.io.InputStreamReader(c.getInputStream()));
            String line, all = "";
            while ((line = r.readLine()) != null) all += line;
            r.close();
            // Very inefficient JSON parsing
            int idx = all.indexOf("\"" + currency + "\":");
            if (idx == -1) throw new Exception("not found");
            int start = idx + currency.length() + 3;
            int end = start;
            while (end < all.length() && (Character.isDigit(all.charAt(end)) || all.charAt(end)=='.')) end++;
            String num = all.substring(start, end);
            return Double.parseDouble(num);
        }

        public void clearCacheV2() {
            cache.clear();
        }
    }

    /**
     * Class to store currency rate information with timestamp
     */
    static
class
