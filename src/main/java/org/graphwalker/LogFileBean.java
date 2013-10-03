package org.graphwalker;

/**
 * Class to use for classnames and methodnames. For example when creating Arraylists.
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
