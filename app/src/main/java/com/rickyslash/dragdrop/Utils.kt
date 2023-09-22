package com.rickyslash.dragdrop

import net.objecthunter.exp4j.ExpressionBuilder

fun evaluateMathFromString(expression: String): Int? {
    return try {
        val result = ExpressionBuilder(expression).build().evaluate().toInt()
        result
    } catch (e: Exception) {
        null
    }
}