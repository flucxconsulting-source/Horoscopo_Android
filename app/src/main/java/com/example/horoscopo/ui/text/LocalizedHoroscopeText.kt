package com.example.horoscopo.ui.text

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.horoscopo.R
import com.example.horoscopo.data.HoroscopePeriod
import com.example.horoscopo.data.ZodiacSign

@Composable
fun localizedElementName(element: String): String = when (element) {
    "Fire" -> stringResource(R.string.element_fire)
    "Earth" -> stringResource(R.string.element_earth)
    "Air" -> stringResource(R.string.element_air)
    "Water" -> stringResource(R.string.element_water)
    else -> element
}

@Composable
fun localizedPlanetName(planet: String): String = when (planet) {
    "Mars" -> stringResource(R.string.planet_mars)
    "Venus" -> stringResource(R.string.planet_venus)
    "Mercury" -> stringResource(R.string.planet_mercury)
    "Moon" -> stringResource(R.string.planet_moon)
    "Sun" -> stringResource(R.string.planet_sun)
    "Pluto" -> stringResource(R.string.planet_pluto)
    "Jupiter" -> stringResource(R.string.planet_jupiter)
    "Saturn" -> stringResource(R.string.planet_saturn)
    "Uranus" -> stringResource(R.string.planet_uranus)
    "Neptune" -> stringResource(R.string.planet_neptune)
    else -> planet
}

@Composable
fun localizedColorName(color: String): String = when (color) {
    "Crimson" -> stringResource(R.string.color_crimson)
    "Forest green" -> stringResource(R.string.color_forest_green)
    "Yellow" -> stringResource(R.string.color_yellow)
    "Silver" -> stringResource(R.string.color_silver)
    "Gold" -> stringResource(R.string.color_gold)
    "Olive" -> stringResource(R.string.color_olive)
    "Rose" -> stringResource(R.string.color_rose)
    "Burgundy" -> stringResource(R.string.color_burgundy)
    "Purple" -> stringResource(R.string.color_purple)
    "Charcoal" -> stringResource(R.string.color_charcoal)
    "Electric blue" -> stringResource(R.string.color_electric_blue)
    "Seafoam" -> stringResource(R.string.color_seafoam)
    else -> color
}

@Composable
fun localizedPeriodLabel(period: HoroscopePeriod): String = when (period) {
    HoroscopePeriod.Day -> stringResource(R.string.period_day_label)
    HoroscopePeriod.Week -> stringResource(R.string.period_week_label)
    HoroscopePeriod.Month -> stringResource(R.string.period_month_label)
}

@Composable
fun localizedPeriodTitle(period: HoroscopePeriod): String = when (period) {
    HoroscopePeriod.Day -> stringResource(R.string.period_day_title)
    HoroscopePeriod.Week -> stringResource(R.string.period_week_title)
    HoroscopePeriod.Month -> stringResource(R.string.period_month_title)
}

@Composable
fun localizedReadingFor(sign: ZodiacSign, period: HoroscopePeriod): String = when (period) {
    HoroscopePeriod.Day -> sign.dailyReading
    HoroscopePeriod.Week -> sign.weeklyReading ?: stringResource(
        R.string.period_week_reading,
        localizedElementName(sign.element).lowercase(),
        sign.summary,
    )
    HoroscopePeriod.Month -> sign.monthlyReading ?: stringResource(
        R.string.period_month_reading,
        localizedPlanetName(sign.rulingPlanet),
        localizedColorName(sign.luckyColor).lowercase(),
    )
}
