package com.example.horoscopo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.horoscopo.data.HoroscopeRepository
import com.example.horoscopo.data.ZodiacSign
import com.example.horoscopo.ui.components.HoroscopeDetailCard
import com.example.horoscopo.ui.components.ZodiacSignCard
import com.example.horoscopo.ui.theme.HoroscopoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HoroscopeApp(
    repository: HoroscopeRepository,
    modifier: Modifier = Modifier,
) {
    val signs = remember { repository.getSigns() }
    var selectedSignId by rememberSaveable { mutableStateOf(signs.first().id) }
    val selectedSign = remember(signs, selectedSignId) {
        repository.getSign(selectedSignId) ?: signs.first()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Horoscope",
                        fontWeight = FontWeight.SemiBold,
                    )
                },
            )
        },
    ) { innerPadding ->
        HoroscopeContent(
            signs = signs,
            selectedSign = selectedSign,
            onSignSelected = { selectedSignId = it.id },
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
private fun HoroscopeContent(
    signs: List<ZodiacSign>,
    selectedSign: ZodiacSign,
    onSignSelected: (ZodiacSign) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.35f),
                    ),
                ),
            ),
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 156.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Header()
            }
            items(
                items = signs,
                key = { it.id },
            ) { sign ->
                ZodiacSignCard(
                    sign = sign,
                    selected = sign.id == selectedSign.id,
                    onClick = { onSignSelected(sign) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                HoroscopeDetailCard(
                    sign = selectedSign,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }
}

@Composable
private fun Header(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "Choose your zodiac sign",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "Tap a sign to see its traits, lucky details, and today's reading.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.72f),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HoroscopeAppPreview() {
    HoroscopoTheme {
        HoroscopeApp(repository = HoroscopeRepository)
    }
}
