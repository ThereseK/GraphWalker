package org.graphwalker.logparser;

/**
 * Class to use for classnames and methodnames.
 *
 * @author theresek
 *
 */
public class LogFileBean {

  private final String className;
  private final String methName;

  public LogFileBean(String methName, String className) {
    this.methName = methName;
    this.className = className;
  }

  public String getMethName() {
    return methName;
  }

  public String getClassName() {
    return className;
  }

}
