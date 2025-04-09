# WebCrawler

WebCrawler is a multi-threaded web crawler that efficiently discovers and processes hyperlinks from a given root URL. The crawler is implemented using Java and leverages Playwright for web scraping, Apache Commons Validator for URL validation, and various concurrency techniques to improve the crawling speed and efficiency.

## Features

- **Multithreaded Crawling**: Crawls hyperlinks concurrently using multiple threads for faster processing.
- **URL Validation**: Validates URLs before processing using Apache Commons `UrlValidator`.
- **Queue Management**: Uses a thread-safe queue to manage URLs during crawling.
- **Link Resolution**: Resolves relative links into absolute URLs to ensure complete link discovery.
- **Crawled URL Tracking**: Tracks discovered and processed URLs in a thread-safe manner.
- **Flexible Configuration**: Allows customization of crawling behavior and thread management.

## Modules

### Run Class

The entry point of the application. It initializes the crawling process and waits for it to complete.

**Features:**

- Initializes the crawling process by invoking methods from the `App` class.
- Crawls from a specified root URL.
- Waits for the crawling process to complete using a `CountDownLatch`.
- Prints the discovered links after crawling is finished.

### App Class

The core class for the crawling process. It handles the crawling of hyperlinks from a given root URL using multithreading.

**Features:**

- Validates the root URL using `UrlValidator` from Apache Commons.
- Uses a fixed thread pool to process URLs concurrently.
- Crawls hyperlinks starting from the root URL and collects them in a set.
- Returns the set of all crawled links.

**Dependencies:**

- Apache Commons Validator (`UrlValidator`)
- ExecutorService (for thread management)
- CountDownLatch (for synchronizing threads)

**Usage Example:**

```java
App app = new App();
app.getHyperLinkOf("https://example.com", new CountDownLatch(5));
Set<String> links = app.returnLinks();
