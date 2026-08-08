package com.example.core.search.adapters

import com.example.core.search.ConnectionStatus
import com.example.core.search.ProviderStatus
import com.example.core.search.SearchResultItem

object InstitutionalRepositoryAdapter : BaseSourceAdapter {
    override val providerName: String = "Ethiopian Institutional Repositories (Jimma & Hawassa)"
    override val providerHomepage: String = "https://repository.ju.edu.et/"
    override val requiredEnvVariable: String = "INSTITUTIONAL_REPOSITORIES_PORTAL"

    override suspend fun checkStatus(currentDate: String): ProviderStatus {
        return ProviderStatus(
            provider_name = providerName,
            provider_homepage = providerHomepage,
            connection_status = ConnectionStatus.MANUAL_SEARCH_ONLY,
            api_status_message = "Manual source search required (JU & HU portals)",
            last_checked_time = currentDate,
            results_returned_count = 0,
            required_env_variable = requiredEnvVariable,
            search_instructions = "Visit Jimma University Repository (https://repository.ju.edu.et/) and Hawassa University Journals (https://journals.hu.edu.et/hu-journals/) for institutional publication archives."
        )
    }

    override suspend fun search(query: String, currentDate: String): List<SearchResultItem> {
        return emptyList()
    }
}
