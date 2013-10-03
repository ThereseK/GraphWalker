package org.graphwalker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class to test classes, methods and logfiles.
 * 
 * @author theresek
 * 
 */
public class ParseExecutionLogFileTest {

  private ParseExecutionLogFile mpv;
  private final String correctClass = "org.graphwalker.multiple.Model_A_API";
  private final String correctModelPath = "src/test/resources/graphml/multiple/switch/A.graphml";
  private final String correctMethod = "e_ExitClient";
  private final String errorClass = "org.graphwalker.multiple.Model_ABC_API";
  private final String errorModelPath = "src/test/resources/graphml/multiple/switch/ABC.graphml";
  private final String errorMethod = "e_ExxitClient";


  @Before
  public void setup() throws Exception {
    this.mpv = new ParseExecutionLogFile(correctClass, correctModelPath);
  }

  @Test(expected = ClassNotFoundException.class)
  public void setup2() throws Exception {
    new ParseExecutionLogFile(errorClass, correctModelPath);
  }

  @Test(expected = FileNotFoundException.class)
  public void setup3() throws Exception {
    this.mpv = new ParseExecutionLogFile(correctClass, errorModelPath);
  }

  @Test(expected = NoSuchMethodException.class)
  public void executeMethodError() throws Exception {
    this.mpv.execute(errorMethod);
  }

  @Test
  public void executeMethod() throws Exception {
    this.mpv.execute(correctMethod);
  }

  @Test(expected = ClassNotFoundException.class)
  public void testClassNotFound() throws Exception {
    this.mpv.getClass(errorClass);
  }

  @Test(expected = FileNotFoundException.class)
  public void testFileNotFound() throws Exception {
    this.mpv.checkModel(errorModelPath);
  }

  @Test(expected = NoSuchMethodException.class)
  public void testNoSuchMethod() throws Exception {
    this.mpv.getMethod(errorMethod);
  }

  @Test
  public void testLogFile() throws Exception {
    File f = new File("src/test/java/org/graphwalker/LogFile.txt");
    ParseExecutionLogFile mp = new ParseExecutionLogFile(f);
    List<LogFileBean> temp = mp.executeLogFile();
    for (LogFileBean m : temp) {
      System.out.println(m.getClassName() + " : " + m.getMethName());
      mp.setCls(mp.getClass(m.getClassName()));
      mp.checkMethod(m.getMethName());
    }
  }

  @Test(expected = NoSuchMethodException.class)
  public void testErrorLogFile() throws Exception {
    File f = new File("src/test/java/org/graphwalker/ErrorLogFile.txt");
    ParseExecutionLogFile mp = new ParseExecutionLogFile(f);
    List<LogFileBean> temp = mp.executeLogFile();
    for (LogFileBean m : temp) {
      System.out.println(m.getClassName() + " : " + m.getMethName());
      mp.setCls(mp.getClass(m.getClassName()));
      mp.checkMethod(m.getMethName());
    }
  }

}
