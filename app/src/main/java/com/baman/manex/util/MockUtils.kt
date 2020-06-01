package com.baman.manex.util


import java.util.*
import kotlin.math.abs

object MockUtils {

    fun <T> pickOne(vararg options: T): T = options[Random().nextInt(options.size)]

    fun <T> pickOne(options: List<T>): T = options[Random().nextInt(options.size)]

    fun getRandomAmount(from: Int, to: Int): Int = Random().nextInt(to - from) + from

    fun getRandomDate(fromMonthsPast: Int, toMonthsFuture: Int): Long {
        val month = 30 * 24 * 60 * 60 * 1000L
        val currentTimeMillis = System.currentTimeMillis()

        val from = currentTimeMillis - fromMonthsPast * month
        val to = currentTimeMillis + toMonthsFuture * month
        val bound = to - from
        val randomLong = abs(Random().nextLong())
        return randomLong % bound + from
    }

    fun getRandomDates(
        fromMonthsPast: Int,
        toMonthsFuture: Int,
        count: Int,
        sorted: Boolean = true
    ): List<Long> {
        val randomDates = mutableListOf<Long>()
        for (i in 0 until count) {
            randomDates.add(getRandomDate(fromMonthsPast, toMonthsFuture))
        }

        if (sorted) randomDates.sort()

        return randomDates
    }

    fun getMockCoordination(baseCoordination: Int): Double {
        val random = Random()
        val bound = 1000
        val randomInt = random.nextInt(bound)
        val randomFloat = randomInt / (bound / 2).toFloat()
        return (baseCoordination - 1 + randomFloat).toDouble()
    }
}
