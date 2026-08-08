package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MenuOpen
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.search.SearchResultItem
import com.example.core.search.UrlValidator

@Composable
fun SearchResultCard(
    item: SearchResultItem,
    isAmharic: Boolean = false
) {
    val context = LocalContext.current

    val primaryUrl = when {
        UrlValidator.isValidUrl(item.full_text_url) -> item.full_text_url
        UrlValidator.isValidUrl(item.source_url) -> item.source_url
        UrlValidator.isValidUrl(item.record_url) -> item.record_url
        else -> ""
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header: Provider & Verification Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = item.source_name,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (item.verification == "URL verified") {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified",
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isAmharic) "የተረጋገጠ አድራሻ" else "URL Verified",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2E7D32)
                        )
                    } else {
                        Text(
                            text = item.verification,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Title
            Text(
                text = item.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            // Authors & Metadata
            if (item.authors.isNotEmpty()) {
                Text(
                    text = "By ${item.authors.joinToString(", ")}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Type, Year, Publisher
            val metaDetails = buildList {
                add(item.document_type)
                item.publication_year?.let { add(it.toString()) }
                if (item.publisher.isNotBlank()) add(item.publisher)
                if (item.journal.isNotBlank()) add(item.journal)
                if (item.university.isNotBlank()) add(item.university)
            }.joinToString(" • ")

            if (metaDetails.isNotBlank()) {
                Text(
                    text = metaDetails,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            // Abstract
            if (item.abstract.isNotBlank()) {
                Text(
                    text = item.abstract,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )
            }

            // Availability Badge
            val (badgeBg, badgeFg) = when {
                item.access_status.contains("full text", ignoreCase = true) -> Color(0xFFE8F5E9) to Color(0xFF1B5E20)
                item.access_status.contains("preview", ignoreCase = true) -> Color(0xFFFFF8E1) to Color(0xFFF57F17)
                else -> Color(0xFFECEFF1) to Color(0xFF37474F)
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = badgeBg,
                modifier = Modifier.padding(top = 2.dp)
            ) {
                Text(
                    text = item.access_status,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = badgeFg,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // Action Buttons (Only render if valid URL exists)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Open verified source
                val sourceUrlToUse = item.source_url.ifBlank { item.record_url }
                if (UrlValidator.isValidUrl(sourceUrlToUse)) {
                    OutlinedButton(
                        onClick = { openBrowserUrl(context, sourceUrlToUse) },
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Icon(Icons.Default.OpenInNew, contentDescription = "Source", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Source", fontSize = 11.sp)
                    }
                }

                // Open publisher / repository
                val pubUrlToUse = item.publisher_url.ifBlank { item.repository_url }
                if (UrlValidator.isValidUrl(pubUrlToUse) && pubUrlToUse != sourceUrlToUse) {
                    OutlinedButton(
                        onClick = { openBrowserUrl(context, pubUrlToUse) },
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Icon(Icons.Default.Launch, contentDescription = "Publisher", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Publisher", fontSize = 11.sp)
                    }
                }

                // Open Preview
                if (UrlValidator.isValidUrl(item.preview_url)) {
                    OutlinedButton(
                        onClick = { openBrowserUrl(context, item.preview_url) },
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Icon(Icons.Default.Preview, contentDescription = "Preview", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Preview", fontSize = 11.sp)
                    }
                }

                // Open Full Text
                val fullTextToUse = item.full_text_url.ifBlank { item.download_url }
                if (UrlValidator.isValidUrl(fullTextToUse)) {
                    Button(
                        onClick = { openBrowserUrl(context, fullTextToUse) },
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.height(36.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = "Full Text", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Full Text", fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Copy Citation Button
                IconButton(
                    onClick = {
                        val citation = formatCitation(item)
                        copyToClipboard(context, "Citation", citation)
                        Toast.makeText(context, "Citation copied to clipboard!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy Citation", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

private fun openBrowserUrl(context: Context, url: String) {
    if (!UrlValidator.isValidUrl(url)) return
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Could not open URL", Toast.LENGTH_SHORT).show()
    }
}

private fun copyToClipboard(context: Context, label: String, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
}

private fun formatCitation(item: SearchResultItem): String {
    val authors = if (item.authors.isNotEmpty()) item.authors.joinToString(", ") else "Unknown Author"
    val year = item.publication_year?.let { " ($it)" } ?: ""
    val title = item.title
    val publisher = if (item.publisher.isNotBlank()) ". ${item.publisher}" else ""
    val url = if (item.source_url.isNotBlank()) ". Available at: ${item.source_url}" else ""
    return "$authors$year. $title$publisher$url."
}
