# Andualem Digital Calendar, Abushakir & Academic Library Platform

An AI-powered Ethiopian knowledge preservation platform combining the 13-month Ethiopian Calendar, Abushakir Bahre Hasab computus engine, Ge'ez language resources, Ethiopian history, Orthodox literature, and real external source adapters for global and Ethiopian academic research.

---

## 1. Real Source Providers & Adapters

The application connects directly to verified real source providers without simulated or placeholder links:

1. **Google Books** (`googleBooksAdapter`):
   - Endpoint: `https://www.googleapis.com/books/v1/volumes?q={query}`
   - Provides book metadata, info links, Google Books reader links, and open access full-text previews.

2. **OpenAlex Academic Index** (`openAlexAdapter`):
   - Endpoint: `https://api.openalex.org/works?search={query}`
   - Provides global academic works, DOI records, publisher landing pages, and legal Open Access PDF links.

3. **Crossref DOI Repository** (`crossrefAdapter`):
   - Endpoint: `https://api.crossref.org/works?query={query}`
   - Provides journal article metadata, DOIs (`https://doi.org/...`), publisher names, and publication years.

4. **Internet Archive Digital Library** (`internetArchiveAdapter`):
   - Endpoint: `https://archive.org/advancedsearch.php`
   - Provides public domain Ethiopian manuscripts, historical texts, Ge'ez grammars, 2UP book previews, and PDF downloads.

5. **Directory of Open Access Journals (DOAJ)** (`doajAdapter`):
   - Endpoint: `https://doaj.org/api/search/articles/{query}`
   - Provides peer-reviewed open access journal articles with verified full-text links.

6. **Ethiopian Academic & Institutional Repositories** (Manual Portal Search Required):
   - **Ethiopian National Academic OER & Digital Library (NDL)**: `https://ndl.ethernet.edu.et/`
   - **Addis Ababa University Electronic Theses & Dissertations (AAU ETD)**: `https://etd.aau.edu.et/`
   - **National Academic Digital Repository of Ethiopia (NADRE)**: `https://nadre.ethernet.edu.et/`
   - **Ethiopian Journals Online (EJOL)**: `https://ejol.ethernet.edu.et/`
   - **Jimma University Institutional Repository**: `https://repository.ju.edu.et/`
   - **Hawassa University Journals**: `https://journals.hu.edu.et/hu-journals/`

*Note: For repositories requiring session tokens or lacking public REST APIs, the app reports status as `MANUAL SEARCH ONLY` and provides direct portal links rather than generating fake results.*

---

## 2. API Setup & Environment Variables

Configure API keys in the **AI Studio Secrets Panel** or create a `.env` file at project root based on `.env.example`:

```env
# Gemini AI
GEMINI_API_KEY=your_gemini_api_key

# Source Adapters
GOOGLE_BOOKS_API_KEY=your_google_books_key
OPENALEX_EMAIL=your_email@domain.com
CROSSREF_EMAIL=your_email@domain.com
SEMANTIC_SCHOLAR_API_KEY=your_key
INTERNET_ARCHIVE_ENABLED=true
DOAJ_API_KEY=your_doaj_key
```

If an API key is omitted, the application operates seamlessly using public endpoints or polite pools, and clearly displays adapter status in the **Source Integrations Screen**.

---

## 3. Search Result Card Features

Every search result card contains:
- **Title**, **Author(s)**, **Document Type**, **Publication Year**, **Publisher / Journal**
- **Availability Status Badge** (`Verified legal full text.`, `Free preview — not full text.`, `Metadata only — no verified full text found.`)
- **Verification Level Badge** (`URL verified`)
- **Action Buttons** (hidden if valid URL unavailable):
  - `[Source]` (Direct record URL)
  - `[Publisher]` (Publisher / landing page)
  - `[Preview]` (Book or 2UP preview link)
  - `[Full Text]` (Verified legal PDF / reader link)
  - `[Copy Citation]` (Copies formatted APA/BibTeX citation to clipboard)

---

## 4. Troubleshooting & Source Status

Navigate to **Library -> Integrations** or tap **Test All** to verify live connection statuses for all 11 source adapters.
