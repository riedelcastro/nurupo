package org.riedelcastro.nurupo

/**
 * @author sriedel
 */
import collection.mutable.ArrayBuffer
import java.io.{FileInputStream, InputStream, File}
import io.Source

/**
 * @author sriedel
 */

/**
 * @author sriedel
 */
object Util extends HasLogger {

  def untilException[T](call: => T,stop:Throwable => Boolean) = Iterator.continually({
    try {
      call
    } catch {
      case t:Throwable if (stop(t)) => null.asInstanceOf[T]
    }
  }).takeWhile(_ != null)

  /**Recursively descend directory, returning a list of files. */
  def files(directory: File): Seq[File] = {
    if (!directory.exists) throw new Error("File " + directory + " does not exist")
    if (directory.isFile) return List(directory)
    val result = new ArrayBuffer[File]
    for (entry: File <- directory.listFiles) {
      if (entry.isFile) result += entry
      else if (entry.isDirectory) result ++= files(entry)
    }
    result
  }

  /**
   * turns value that can be null into Options that are None if the value t is null, or Some(t)
   * otherwise.
   */
  def option[T](t: T) = if (t == null) None else Some(t)

  /**
   * Turns integers into options: None if i==-1, or Some(i) otherwise.
   */
  def optMinusOne(i: Int): Option[Int] = if (i == -1) None else Some(i)

  /**
   * Takes a relative path name and finds the corresponding file in the classpath. Returns
   * its absolute path.
   */
  def resolveRelativePathUsingClassPath(path: String): String = {
    val resourceURL = getClass.getResource("/" + path)
    if (resourceURL == null)
      new File(path).getAbsolutePath
    else
      new File(resourceURL.toURI).getAbsolutePath
  }

  /**
   * Loads a resource as stream. This returns either a resource in the classpath,
   * or in case no such named resource exists, from the file system.
   */
  def getStreamFromClassPathOrFile(name: String): InputStream = {
    val is: InputStream = getClass.getClassLoader.getResourceAsStream(name)
    if (is == null) {
      logger.info("Loaded resource %s from file system".format(name))
      new FileInputStream(name)
    }
    else {
      logger.info("Loaded resource %s from class path".format(name))
      is
    }

  }

  /**
   * Loads a resource as stream. This returns either a resource in the file system,
   * or in case no such named file exists, from the class path system.
   */
  def getStreamFromFileOrClassPath(name: String): InputStream = {
    val file = new File(name)
    if (file.exists) {
      logger.info("Loaded resource %s from file system".format(name))
      new FileInputStream(file)
    } else {
      val is: InputStream = getClass.getClassLoader.getResourceAsStream(name)
      if (is == null) {
        sys.error("Couldn't find resource %s on file system or class path".format(name))
      } else {
        logger.info("Loaded resource %s from class path".format(name))
        is
      }
    }
  }


  /**
   * Bins a number and returns the bin. If number is negative,
   * bin number will be negative.
   */
  def bin(number: Int, bins: Seq[Int]): Int = {
    val abs = scala.math.abs(number)
    val sign = if (number >= 0) 1 else -1
    for (i <- bins) {
      if (abs < i) return sign * i
    }
    sign * (bins.last + 5)
  }

  def loadStrings(file: String): Seq[String] = {
    val res = new ArrayBuffer[String]
    val source = Source.fromFile(file)
    for (line <- source.getLines()) {
      res += line
    }
    res
  }

}

class Counting(val interval: Int) {
  def this(interval: Int, proc: Int => Unit) {
    this (interval)
    this.proc = proc
  }

  var count = 0
  var proc: Int => Unit = count => {
    println("Processed %s".format(count))
  }

  def perform(proc: Int => Unit) {
    count += 1
    if (count % interval == 0) proc(count)
  }

  def perform() {
    perform(proc)
  }

  def done() {
    done(proc)
  }

  def done(proc: Int => Unit) {
    proc(count)
    count = 0
  }

  def procedure = proc

  def procedure_=(proc: Int => Unit) {
    this.proc = proc
  }

  def apply[T](iterable: Iterable[T]): {def foreach(c: T => Unit)} = apply(iterable.iterator)


  def apply[T](iterator: Iterator[T]) = new {
    def foreach(actualCode: T => Unit) {
      for (elem <- iterator) {
        actualCode(elem)
        perform()
      }
      done()
    }
  }

}
