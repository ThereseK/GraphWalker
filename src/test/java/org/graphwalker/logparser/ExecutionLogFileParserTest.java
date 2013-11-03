package org.graphwalker.logparser;

import java.io.File;
import java.util.List;

import org.graphwalker.logparser.ExecutionLogFileParser;
import org.junit.Test;

/**
 * Test class to test classes, methods and logfiles.
 *
 * @author theresek
 *
 */
public class ExecutionLogFileParserTest {

  @Test
  public void testLogFile() throws Exception {
    File f = new File("src/test/java/org/graphwalker/logparser/LogFile.txt");
    List<LogFileBean> temp = ExecutionLogFileParser.parseLogFile(f);
    for (LogFileBean m : temp) {
      System.out.println(m.getClassName() + " : " + m.getMethName());
    }
  }

  @Test(expected = NoSuchMethodException.class)
  public void testErrorLogFile() throws Exception {
    File f = new File("src/test/java/org/graphwalker/logparser/ErrorLogFile.txt");
    List<LogFileBean> temp = ExecutionLogFileParser.parseLogFile(f);
    for (LogFileBean m : temp) {
      System.out.println(m.getClassName() + " : " + m.getMethName());
    }
  }

}
