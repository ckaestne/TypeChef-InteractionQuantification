package de.fosd.typechef.interactions

import de.fosd.typechef.featureexpr.FeatureExprParser
import java.io.{BufferedWriter, FileWriter, File}
import de.fosd.typechef.featureexpr.FeatureExprFactory._
import de.fosd.typechef.typesystem.linker.{CSignature, InterfaceWriter, CInterface}

object CollectLinkerStats extends App {

    val path = "../BusyboxAnalysis/gitbusybox/"
    val fileList = scala.io.Source.fromFile(path + "filelist").getLines()

    val outputE =new BufferedWriter(new FileWriter(new File(Stats.EXPORTS)))
    val outputI =new BufferedWriter(new FileWriter(new File(Stats.IMPORTS)))
    val outputG =new BufferedWriter(new FileWriter(new File(Stats.EXPORTSGROUPED)))
    val outputGn =new BufferedWriter(new FileWriter(new File(Stats.EXPORTSGROUPEDNEG)))

    var allExports = Seq[CSignature]()

    for (fileStr <- fileList)  {

        val pcFile = path + fileStr + ".pc"
        val pc =
            if (new File(pcFile).exists())
                new FeatureExprParser().parseFile(pcFile)
            else True

        val interfaceFile = new File( path + fileStr + ".interface")
        assert(interfaceFile.exists())
        val interface=new InterfaceWriter(){}.readInterface(interfaceFile)

        allExports = interface.exports.map(_.and(pc)) ++ allExports

        for (e<-interface.exports)
            outputE.write( "%s;%s;%s;%d\n".format(e.name, (e.fexpr and pc).toTextExpr, e.pos.head.getFile, e.pos.head.getLine))
        for (e<-interface.imports)
            outputI.write( "%s;%s;%s;%d\n".format(e.name, (e.fexpr and pc).toTextExpr, e.pos.head.getFile, e.pos.head.getLine))
    }
    outputE.close
    outputI.close()


    val groupedExports = allExports.groupBy(_.name)

    for ((name, sigs) <-groupedExports) {
        val anyExport = sigs.map(_.fexpr).reduce(_ or _)
        outputG.write( "%s;%s;%s;%d\n".format(name, anyExport.toTextExpr, "", -1))
        outputGn.write( "%s;%s;%s;%d\n".format(name, anyExport.not().toTextExpr, "", -1))

    }

    outputG.close()
    outputGn.close()

}
