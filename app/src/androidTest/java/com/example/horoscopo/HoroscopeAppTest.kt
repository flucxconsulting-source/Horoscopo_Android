package com.example.horoscopo

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.horoscopo.data.HoroscopeDataSource
import com.example.horoscopo.data.HoroscopeRepository
import com.example.horoscopo.data.ZodiacSign
import com.example.horoscopo.ui.screens.HoroscopeApp
import com.example.horoscopo.ui.theme.HoroscopoTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HoroscopeAppTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun homeScreen_showsZodiacSigns() {
        composeRule.setContent {
            HoroscopoTheme {
                HoroscopeApp(repository = FakeHoroscopeDataSource())
            }
        }

        composeRule.onNodeWithText("Choose your zodiac sign").assertIsDisplayed()
        composeRule.onNodeWithText("Aries").assertIsDisplayed()
        composeRule.onNodeWithText("Taurus").assertIsDisplayed()
    }

    @Test
    fun tappingSign_opensDetailScreen() {
        composeRule.setContent {
            HoroscopoTheme {
                HoroscopeApp(repository = FakeHoroscopeDataSource())
            }
        }

        composeRule.onNodeWithText("Aries").performClick()

        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText("Today").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("Remote Aries reading.").assertIsDisplayed()
    }

    private class FakeHoroscopeDataSource : HoroscopeDataSource {
        private val signs = HoroscopeRepository.getSigns()

        override fun getSigns(): List<ZodiacSign> = signs

        override suspend fun getSign(id: String): ZodiacSign? {
            return signs.firstOrNull { it.id == id }?.let { sign ->
                if (sign.id == "aries") {
                    sign.copy(dailyReading = "Remote Aries reading.")
                } else {
                    sign
                }
            }
        }
    }
}
