package com.example.horoscopo

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
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

        composeRule.onNodeWithText("Aries").assertIsDisplayed()
        composeRule.onNodeWithText("Taurus").assertIsDisplayed()
        composeRule.onNodeWithText("Pisces").assertIsDisplayed()
        composeRule
            .onAllNodesWithContentDescription("Show Week horoscope for Aries")
            .fetchSemanticsNodes()
            .let { nodes -> assert(nodes.isEmpty()) }
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

    @Test
    fun favouriteButton_togglesSelectedSign() {
        composeRule.setContent {
            HoroscopoTheme {
                HoroscopeApp(repository = FakeHoroscopeDataSource())
            }
        }

        composeRule.onNodeWithContentDescription("Mark Aries as favourite").performClick()

        composeRule.onNodeWithContentDescription("Remove Aries from favourites").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Remove Aries from favourites").performClick()

        composeRule.onNodeWithContentDescription("Mark Aries as favourite").assertIsDisplayed()
    }

    @Test
    fun favouriteButton_allowsOnlyOneFavouriteAtATime() {
        composeRule.setContent {
            HoroscopoTheme {
                HoroscopeApp(repository = FakeHoroscopeDataSource())
            }
        }

        composeRule.onNodeWithContentDescription("Mark Aries as favourite").performClick()
        composeRule.onNodeWithContentDescription("Mark Taurus as favourite").performClick()

        composeRule.onNodeWithContentDescription("Remove Taurus from favourites").assertIsDisplayed()
        composeRule
            .onAllNodesWithContentDescription("Remove Aries from favourites")
            .fetchSemanticsNodes()
            .let { nodes -> assert(nodes.isEmpty()) }
        composeRule.onNodeWithContentDescription("Mark Aries as favourite").assertIsDisplayed()
    }

    @Test
    fun detailFavouriteButton_togglesAndSyncsWithHomeCard() {
        composeRule.setContent {
            HoroscopoTheme {
                HoroscopeApp(repository = FakeHoroscopeDataSource())
            }
        }

        composeRule.onNodeWithText("Aries").performClick()
        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText("Today").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithContentDescription("Mark Aries as favourite").performClick()
        composeRule.onNodeWithContentDescription("Remove Aries from favourites").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Go back").performClick()

        composeRule.onNodeWithContentDescription("Remove Aries from favourites").assertIsDisplayed()
    }

    @Test
    fun detailCard_periodToggleChangesVisibleReading() {
        composeRule.setContent {
            HoroscopoTheme {
                HoroscopeApp(repository = FakeHoroscopeDataSource())
            }
        }

        composeRule.onNodeWithText("Aries").performClick()
        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText("Today").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithContentDescription("Show Week horoscope for Aries").performClick()

        composeRule
            .onNodeWithText("This week favors your Fire nature", substring = true)
            .assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Show Month horoscope for Aries").performClick()

        composeRule
            .onNodeWithText("This month, work with Mars", substring = true)
            .assertIsDisplayed()
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
