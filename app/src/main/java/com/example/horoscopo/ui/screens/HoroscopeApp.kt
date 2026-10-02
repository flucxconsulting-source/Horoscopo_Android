package com.example.horoscopo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
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
import com.example.horoscopo.R
import com.example.horoscopo.data.HoroscopeDataSource
import com.example.horoscopo.data.HoroscopePeriod
import com.example.horoscopo.data.HoroscopeRepository
import com.example.horoscopo.data.ZodiacSign
import com.example.horoscopo.navigation.HoroscopeDestinations
import com.example.horoscopo.ui.components.HoroscopeDetailCard
import com.example.horoscopo.ui.components.ZodiacSignCard
import com.example.horoscopo.ui.state.HoroscopeDetailUiState
import com.example.horoscopo.ui.text.localizedColorName
import com.example.horoscopo.ui.text.localizedElementName
import com.example.horoscopo.ui.text.localizedPlanetName
import com.example.horoscopo.ui.theme.HoroscopoTheme
import com.example.horoscopo.ui.viewmodel.HoroscopeDetailViewModel

@Composable
fun HoroscopeApp(
    repository: HoroscopeDataSource,
    modifier: Modifier = Modifier,
) {
    val signs = remember { repository.getSigns() }
    val navController = rememberNavController()
    var favouriteSignId by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedPeriods by remember { mutableStateOf<Map<String, HoroscopePeriod>>(emptyMap()) }

    NavHost(
        navController = navController,
        startDestination = HoroscopeDestinations.Home,
        modifier = modifier.fillMaxSize(),
    ) {
        composable(route = HoroscopeDestinations.Home) {
            HoroscopeHomeScreen(
                signs = signs,
                favouriteSignId = favouriteSignId,
                onSignSelected = { sign ->
                    navController.navigate(HoroscopeDestinations.detailRoute(sign.id))
                },
                onFavouriteSelected = { sign ->
                    favouriteSignId = if (favouriteSignId == sign.id) null else sign.id
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
                favouriteSignId = favouriteSignId,
                selectedPeriod = signId?.let { selectedPeriods[it] } ?: HoroscopePeriod.Day,
                onNavigateBack = { navController.popBackStack() },
                onRetry = detailViewModel::retry,
                onFavouriteSelected = { sign ->
                    favouriteSignId = if (favouriteSignId == sign.id) null else sign.id
                },
                onPeriodSelected = { sign, period ->
                    selectedPeriods = selectedPeriods + (sign.id to period)
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HoroscopeHomeScreen(
    signs: List<ZodiacSign>,
    favouriteSignId: String?,
    onSignSelected: (ZodiacSign) -> Unit,
    onFavouriteSelected: (ZodiacSign) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showFilters by rememberSaveable { mutableStateOf(false) }
    var favouriteOnly by rememberSaveable { mutableStateOf(false) }
    var birthDate by rememberSaveable { mutableStateOf("") }
    var selectedPlanet by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedElement by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedColor by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedSignId by rememberSaveable { mutableStateOf<String?>(null) }

    val filteredSigns = remember(
        signs,
        favouriteSignId,
        favouriteOnly,
        birthDate,
        selectedPlanet,
        selectedElement,
        selectedColor,
        selectedSignId,
    ) {
        signs.filter { sign ->
            val matchesFavourite = !favouriteOnly || sign.id == favouriteSignId
            val matchesBirthDate = birthDate.toBirthDateOrNull()?.let { date ->
                sign.containsBirthDate(date)
            } ?: true
            val matchesPlanet = selectedPlanet == null || sign.rulingPlanet == selectedPlanet
            val matchesElement = selectedElement == null || sign.element == selectedElement
            val matchesColor = selectedColor == null || sign.luckyColor == selectedColor
            val matchesSign = selectedSignId == null || sign.id == selectedSignId

            matchesFavourite &&
                matchesBirthDate &&
                matchesPlanet &&
                matchesElement &&
                matchesColor &&
                matchesSign
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.screen_title_horoscope),
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                actions = {
                    IconButton(onClick = { showFilters = true }) {
                        Icon(
                            imageVector = Icons.Filled.FilterList,
                            contentDescription = stringResource(R.string.action_open_filters),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        HoroscopeHomeContent(
            signs = filteredSigns,
            favouriteSignId = favouriteSignId,
            onSignSelected = onSignSelected,
            onFavouriteSelected = onFavouriteSelected,
            modifier = Modifier.padding(innerPadding),
        )

        if (showFilters) {
            HoroscopeFilterDialog(
                signs = signs,
                favouriteOnly = favouriteOnly,
                birthDate = birthDate,
                selectedPlanet = selectedPlanet,
                selectedElement = selectedElement,
                selectedColor = selectedColor,
                selectedSignId = selectedSignId,
                onFavouriteOnlyChange = { favouriteOnly = it },
                onBirthDateChange = { birthDate = it },
                onPlanetSelected = { selectedPlanet = it },
                onElementSelected = { selectedElement = it },
                onColorSelected = { selectedColor = it },
                onSignSelected = { selectedSignId = it },
                onClear = {
                    favouriteOnly = false
                    birthDate = ""
                    selectedPlanet = null
                    selectedElement = null
                    selectedColor = null
                    selectedSignId = null
                },
                onDismiss = { showFilters = false },
            )
        }
    }
}

@Composable
private fun HoroscopeHomeContent(
    signs: List<ZodiacSign>,
    favouriteSignId: String?,
    onSignSelected: (ZodiacSign) -> Unit,
    onFavouriteSelected: (ZodiacSign) -> Unit,
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
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val gridPadding = 8.dp
            val gridSpacing = 8.dp
            val rows = 6
            val cardHeight = (maxHeight - (gridPadding * 2) - (gridSpacing * (rows - 1))) / rows

            if (signs.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(R.string.empty_filtered_signs),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(gridPadding),
                    horizontalArrangement = Arrangement.spacedBy(gridSpacing),
                    verticalArrangement = Arrangement.spacedBy(gridSpacing),
                    userScrollEnabled = false,
                ) {
                    items(
                        items = signs,
                        key = { it.id },
                    ) { sign ->
                        ZodiacSignCard(
                            sign = sign,
                            selected = false,
                            isFavourite = sign.id == favouriteSignId,
                            onClick = { onSignSelected(sign) },
                            onFavouriteClick = { onFavouriteSelected(sign) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(cardHeight),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HoroscopeFilterDialog(
    signs: List<ZodiacSign>,
    favouriteOnly: Boolean,
    birthDate: String,
    selectedPlanet: String?,
    selectedElement: String?,
    selectedColor: String?,
    selectedSignId: String?,
    onFavouriteOnlyChange: (Boolean) -> Unit,
    onBirthDateChange: (String) -> Unit,
    onPlanetSelected: (String?) -> Unit,
    onElementSelected: (String?) -> Unit,
    onColorSelected: (String?) -> Unit,
    onSignSelected: (String?) -> Unit,
    onClear: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.filters_title))
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    FilterChip(
                        selected = favouriteOnly,
                        onClick = { onFavouriteOnlyChange(!favouriteOnly) },
                        label = {
                            Text(
                                text = stringResource(R.string.filter_favourite),
                                style = MaterialTheme.typography.labelSmall,
                            )
                        },
                    )

                    OutlinedTextField(
                        value = birthDate,
                        onValueChange = onBirthDateChange,
                        modifier = Modifier.weight(1f),
                        label = {
                            Text(
                                text = stringResource(R.string.filter_dob),
                                style = MaterialTheme.typography.labelSmall,
                            )
                        },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.filter_dob_placeholder),
                                style = MaterialTheme.typography.labelSmall,
                            )
                        },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodySmall,
                    )
                }

                FilterSelect(
                    title = stringResource(R.string.filter_element),
                    options = signs.map { it.element }.distinct(),
                    selectedOption = selectedElement,
                    onOptionSelected = onElementSelected,
                    optionLabel = { localizedElementName(it) },
                )
                FilterSelect(
                    title = stringResource(R.string.filter_sign),
                    options = signs.map { it.name },
                    selectedOption = signs.firstOrNull { it.id == selectedSignId }?.name,
                    onOptionSelected = { selectedName ->
                        onSignSelected(signs.firstOrNull { it.name == selectedName }?.id)
                    },
                )
                FilterSelect(
                    title = stringResource(R.string.filter_planet),
                    options = signs.map { it.rulingPlanet }.distinct(),
                    selectedOption = selectedPlanet,
                    onOptionSelected = onPlanetSelected,
                    optionLabel = { localizedPlanetName(it) },
                )
                FilterSelect(
                    title = stringResource(R.string.filter_color),
                    options = signs.map { it.luckyColor }.distinct(),
                    selectedOption = selectedColor,
                    onOptionSelected = onColorSelected,
                    optionLabel = { localizedColorName(it) },
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.action_done))
            }
        },
        dismissButton = {
            TextButton(onClick = onClear) {
                Text(text = stringResource(R.string.action_clear))
            }
        },
    )
}

private data class BirthDate(val month: Int, val day: Int)

private fun String.toBirthDateOrNull(): BirthDate? {
    if (isBlank()) return null

    val parts = trim().split("-", "/")
    if (parts.size != 2) return null

    val month = parts[0].toIntOrNull() ?: return null
    val day = parts[1].toIntOrNull() ?: return null
    val daysInMonth = listOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    if (month !in 1..12) return null
    if (day !in 1..daysInMonth[month - 1]) return null

    return BirthDate(month = month, day = day)
}

private fun ZodiacSign.containsBirthDate(date: BirthDate): Boolean {
    val (startText, endText) = dateRange.split(" - ")
    val start = startText.toBirthDateFromRange()
    val end = endText.toBirthDateFromRange()
    val currentDay = date.toDayOfYear()
    val startDay = start.toDayOfYear()
    val endDay = end.toDayOfYear()

    return if (startDay <= endDay) {
        currentDay in startDay..endDay
    } else {
        currentDay >= startDay || currentDay <= endDay
    }
}

private fun String.toBirthDateFromRange(): BirthDate {
    val parts = split(" ")
    return BirthDate(
        month = monthNumber(parts[0]),
        day = parts[1].toInt(),
    )
}

private fun BirthDate.toDayOfYear(): Int {
    val daysBeforeMonth = listOf(0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335)
    return daysBeforeMonth[month - 1] + day
}

private fun monthNumber(month: String): Int = when (month) {
    "Jan" -> 1
    "Feb" -> 2
    "Mar" -> 3
    "Apr" -> 4
    "May" -> 5
    "Jun" -> 6
    "Jul" -> 7
    "Aug" -> 8
    "Sep" -> 9
    "Oct" -> 10
    "Nov" -> 11
    "Dec" -> 12
    else -> error("Unsupported month abbreviation: $month")
}

@Composable
private fun FilterSelect(
    title: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String?) -> Unit,
    modifier: Modifier = Modifier,
    optionLabel: @Composable (String) -> String = { it },
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedOptionLabel = selectedOption?.let { optionLabel(it) } ?: stringResource(R.string.filter_all)

    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        TextButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.filter_select_format, title, selectedOptionLabel),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.filter_all),
                        style = MaterialTheme.typography.bodySmall,
                    )
                },
                onClick = {
                    onOptionSelected(null)
                    expanded = false
                },
            )
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = optionLabel(option),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HoroscopeDetailScreen(
    uiState: HoroscopeDetailUiState,
    favouriteSignId: String?,
    selectedPeriod: HoroscopePeriod,
    onNavigateBack: () -> Unit,
    onRetry: () -> Unit,
    onFavouriteSelected: (ZodiacSign) -> Unit,
    onPeriodSelected: (ZodiacSign, HoroscopePeriod) -> Unit,
    modifier: Modifier = Modifier,
) {
    val screenTitle = when (uiState) {
        is HoroscopeDetailUiState.Content -> uiState.sign.name
        is HoroscopeDetailUiState.Error -> stringResource(R.string.detail_sign_not_found)
        HoroscopeDetailUiState.Loading -> stringResource(R.string.detail_loading)
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
                            contentDescription = stringResource(R.string.action_go_back),
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
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(
                            text = uiState.message,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Button(onClick = onRetry) {
                            Text(text = stringResource(R.string.action_retry))
                        }
                    }
                }

                is HoroscopeDetailUiState.Content -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        HoroscopeDetailCard(
                            sign = uiState.sign,
                            isFavourite = uiState.sign.id == favouriteSignId,
                            selectedPeriod = selectedPeriod,
                            onFavouriteClick = { onFavouriteSelected(uiState.sign) },
                            onPeriodSelected = { period -> onPeriodSelected(uiState.sign, period) },
                            modifier = Modifier.weight(1f),
                        )
                        Text(
                            text = stringResource(R.string.detail_state_lesson, uiState.sign.id),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.72f),
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HoroscopeAppPreview() {
    HoroscopoTheme {
        HoroscopeApp(repository = HoroscopeRepository)
    }
}
