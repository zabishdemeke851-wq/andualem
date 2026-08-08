package com.example

import com.example.core.abushakir.BahreHasab
import com.example.core.calendar.EthiopianCalendar
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EthiopianCalendarUnitTest {

    @Test
    fun testLeapYears() {
        // Ethiopian leap years occur every 4 years when year % 4 == 3
        assertTrue("2015 EC is a leap year (Pagume 6)", EthiopianCalendar.isLeapYear(2015))
        assertTrue("2019 EC is a leap year (Pagume 6)", EthiopianCalendar.isLeapYear(2019))
        assertFalse("2016 EC is not a leap year", EthiopianCalendar.isLeapYear(2016))
        assertFalse("2017 EC is not a leap year", EthiopianCalendar.isLeapYear(2017))
        assertFalse("2018 EC is not a leap year", EthiopianCalendar.isLeapYear(2018))
    }

    @Test
    fun testDaysInMonth() {
        assertEquals("Regular months have 30 days", 30, EthiopianCalendar.getDaysInMonth(2016, 1))
        assertEquals("Regular months have 30 days", 30, EthiopianCalendar.getDaysInMonth(2016, 12))
        assertEquals("Pagume in non-leap year has 5 days", 5, EthiopianCalendar.getDaysInMonth(2016, 13))
        assertEquals("Pagume in leap year 2015 has 6 days", 6, EthiopianCalendar.getDaysInMonth(2015, 13))
    }

    @Test
    fun testGregorianToEthiopianConversion() {
        // Sep 12, 2023 GC (following leap year 2015 EC) = Meskerem 1, 2016 EC
        val ethDate1 = EthiopianCalendar.gregorianToEthiopian(2023, 9, 12)
        assertEquals("Year should be 2016 EC", 2016, ethDate1.year)
        assertEquals("Month should be Meskerem (1)", 1, ethDate1.month)
        assertEquals("Day should be 1", 1, ethDate1.day)

        // Jan 7, 2024 GC (Genna) = Tahsas 28/29, 2016 EC
        val ethDate2 = EthiopianCalendar.gregorianToEthiopian(2024, 1, 7)
        assertEquals("Year should be 2016 EC", 2016, ethDate2.year)
        assertEquals("Month should be Tahsas (4)", 4, ethDate2.month)
    }

    @Test
    fun testEthiopianToGregorianConversion() {
        // Meskerem 1, 2016 EC = Sep 12, 2023 GC
        val gregDate = EthiopianCalendar.ethiopianToGregorian(2016, 1, 1)
        assertEquals(2023, gregDate.year)
        assertEquals(9, gregDate.month)
        assertEquals(12, gregDate.day)
    }

    @Test
    fun testBahreHasabCalculations() {
        // Test Bahre Hasab computus for 2016 EC
        val res2016 = BahreHasab.calculate(2016)
        assertEquals("Amete Alem for 2016 EC", 7516L, res2016.ameteAlem)
        assertEquals("Evangelist for 2016 EC", "John", res2016.evangelistEn)

        // Verify key Bahre Hasab properties exist
        assertTrue("Wenber is non-negative", res2016.wenber >= 0)
        assertTrue("Abekt is valid", res2016.abekt in 0..30)
        assertTrue("Metke is valid", res2016.metke in 0..30)
    }
}
