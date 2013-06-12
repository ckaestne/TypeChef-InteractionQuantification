package de.fosd.typechef.interactions {

import de.fosd.typechef.featureexpr.{LeakBDD, FeatureExprFactory, FeatureExpr, FeatureExprParser}
import java.io.File
import de.fosd.typechef.featureexpr.FeatureExprFactory._
import de.fosd.typechef.featureexpr.bdd.BDDFeatureExpr
import java.util
import scala.io.Source

object Stats {
    val EXPORTS = "exports.lst"
    val EXPORTSGROUPED = "exportsgrouped.lst"
    val EXPORTSGROUPEDNEG = "exportsgroupedneg.lst"
    val IMPORTS = "imports.lst"


    val FILEPC = "filepc.lst"
    val ASTTL = "asttoplevel.lst"
    val ASTANY = "astany.lst"
    val CCFG = "cfg.lst"
    val ANALYSIS = "analysis.lst"
    val EXPRTYPING = "exprtypes.lst"

    case class Stat(file: String, descr: String, negated: Boolean = false)

    val stats = List(
        Stat(FILEPC, ".c File compilation"),
        Stat(ASTTL, "Top-level declarations"),
        Stat(ASTANY, "Structural nesting in the AST\n(counting only subtrees with distinct presence conditions)"),
        Stat(IMPORTS, "Imported functions"),
        Stat(EXPORTS, "Exported functions"),
        Stat(EXPORTSGROUPED, "Condition that a function is globally exported in the project"),
        Stat(EXPORTSGROUPED, "Condition that a function is globally NOT exported in the project\n(relevant for linker checks)", true),
        Stat(ASTTL, "Just for fun: condition that a top-level expression is locally NOT defined", true),
        Stat(EXPRTYPING, "Expression types"),
        Stat(CCFG, "Edges in an (incomplete?) control-flow graph"),
        Stat(ANALYSIS, "Warnings from the simple static analyses (all type based, almost all integer warnings)")
    )

}

object AnalyseStatistics extends App {
//    FeatureExprFactory.setDefault(FeatureExprFactory.bdd)

    for (stat <- Stats.stats) {

        val lines = Source.fromFile(stat.file).getLines()


        val results = for (line <- lines) yield {

            val elem = line.split(";")
            val (node, fexprStr, location, lineNr) = (elem.dropRight(3).last, elem.dropRight(2).last, elem.dropRight(1).last, elem.last)

            var fexpr = new FeatureExprParser().parse(fexprStr)

            if (stat.negated) fexpr=fexpr.not()

            (interactionDegree(fexpr), 1)
        }

        println(stat.descr + ": \n")

        val summary = results.toList.groupBy(_._1).mapValues(_.size)
        summary.keys.toList.sorted.map(k => println("%d -> %d".format(k, summary(k))))

        println("\n\n")
    }

    def interactionDegree(fexpr: FeatureExpr): Int = {
        //interaction degree is the smallest number of variables that need to be set to reproduce the problem
        //we use the shortest path in a BDD as simple way of computing this

        //this does not always return the optimal solution, because variable ordering matters!
        //for example (fa and fb) or (fa andNot fb and fc) or (fa.not and fb) will produce a result 2 instead of 1

        //also does not consider the feature model

//        val bdd = new LeakBDD(fexpr.asInstanceOf[BDDFeatureExpr])
//
//        import scala.collection.JavaConversions._
//
//        val allsat = bdd.getBDD.allsat().asInstanceOf[util.LinkedList[Array[Byte]]]
//
//        if (allsat.isEmpty) 0
//        else allsat.map(_.count(_ >= 0)).min
//
        fexpr.collectDistinctFeatures.size
    }

}

}

package de.fosd.typechef.featureexpr {

import de.fosd.typechef.featureexpr.bdd.BDDFeatureExpr

class LeakBDD(bdd: BDDFeatureExpr) {

    def getBDD = bdd.bdd
}

}