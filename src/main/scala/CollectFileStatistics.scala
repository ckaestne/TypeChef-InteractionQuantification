package de.fosd.typechef.interactions {

import de.fosd.typechef.featureexpr.FeatureExprParser
import java.io.{BufferedWriter, FileWriter, File}
import de.fosd.typechef.featureexpr.FeatureExprFactory._

object CollectFileStatistics extends App {

    val path = "../BusyboxAnalysis/gitbusybox/"
    val fileList = scala.io.Source.fromFile(path + "filelist").getLines()

    val output =new BufferedWriter(new FileWriter(new File(Stats.FILEPC)))

    for (fileStr <- fileList)  {

        val pcFile = path + fileStr + ".pc"
        val fexpr =
            if (new File(pcFile).exists())
                new FeatureExprParser().parseFile(pcFile)
            else True

        val out = "%s;%s;%s;-1\n".format(fileStr, fexpr.toTextExpr, fileStr)
        print(out)
        output.write(out)
    }
    output.close



}
}
