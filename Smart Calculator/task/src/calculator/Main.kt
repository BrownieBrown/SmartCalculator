package calculator

import java.lang.NumberFormatException
import java.math.BigInteger
import kotlin.system.exitProcess

fun main() {
    CalculatorProgram().start()
}

val mapOfVariables = mutableMapOf<String, BigInteger>()

class CalculatorProgram {

    fun start() {
        try {
            handleInput()
        } catch (exception: NumberFormatException) {
            println("Invalid expression")
        }
    }

    private fun handleInput() {
        while (true) {
            val line = readLine()!!
            when {
                line == "" -> {}
                line.startsWith("/") -> checkCommand(line)
                lineContainsVariable(line) -> checkSingleVariable(line)
                line.contains("(\\()".toRegex()) -> checkForInvalidExpressionLeftParentheses(line)
                line.contains("(\\))".toRegex()) -> checkForInvalidExpressionRightParentheses(line)
                line.contains("(/)".toRegex()) -> checkForInvalidExpressionDivision(line)
                line.contains("(\\*)".toRegex()) -> checkForInvalidExpressionMultiplication(line)
                line.contains("=") -> isVariableInMap(line)
                line.split(" ").size == 1 -> println(line)
                else -> InfixToPostfix().buildStack(line)
            }
        }
    }

    private fun checkCommand(line: String) {
        when (line) {
            "/exit" -> exit()
            "/help" -> help()
            else -> println("Unknown command")
        }
    }

    private fun exit() {
        println("Bye!")
        exitProcess(1)
    }

    private fun help() {
        println("The program calculates numbers")
    }

    private fun lineContainsVariable(input: String):Boolean {
        return removeWhiteSpace(input).contains("^[a-zA-Z]+$".toRegex())
    }

    private fun checkSingleVariable(input: String) {
        if (mapOfVariables.containsKey(removeWhiteSpace(input))) println(mapOfVariables[removeWhiteSpace(input)])
        else println("Unknown variable")
    }

    private fun checkForInvalidExpressionLeftParentheses(input: String) {
        if (!input.contains("(\\))".toRegex())) println("Invalid Expression")
        else InfixToPostfix().buildStack(input)
    }

    private fun checkForInvalidExpressionRightParentheses(input: String) {
        if (!input.contains("(\\()".toRegex())) println("Invalid Expression")
        else InfixToPostfix().buildStack(input)
    }

    private fun checkForInvalidExpressionMultiplication(input: String) {
        if (input.count { it == '*' } > 1) println("Invalid Expression")
        else InfixToPostfix().buildStack(input)
    }

    private fun checkForInvalidExpressionDivision(input: String) {
        if (input.count { it == '/' } > 1) println("Invalid Expression")
        else InfixToPostfix().buildStack(input)
    }

    private fun removeWhiteSpace(input: String): String {
        return input.replace("\\s".toRegex(), "")
    }

    private fun isVariableInMap(line: String) {
        line.replace("\\s".toRegex(), "").also {
            if (isDigitAssignment(it)) {
                assignValueToVariable(it)
            } else if (isVariableAssignment(it)) {
                if (mapContainsVariableAfter(it)) {
                    assignVariableToVariable(it)
                } else println("Unknown variable")
            } else {
                isInvalidIdentifier(it)
                isInvalidAssignment(it)
            }
        }
    }

    private fun isDigitAssignment(input: String): Boolean {
        return input.contains("^([a-zA-Z]+[=]([\\d]+|-[\\d]+))$".toRegex())
    }

    private fun assignValueToVariable(input: String) {
        mapOfVariables[input.substringBefore('=')] = BigInteger(input.substringAfter("="))
    }

    private fun isVariableAssignment(input: String): Boolean {
        return input.contains("^([a-zA-Z]+[=][a-zA-Z]+)$".toRegex())
    }

    private fun mapContainsVariableAfter(input: String): Boolean {
        return mapOfVariables.containsKey(input.substringAfter('='))
    }

    private fun assignVariableToVariable(input: String) {
        mapOfVariables[input.substringBefore('=')] = mapOfVariables[input.substringAfter('=')]!!
    }

    private fun isInvalidIdentifier(input: String) {
        if (input.substringBefore('=').contains("\\d".toRegex())) println("Invalid identifier")
    }

    private fun isInvalidAssignment(input: String) {
        if (input.substringAfter('=').contains("\\d".toRegex())) println("Invalid assignment")
    }
}

class InfixToPostfix {

    fun buildStack(input: String) {
        val result = mutableListOf<String>()
        val stack = mutableListOf<String>()

        val formattedUserInput = formatUserInputToList(input)

        for (string in formattedUserInput) {
            when {
                string.isOperand() -> {
                    result += string
                }
                string.isVariable() -> {
                    result += string
                }
                string.isOperator() -> {
                    while (stack.isNotEmpty()
                            && !stack.last().isOpeningParentheses()
                            && !incomingOperatorHasHigherPriority(stack.last(), string)) {
                        result += stack.last()
                        stack.popString()
                    }
                    stack += string
                }
                string.isOpeningParentheses() -> {
                    stack += string
                }
                string.isClosingParentheses() -> {
                    while (stack.isNotEmpty() && !stack.last().isOpeningParentheses()){
                        result += stack.last()
                        stack.popString()
                    }
                    stack.popString()
                }
            }
        }
        while (stack.isNotEmpty()) {
            result += stack.last()
            stack.popString()
        }
        calculateResult(result)
    }

    private fun MutableList<String>.popString() {
        this.removeAt(this.lastIndex)
    }

    private fun MutableList<BigInteger>.popInt() {
        this.removeAt(this.lastIndex)
    }

    private fun formatUserInputToList(input: String): List<String> {
        return input
                .replace("\\s+".toRegex(), "")
                .replace("(--)+".toRegex(), "+")
                .replace("(\\+)+".toRegex(), "+")
                .replace("\\+-".toRegex(), "-")
                .replace("( )+( )".toRegex(), " ")
                .replace("\\+".toRegex(), " + ")
                .replace("-".toRegex(), " - ")
                .replace("\\*".toRegex(), " * ")
                .replace("/".toRegex(), " / ")
                .replace("\\(".toRegex(), " ( ")
                .replace("\\)".toRegex(), " ) ")
                .replace("\\s+".toRegex(), " ")
                .trimStart()
                .trimEnd()
                .split(" ")
    }

    private fun String.isOperand(): Boolean {
        return this.contains("\\d".toRegex())
    }

    private fun String.isVariable(): Boolean {
        return this.contains("[a-zA-Z]".toRegex())
    }

    private fun String.isOperator(): Boolean {
        return this.contains("[+-/*+^]".toRegex())
    }

    private fun String.isOpeningParentheses(): Boolean {
        return this == "("
    }

    private fun String.isClosingParentheses(): Boolean {
        return this == ")"
    }

    private fun incomingOperatorHasHigherPriority(topOfStack: String, currentString: String): Boolean {
        val topOfStackWeight = getOperatorWeight(topOfStack)
        val currentStringWeight = getOperatorWeight(currentString)

        return when {
            currentStringWeight > topOfStackWeight -> true
            else -> false
        }
    }

    private fun getOperatorWeight(operator: String): Int {
        return when(operator) {
            "+" -> 1
            "-" -> 1
            "*" -> 2
            "/" -> 2
            "^" -> 3
            else -> 0
        }
    }

    private fun calculateResult(result: MutableList<String>) {
        val stack = mutableListOf<BigInteger>()
        val variable = "[a-zA-Z]".toRegex()
        for (postfixExpression in result) {
            when {
                postfixExpression.isOperand() -> {
                    stack += BigInteger(postfixExpression)
                }
                postfixExpression.matches(variable) -> {
                    stack += mapOfVariables.getValue(postfixExpression)
                }
                postfixExpression.isOperator() -> {
                    when (postfixExpression) {
                        "+" -> {
                            val resultAddition = addition(stack[stack.lastIndex - 1], stack[stack.lastIndex])
                            stack.popInt()
                            stack.popInt()
                            stack += resultAddition

                        }
                        "-" -> {
                            val resultSubtraction = subtraction(stack[stack.lastIndex - 1], stack[stack.lastIndex])
                            stack.popInt()
                            stack.popInt()
                            stack += resultSubtraction
                        }
                        "*" -> {
                            val resultMultiplication = multiplication(stack[stack.lastIndex - 1], stack[stack.lastIndex])
                            stack.popInt()
                            stack.popInt()
                            stack += resultMultiplication
                        }
                        "/" -> {
                            val resultDivision = division(stack[stack.lastIndex - 1], stack[stack.lastIndex])
                            stack.popInt()
                            stack.popInt()
                            stack += resultDivision
                        }
                        "^" -> {
                            println()
                        }
                    }
                }
            }
        }
        println(stack[0])
    }

    private fun addition(a:BigInteger, b:BigInteger): BigInteger {
        return a + b
    }

    private fun subtraction(a:BigInteger, b:BigInteger): BigInteger {
        return a - b
    }

    private fun division(a:BigInteger, b:BigInteger): BigInteger {
        return a / b
    }

    private fun multiplication(a:BigInteger, b:BigInteger): BigInteger {
        return a * b
    }
}