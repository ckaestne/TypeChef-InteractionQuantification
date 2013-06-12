package de.fosd.typechef.interactions

import org.junit._
import Assert._
import de.fosd.typechef.featureexpr.FeatureExprFactory._
import de.fosd.typechef.featureexpr.{LeakBDD, FeatureExprFactory}
import de.fosd.typechef.featureexpr.bdd.BDDFeatureExpr
import java.util

class InteractionQuantificationTest {
    FeatureExprFactory.setDefault(FeatureExprFactory.bdd)

    val fa = createDefinedExternal("a")
    val fb = createDefinedExternal("b")
    val fc = createDefinedExternal("c")

    @Test
    def testDegree() {


        import de.fosd.typechef.interactions.AnalyseStatistics.interactionDegree

        assertEquals(0, interactionDegree(True))
        assertEquals(0, interactionDegree(False))
        assertEquals(1, interactionDegree(fa))
        assertEquals(2, interactionDegree(fa and fb))

        assertEquals(1, interactionDegree(fb or fa))
        assertEquals(1, interactionDegree(fa or fb.not()))
        assertEquals(1, interactionDegree(fa or (fb or fc)))
        assertEquals(1, interactionDegree(fa or (fb and fc)))
        assertEquals(2, interactionDegree(fa and (fb or fc)))
        //        assertEquals(1,interactionDegree((fa and fb) or (fa andNot fb and fc) or (fa.not and fb)))

    }

    @Test
    def testTmp() {


    }
}
