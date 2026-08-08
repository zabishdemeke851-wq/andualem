package com.example.core.calendar

import java.util.Calendar
import java.util.TimeZone

data class EthiopianDate(
    val year: Int,
    val month: Int, // 1 to 13
    val day: Int,    // 1 to 30 (or 1 to 5/6 for Pagume)
    val dayOfWeek: Int = 1 // 1=Sunday, 7=Saturday
) {
    val isLeapYear: Boolean get() = EthiopianCalendar.isLeapYear(year)
    val monthNameEn: String get() = EthiopianCalendar.MONTH_NAMES_EN.getOrElse(month - 1) { "" }
    val monthNameAm: String get() = EthiopianCalendar.MONTH_NAMES_AM.getOrElse(month - 1) { "" }
    val monthNameGeez: String get() = EthiopianCalendar.MONTH_NAMES_GEEZ.getOrElse(month - 1) { "" }
}

data class GregorianDate(
    val year: Int,
    val month: Int, // 1 to 12
    val day: Int
)

object EthiopianCalendar {
    const val ETHIOPIAN_EPOCH = 1724221

    val MONTH_NAMES_EN = listOf(
        "Meskerem", "Tikimt", "Hidar", "Tahsas", "Tir", "Yekatit",
        "Megabit", "Miazia", "Ginbot", "Sene", "Hamle", "Nehase", "Pagume"
    )

    val MONTH_NAMES_AM = listOf(
        "መስከረም", "ጥቅምት", "ኅዳር", "ታኅሣሥ", "ጥር", "የካቲት",
        "መጋቢት", "ሚያዝያ", "ግንቦት", "ሰኔ", "ሐምሌ", "ነሐሴ", "ጳጉሜን"
    )

    val MONTH_NAMES_GEEZ = listOf(
        "መስከረም", "ጥቅምት", "ኅዳር", "ታኅሣሥ", "ጥር", "የካቲት",
        "መጋቢት", "ሚያዝያ", "ግንቦት", "ሰኔ", "ሐምሌ", "ነሐሴ", "ጳጉሜን"
    )

    val WEEKDAY_NAMES_EN = listOf(
        "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    )

    val WEEKDAY_NAMES_AM = listOf(
        "እሑድ", "ሰኞ", "ማክሰኞ", "ረቡዕ", "ሐሙስ", "ዓርብ", "ቅዳሜ"
    )

    fun isLeapYear(year: Int): Boolean {
        // Ethiopian leap year occurs every 4 years when year % 4 == 3
        return (year % 4) == 3
    }

    /**
     * Converts Ethiopian Date to Julian Day Number (JDN)
     */
    fun ethiopianToJdn(year: Int, month: Int, day: Int): Long {
        return (ETHIOPIAN_EPOCH + 365.toLong() * (year - 1) +
                (year / 4).toLong() +
                30.toLong() * (month - 1) +
                day.toLong() - 1)
    }

    /**
     * Converts Julian Day Number (JDN) to Ethiopian Date
     */
    fun jdnToEthiopian(jdn: Long): EthiopianDate {
        var year = ((4L * (jdn - ETHIOPIAN_EPOCH) + 1463L) / 1461L).toInt()
        if (jdn < ethiopianToJdn(year, 1, 1)) {
            year--
        }
        val dayOfYear = (jdn - ethiopianToJdn(year, 1, 1)).toInt()
        val month = (dayOfYear / 30) + 1
        val day = (dayOfYear % 30) + 1

        // Calculate weekday: JDN + 1 mod 7 gives 1=Sunday, 7=Saturday
        val dayOfWeek = (((jdn + 1) % 7) + 1).toInt()

        return EthiopianDate(year, month, day, dayOfWeek)
    }

    /**
     * Converts Gregorian Date to Julian Day Number (JDN)
     */
    fun gregorianToJdn(year: Int, month: Int, day: Int): Long {
        val a = (14 - month) / 12
        val y = year + 4800 - a
        val m = month + 12 * a - 3
        return day + (153 * m + 2) / 5 + 365L * y + y / 4 - y / 100 + y / 400 - 32045
    }

    /**
     * Converts Julian Day Number (JDN) to Gregorian Date
     */
    fun jdnToGregorian(jdn: Long): GregorianDate {
        val l = jdn + 68569
        val n = 4 * l / 146097
        val l1 = l - (146097 * n + 3) / 4
        val i = 4000 * (l1 + 1) / 1464001
        val l2 = l1 - 1461 * i / 4 + 31
        val j = 80 * l2 / 2447
        val day = (l2 - 2447 * j / 80).toInt()
        val l3 = j / 11
        val month = (j + 2 - 12 * l3).toInt()
        val year = (100 * (n - 49) + i + l3).toInt()

        return GregorianDate(year, month, day)
    }

    /**
     * Gregorian to Ethiopian conversion
     */
    fun gregorianToEthiopian(year: Int, month: Int, day: Int): EthiopianDate {
        val jdn = gregorianToJdn(year, month, day)
        return jdnToEthiopian(jdn)
    }

    /**
     * Ethiopian to Gregorian conversion
     */
    fun ethiopianToGregorian(year: Int, month: Int, day: Int): GregorianDate {
        val jdn = ethiopianToJdn(year, month, day)
        return jdnToGregorian(jdn)
    }

    /**
     * Get current Ethiopian date for today's system date
     */
    fun getTodayEthiopian(): EthiopianDate {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("Africa/Addis_Ababa"))
        val gYear = cal.get(Calendar.YEAR)
        val gMonth = cal.get(Calendar.MONTH) + 1
        val gDay = cal.get(Calendar.DAY_OF_MONTH)
        return gregorianToEthiopian(gYear, gMonth, gDay)
    }

    /**
     * Number of days in an Ethiopian month
     */
    fun getDaysInMonth(year: Int, month: Int): Int {
        if (month in 1..12) return 30
        if (month == 13) {
            return if (isLeapYear(year)) 6 else 5
        }
        return 30
    }

    /**
     * Formats an Ethiopian Date string in Amharic or English
     */
    fun formatEthiopian(date: EthiopianDate, inAmharic: Boolean = true): String {
        return if (inAmharic) {
            "${date.year} ${date.monthNameAm} ${date.day}"
        } else {
            "${date.monthNameEn} ${date.day}, ${date.year} EC"
        }
    }
}
