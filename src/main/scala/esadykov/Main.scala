package esadykov

import esadykov.nets.{NetNode, NetElement}
import esadykov.expressions.{Expression, Empty}

/**
 * @author Ernest Sadykov
 * @since 01.05.2014
 */
object Main {
    val usageString = "Usage: ..."

    def main(args: Array[String]) {
        if (args.length < 2) {
            println(usageString)
            return
        }

        println(args(0) + " " + args(1))

        val componentsForAlgebra1: List[Map[String, List[String]]] = componentsForAlgebra(args(0))
        val componentsForAlgebra2: List[Map[String, List[String]]] = componentsForAlgebra(args(1), 10)

        println("components 1: \n" + componentsForAlgebra1.mkString("\n"))
        println("components 2: \n" + componentsForAlgebra2.mkString("\n"))
    }

    def componentsForAlgebra(filename: String, counter: Int = 1): List[Map[String, List[String]]] = {
        val netElements: Map[String, NetElement] = XmlNetManager.readNetElements(filename)
        XmlNetManager.connectNodes(netElements)
        val source = NetNode.findSource(netElements.values)


        val generatedExpression = NetNode.traverse(
            start = source,
            wereThere = Set.empty,
            acc = Empty(),
            inputs = NetNode.inputs(netElements),
            destination = NetNode.findSink(netElements.values)
        )
        val normalized: Expression = generatedExpression.normalize().normalize()
        val components: List[List[Expression]] = Expression.components(normalized)

        components.map(Expression.componentsForAlgebra(_, counter))
    }
}
