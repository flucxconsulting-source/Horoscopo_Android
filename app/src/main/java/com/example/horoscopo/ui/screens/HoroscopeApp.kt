package com.example.horoscopo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.horoscopo.data.HoroscopeDataSource
import com.example.horoscopo.data.HoroscopeRepository
import com.example.horoscopo.data.ZodiacSign
import com.example.horoscopo.navigation.HoroscopeDestinations
import com.example.horoscopo.ui.components.HoroscopeDetailCard
import com.example.horoscopo.ui.components.ZodiacSignCard
import com.example.horoscopo.ui.state.HoroscopeDetailUiState
import com.example.horoscopo.ui.theme.HoroscopoTheme
import com.example.horoscopo.ui.viewmodel.HoroscopeDetailViewModel

@Composable
fun HoroscopeApp(
    repository: HoroscopeDataSource,
    modifier: Modifier = Modifier,
) {
    val signs = remember { repository.getSigns() }
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HoroscopeDestinations.Home,
        modifier = modifier.fillMaxSize(),
    ) {
        composable(route = HoroscopeDestinations.Home) {
            HoroscopeHomeScreen(
                signs = signs,
                onSignSelected = { sign ->
                    navController.navigate(HoroscopeDestinations.detailRoute(sign.id))
                },
            )
        }
        composable(
            route = HoroscopeDestinations.Detail,
            arguments = listOf(
                navArgument(HoroscopeDestinations.SignIdArg) {
                    type = NavType.StringType
                },
            ),
        ) { backStackEntry ->
            val signId = backStackEntry.arguments?.getString(HoroscopeDestinations.SignIdArg)
            val detailViewModel: HoroscopeDetailViewModel = viewModel(
                key = "detail-$signId",
                factory = HoroscopeDetailViewModel.Factory(
                    signId = signId,
                    repository = repository,
                ),
            )
            val uiState = detailViewModel.uiState.collectAsStateWithLifecycle()
            HoroscopeDetailScreen(
                uiState = uiState.value,
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HoroscopeHomeScreen(
    signs: List<ZodiacSign>,
    onSignSelected: (ZodiacSign) -> Unit,
    modifier: Modifier = Modifier,
) {
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
        HoroscopeHomeContent(
            signs = signs,
            onSignSelected = onSignSelected,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
private fun HoroscopeHomeContent(
    signs: List<ZodiacSign>,
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
                    selected = false,
                    onClick = { onSignSelected(sign) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HoroscopeDetailScreen(
    uiState: HoroscopeDetailUiState,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val screenTitle = when (uiState) {
        is HoroscopeDetailUiState.Content -> uiState.sign.name
        is HoroscopeDetailUiState.Error -> "Sign not found"
        HoroscopeDetailUiState.Loading -> "Loading"
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = screenTitle,
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f),
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                        ),
                    ),
                )
                .safeDrawingPadding(),
        ) {
            when (uiState) {
                HoroscopeDetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(24.dp))
                }

                is HoroscopeDetailUiState.Error -> {
                    Text(
                        text = uiState.message,
                        modifier = Modifier.padding(24.dp),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }

                is HoroscopeDetailUiState.Content -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        HoroscopeDetailCard(sign = uiState.sign)
                        Text(
                            text = "State lesson: this screen is rendering the Content state for ${uiState.sign.id}.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.72f),
                        )
                    }
                }
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
            text = "Tap a sign to open its detail screen with traits, lucky details, and today's reading.",
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
