package org.riedelcastro.nurupo

/**
 * @author Sebastian Riedel
 */
trait Builder1[+Argument,Built] {self =>
  def argument:Argument
  def built:Built

  def map[NewBuilt](f: Argument => NewBuilt): BuilderN[Argument, NewBuilt] = {
    new BuilderN[Argument, NewBuilt] {
      val tmpArg = self.argument
      def arguments = Seq(tmpArg)
      def built = f(tmpArg)
    }
  }

  def flatMap[NewArgument >: Argument, NewBuilt](f: Argument => BuilderN[NewArgument, NewBuilt]): BuilderN[NewArgument, NewBuilt] = {
    new BuilderN[NewArgument, NewBuilt] {
      val tmpArg = self.argument

      val applied = f(tmpArg)
      def arguments = tmpArg +: applied.arguments
      def built = applied.built
    }
  }

}

trait BuilderN[+Argument,Built] { self =>

  def arguments:Seq[Argument]
  def built:Built

  def map[NewBuilt](f: Seq[Argument] => NewBuilt): BuilderN[Argument, NewBuilt] = {
    new BuilderN[Argument, NewBuilt] {
      val tmpArguments = self.arguments
      def arguments = tmpArguments
      def built = f(tmpArguments)
    }
  }

  def flatMap[NewArgument >: Argument, NewBuilt](f: Seq[Argument] => BuilderN[NewArgument, NewBuilt]): BuilderN[NewArgument, NewBuilt] = {
    new BuilderN[NewArgument, NewBuilt] {
      val tmpArguments = self.arguments
      val applied = f(tmpArguments)
      def arguments = tmpArguments ++ applied.arguments
      def built = applied.built
    }
  }

}



object BuilderTest {

  def main(args: Array[String]) {

    case class Dom[I](i:I) extends Builder1[I, Nothing] {
      def argument = i
      def built = sys.error("empty")
    }

    val test3 = for (x <- Dom(1); y <- Dom(2); z <- Dom(3)) yield x
    println(test3.arguments)
    println(test3.built)

  }
}

