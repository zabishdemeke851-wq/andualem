package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DocumentEntity
import com.example.ui.MainViewModel
import com.example.ui.components.SearchResultCard

@Composable
fun LibraryScreen(viewModel: MainViewModel) {
    val isAmharic by viewModel.isAmharic.collectAsState()
    val searchQuery by viewModel.librarySearchQuery.collectAsState()
    val realSearchResults by viewModel.realSearchResults.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val documents by viewModel.documentsList.collectAsState()

    var activeTab by remember { mutableStateOf(0) } // 0 = Live External Adapters, 1 = Source Status, 2 = Local Manuscripts

    val ethiopianTopicSuggestions = remember {
        listOf(
            "Ethiopian Calendar",
            "Bahre Hasab",
            "Abushakir",
            "Ethiopian Astronomy",
            "Ge'ez Grammar",
            "Ethiopian Manuscripts",
            "ባሕረ ሐሳብ",
            "አቡሻኪር",
            "የኢትዮጵያ ዘመን አቆጣጠር"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Title & Mode Selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (isAmharic) "ዲጂታል ቤተ-መጽሐፍትና የምርምር ምንጮች" else "Digital Library & Research Sources",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (isAmharic) "የተረጋገጡ የውጭና የአካዳሚክ ምንጮች" else "Verified Real Source Providers",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Tab Selector
        TabRow(
            selectedTabIndex = activeTab,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.clip(RoundedCornerShape(12.dp))
        ) {
            Tab(
                selected = activeTab == 0,
                onClick = { activeTab = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isAmharic) "የቀጥታ ምንጭ ፍለጋ" else "Live Sources", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            )
            Tab(
                selected = activeTab == 1,
                onClick = { activeTab = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Sensors, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isAmharic) "የምንጮች ሁኔታ" else "Integrations", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            )
            Tab(
                selected = activeTab == 2,
                onClick = { activeTab = 2 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LibraryBooks, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isAmharic) "የተዘጋጁ መጽሐፍት" else "Local Index", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        when (activeTab) {
            0 -> LiveSearchTab(
                viewModel = viewModel,
                searchQuery = searchQuery,
                isAmharic = isAmharic,
                isSearching = isSearching,
                realSearchResults = realSearchResults,
                ethiopianTopicSuggestions = ethiopianTopicSuggestions
            )
            1 -> SourceIntegrationsScreen(viewModel = viewModel)
            2 -> LocalCatalogTab(
                viewModel = viewModel,
                documents = documents,
                isAmharic = isAmharic
            )
        }
    }
}

@Composable
fun LiveSearchTab(
    viewModel: MainViewModel,
    searchQuery: String,
    isAmharic: Boolean,
    isSearching: Boolean,
    realSearchResults: com.example.core.search.SearchResponse?,
    ethiopianTopicSuggestions: List<String>
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Search Input Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { query ->
                viewModel.librarySearchQuery.value = query
                if (query.trim().length >= 3) {
                    viewModel.performRealSearch(query)
                }
            },
            placeholder = { Text(if (isAmharic) "የእውነተኛ ምንጮች ፍለጋ (ምሳሌ፡ Bahre Hasab, Abushakir)..." else "Search real source providers (e.g. Bahre Hasab)...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.librarySearchQuery.value = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                } else {
                    IconButton(onClick = { viewModel.performRealSearch("Ethiopian Calendar") }) {
                        Icon(Icons.Default.CloudSync, contentDescription = "Run Test")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        )

        // Topic Quick Suggestion Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(ethiopianTopicSuggestions) { topic ->
                SuggestionChip(
                    onClick = {
                        viewModel.librarySearchQuery.value = topic
                        viewModel.performRealSearch(topic)
                    },
                    label = { Text(topic, fontSize = 11.sp) },
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }

        // Search Progress Bar
        if (isSearching) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Text(
                text = if (isAmharic) "ከ11 የምንጭ አዳፕተሮች መረጃ እየተፈለገ ነው..." else "Searching 11 source adapters (Google Books, OpenAlex, Crossref, Internet Archive, DOAJ)...",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        // Search Summary Banner
        realSearchResults?.let { resp ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "QUERY: \"${resp.query}\" • ${resp.verified_results} Verified Results Found",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Searched Adapters: ${resp.providers_searched.joinToString(", ")}",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                    if (resp.providers_unavailable.isNotEmpty()) {
                        Text(
                            text = "Manual Search Portals: ${resp.providers_unavailable.joinToString(", ")}",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }

        // Results List
        val allResults = remember(realSearchResults) {
            if (realSearchResults == null) emptyList()
            else realSearchResults.books + realSearchResults.research + realSearchResults.theses + realSearchResults.other_sources
        }

        if (allResults.isEmpty() && !isSearching) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (searchQuery.isBlank())
                            (if (isAmharic) "የሚፈልጉትን ርዕስ ከላይ ይፃፉ ወይም የፍለጋ ጥቆማዎችን ይጫኑ" else "Type a search query or tap a topic suggestion above")
                        else
                            (if (isAmharic) "ምንም የተረጋገጠ ውጤት አልተገኘም" else "No verified records found for this query"),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            viewModel.librarySearchQuery.value = "Ethiopian Calendar"
                            viewModel.performRealSearch("Ethiopian Calendar")
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (isAmharic) "ምሳሌ 'Ethiopian Calendar' ፈልግ" else "Search 'Ethiopian Calendar'")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(allResults) { item ->
                    SearchResultCard(item = item, isAmharic = isAmharic)
                }
            }
        }
    }
}

@Composable
fun LocalCatalogTab(
    viewModel: MainViewModel,
    documents: List<DocumentEntity>,
    isAmharic: Boolean
) {
    if (documents.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isAmharic) "ምንም የታሸገ መጽሐፍ አልተገኘም" else "No local index items found",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(documents) { doc ->
                DocumentListItem(doc = doc, isAmharic = isAmharic) {
                    viewModel.selectDocumentForReader(doc)
                }
            }
        }
    }
}

@Composable
fun DocumentListItem(
    doc: DocumentEntity,
    isAmharic: Boolean,
    onClick: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp, 76.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(android.graphics.Color.parseColor(doc.coverColorHex))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoStories,
                        contentDescription = "Book Cover",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isAmharic && doc.titleAmharic.isNotBlank()) doc.titleAmharic else doc.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "${doc.author} • ${doc.year}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    if (doc.description.isNotBlank()) {
                        Text(
                            text = doc.description,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.padding(top = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        AssistChip(
                            onClick = {},
                            label = { Text(doc.category, fontSize = 10.sp) },
                            modifier = Modifier.height(24.dp)
                        )
                        AssistChip(
                            onClick = {},
                            label = { Text("${doc.pageCount} pgs", fontSize = 10.sp) },
                            modifier = Modifier.height(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons Bar: Read In-App, Open Web Source, Download PDF
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Open / Read In-App
                Button(
                    onClick = { onClick() },
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.AutoStories, contentDescription = "Read", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isAmharic) "አንብብ" else "Read", fontSize = 11.sp)
                }

                // Open Web Source URL
                if (doc.sourceUrl.isNotBlank()) {
                    OutlinedButton(
                        onClick = {
                            try {
                                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(doc.sourceUrl))
                                intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                android.widget.Toast.makeText(context, "Could not open URL", android.widget.Toast.LENGTH_SHORT).show()
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(Icons.Default.OpenInNew, contentDescription = "Source", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isAmharic) "ምንጭ" else "Source", fontSize = 11.sp)
                    }
                }

                // Download Document / PDF
                if (doc.downloadUrl.isNotBlank()) {
                    OutlinedButton(
                        onClick = {
                            try {
                                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(doc.downloadUrl))
                                intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                android.widget.Toast.makeText(context, "Could not download document", android.widget.Toast.LENGTH_SHORT).show()
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = "Download", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isAmharic) "አውርድ" else "Download", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
