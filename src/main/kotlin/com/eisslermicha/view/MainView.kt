package com.eisslermicha.view

import com.eisslermicha.app.Styles
import org.matheclipse.core.eval.EvalEngine
import org.matheclipse.core.parser.ExprParser
import org.matheclipse.core.parser.ExprParserFactory
import tornadofx.*

class MainView : View("Test") {

    override val root = hbox {
//        label(title) {
//            addClass(Styles.heading)
//        }
        val textfield = textfield()
        button("Test") {
            action {
//                println(textfield.text)
                val parser = ExprParser(EvalEngine.get(), ExprParserFactory.RELAXED_STYLE_FACTORY, true)
                val expr = parser.parse(textfield.text)
//                val f = BufferedImage


            }
        }
    }
}