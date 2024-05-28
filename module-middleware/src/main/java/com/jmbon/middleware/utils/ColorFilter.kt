package com.jmbon.middleware.utils

import androidx.palette.graphics.Palette

object ColorFilter {
    val DEFAULT_FILTER: Palette.Filter = object : Palette.Filter {
        private val BLACK_MAX_LIGHTNESS = 0.1f
        private val WHITE_MIN_LIGHTNESS = 0.85f


        /**
         * @return true if the color represents a color which is close to black.
         */
        private fun isBlack(hslColor: FloatArray): Boolean {
            return hslColor[2] <= BLACK_MAX_LIGHTNESS
        }

        /**
         * @return true if the color represents a color which is close to white.
         */
        private fun isWhite(hslColor: FloatArray): Boolean {
            return hslColor[2] >= WHITE_MIN_LIGHTNESS
        }

        /**
         * @return true if the color lies close to the red side of the I line.
         */
        private fun isNearRedILine(hslColor: FloatArray): Boolean {
            return hslColor[0] in 10f..37f && hslColor[1] <= 0.92f  //hslColor[1] 饱和度
        }

        override fun isAllowed(rgb: Int, hsl: FloatArray): Boolean {
            return !isWhite(hsl) && !isBlack(hsl) && !isNearRedILine(hsl)
        }
    }
}