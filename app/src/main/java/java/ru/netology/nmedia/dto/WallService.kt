package ru.netology.nmedia.dto

import java.math.RoundingMode
import java.text.DecimalFormat

object WallService {
    fun displayCount(count : Int) : String {
        val decimalFormat = DecimalFormat("#.#")
        decimalFormat.roundingMode = RoundingMode.DOWN
        return when (count) {
            in 0..999 -> "$count"
            in 1_000..9_999 -> "${decimalFormat.format(count.toFloat() / 1_000)}K"
            in 10_000..999_999 -> "${count / 1_000}K"
            else -> "${decimalFormat.format(count.toFloat() / 1_000_000)}М"
        }
    }

}