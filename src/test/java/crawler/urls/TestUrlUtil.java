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
// Changes: refactored package structure,
// Implementation: refactored tests from TestNG to JUnit 5

package crawler.urls;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


public class TestUrlUtil {

  private static Stream<Arguments> getDecodeStrings() {
    return Stream.of(
        Arguments.of("%%32%35", "%"),
        Arguments.of("%2%35", "%"),
        Arguments.of("%%325", "%"),
        Arguments.of("%%32%3525", "%"),
        Arguments.of("%%%32%35", "%%"),
        Arguments.of("%%32%35%", "%%"),
        Arguments.of("%%32%3532", "2"),
        Arguments.of("%%%32%3532%%32%3535", "%"),
        Arguments.of("/%25%32%35", "/%"),
        Arguments.of("/%2%2%2", "/%2%2%2"),
        Arguments.of("/%2%%335", "/%"),
        Arguments.of("/%25%32%35%25%32%35", "/%%"),
        Arguments.of("/%2525252525252525", "/%"),
        Arguments.of("/asdf%25%32%35asd", "/asdf%asd"),
        Arguments.of("/%%%25%32%35asd%%", "/%%%asd%%"),
        Arguments.of("/%2E%73%65%63%75%72%65/%77%77%77%2E%65%62%61%79%2E%63%6F%6D/", "/.secure/www.ebay.com/"),
        Arguments.of("/uploads/%20%20%20%20/", "/uploads/    /"),
        Arguments.of("/%257Ea%2521b%2540c%2523d%2524e%25f%255E00%252611%252A22%252833%252944_55%252B",
            "/~a!b@c#d$e%f^00&11*22(33)44_55+")
    );
  }

  @ParameterizedTest
  @MethodSource("getDecodeStrings")
  public void testDecode(String input, String expectedDecodedString) {
    assertEquals(UrlUtil.decode(input), expectedDecodedString);
  }

  private static Stream<Arguments> getEncodeStrings() {
    return Stream.of(
        Arguments.of("/lnjbk%", "/lnjbk%25"),
        Arguments.of("/%2%2%2", "/%252%252%252")
    );
  }

  @ParameterizedTest
  @MethodSource("getEncodeStrings")
  public void testEncode(String input, String expectedEncodedString) {
    assertEquals(UrlUtil.encode(input), expectedEncodedString);
  }

  private static Stream<Arguments> getExtraDotsStrings() {
    return Stream.of(
        Arguments.of(".s..ales.....com", "s.ales.com"),
        Arguments.of("33r.nEt...", "33r.nEt"),
        Arguments.of("[::-34:50]...", "[::-34:50]"),
        Arguments.of("asdf.[-34::192.168.34.-3]...", "asdf.[-34::192.168.34.-3]"),
        Arguments.of(".", "")
    );
  }

  @ParameterizedTest
  @MethodSource("getExtraDotsStrings")
  public void testExtraDotsHosts(String input, String expected) {
    assertEquals(UrlUtil.removeExtraDots(input), expected);
  }
}
