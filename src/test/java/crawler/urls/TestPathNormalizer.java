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
package crawler.urls;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TestPathNormalizer {

  private static Stream<Arguments> getPaths() {
    return Stream.of(
        Arguments.of("/%25%32%35", "/%25"),
        Arguments.of("/%2%2%2", "/%252%252%252"),
        Arguments.of("/%2%%335", "/%25"),
        Arguments.of("/%25%32%35%25%32%35", "/%25%25"),
        Arguments.of("/%2525252525252525", "/%25"),
        Arguments.of("/asdf%25%32%35asd", "/asdf%25asd"),
        Arguments.of("/%%%25%32%35asd%%", "/%25%25%25asd%25%25"),
        Arguments.of("/%2E%73%65%63%75%72%65/%77%77%77%2E%65%62%61%79%2E%63%6F%6D/", "/.secure/www.ebay.com/"),
        Arguments.of("/uploads/%20%20%20%20/.verify/.eBaysecure=updateuserdataxplimnbqmn-xplmvalidateinfoswqpcmlx=hgplmcx/",
            "/uploads/%20%20%20%20/.verify/.eBaysecure=updateuserdataxplimnbqmn-xplmvalidateinfoswqpcmlx=hgplmcx/"),
        Arguments.of("/%257Ea%2521b%2540c%2523d%2524e%25f%255E00%252611%252A22%252833%252944_55%252B",
            "/~a!b@c%23d$e%25f^00&11*22(33)44_55+"),
        Arguments.of("/lala/.././../..../", "/..../"),
        Arguments.of("//asdfasdf/awef/sadf/sdf//", "/asdfasdf/awef/sadf/sdf/"),
        Arguments.of("/", "/"),
        Arguments.of("/a/../b/c", "/b/c"),
        Arguments.of("/blah/..", "/"),
        Arguments.of("../", "../"),
        Arguments.of("/asdf/.", "/asdf/"),
        Arguments.of("/a/b/./././././../c/d", "/a/c/d"),
        Arguments.of("/a/b//////.///././././../c/d", "/a/c/d"),
        Arguments.of("//../a/c/..///sdf", "/a/sdf"),
        Arguments.of("/../asdf", "/asdf"),
        Arguments.of("/../asdf/", "/asdf/"),
        Arguments.of("/a/b/..c", "/a/b/..c"),
        Arguments.of("/a/b/.././", "/a/"),
        Arguments.of("/a/b/./", "/a/b/"),
        Arguments.of("/a/b/../..", "/"),
        Arguments.of("/a/b/../../../../../../", "/"),
        Arguments.of("/a/b/../../../../../..", "/"),
        Arguments.of("/a/b/../../../../../../c/d", "/c/d"),
        Arguments.of("/a/b/../../../../../../c/d/", "/c/d/"),
        Arguments.of("/a/b/../.", "/a/"),
        Arguments.of("/a/b/..", "/a/"),
        Arguments.of("/1.html", "/1.html"),
        Arguments.of("/1/2.html?param=1", "/1/2.html?param=1"),
        Arguments.of("/a./b.", "/a./b."),
        Arguments.of("/a./b./", "/a./b./")
    );
  }

  @ParameterizedTest
  @MethodSource("getPaths")
  public void testPaths(String path, String expectedPath) {
    PathNormalizer pathNormalizer = new PathNormalizer();

    assertEquals(pathNormalizer.normalizePath(path), expectedPath);
  }
}
