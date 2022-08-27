package com.yallina.myapplication.utils

abstract class EntityMapper<In, Out> {
    fun transform(input: In): Out {
        return map(input)
    }

    fun transform(inputs: List<In>): List<Out> {
        return inputs.map { input -> map(input) }
    }

    abstract fun map(input: In): Out
}