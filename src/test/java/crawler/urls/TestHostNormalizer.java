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
// Changes: refactored package structure
// Implementation: refactored tests from TestNG to JUnit 5
package crawler.urls;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TestHostNormalizer {

  private static Stream<Arguments> getIPAddresses() {
    return Stream.of(
        Arguments.of("[fefe::]", "[fefe:0:0:0:0:0:0:0]"),
        Arguments.of("[::ffff]", "[0:0:0:0:0:0:0:ffff]"),
        Arguments.of("[::255.255.255.255]", "[0:0:0:0:0:0:ffff:ffff]"),
        Arguments.of("[::]", "[0:0:0:0:0:0:0:0]"),
        Arguments.of("[::1]", "[0:0:0:0:0:0:0:1]"),
        Arguments.of("[aAaA::56.7.7.5]", "[aaaa:0:0:0:0:0:3807:705]"),
        Arguments.of("[BBBB:ab:f78F:f:DDDD:bab:56.7.7.5]", "[bbbb:ab:f78f:f:dddd:bab:3807:705]"),
        Arguments.of("[Aaaa::1]", "[aaaa:0:0:0:0:0:0:1]"),
        Arguments.of("[::192.167.2.2]", "[0:0:0:0:0:0:c0a7:202]"),
        Arguments.of("[0:ffff::077.0x22.222.11]", "[0:ffff:0:0:0:0:3f22:de0b]"),
        Arguments.of("[0::ffff:077.0x22.222.11]", "63.34.222.11"),
        Arguments.of("192.168.1.1", "192.168.1.1"),
        Arguments.of("0x92.168.1.1", "146.168.1.1"),
        Arguments.of("3279880203", "195.127.0.11")
    );
  }

  @ParameterizedTest
  @MethodSource("getIPAddresses")
  public void testIpHostNormalizationAndGetBytes(String original, String expectedHost) throws UnknownHostException {
    HostNormalizer hostNormalizer = new HostNormalizer(original);
    assertEquals(hostNormalizer.getNormalizedHost(), expectedHost);

    InetAddress address = InetAddress.getByName(expectedHost);
    byte[] expectedBytes;
    if (address instanceof Inet4Address) {
      expectedBytes = new byte[16];
      expectedBytes[10] = (byte) 0xff;
      expectedBytes[11] = (byte) 0xff;
      System.arraycopy(address.getAddress(), 0, expectedBytes, 12, 4);
    } else {
      expectedBytes = address.getAddress();
    }
    assertTrue(Arrays.equals(hostNormalizer.getBytes(), expectedBytes));
  }

  private static Stream<String> getNormalHosts() {
    return Stream.of(
        "sALes.com",
        "33r.nEt",
        "173839.com",
        "192.168.-3.1",
        "[::-34:50]",
        "[-34::192.168.34.-3]"
    );
  }

  @ParameterizedTest
  @MethodSource("getNormalHosts")
  public void testSanityAddresses(String host) {
    HostNormalizer hostNormalizer = new HostNormalizer(host);
    assertEquals(hostNormalizer.getNormalizedHost(), host.toLowerCase());
    assertNull(hostNormalizer.getBytes());
  }
}
