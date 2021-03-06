package org.riedelcastro.nurupo

import org.apache.log4j.Logger
import java.lang.String


/**
 * A HasLogger object has a logger object it can log to. This logger will
 * be named after the class.
 * @author sriedel
 */
trait HasLogger {

  /**
   * The logger of this object. Uses the class name as logger name.
   */
  val logger = Logger.getLogger(loggerName)

  /**
   * Returns the name to use for the logger.
   */
  def loggerName = getClass.getSimpleName

  /**
   * creates an object that lazily evaluates the given function when `toString` is called.
   */
  def lazyString(func: => String) = new Object {
    override def toString: String = func
  }

  def debugLazy(msg: String) {logger.debug(lazyString(msg))}
  def infoLazy(msg: String) {logger.info(lazyString(msg))}
  def errorLazy(msg: String) {logger.error(lazyString(msg))}
  def warnLazy(msg: String) {logger.warn(lazyString(msg))}
  def traceLazy(msg: String) {logger.trace(lazyString(msg))}


}
