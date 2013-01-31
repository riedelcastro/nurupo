package org.riedelcastro.nurupo

import java.util.Properties
import reflect.Manifest
import java.io.{FileInputStream, File, InputStream}

/**
 * Very simple configuration file loader based on java properties files.
 * @author sriedel
 */
class Config(val properties: Properties) {
  def this(in: InputStream) {
    this ({val props = new Properties; props.load(in); props})
  }

  def get[T](key: String)(implicit m: Manifest[T]): T = {
    val value = properties.getProperty(key)
    if (value == null) sys.error("No value associated with key " + key)
    createBySimpleErasureName(m.runtimeClass.getSimpleName, value)
  }

  def get[T](key: String, default: T)(implicit m: Manifest[T]): T = {
    val value = properties.getProperty(key)
    if (value == null) default else createBySimpleErasureName(m.runtimeClass.getSimpleName, value)
  }

  private def createBySimpleErasureName[T](name: String, untrimmed: String): T = {
    val value = untrimmed.trim
    val result = name match {
      case "int" => value.toInt
      case "String" => value
      case "double" => value.toDouble
      case "boolean" => value.toBoolean
      case "File" => new File(value)
      case "FileInputStream" => new FileInputStream(value)
      case x if (x.endsWith("[]")) => {
        val prefix = x.substring(0, x.indexOf('['))
        val split = value.split(",").map(_.trim).map(createBySimpleErasureName(prefix, _))

      }
      case x => sys.error("Can't convert type " + x)
    }
    result.asInstanceOf[T]
  }

  def put[T](key: String, value: T) = {
    properties.put(key, value.asInstanceOf[Object])
  }

}
