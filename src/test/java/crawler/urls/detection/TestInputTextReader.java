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
package crawler.urls.detection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


public class TestInputTextReader {
  private static final String CONTENT = "HELLO WORLD";

  @Test
  public void testSimpleRead() {
    InputTextReader reader = new InputTextReader(CONTENT);
    for (int i = 0; i < CONTENT.length(); i++) {
      assertEquals(reader.read(), CONTENT.charAt(i));
    }
  }

  @Test
  public void testEOF() {
    InputTextReader reader = new InputTextReader(CONTENT);
    for (int i = 0; i < CONTENT.length() - 1; i++) {
      reader.read();
    }

    assertFalse(reader.eof());
    reader.read();
    assertTrue(reader.eof());
  }

  @Test
  public void testGoBack() {
    InputTextReader reader = new InputTextReader(CONTENT);
    assertEquals(reader.read(), CONTENT.charAt(0));
    reader.goBack();
    assertEquals(reader.read(), CONTENT.charAt(0));
    assertEquals(reader.read(), CONTENT.charAt(1));
    assertEquals(reader.read(), CONTENT.charAt(2));
    reader.goBack();
    reader.goBack();
    assertEquals(reader.read(), CONTENT.charAt(1));
    assertEquals(reader.read(), CONTENT.charAt(2));
  }

  @Test
  public void testSeek() {
    InputTextReader reader = new InputTextReader(CONTENT);
    reader.seek(4);
    assertEquals(reader.read(), CONTENT.charAt(4));

    reader.seek(1);
    assertEquals(reader.read(), CONTENT.charAt(1));
  }

  @Test
  public void testEndlessLoopDetection() {
    InputTextReader reader = new InputTextReader(CONTENT);
    assertThrows(
        NegativeArraySizeException.class,
        () -> {
            for (int i = 0; i < InputTextReader.MAX_BACKTRACK_MULTIPLIER + 1; i++) {
                reader.seek(CONTENT.length());
                reader.seek(0);
            }
        }
    );
  }
}
