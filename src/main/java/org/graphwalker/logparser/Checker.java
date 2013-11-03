package org.graphwalker.logparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;

import org.graphwalker.generators.PathGenerator;
import org.graphwalker.generators.RandomPathGenerator;

/**
 * Class that checks if a method, class and model exists.
 *
 * @author theresek
 *
 */
public class Checker {

  private final Class<?> noparams[] = {};
  private Class<?> cls;
  private Object obj;

  /**
   * Simplified constructor of this class.
   *
   * @param file
   */
  public Checker() {
  }

  /**
   * Constructor for this class with params.
   *
   * @param className
   * @param model
   * @throws Exception
   */
  public Checker(String className, String model) throws Exception {
    if (modelExists(model)) {
      this.cls = Class.forName(className);
      Class<?> params[] = {String.class, Boolean.TYPE, PathGenerator.class};
      Object args[] = {model, false, new RandomPathGenerator()};
      this.obj = cls.getDeclaredConstructor(params).newInstance(args);
    } else {
      throw new FileNotFoundException();
    }
  }

  /**
   * Returns true if model exists and false if not.
   * 
   * @param model
   */

  protected boolean modelExists(String model) throws Exception {
    File f = new File(model);
    return f.exists();
  }

  /**
   * Returns the declared method.
   *
   * @param methodName
   * @return
   * @throws Exception
   */

  protected Method getMethod(String methodName) throws Exception {
    return this.cls.getDeclaredMethod(methodName, this.noparams);
  }


  /**
   * Returns a method.
   *
   * @param methodName
   * @return
   * @throws NoSuchMethodException
   */

  protected Method checkMethod(String methodName) throws NoSuchMethodException {
    return this.cls.getMethod(methodName);
  }


  /**
   * Executes a method in a models class.
   *
   * @param methodName
   * @throws Exception
   */

  protected void execute(String methodName) throws Exception {
    Method meth = getMethod(methodName);
    meth.invoke(this.obj);
  }
}
