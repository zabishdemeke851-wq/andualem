package com.example.core.time

import java.util.Calendar
import java.util.TimeZone

data class EthiopianClockTime(
    val hour: Int,      // 1 to 12
    val minute: Int,    // 0 to 59
    val second: Int,    // 0 to 59
    val periodEn: String, // "Day (Ken)" or "Night (Mese)"
    val periodAm: String  // "ቀን" or "ሌሊት"
) {
    val formattedAm: String get() = String.format("%02d:%02d:%02d %s", hour, minute, second, periodAm)
    val formattedEn: String get() = String.format("%02d:%02d:%02d %s", hour, minute, second, periodEn)
}

object EthiopianTime {

    /**
     * Converts a Civil 24-hour time to Ethiopian traditional clock time.
     * 6:00 AM Civil -> 12:00 Day
     * 7:00 AM Civil -> 01:00 Day
     * 6:00 PM Civil -> 12:00 Night
     * 7:00 PM Civil -> 01:00 Night
     */
    fun civilToEthiopianClock(civilHour24: Int, minute: Int, second: Int): EthiopianClockTime {
        // Shift civil hour by -6 hours (mod 12)
        val rawHour = (civilHour24 + 6) % 12
        val ethHour = if (rawHour == 0) 12 else rawHour

        val isDaytime = civilHour24 in 6..17

        val periodEn = if (isDaytime) "Daytime (Ken)" else "Nighttime (Mese)"
        val periodAm = if (isDaytime) "ቀን" else "ሌሊት"

        return EthiopianClockTime(
            hour = ethHour,
            minute = minute,
            second = second,
            periodEn = periodEn,
            periodAm = periodAm
        )
    }

    /**
     * Gets current Ethiopian clock time for current system time in East Africa Timezone
     */
    fun getCurrentEthiopianClockTime(): EthiopianClockTime {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("Africa/Addis_Ababa"))
        val hour24 = cal.get(Calendar.HOUR_OF_DAY)
        val min = cal.get(Calendar.MINUTE)
        val sec = cal.get(Calendar.SECOND)
        return civilToEthiopianClock(hour24, min, sec)
    }
}
