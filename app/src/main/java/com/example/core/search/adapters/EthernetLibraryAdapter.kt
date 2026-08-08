package com.example.core.search.adapters

import com.example.core.search.ConnectionStatus
import com.example.core.search.ProviderStatus
import com.example.core.search.SearchResultItem

object EthernetLibraryAdapter : BaseSourceAdapter {
    override val providerName: String = "Ethiopian National Academic OER & Digital Library"
    override val providerHomepage: String = "https://ndl.ethernet.edu.et/"
    override val requiredEnvVariable: String = "NDL_ETHERNET_PORTAL"

    override suspend fun checkStatus(currentDate: String): ProviderStatus {
        return ProviderStatus(
            provider_name = providerName,
            provider_homepage = providerHomepage,
            connection_status = ConnectionStatus.MANUAL_SEARCH_ONLY,
            api_status_message = "Manual source search required (NDL portal)",
            last_checked_time = currentDate,
            results_returned_count = 0,
            required_env_variable = requiredEnvVariable,
            search_instructions = "Visit https://ndl.ethernet.edu.et/ to search Ethiopia's national digital library and educational open resources."
        )
    }

    override suspend fun search(query: String, currentDate: String): List<SearchResultItem> {
        return emptyList()
    }
}
