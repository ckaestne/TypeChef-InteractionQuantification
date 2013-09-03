package de.fosd.typechef.interactions

import de.fosd.typechef.featureexpr.FeatureExprParser
import java.io.{BufferedWriter, FileWriter, File}
import de.fosd.typechef.featureexpr.FeatureExprFactory._

object PreprocessCFG extends App {

    val path = "../BusyboxAnalysis/gitbusybox/"
    val lines = scala.io.Source.fromFile(path + "busybox.cfg").getLines()

    val output =new BufferedWriter(new FileWriter(new File(Stats.CCFG)))

    for (line <- lines; if line.head=='E')   {

        val fexprStr= line.substring(line.lastIndexOf(';')+1)


        val out = "%s;%s;%s;-1\n".format("cfgedge",fexprStr, "")
        print(out)
        output.write(out)
    }
    output.close



}
