package com.example.core.search

import java.net.URI

object UrlValidator {

    /**
     * Validates if a URL string is a well-formed, reachable-looking HTTP/HTTPS URL.
     */
    fun isValidUrl(url: String?): Boolean {
        if (url.isNullOrBlank()) return false
        val trimmed = url.trim()
        if (!trimmed.startsWith("http://", ignoreCase = true) && !trimmed.startsWith("https://", ignoreCase = true)) {
            return false
        }
        
        // Reject known fake/placeholder URLs
        val lower = trimmed.lowercase()
        if (lower.contains("example.com") || lower.contains("placeholder") || lower.contains("localhost") || lower.contains("fake")) {
            return false
        }

        return try {
            val uri = URI(trimmed)
            val host = uri.host
            !host.isNullOrBlank() && host.contains(".")
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Ensures HTTPS if valid, or returns trimmed URL.
     */
    fun sanitizeUrl(url: String?): String {
        if (url.isNullOrBlank()) return ""
        val trimmed = url.trim()
        if (!isValidUrl(trimmed)) return ""
        return if (trimmed.startsWith("http://", ignoreCase = true) && !trimmed.contains("localhost")) {
            // Prefer HTTPS where applicable unless it's IP
            trimmed.replaceFirst("http://", "https://", ignoreCase = true)
        } else {
            trimmed
        }
    }

    /**
     * Verifies that the URL belongs to expected provider domain or recognized repository.
     */
    fun isUrlFromProvider(url: String, providerDomain: String): Boolean {
        if (!isValidUrl(url)) return false
        return url.lowercase().contains(providerDomain.lowercase())
    }
}
