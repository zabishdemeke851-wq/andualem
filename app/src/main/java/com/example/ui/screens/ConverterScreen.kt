package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.calendar.EthiopianCalendar
import com.example.ui.MainViewModel

@Composable
fun ConverterScreen(viewModel: MainViewModel) {
    val isAmharic by viewModel.isAmharic.collectAsState()
    val context = LocalContext.current

    var gYear by remember { mutableStateOf("2026") }
    var gMonth by remember { mutableStateOf("9") }
    var gDay by remember { mutableStateOf("11") }

    var ethResultText by remember { mutableStateOf("2019 መስከረም 1 (Meskerem 1, 2019 EC)") }

    var eYear by remember { mutableStateOf("2018") }
    var eMonth by remember { mutableStateOf("1") }
    var eDay by remember { mutableStateOf("1") }

    var gregResultText by remember { mutableStateOf("2025-09-11") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (isAmharic) "ቀን መለወጫ (Gregorian ↔ Ethiopian)" else "Calendar Converter",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        // Panel 1: Gregorian -> Ethiopian
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isAmharic) "ከግሬጎሪያን ወደ ኢትዮጵያ ዘመን አቆጣጠር" else "Gregorian → Ethiopian Date",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = gYear,
                        onValueChange = { gYear = it },
                        label = { Text("Year") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = gMonth,
                        onValueChange = { gMonth = it },
                        label = { Text("Month (1-12)") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = gDay,
                        onValueChange = { gDay = it },
                        label = { Text("Day") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val y = gYear.toIntOrNull() ?: 2026
                        val m = gMonth.toIntOrNull() ?: 9
                        val d = gDay.toIntOrNull() ?: 11
                        val eth = EthiopianCalendar.gregorianToEthiopian(y, m, d)
                        ethResultText = "${eth.year} ${eth.monthNameAm} ${eth.day} (${eth.monthNameEn} ${eth.day}, ${eth.year} EC)"
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = if (isAmharic) "ቀይር (Convert)" else "Convert to Ethiopian")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = ethResultText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        IconButton(onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Ethiopian Date", ethResultText))
                            Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy")
                        }
                    }
                }
            }
        }

        // Panel 2: Ethiopian -> Gregorian
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isAmharic) "ከኢትዮጵያ ወደ ግሬጎሪያን ዘመን አቆጣጠር" else "Ethiopian → Gregorian Date",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = eYear,
                        onValueChange = { eYear = it },
                        label = { Text("Year") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = eMonth,
                        onValueChange = { eMonth = it },
                        label = { Text("Month (1-13)") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = eDay,
                        onValueChange = { eDay = it },
                        label = { Text("Day") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val y = eYear.toIntOrNull() ?: 2018
                        val m = eMonth.toIntOrNull() ?: 1
                        val d = eDay.toIntOrNull() ?: 1
                        val greg = EthiopianCalendar.ethiopianToGregorian(y, m, d)
                        gregResultText = "${greg.year}-${String.format("%02d", greg.month)}-${String.format("%02d", greg.day)}"
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text(text = if (isAmharic) "ቀይር (Convert)" else "Convert to Gregorian")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = gregResultText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        IconButton(onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Gregorian Date", gregResultText))
                            Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy")
                        }
                    }
                }
            }
        }
    }
}
