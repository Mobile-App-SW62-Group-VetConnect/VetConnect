package com.luciano.vetconnect.shared.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luciano.vetconnect.R
import com.luciano.vetconnect.shared.ui.theme.*

@Composable
fun ReviewItem(
    userName: String,
    rating: Int,
    timeAgo: String,
    reviewText: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.user_avatar),
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = userName,
                fontWeight = FontWeight.Bold,
                color = TextColors.Primary
            )
            Text(
                text = timeAgo,
                color = TextColors.Secondary,
                fontSize = 12.sp
            )
            Row {
                repeat(5) { index ->
                    Icon(
                        painter = painterResource(
                            id = if (index < rating) R.drawable.ic_star_filled
                            else R.drawable.ic_star_outline
                        ),
                        contentDescription = "Star",
                        tint = BrandColors.Secondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Text(
                text = reviewText,
                fontSize = 14.sp,
                color = TextColors.Primary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}