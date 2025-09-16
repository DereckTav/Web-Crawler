package crawler.cache;

// TODO Respecting robots.txt
        // caching for robots.txt give TTL to each
        // for prominent research websites maybe just keep it for longer? (could also check if they would ever change it)


// if rule not in cache rquest new rule to cache

public final class RobotsCache {
    // for getting robots.txt I can provide supplier method torequest robots
    // RobotsRule getOrLoad(domain, supplier<> ...);
}
