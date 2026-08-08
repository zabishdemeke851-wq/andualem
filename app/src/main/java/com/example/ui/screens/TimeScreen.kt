package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel

@Composable
fun TimeScreen(viewModel: MainViewModel) {
    val ethClock by viewModel.ethiopianClockTime.collectAsState()
    val isAmharic by viewModel.isAmharic.collectAsState()

    val cal = remember(ethClock) { java.util.Calendar.getInstance() }
    val civilFormatted = remember(ethClock) {
        val h = cal.get(java.util.Calendar.HOUR_OF_DAY)
        val m = cal.get(java.util.Calendar.MINUTE)
        val s = cal.get(java.util.Calendar.SECOND)
        String.format("%02d:%02d:%02d Civil", h, m, s)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = if (isAmharic) "የኢትዮጵያ ባህላዊ ሰዓት አቆጣጠር" else "Ethiopian Traditional Timekeeping",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        // Clock Card
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.WbSunny,
                    contentDescription = "Sun",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(56.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = ethClock.formattedAm,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    text = ethClock.periodEn,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                    modifier = Modifier.padding(top = 4.dp)
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
                )

                Text(
                    text = "International Civil Time: $civilFormatted",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // Explanation Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = if (isAmharic) "የኢትዮጵያ ሰዓት አቆጣጠር እንዴት ይሠራል?" else "How Ethiopian Time Works",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (isAmharic) 
                        "በኢትዮጵያ ባህል ሰዓት የሚጀመረው ከፀሐይ መውጣት (6:00 AM) ጀምሮ ሲሆን፡ 6:00 AM ሲሆን የኢትዮጵያ ሰዓት 12:00 ቀን ይባላል። 7:00 AM ደግሞ 1:00 ቀን ይባላል። ምሽት 6:00 PM ሲሆን ደግሞ 12:00 ሌሊት ይባላል።"
                    else 
                        "In traditional Ethiopian timekeeping, the 12-hour daytime cycle begins at sunrise (approx. 6:00 AM Civil time = 12:00 Day). 7:00 AM Civil corresponds to 1:00 Day. The 12-hour nighttime cycle begins at sunset (approx. 6:00 PM Civil = 12:00 Night).",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 20.sp
                )
            }
        }
    }
}
