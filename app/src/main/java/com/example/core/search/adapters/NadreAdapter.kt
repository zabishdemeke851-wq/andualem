package com.example.core.search.adapters

import com.example.core.search.ConnectionStatus
import com.example.core.search.ProviderStatus
import com.example.core.search.SearchResultItem

object NadreAdapter : BaseSourceAdapter {
    override val providerName: String = "National Academic Digital Repository of Ethiopia (NADRE)"
    override val providerHomepage: String = "https://nadre.ethernet.edu.et/"
    override val requiredEnvVariable: String = "NADRE_DIRECT_PORTAL"

    override suspend fun checkStatus(currentDate: String): ProviderStatus {
        return ProviderStatus(
            provider_name = providerName,
            provider_homepage = providerHomepage,
            connection_status = ConnectionStatus.MANUAL_SEARCH_ONLY,
            api_status_message = "Manual source search required (Zenodo-Invenio portal)",
            last_checked_time = currentDate,
            results_returned_count = 0,
            required_env_variable = requiredEnvVariable,
            search_instructions = "Visit https://nadre.ethernet.edu.et/ directly to access cross-institutional Ethiopian academic dataset repositories."
        )
    }

    override suspend fun search(query: String, currentDate: String): List<SearchResultItem> {
        return emptyList()
    }
}
