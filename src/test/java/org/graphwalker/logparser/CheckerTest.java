package org.graphwalker.logparser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CheckerTest {

  private Checker c;
  private final String correctClass = "org.graphwalker.multiple.Model_A_API";
  private final String correctModelPath = "src/test/resources/graphml/multiple/switch/A.graphml";
  private final String correctMethod = "e_ExitClient";
  private final String errorClass = "org.graphwalker.multiple.Model_ABC_API";
  private final String errorModelPath = "src/test/resources/graphml/multiple/switch/ABC.graphml";
  private final String errorMethod = "e_ExxitClient";

  @Before
  public void setup() throws Exception {
    this.c = new Checker(correctClass, correctModelPath);
  }

  @Test(expected = NoSuchMethodException.class)
  public void executeMethodError() throws Exception {
    this.c.execute(errorMethod);
  }

  @Test
  public void executeMethod() throws Exception {
    this.c.execute(correctMethod);
  }

  @Test(expected = ClassNotFoundException.class)
  public void testClassNotFound() throws Exception {
    Class.forName(errorClass);
  }

  @Test
  public void testFileNotFound() throws Exception {
    assertFalse(this.c.modelExists(errorModelPath));
  }

  @Test(expected = NoSuchMethodException.class)
  public void testNoSuchMethod() throws Exception {
    this.c.getMethod(errorMethod);
  }
}
