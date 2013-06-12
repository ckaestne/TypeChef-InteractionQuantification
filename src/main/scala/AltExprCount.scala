package de.fosd.typechef.interactions {

import de.fosd.typechef.featureexpr.{LeakBDD, FeatureExprFactory, FeatureExpr, FeatureExprParser}
import de.fosd.typechef.featureexpr.bdd.BDDFeatureExpr
import java.util
import scala.io.Source


object AltExprCount extends App {



    val lines = Source.fromFile("expralternatives.lst").getLines()


    val results = for (line <- lines) yield {

        val elem = line.split(";").head.toInt

        (elem,elem)
    }


    val summary = results.toList.groupBy(_._1).mapValues(_.size)
    summary.keys.toList.sorted.map(k=>println("%d -> %d".format(k, summary(k))))

}
}

