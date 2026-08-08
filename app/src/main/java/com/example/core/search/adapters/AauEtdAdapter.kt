package com.example.core.search.adapters

import com.example.core.search.ConnectionStatus
import com.example.core.search.ProviderStatus
import com.example.core.search.SearchResultItem

object AauEtdAdapter : BaseSourceAdapter {
    override val providerName: String = "Addis Ababa University Electronic Theses and Dissertations"
    override val providerHomepage: String = "https://etd.aau.edu.et/"
    override val requiredEnvVariable: String = "AAU_ETD_DIRECT_PORTAL"

    override suspend fun checkStatus(currentDate: String): ProviderStatus {
        return ProviderStatus(
            provider_name = providerName,
            provider_homepage = providerHomepage,
            connection_status = ConnectionStatus.MANUAL_SEARCH_ONLY,
            api_status_message = "Manual source search required (DSpace portal)",
            last_checked_time = currentDate,
            results_returned_count = 0,
            required_env_variable = requiredEnvVariable,
            search_instructions = "Visit https://etd.aau.edu.et/ directly to search 50,000+ AAU master's theses and doctoral dissertations."
        )
    }

    override suspend fun search(query: String, currentDate: String): List<SearchResultItem> {
        // Do not generate fake results for un-APId portals
        return emptyList()
    }
}
