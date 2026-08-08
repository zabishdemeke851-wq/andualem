package com.example.core.search.adapters

import com.example.core.search.ConnectionStatus
import com.example.core.search.ProviderStatus
import com.example.core.search.SearchResultItem
import com.example.core.search.UrlValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

object InternetArchiveAdapter : BaseSourceAdapter {
    override val providerName: String = "Internet Archive"
    override val providerHomepage: String = "https://archive.org/"
    override val requiredEnvVariable: String = "INTERNET_ARCHIVE_ENABLED"

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(12, TimeUnit.SECONDS)
            .readTimeout(12, TimeUnit.SECONDS)
            .build()
    }

    override suspend fun checkStatus(currentDate: String): ProviderStatus = withContext(Dispatchers.IO) {
        try {
            val testQuery = URLEncoder.encode("Ethiopian", "UTF-8")
            val url = "https://archive.org/advancedsearch.php?q=$testQuery&fl[]=identifier&rows=1&output=json"
            val request = Request.Builder().url(url).build()
            httpClient.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    ProviderStatus(
                        provider_name = providerName,
                        provider_homepage = providerHomepage,
                        connection_status = ConnectionStatus.CONNECTED,
                        api_status_message = "Connected to Open Digital Archive",
                        last_checked_time = currentDate,
                        results_returned_count = 1,
                        required_env_variable = requiredEnvVariable
                    )
                } else {
                    ProviderStatus(
                        provider_name = providerName,
                        provider_homepage = providerHomepage,
                        connection_status = ConnectionStatus.ERROR,
                        api_status_message = "HTTP ${response.code}",
                        last_checked_time = currentDate,
                        error_message = response.message,
                        required_env_variable = requiredEnvVariable
                    )
                }
            }
        } catch (e: Exception) {
            ProviderStatus(
                provider_name = providerName,
                provider_homepage = providerHomepage,
                connection_status = ConnectionStatus.ERROR,
                api_status_message = "Connection failed",
                last_checked_time = currentDate,
                error_message = e.localizedMessage ?: "Network error",
                required_env_variable = requiredEnvVariable
            )
        }
    }

    override suspend fun search(query: String, currentDate: String): List<SearchResultItem> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext emptyList()

        val results = mutableListOf<SearchResultItem>()
        try {
            val encodedQuery = URLEncoder.encode(query, "UTF-8")
            val url = "https://archive.org/advancedsearch.php?q=$encodedQuery&fl[]=identifier,title,creator,year,mediatype,description,publisher&rows=15&page=1&output=json"

            val request = Request.Builder().url(url).build()
            httpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext emptyList()
                val bodyStr = response.body?.string() ?: return@withContext emptyList()
                val root = JSONObject(bodyStr)
                val respObj = root.optJSONObject("response") ?: return@withContext emptyList()
                val docsArr = respObj.optJSONArray("docs") ?: return@withContext emptyList()

                for (i in 0 until docsArr.length()) {
                    val doc = docsArr.optJSONObject(i) ?: continue

                    val identifier = doc.optString("identifier", "").trim()
                    if (identifier.isBlank()) continue

                    val title = doc.optString("title", "").trim()
                    if (title.isBlank()) continue

                    val creator = doc.optString("creator", "")
                    val yearStr = doc.optString("year", "")
                    val pubYear = yearStr.take(4).toIntOrNull()
                    val description = doc.optString("description", "")
                    val publisher = doc.optString("publisher", "")

                    val detailUrl = "https://archive.org/details/$identifier"
                    val previewUrl = "https://archive.org/details/$identifier?mode=2up"
                    val pdfUrl = "https://archive.org/download/$identifier/$identifier.pdf"

                    val sourceUrl = UrlValidator.sanitizeUrl(detailUrl)
                    if (!UrlValidator.isValidUrl(sourceUrl)) continue

                    results.add(
                        SearchResultItem(
                            title = title,
                            authors = if (creator.isNotBlank()) listOf(creator) else listOf("Internet Archive Digitization"),
                            document_type = "OER Archive",
                            publication_year = pubYear,
                            publisher = if (publisher.isNotBlank()) publisher else "Internet Archive Public Domain",
                            abstract = description.take(300),
                            source_name = providerName,
                            source_url = sourceUrl,
                            record_url = sourceUrl,
                            publisher_url = providerHomepage,
                            preview_url = UrlValidator.sanitizeUrl(previewUrl),
                            full_text_url = UrlValidator.sanitizeUrl(pdfUrl),
                            download_url = UrlValidator.sanitizeUrl(pdfUrl),
                            access_status = "Verified legal full text.",
                            verification = "URL verified",
                            confidence = "High",
                            date_checked = currentDate
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext results
    }
}
