// VideoCache.java
import java.util.LinkedHashMap;
import java.util.Map;

public class VideoCache<K, V> extends LinkedHashMap<K, V> {
    private final int maxCapacity;
    
    public VideoCache(int maxCapacity) {
        super(maxCapacity, 0.75f, true);  // accessOrder = true
        this.maxCapacity = maxCapacity;
    }
    
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxCapacity;
    }
    
    public void displayCache() {
        System.out.println("Cache contents (most recent to least recent):");
        forEach((key, value) -> System.out.println("  " + key + " -> " + value));
    }
}

// LRUCacheDemo.java
public class LRUCacheDemo {
    public static void main(String[] args) {
        System.out.println("=== LRU Cache Eviction Engine ===\n");
        
        VideoCache<String, String> cache = new VideoCache<>(5);
        
        System.out.println("Adding 5 videos...");
        cache.put("V001", "Tutorial Video");
        cache.put("V002", "Action Movie");
        cache.put("V003", "Documentary");
        cache.put("V004", "Comedy Special");
        cache.put("V005", "Music Video");
        
        cache.displayCache();
        
        System.out.println("\nAccessing V001 (makes it most recent)...");
        cache.get("V001");
        
        cache.displayCache();
        
        System.out.println("\nAdding 6th video (V006)...");
        cache.put("V006", "New Video");
        
        System.out.println("\nAfter adding V006 (V002 should be evicted):");
        cache.displayCache();
        
        System.out.println("\n=== Memory Analysis ===");
        System.out.println("accessOrder=true: Uses access order instead of insertion order");
        System.out.println("removeEldestEntry: Called after every put()");
        System.out.println("Eldest entry is removed when size > capacity");
        System.out.println("V002 was evicted because it was least recently accessed");
        System.out.println("V001 remained because it was accessed recently");
    }
}