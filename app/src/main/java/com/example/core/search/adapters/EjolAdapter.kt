package com.example.core.search.adapters

import com.example.core.search.ConnectionStatus
import com.example.core.search.ProviderStatus
import com.example.core.search.SearchResultItem

object EjolAdapter : BaseSourceAdapter {
    override val providerName: String = "Ethiopian Journals Online (EJOL)"
    override val providerHomepage: String = "https://ejol.ethernet.edu.et/"
    override val requiredEnvVariable: String = "EJOL_DIRECT_PORTAL"

    override suspend fun checkStatus(currentDate: String): ProviderStatus {
        return ProviderStatus(
            provider_name = providerName,
            provider_homepage = providerHomepage,
            connection_status = ConnectionStatus.MANUAL_SEARCH_ONLY,
            api_status_message = "Manual source search required (OJS portal)",
            last_checked_time = currentDate,
            results_returned_count = 0,
            required_env_variable = requiredEnvVariable,
            search_instructions = "Visit https://ejol.ethernet.edu.et/ to search peer-reviewed journals published across Ethiopian universities."
        )
    }

    override suspend fun search(query: String, currentDate: String): List<SearchResultItem> {
        return emptyList()
    }
}
