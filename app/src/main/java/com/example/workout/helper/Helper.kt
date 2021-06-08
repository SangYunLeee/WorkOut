package com.example.workout.helper


import java.math.RoundingMode
import java.text.DecimalFormat

class Helper {
    companion object {
        fun roundOffDecimal(number: Double): Double {
            val df = DecimalFormat("#.#")
            df.roundingMode = RoundingMode.CEILING
            return df.format(number).toDouble()
        }
    }
}