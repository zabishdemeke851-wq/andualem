package com.example.core.search.adapters

import com.example.core.search.ProviderStatus
import com.example.core.search.SearchResultItem

interface BaseSourceAdapter {
    val providerName: String
    val providerHomepage: String
    val requiredEnvVariable: String
    
    suspend fun search(query: String, currentDate: String): List<SearchResultItem>
    suspend fun checkStatus(currentDate: String): ProviderStatus
}
