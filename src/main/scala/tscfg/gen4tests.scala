package tscfg

import java.io.{File, FileOutputStream, OutputStreamWriter, PrintWriter}

import tscfg.generators.java.JavaGen
import tscfg.generators.scala.ScalaGen
import tscfg.generators.{GenOpts, Generator}

object gen4tests {
  def main(args: Array[String]): Unit = {
    val sourceDir = new File("src/main/tscfg/example")
    sourceDir.listFiles().filter(_.getName.endsWith(".spec.conf")) foreach generate
  }

  private def generate(confFile: File): Unit = {
    val source = io.Source.fromFile(confFile).mkString.trim
    val buildResult = ModelBuilder(source)
    val objectType = buildResult.objectType

    val baseGenOpts = GenOpts("tscfg.example", "?", j7 = false)

    def updatedGenOpts(className: String): GenOpts = {
      var genOpts = baseGenOpts.copy(className = className)
      val opts = {
        val linePat = """\s*//\s*GenOpts:(.*)""".r
        source.split("\n")
          .collect { case linePat(xs) ⇒ xs.trim }
          .flatMap(_.split("\\s+"))
      }

      opts foreach {
        case "--scala:fp"      ⇒ genOpts = genOpts.copy(reportFullPath = true)
        case "--scala:bt"      ⇒ genOpts = genOpts.copy(useBackticks = true)
        case "--java:getters"  ⇒ genOpts = genOpts.copy(genGetters = true)
        case "--java:optionals"  ⇒ genOpts = genOpts.copy(useOptionals = true)
        case "--durations"     ⇒ genOpts = genOpts.copy(useDurations = true)

        // $COVERAGE-OFF$
        case opt ⇒ println(s"WARN: $confFile: unrecognized GenOpts argument: `$opt'")
        // $COVERAGE-ON$
      }
      genOpts
    }

    val name = confFile.getName
    val (base, _) = name.span(_ != '.')
    val classNameSuffix = util.upperFirst(base.replace('-', '_')) + "Cfg"

    List("Scala", "Java") foreach { lang ⇒
      val targetScalaDir = new File("src/test/" + lang.toLowerCase + "/tscfg/example")
      targetScalaDir.mkdirs()

      val className = lang + classNameSuffix

      val fileName = className + "." + lang.toLowerCase
      val targetFile = new File(targetScalaDir, fileName)
      // $COVERAGE-OFF$
      if (confFile.lastModified >= targetFile.lastModified) {
        val genOpts = updatedGenOpts(className)
        println(s"generating for $name -> $fileName")
        val generator: Generator = lang match {
          case "Scala" ⇒ new ScalaGen(genOpts)
          case "Java" ⇒  new JavaGen(genOpts)
        }

        val results = generator.generate(objectType)
        val out= new PrintWriter((new OutputStreamWriter(
            new FileOutputStream(targetFile), "UTF-8")))
        out.println(results.code)
        out.close()
      }
      // $COVERAGE-ON$
    }
  }
}
