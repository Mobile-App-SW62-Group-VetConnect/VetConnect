package com.luciano.vetconnect.shared.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.luciano.vetconnect.shared.ui.theme.BrandColors

@Composable
fun RatingStars(
    rating: Int,
    modifier: Modifier = Modifier,
    size: Int = 16
) {
    Row(modifier = modifier) {
        repeat(5) { index ->
            Icon(
                imageVector = when {
                    index + 1 <= rating -> Icons.Default.Star
                    index + 0.5f <= rating -> Icons.Default.StarHalf
                    else -> Icons.Default.StarOutline
                },
                contentDescription = null,
                tint = BrandColors.Secondary,
                modifier = Modifier.size(size.dp)
            )
        }
    }
}