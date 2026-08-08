package com.example.core.abushakir

import com.example.core.calendar.EthiopianCalendar
import com.example.core.calendar.EthiopianDate

data class MovableFeast(
    val nameEn: String,
    val nameAm: String,
    val ethiopianMonth: Int,
    val ethiopianDay: Int,
    val description: String
)

data class BahreHasabResult(
    val ethiopianYear: Int,
    val ameteAlem: Long,
    val evangelistEn: String, // Matthew, Mark, Luke, John
    val evangelistAm: String,
    val wenber: Int,
    val abekt: Int,
    val metke: Int,
    val metkeMonthEn: String,
    val metkeMonthAm: String,
    val movableFeasts: List<MovableFeast>
)

object BahreHasab {

    val EVANGELISTS_EN = listOf("John", "Matthew", "Mark", "Luke")
    val EVANGELISTS_AM = listOf("ዮሐንስ", "ማቴዎስ", "ማርቆስ", "ሉቃስ")

    /**
     * Calculates Bahre Hasab for a given Ethiopian Year
     */
    fun calculate(year: Int): BahreHasabResult {
        val ameteAlem = year + 5500L
        val evangelistIndex = (ameteAlem % 4).toInt()
        val evangelistEn = EVANGELISTS_EN[evangelistIndex]
        val evangelistAm = EVANGELISTS_AM[evangelistIndex]

        val wenber = if ((ameteAlem % 19) == 0L) 18 else ((ameteAlem % 19) - 1).toInt()
        val abekt = (wenber * 11) % 30
        val metke = if (abekt == 0) 30 else 30 - abekt

        val metkeMonth = if (metke > 14) 1 else 2 // 1 = Meskerem, 2 = Tikimt
        val metkeMonthEn = if (metkeMonth == 1) "Meskerem" else "Tikimt"
        val metkeMonthAm = if (metkeMonth == 1) "መስከረም" else "ጥቅምት"

        // Calculate Day of Week for Metke
        val metkeJdn = EthiopianCalendar.ethiopianToJdn(year, metkeMonth, metke)
        val metkeDayOfWeek = (((metkeJdn + 1) % 7) + 1).toInt() // 1=Sunday, ..., 7=Saturday

        // Nineveh (Nenewe) fast day computation:
        // Nineveh is calculated based on Metke and Tewsak weekday offset
        // Distance to Nineveh Sunday:
        val ninevehMonth = if (metkeMonth == 1) 5 else 6 // Tir or Yekatit
        
        // Nineveh base offset from Metke:
        // Tewsak values based on Metke weekday:
        val tewsakDay = when (metkeDayOfWeek) {
            1 -> 7  // Sunday
            2 -> 6  // Monday
            3 -> 5  // Tuesday
            4 -> 4  // Wednesday
            5 -> 3  // Thursday
            6 -> 2  // Friday
            7 -> 1  // Saturday
            else -> 0
        }
        
        var neneweDay = metke + tewsakDay
        var neneweMonth = if (metkeMonth == 1) 5 else 6 // Tir (5) or Yekatit (6)
        if (neneweDay > 30) {
            neneweDay -= 30
            neneweMonth += 1
        }

        val feasts = calculateFeastsFromNineveh(year, neneweMonth, neneweDay)

        return BahreHasabResult(
            ethiopianYear = year,
            ameteAlem = ameteAlem,
            evangelistEn = evangelistEn,
            evangelistAm = evangelistAm,
            wenber = wenber,
            abekt = abekt,
            metke = metke,
            metkeMonthEn = metkeMonthEn,
            metkeMonthAm = metkeMonthAm,
            movableFeasts = feasts
        )
    }

    private fun calculateFeastsFromNineveh(year: Int, nMonth: Int, nDay: Int): List<MovableFeast> {
        val nJdn = EthiopianCalendar.ethiopianToJdn(year, nMonth, nDay)

        fun offsetDate(offsetDays: Int): EthiopianDate {
            return EthiopianCalendar.jdnToEthiopian(nJdn + offsetDays)
        }

        val nenewe = offsetDate(0)
        val abiyTsom = offsetDate(14)
        val debreZeyt = offsetDate(41)
        val hosanna = offsetDate(62)
        val siklet = offsetDate(67)
        val fasika = offsetDate(69)
        val rikbeKahnat = offsetDate(93)
        val erget = offsetDate(108)
        val paraklitos = offsetDate(118)
        val tsomeHawariat = offsetDate(119)

        return listOf(
            MovableFeast("Fast of Nineveh (Tsome Nenewe)", "ጾመ ነነዌ", nenewe.month, nenewe.day, "3-day fast commemorating the repentance of Nineveh"),
            MovableFeast("Great Lent (Abiy Tsom)", "ዓቢይ ጾም", abiyTsom.month, abiyTsom.day, "The 55-day holy fast before Easter"),
            MovableFeast("Debre Zeyt (Mount of Olives)", "ደብረ ዘይት", debreZeyt.month, debreZeyt.day, "Mid-point of Great Lent"),
            MovableFeast("Hosanna (Palm Sunday)", "ሆሣዕና", hosanna.month, hosanna.day, "Christ's triumphal entry into Jerusalem"),
            MovableFeast("Good Friday (Siklet)", "ስቅለት", siklet.month, siklet.day, "Crucifixion of Jesus Christ"),
            MovableFeast("Fasika (Ethiopian Easter)", "ፋሲካ", fasika.month, fasika.day, "Feast of the Resurrection of Jesus Christ"),
            MovableFeast("Rikbe Kahnat", "ርክበ ካህናት", rikbeKahnat.month, rikbeKahnat.day, "Synod of Priests"),
            MovableFeast("Feast of Ascension (Erget)", "ዕርገት", erget.month, erget.day, "Ascension of Christ into Heaven 40 days after Easter"),
            MovableFeast("Pentecost (Paraklitos)", "ጰራቅሊጦስ", paraklitos.month, paraklitos.day, "Descent of the Holy Spirit 50 days after Easter"),
            MovableFeast("Fast of the Apostles (Tsome Hawariat)", "ጾመ ሐዋርያት", tsomeHawariat.month, tsomeHawariat.day, "Apostles' Fast following Pentecost")
        )
    }
}
