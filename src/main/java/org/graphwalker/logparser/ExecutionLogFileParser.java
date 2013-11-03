package org.graphwalker.logparser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Checks a logfile for methods and classes.
 *
 * @author theresek
 *
 */
public class ExecutionLogFileParser {

  private final static List<LogFileBean> values = new ArrayList<LogFileBean>();


  /**
   * Parses a logfile.
   *
   * @return
   * @throws Exception
   */
  public static List<LogFileBean> parseLogFile(File logFile) throws Exception {
    Scanner scanner = new Scanner(logFile);
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      parseLine(line);
    }
    return values;
  }

  /**
   * Parses each line in the logfile and splits it up in classnames and methodnames.
   *
   * @param line
   */
  private static void parseLine(String line) {
    String regex = "(.+)\\.(\\w+)$";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(line);
    if (m.matches()) {
      String className = m.group(1);
      String methName = m.group(2);
      LogFileBean lfb = new LogFileBean(methName, className);
      values.add(lfb);
    }

  }

}
