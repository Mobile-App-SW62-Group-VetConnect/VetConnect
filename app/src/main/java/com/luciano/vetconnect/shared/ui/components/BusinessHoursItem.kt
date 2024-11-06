package com.luciano.vetconnect.shared.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.luciano.vetconnect.shared.ui.theme.*

data class BusinessHours(
    val dayRange: String,
    var startTime: String,
    var endTime: String
)

@Composable
fun BusinessHoursItem(
    hours: BusinessHours,
    isEditable: Boolean = false,
    onStartTimeClick: () -> Unit = {},
    onEndTimeClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = hours.dayRange,
            color = TextColors.Primary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        if (hours.startTime != "Cerrado") {
            if (isEditable) {
                Button(
                    onClick = onStartTimeClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BackgroundColors.Primary,
                        contentColor = TextColors.Primary
                    )
                ) {
                    Text(hours.startTime)
                }

                Text(" - ", color = TextColors.Primary)

                Button(
                    onClick = onEndTimeClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BackgroundColors.Primary,
                        contentColor = TextColors.Primary
                    )
                ) {
                    Text(hours.endTime)
                }
            } else {
                Text(
                    text = "${hours.startTime} - ${hours.endTime}",
                    color = TextColors.Secondary
                )
            }
        } else {
            Text(
                text = "Cerrado",
                color = TextColors.Secondary
            )
        }
    }
}