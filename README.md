# Hypercrawl

Hypercrawl is a multi-threaded web crawler that efficiently discovers and processes hyperlinks from a given root URL. The crawler is implemented using Java and leverages Playwright for web scraping, Apache Commons Validator for URL validation, and various concurrency techniques to improve the crawling speed and efficiency.

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
```

### Spider Class
Responsible for crawling and processing web pages concurrently. Each Spider instance crawls a page, discovers hyperlinks, and adds them to the queue for further crawling.

**Features:**

- Handles the crawling of the root URL and subsequent pages.
- Uses a queue to manage discovered URLs and ensures concurrent crawling.
- Tracks discovered and processed URLs using the `Crawled` class.
- Implements the `Runnable` interface to run concurrently with other spiders.

### Crawled Class
Tracks discovered and processed hyperlinks in a thread-safe manner.

**Features:**

- Tracks discovered URLs that have not been processed.
- Tracks processed URLs.
- Provides thread-safe access to these sets using a `ReentrantReadWriteLock`.
- Ensures that the number of processed links does not exceed a predefined limit.

**Usage:**

- Add discovered links to the discovered set.
- Move links to the processed set once they are processed.
- Retrieve the set of processed links using `getLinks()`.
- Use `isFull()` to check if the maximum number of processed links has been reached.

### Queue Class
Manages a thread-safe queue of URLs to be crawled.

**Features:**

- Allows adding and removing links from the queue in a thread-safe manner.
- Provides methods to check if the queue is empty.
- Can clear the queue when necessary.

**Usage:**

- Spiders can add discovered links to the queue and dequeue links for further crawling.

### LinkFinder Class
Retrieves and processes valid links (absolute URLs) from a given webpage. Resolves relative links to absolute URLs and validates them.

**Features:**

- Extracts both absolute and relative links from a page.
- Resolves relative links into absolute URLs.
- Validates the extracted links based on predefined criteria (e.g., avoids invalid or unsupported URLs).

**Dependencies:**

- Playwright (for interacting with a web browser)

## Installation

Clone this repository to your local machine:

```bash
git clone https://github.com/yourusername/hypercrawl.git
```
Navigate to the project directory:

```bash
cd hypercrawl
```

Usage
Run the application by executing the Run class, which initializes the crawling process and waits for it to complete.

Example:
```java
Run run = new Run();
run.main(new String[]{"https://example.com"});
```

Crawl links from a URL using the App class:

```java
App app = new App();
CountDownLatch latch = new CountDownLatch(5); // Set the number of threads
app.getHyperLinkOf("https://example.com", latch);
Set<String> links = app.returnLinks();
System.out.println(links);
```
Customize the crawler behavior by modifying the thread pool size, URL validation rules, and other parameters in the respective classes.
Add Robot.txt case to not crawl websites that don't allow crawling.
