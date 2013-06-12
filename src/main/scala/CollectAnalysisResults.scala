package de.fosd.typechef.interactions

import de.fosd.typechef.featureexpr.FeatureExprParser
import java.io.{BufferedWriter, FileWriter, File}
import de.fosd.typechef.featureexpr.FeatureExprFactory._

object CollectAnalysisResults extends App {

    val path = "../BusyboxAnalysis/gitbusybox/"
    val fileList = scala.io.Source.fromFile(path + "filelist").getLines()

    val output =new BufferedWriter(new FileWriter(new File(Stats.ANALYSIS)))

    for (fileStr <- fileList)  {

        val pcFile = path + fileStr + ".pc"
        val pc =
            if (new File(pcFile).exists())
                new FeatureExprParser().parseFile(pcFile)
            else True

        val xmlFile = path + fileStr + ".c.xml"
        if (new File(xmlFile).exists()){
        val root = xml.XML.loadFile(xmlFile)

        val errors = root  \ "typeerror"

        for (error <- errors) {

            val fexprStr = (error \ "featurestr").text.trim
            val severity = (error \ "severity").text.trim + ":" + (error \ "severityextra").text.trim
            val fexpr = new FeatureExprParser().parse(fexprStr.replace("True","1").replace("False","0"))
            val line = ((error \ "position").head \ "line").text.trim

            val out = "%s;%s;%s;%s\n".format(severity, (fexpr and pc).toTextExpr, fileStr,line)
            print(out)
            output.write(out)
        }} else println(xmlFile+" not found")
    }
    output.close



}
