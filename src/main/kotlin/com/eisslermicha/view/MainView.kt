package com.eisslermicha.view

import javafx.beans.property.SimpleObjectProperty
import javafx.embed.swing.SwingFXUtils
import javafx.scene.control.TextField
import javafx.scene.image.Image
import org.matheclipse.core.basic.Config
import org.matheclipse.core.eval.ExprEvaluator
import org.matheclipse.core.eval.TeXUtilities
import org.matheclipse.core.expression.F
import org.matheclipse.core.interfaces.ISymbol
import org.scilab.forge.jlatexmath.TeXConstants
import org.scilab.forge.jlatexmath.TeXFormula
import tornadofx.*
import java.awt.image.BufferedImage
import java.io.StringWriter

const val BACKSLASH = "BACKSLASH"

class MainView : View("Physix2Latex") {

    fun escapeSymbol(symbol: String): String {

        return symbol.replace("\\", BACKSLASH, true)
    }

    fun unescapeSymbol(symbol: String): String {
        return symbol.replace(BACKSLASH, "\\", true)
    }

//    var texImage: Image? = null
    val imgObserver = SimpleObjectProperty<Image>(
        SwingFXUtils.toFXImage(TeXFormula("x^2").createBufferedImage(TeXConstants.STYLE_TEXT, 30F, java.awt.Color.BLACK, null) as BufferedImage?, null))

    override val root = vbox {
        hbox {

            lateinit var textInput: TextField

            fun calculate() {
                println(textInput.text)
                Config.PARSER_USE_LOWERCASE_SYMBOLS = false

                val util = ExprEvaluator(false, 100)
                val varStrings = arrayOf("x", "\\gamma", "z")
                val vars = ArrayList<ISymbol>()
                varStrings.forEach {
                    val sym = util.defineVariable(escapeSymbol(it))
                    vars.add(sym)
                }
                val f = util.eval(escapeSymbol(textInput.text))

//                val parser = ExprParser(EvalEngine.get(), ExprParserFactory.RELAXED_STYLE_FACTORY, true)
//                val expr = parser.parse(textfield.text)

                var result = util.parse("0")
                vars.forEach { symbol ->
                    val d = util.eval(F.D(f, symbol))
                    val eSym = util.defineVariable(BACKSLASH + "Delta " + symbol.symbolName)
                    result = F.Plus(result, F.pow(F.Times(d, eSym), util.parse("2")))
                }
                result = util.eval(F.Simplify(F.Sqrt(result)))

                val texUtil = TeXUtilities(util.evalEngine, true)
                val sw = StringWriter(100)
                texUtil.toTeX(result, sw)
                val texFormula = TeXFormula(unescapeSymbol(sw.toString()))
                val awtImg = texFormula.createBufferedImage(TeXConstants.STYLE_TEXT, 30F, java.awt.Color.BLACK, null)
                val texImage = SwingFXUtils.toFXImage(awtImg as BufferedImage?, null)
                imgObserver.set(texImage)


//                val f = BufferedImage
            }
            textInput = textfield("x^2") {
                action {
                    calculate()
                }
            }

            button("Berechnen").setOnAction {
                calculate()
            }
        }
        imageview(imgObserver)

//            label(title) {
//                addClass(Styles.heading)
//            }
    }

//    class TexCanvas(width: Double, height: Double) : javafx.scene.canvas.Canvas(width, height) {
//
//        lateinit var g2: FXGraphics2D
//
//        init {
//
//        }
//    }
}