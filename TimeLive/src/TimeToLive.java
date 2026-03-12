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

    static HashMap<String, DNSEntry> dnsCache = new HashMap<>();
    static int hits = 0, misses = 0;

 UC3-Network
    // Simulated upstream DNS lookup
    static String queryUpstream(String domain) {
        return "10.0.0.25";
=======
    // Simulated upstream DNS query
    static String queryUpstream(String domain) {
        return "192.168.1.100";
 main
    }

    static void resolve(String domain) {

        if (dnsCache.containsKey(domain)) {

            DNSEntry entry = dnsCache.get(domain);

            if (!entry.isExpired()) {
                hits++;
                System.out.println("resolve(\"" + domain + "\") → Cache HIT → " + entry.ip);
                return;
            } else {
                System.out.println("resolve(\"" + domain + "\") → Cache EXPIRED");
                dnsCache.remove(domain);
            }
        }

        misses++;
        String ip = queryUpstream(domain);

UC3-Network
        dnsCache.put(domain, new DNSEntry(ip, 180));

        System.out.println("resolve(\"" + domain + "\") → Cache MISS → Query upstream → "
                + ip + " (TTL: 180s)");
=======
        cache.put(domain, new DNSEntry(ip, 120));

        System.out.println("resolve(\"" + domain + "\") → Cache MISS → Query upstream → "
                + ip + " (TTL: 120s)");
 main
    }

    static void getCacheStats() {

        int total = hits + misses;
        double hitRate = (total == 0) ? 0 : (hits * 100.0 / total);

        System.out.println("Cache Hit Rate: " + hitRate + "%");
    }

    public static void main(String[] args) {

        resolve("intranet.company.com");
        resolve("intranet.company.com");

        resolve("mail.company.com");
        resolve("intranet.company.com");

        getCacheStats();
    }
}