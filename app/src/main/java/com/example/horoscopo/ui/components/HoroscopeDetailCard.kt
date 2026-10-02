package com.example.horoscopo.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horoscopo.R
import com.example.horoscopo.data.HoroscopePeriod
import com.example.horoscopo.data.ZodiacSign
import com.example.horoscopo.ui.text.localizedColorName
import com.example.horoscopo.ui.text.localizedElementName
import com.example.horoscopo.ui.text.localizedPeriodLabel
import com.example.horoscopo.ui.text.localizedPeriodTitle
import com.example.horoscopo.ui.text.localizedPlanetName
import com.example.horoscopo.ui.text.localizedReadingFor

@Composable
fun HoroscopeDetailCard(
    sign: ZodiacSign,
    isFavourite: Boolean,
    selectedPeriod: HoroscopePeriod,
    onFavouriteClick: () -> Unit,
    onPeriodSelected: (HoroscopePeriod) -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                    text = sign.symbol,
                    fontSize = 42.sp,
                    lineHeight = 42.sp,
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp, end = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = sign.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = sign.dateRange,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                IconButton(
                    onClick = onFavouriteClick,
                    modifier = Modifier.size(44.dp),
                ) {
                    Icon(
                        imageVector = if (isFavourite) {
                            Icons.Filled.Favorite
                        } else {
                            Icons.Outlined.FavoriteBorder
                        },
                        contentDescription = if (isFavourite) {
                            stringResource(R.string.favourite_remove, sign.name)
                        } else {
                            stringResource(R.string.favourite_mark, sign.name)
                        },
                        tint = if (isFavourite) {
                            Color(0xFFD32F2F)
                        } else {
                            MaterialTheme.colorScheme.onSecondaryContainer
                        },
                    )
                }
            }

            Text(
                text = sign.summary,
                style = MaterialTheme.typography.bodyLarge,
            )

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                PeriodToggle(
                    selectedPeriod = selectedPeriod,
                    onPeriodSelected = onPeriodSelected,
                    signName = sign.name,
                )
                Text(
                    text = localizedPeriodTitle(selectedPeriod),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = localizedReadingFor(sign, selectedPeriod),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                DetailInfoBox(
                    label = stringResource(R.string.filter_element),
                    value = localizedElementName(sign.element),
                    modifier = Modifier.weight(1f),
                )
                DetailInfoBox(
                    label = stringResource(R.string.filter_planet),
                    value = localizedPlanetName(sign.rulingPlanet),
                    modifier = Modifier.weight(1f),
                )
                DetailInfoBox(
                    label = stringResource(R.string.filter_color),
                    value = localizedColorName(sign.luckyColor),
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun DetailInfoBox(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(horizontal = 8.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.72f),
            textAlign = TextAlign.Center,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun PeriodToggle(
    selectedPeriod: HoroscopePeriod,
    onPeriodSelected: (HoroscopePeriod) -> Unit,
    signName: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        HoroscopePeriod.entries.forEach { period ->
            val selected = period == selectedPeriod
            val periodLabel = localizedPeriodLabel(period)
            val periodDescription = stringResource(R.string.period_show, periodLabel, signName)
            val periodClickLabel = stringResource(
                R.string.period_show,
                periodLabel.lowercase(),
                signName,
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        color = if (selected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.outlineVariant
                        },
                        shape = RoundedCornerShape(8.dp),
                    )
                    .clickable(
                        role = Role.Button,
                        onClickLabel = periodClickLabel,
                    ) {
                        onPeriodSelected(period)
                    }
                    .semantics {
                        contentDescription = periodDescription
                    }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = periodLabel,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                    color = if (selected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                )
            }
        }
    }
}
