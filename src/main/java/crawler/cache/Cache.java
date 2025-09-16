package crawler.cache;

// will be using guava for this, saves time.

// TODO Respecting robots.txt
        // caching for robots.txt give TTL to each
        // for prominent research websites maybe just keep it for longer? (could also check if they would ever change it)

// if no robots ttl should be one day
// if robots ttl should be once a week
// caches should be singletons
    // why if it was single application
    // and each link had its own generated cache
    // likly to be duplicates
    // by keeping it a singleton, when application runs
    // no duplicate memory

// use default
public interface Cache {

    
}