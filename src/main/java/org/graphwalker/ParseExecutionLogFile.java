package org.graphwalker;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.graphwalker.generators.PathGenerator;
import org.graphwalker.generators.RandomPathGenerator;

/**
 * Class that checks if a method, class and model exists. Also checks a log file for methods and
 * classes
 *
 * @author theresek
 * 
 */
public class ParseExecutionLogFile {

  private final Class<?> noparams[] = {};
  private Class<?> cls;
  private Object obj;
  private File file;

  /**
   * Simplified constructor of this class.
   *
   * @param file
   */
  public ParseExecutionLogFile(File file) {
    this.file = file;
  }

  /**
   * Constructor for this class with params.
   *
   * @param className
   * @param model
   * @throws Exception
   */
  public ParseExecutionLogFile(String className, String model) throws Exception {
    checkModel(model);
    this.cls = getClass(className);
    Class<?> params[] = {String.class, Boolean.TYPE, PathGenerator.class};
    Object args[] = {model, false, new RandomPathGenerator()};
    this.obj = cls.getDeclaredConstructor(params).newInstance(args);
  }

  /**
   * Checks if model exists.
   *
   * @param model
   * @throws Exception
   */
  public void checkModel(String model) throws Exception {
    File f = new File(model);
    if (!f.exists()) {
      throw new FileNotFoundException(model);
    }
  }

  /**
   * Returns the class from a classname.
   *
   * @param className
   * @return
   * @throws Exception
   */
  public Class<?> getClass(String className) throws Exception {
    return Class.forName(className);
  }

  /**
   * Returns the declared method.
   *
   * @param methodName
   * @return
   * @throws Exception
   */
  public Method getMethod(String methodName) throws Exception {
    return this.cls.getDeclaredMethod(methodName, this.noparams);
  }

  /**
   * Returns a method.
   *
   * @param methodName
   * @return
   * @throws NoSuchMethodException
   */
  public Method checkMethod(String methodName) throws NoSuchMethodException {
    return this.cls.getMethod(methodName);

  }

  /**
   * Executes a method in a models class.
   *
   * @param methodName
   * @throws Exception
   */
  public void execute(String methodName) throws Exception {
    Method meth = getMethod(methodName);
    meth.invoke(this.obj);
  }

  /**
   * Exucutes a logfile, row by row, and splits it up in classnames and methodnames.
   *
   * @return
   * @throws Exception
   */
  public List<LogFileBean> executeLogFile() throws Exception {
    Scanner scanner = new Scanner(this.file);
    List<LogFileBean> values = new ArrayList<LogFileBean>();
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      StringBuffer sb = new StringBuffer(line);
      line = sb.reverse().toString();
      String array[] = line.split("\\.", 2);
      String methName = new StringBuffer(array[0]).reverse().toString();
      String className = new StringBuffer(array[1]).reverse().toString();
      LogFileBean m = new LogFileBean(methName, className);
      values.add(m);
    }
    return values;
  }

  /**
   * Sets the private Class<?>.
   *
   * @param cls
   */
  public void setCls(Class<?> cls) {
    this.cls = cls;
  }

}
