import java.util.HashMap;

class DNSEntry {
    String ip;
    long expiryTime;

    DNSEntry(String ip, int ttl) {
        this.ip = ip;
        this.expiryTime = System.currentTimeMillis() + ttl * 1000;
    }

    boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}
public class TimeToLive {

    static HashMap<String, DNSEntry> cache = new HashMap<>();
    static int hits = 0, misses = 0;

    static String queryUpstream(String domain) {
        return "172.217.14.206"; // simulated IP
    }

    static void resolve(String domain) {

        if (cache.containsKey(domain)) {

            DNSEntry entry = cache.get(domain);

            if (!entry.isExpired()) {
                hits++;
                System.out.println("resolve(\"" + domain + "\") → Cache HIT → " + entry.ip);
                return;
            } else {
                System.out.println("resolve(\"" + domain + "\") → Cache EXPIRED");
                cache.remove(domain);
            }
        }

        misses++;
        String ip = queryUpstream(domain);

        cache.put(domain, new DNSEntry(ip, 300));

        System.out.println("resolve(\"" + domain + "\") → Cache MISS → Query upstream → " + ip + " (TTL: 300s)");
    }

    static void getStats() {
        int total = hits + misses;
        double rate = (total == 0) ? 0 : (hits * 100.0 / total);

        System.out.println("Hit Rate: " + rate + "%");
    }

    public static void main(String[] args) {

        resolve("google.com");
        resolve("google.com");

        resolve("openai.com");
        resolve("google.com");

        getStats();
    }
}
