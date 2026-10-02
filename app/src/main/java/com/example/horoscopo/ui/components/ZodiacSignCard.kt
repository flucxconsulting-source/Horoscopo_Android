package com.example.horoscopo.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.icons.filled.WaterDrop
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.horoscopo.data.ZodiacSign

@Composable
fun ZodiacSignCard(
    sign: ZodiacSign,
    selected: Boolean,
    isFavourite: Boolean,
    onClick: () -> Unit,
    onFavouriteClick: () -> Unit,
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
            .clickable(role = Role.Button, onClick = onClick),
        colors = colors,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    Text(
                        text = sign.symbol,
                        fontSize = 36.sp,
                        lineHeight = 36.sp,
                    )
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
                        ElementLabel(element = sign.element)
                    }
                }
                IconButton(
                    onClick = onFavouriteClick,
                    modifier = Modifier.size(36.dp),
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
    }
}

@Composable
private fun ElementLabel(
    element: String,
    modifier: Modifier = Modifier,
) {
    val color = elementColor(element)
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = elementIcon(element),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(14.dp),
        )
        Text(
            text = element,
            style = MaterialTheme.typography.labelLarge,
            color = color,
            maxLines = 1,
        )
    }
}

private fun elementColor(element: String): Color = when (element) {
    "Fire" -> Color(0xFFE53935)
    "Earth" -> Color(0xFF6D8B3D)
    "Air" -> Color(0xFF42A5F5)
    "Water" -> Color(0xFF26A69A)
    else -> Color.White
}

private fun elementIcon(element: String): ImageVector = when (element) {
    "Fire" -> Icons.Filled.LocalFireDepartment
    "Earth" -> Icons.Filled.Terrain
    "Air" -> Icons.Filled.Air
    "Water" -> Icons.Filled.WaterDrop
    else -> Icons.Filled.Air
}
