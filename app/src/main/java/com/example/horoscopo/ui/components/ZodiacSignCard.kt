package com.example.horoscopo.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horoscopo.data.HoroscopePeriod
import com.example.horoscopo.data.ZodiacSign
import com.example.horoscopo.data.readingFor

@Composable
fun ZodiacSignCard(
    sign: ZodiacSign,
    selected: Boolean,
    isFavourite: Boolean,
    selectedPeriod: HoroscopePeriod,
    onClick: () -> Unit,
    onFavouriteClick: () -> Unit,
    onPeriodSelected: (HoroscopePeriod) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = if (selected) {
        CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    } else {
        CardDefaults.elevatedCardColors()
    }

    ElevatedCard(
        modifier = modifier
            .heightIn(min = 118.dp)
            .clickable(role = Role.Button, onClick = onClick),
        colors = colors,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = sign.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = sign.dateRange,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = sign.symbol,
                        fontSize = 30.sp,
                        lineHeight = 30.sp,
                    )
                    IconButton(
                        onClick = onFavouriteClick,
                        modifier = Modifier.size(40.dp),
                    ) {
                        Icon(
                            imageVector = if (isFavourite) {
                                Icons.Filled.Favorite
                            } else {
                                Icons.Outlined.FavoriteBorder
                            },
                            contentDescription = if (isFavourite) {
                                "Remove ${sign.name} from favourites"
                            } else {
                                "Mark ${sign.name} as favourite"
                            },
                            tint = if (isFavourite) Color(0xFFD32F2F) else Color.White,
                        )
                    }
                }
            }
            Text(
                text = sign.readingFor(selectedPeriod),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            PeriodToggle(
                selectedPeriod = selectedPeriod,
                onPeriodSelected = onPeriodSelected,
                signName = sign.name,
            )
        }
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
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        HoroscopePeriod.entries.forEach { period ->
            val selected = period == selectedPeriod
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(6.dp))
                    .border(
                        width = 1.dp,
                        color = if (selected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.outlineVariant
                        },
                        shape = RoundedCornerShape(6.dp),
                    )
                    .clickable(
                        role = Role.Button,
                        onClickLabel = "Show ${period.label.lowercase()} horoscope for $signName",
                    ) {
                        onPeriodSelected(period)
                    }
                    .semantics {
                        contentDescription = "Show ${period.label} horoscope for $signName"
                    }
                    .padding(vertical = 4.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = period.label,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                    color = if (selected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    maxLines = 1,
                )
            }
        }
    }
}
