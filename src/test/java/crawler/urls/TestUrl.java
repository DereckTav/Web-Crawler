/**
 * Copyright 2015 LinkedIn Corp. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

// Modified by DereckTav (2025-09-08)
// Changes: refactored package structure, implementation
// any ftp test cases where commented out becuase application doesn't
// accept those
package crawler.urls;

import java.util.stream.Stream;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


public class TestUrl {

  private static Stream<Arguments> getUsernamePasswordUrls() {
    return Stream.of(
        Arguments.of("http://www.google.com/", "www.google.com", "/", "", ""),
        Arguments.of("nooooo:password@teeee.com", "teeee.com", "/", "nooooo", "password"),
        Arguments.of("hello:ono@bob.com/lala.html", "bob.com", "/lala.html", "hello", "ono"),
        Arguments.of("lala:asdfjdj1k@bob.com", "bob.com", "/", "lala", "asdfjdj1k"),
        Arguments.of("sdf@bob.com", "bob.com", "/", "sdf", ""),
        Arguments.of("@www.google.com", "www.google.com", "/", "", ""),
        Arguments.of("lalal:@www.gogo.com", "www.gogo.com", "/", "lalal", ""),
        Arguments.of("nono:boo@[::1]", "[::1]", "/", "nono", "boo"),
        Arguments.of("nono:boo@yahoo.com/@1234", "yahoo.com", "/@1234", "nono", "boo")
    );
  }

  @ParameterizedTest
  @MethodSource("getUsernamePasswordUrls")
  public void testUsernamePasswordUrls(String testInput, String host, String path, String username, String password)
      throws MalformedURLException {
    Url url = Url.create(testInput);
    assertEquals(url.getHost(), host);
    assertEquals(url.getPath(), path);
    assertEquals(url.getUsername(), username);
    assertEquals(url.getPassword(), password);
  }

  private static Stream<Arguments> getPortUrls() {
    return Stream.of(
        Arguments.of("http://www.google.com:820", "www.google.com", "/", 820),
        Arguments.of("foooo.coo:80", "foooo.coo", "/", 80),
        Arguments.of("[::ffff:192.168.1.1]:800", "[::ffff:192.168.1.1]", "/", 800),
        Arguments.of("[::1]:900/dodododo", "[::1]", "/dodododo", 900),
        Arguments.of("hdh:@[::1]:9/nono", "[::1]", "/nono", 9),
        Arguments.of("http://touch.www.linkedin.com:9000", "touch.www.linkedin.com", "/", 9000)
    );
  }

  @ParameterizedTest
  @MethodSource("getPortUrls")
  public void testPort(String testInput, String host, String path, int port) throws MalformedURLException {
    Url url = Url.create(testInput);
    assertEquals(url.getHost(), host);
    assertEquals(url.getPath(), path);
    assertEquals(url.getPort(), port);
  }

  private static Stream<Arguments> getQueryUrls() {
    return Stream.of(
        Arguments.of("http://www.google.com/", "www.google.com", "/", ""),
        Arguments.of("www.google.com/lala?here=2", "www.google.com", "/lala", "?here=2"),
        Arguments.of("bewp.bop.com/boop?bip=2&bep=3", "bewp.bop.com", "/boop", "?bip=2&bep=3"),
        Arguments.of("[fe80::1:192.168.12.3]/nooo?dop=2&wop=4", "[fe80::1:192.168.12.3]", "/nooo", "?dop=2&wop=4"),
        Arguments.of("[::1:192.1.1.1]:80/nooo?dop=[::1]&wop=4", "[::1:192.1.1.1]", "/nooo", "?dop=[::1]&wop=4")
    );
  }

  @ParameterizedTest
  @MethodSource("getQueryUrls")
  public void testQuery(String testInput, String host, String path, String query) throws MalformedURLException {
    Url url = Url.create(testInput);
    assertEquals(url.getHost(), host);
    assertEquals(url.getPath(), path);
    assertEquals(url.getQuery(), query);
  }

  private static Stream<Arguments> getSchemeUrls() {
    return Stream.of(
        Arguments.of("http://www.google.com/", "http", "www.google.com", "/"),
        Arguments.of("//www.google.com/", "", "www.google.com", "/"),
        Arguments.of("//123825342/", "", "123825342", "/"),
        Arguments.of("//hello/", "", "hello", "/"),
        Arguments.of("//hello:/", "", "hello", "/")
    );
  }

  @ParameterizedTest
  @MethodSource("getSchemeUrls")
  public void testScheme(String testInput, String scheme, String host, String path) throws MalformedURLException {
    Url url = Url.create(testInput);
    assertEquals(url.getScheme(), scheme);
    assertEquals(url.getHost(), host);
    assertEquals(url.getPath(), path);
  }

  private static Stream<Arguments> getUrlsAndHosts() {
    return Stream.of(
        Arguments.of("www.booopp.com:20#fa", "www.booopp.com", "http://www.booopp.com:20/#fa"),
        Arguments.of("www.yahooo.com:20?fff#aa", "www.yahooo.com", "http://www.yahooo.com:20/?fff#aa"),
        Arguments.of("www.google.com#fa", "www.google.com", "http://www.google.com/#fa"),
        Arguments.of("www.google.com?3fd#fa", "www.google.com", "http://www.google.com/?3fd#fa"),
        Arguments.of("//www.google.com/", "www.google.com", "//www.google.com/"),
        Arguments.of("http://www.google.com/", "www.google.com", "http://www.google.com/"),
        // Arguments.of("ftp://whosdere:me@google.com/", "google.com", "ftp://whosdere:me@google.com/"),
        Arguments.of("ono:doope@fb.net:9090/dhdh", "fb.net", "http://ono:doope@fb.net:9090/dhdh"),
        Arguments.of("ono:a@fboo.com:90/dhdh/@1234", "fboo.com", "http://ono:a@fboo.com:90/dhdh/@1234"),
        Arguments.of("fbeoo.net:990/dhdeh/@1234", "fbeoo.net", "http://fbeoo.net:990/dhdeh/@1234"),
        Arguments.of("fbeoo:@boop.com/dhdeh/@1234?aj=r", "boop.com", "http://fbeoo@boop.com/dhdeh/@1234?aj=r"),
        Arguments.of("bloop:@noooo.com/doop/@1234", "noooo.com", "http://bloop@noooo.com/doop/@1234"),
        Arguments.of("bah.com/lala/@1234/@dfd@df?@dsf#ono", "bah.com", "http://bah.com/lala/@1234/@dfd@df?@dsf#ono"),
        Arguments.of("https://dewd:dood@www.google.com:20/?why=is&this=test#?@Sdsf", "www.google.com", "https://dewd:dood@www.google.com:20/?why=is&this=test#?@Sdsf")
    );
  }

  @ParameterizedTest
  @MethodSource("getUrlsAndHosts")
  public void testHostAndFullUrl(String testInput, String host, String fullUrl) throws MalformedURLException {
    Url url = Url.create(testInput);
    assertEquals(url.getHost(), host, testInput);
    assertEquals(url.getFullUrl(), fullUrl);
    int fragmentIndex = fullUrl.indexOf("#");
    assertEquals(url.getFullUrlWithoutFragment(),
        fragmentIndex == -1 ? fullUrl : fullUrl.substring(0, fragmentIndex));
  }

  private static Stream<Arguments> getSingleDomainUrls() {
    return Stream.of(
        Arguments.of("localhost:9000/", "localhost", 9000, "http://localhost:9000/"),
        Arguments.of("go/tj", "go", 80, "http://go/tj")
    );
  }

  @ParameterizedTest
  @MethodSource("getSingleDomainUrls")
  public void testSingleDomainUrls(String testInput, String host, int port, String fullUrl)
      throws MalformedURLException {
    Url url = Url.create(testInput);
    assertEquals(url.getHost(), host);
    assertEquals(url.getPort(), port);
    assertEquals(url.getFullUrl(), fullUrl);
  }
}
