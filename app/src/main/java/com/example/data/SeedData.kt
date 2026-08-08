package com.example.data

object SeedData {

    val INITIAL_DOCUMENTS = listOf(
        DocumentEntity(
            id = 1,
            title = "The Authentic Ethiopian Calendar (ትክክለኛው የኢትዮጵያ ዘመን አቆጣጠር)",
            titleAmharic = "ትክክለኛው የኢትዮጵያ ዘመን አቆጣጠር ፤ አላሙ የተሳሳተውን የጎርጎርዮስ ዘመን አቆጣጠር መከተል",
            author = "ኃይሌ ስሜ (Haile Seme)",
            category = "Orthodox Books",
            language = "Amharic",
            year = 2000,
            pageCount = 158,
            description = "ስለ የኢትዮጵያ ዘመን አቆጣጠር፣ የጌታችን ልደት ስሌት፣ የጎርጎርዮስ ካሌንደር ልዩነት፣ አሜተ ዓለምና አሜተ ምሕረት፣ የሊቃውንትና የቤተክርስቲያን ታሪክ የተፃፈ ጥናታዊ መጽሐፍ።",
            content = """
                ምዕራፍ አንድ: መግቢያ እና መነሻ
                የሰው ልጅ በሰማያዊ አካላትና በተፈጥሮ ሁነቶች መፈራረቅ የዘመን አቆጣጠሩን መሠረተ። የኢትዮጵያ ዘመን አቆጣጠር በብሉይና በሐዲስ ኪዳን መጻሕፍት እንዲሁም በሊቃውንት ትምህርት ላይ የተመሠረተ ጥንታዊ አቆጣጠር ነው።

                ምዕራፍ ሁለት: የኢትዮጵያ ዘመን አቆጣጠር ታሪካዊ ፍጭቶች
                የኢትዮጵያ ዘመን አቆጣጠር ከጌታችን ልደት በፊት 5500 ዓመተ ዓለም ጀምሮ ይቆጥራል። በየ4 ዓመቱ ጳጉሜ 6 የምትሆንበት ወንጌላዊ ዮሐንስ ዘመን አለ።

                ምዕራፍ ሦስት: የግብፅ /ቅብጥ/ ቤተ ክርስቲያን ከኢትዮጵያ ቤተ ክርስቲያን ጋር ያለው ግንኙነት
                የቅብጥና የኢትዮጵያ ቤተ ክርስቲያን በዘመን አቆጣጠርና በባሕረ ሐሳብ ስሌት የጠበቀ ወንድማማችነትና የታሪክ ትስስር አላቸው።

                ምዕራፍ አራት: ኢትዮጵያ እና ጌታችን ሥጋዌ
                ጌታችን ኢየሱስ ክርስቶስ በተወለደበት ዘመን የነበሩ የከዋክብትና የዘመን ምልክቶች፣ የሰብአ ሰገል ጉዞና የኢትዮጵያ ታሪካዊ ድርሻ።

                ምዕራፍ አምስት: የኢትዮጵያ ዘመን መቆጠሪያ የልዑላዊ ነውን?
                የኢትዮጵያ ዘመን አቆጣጠር የራሱ የሆነ የፀሐይና የጨረቃ ወራት፣ የወራት ርዝማኔና የበዓላት አወጣጥ ስሌት ያለው ሉዓላዊ ሥርዓት ነው።

                ምዕራፍ ስድስት: ክርስቲያናዊው ዘመን አቆጣጠርና መመንጨቱ
                የክርስቲያናዊ ዘመን አቆጣጠር መነሻና የጌታችን ልደት ዓመት ስሌቶች በቤተክርስቲያን ሊቃውንት የተደረገ ጥናት።

                ምዕራፍ ፯ - ፲፪: የምዕራባውያን ዘመን አቆጣጠርና የዲዮናስዮስ ስህተት
                የጎርጎርዮስ ዘመን አቆጣጠር ከኢትዮጵያ ዘመን አቆጣጠር በ7 እና 8 ዓመታት የሚለይበት ዋናው ምክንያት የምዕራባውያን የቀን አቆጣጠር ስህተቶች ናቸው።
            """.trimIndent(),
            tags = "Haile Seme, Ethiopian Calendar, Gregorian Calendar, Ethiopian Orthodox",
            isFeatured = true,
            coverColorHex = "#0F6230",
            sourceUrl = "https://www.ethiopianorthodox.org",
            downloadUrl = "https://eathebook.org/"
        ),
        DocumentEntity(
            id = 2,
            title = "Time, Ethiopian Calendar Algorithms & Astronomy (ጊዜ ፣ የኢትዮጵያ ዘመን አቆጣጠር ቀመር)",
            titleAmharic = "ጊዜ ፣ የኢትዮጵያ ዘመን አቆጣጠር ቀመር እና የዘመናዊ ሥነፈለክ መንደርደሪያዎች",
            author = "ሄዞን (Anteneh Biru)",
            category = "Science & Astronomy",
            language = "Amharic",
            year = 2012,
            pageCount = 24,
            description = "ስለ ጊዜ ፍልስፍና፣ የኢትዮጵያ ዘመን አቆጣጠር የሒሳብ ስሌት (ዓመተ ዓለም፣ ወንጌላዊ፣ መጠነ ራብዕ፣ ወንበር፣ አበቅቴ፣ መጥቅዕ፣ መባጃ ሐመር)፣ የመጽሐፈ ሄኖክ ሥነ-ኮከብ፣ የአቡሻኪር ቀመር እና የኤክሴል ቪዥዋል ቤዚክ አልጎሪዝም ጥናት።",
            content = """
                1. መግቢያ እና ሐተታ ጊዜ
                ጊዜ ያለማቋረጥ የሚፈስ የተፈጥሮ ሂደት ነው። የቀደሙት ፈላስፎችና የሥነ-ፈለክ ሊቃውንት ጊዜን በፀሐይና ጨረቃ ዑደቶች ለክተዋል።

                2. የኢትዮጵያ ዘመን ሥነ ስሌት
                - ዓመተ ዓለም = 5500 + ዓመተ ምሕረት
                - ወንጌላዊ = (ዓመተ ዓለም) Mod 4 (0=ዮሐንስ, 1=ማቴዎስ, 2=ሉቃስ, 3=ማርቆስ)
                - መጠነ ራቢት = (ዓመተ ዓለም) / 4
                - ዘመን መለወጫ (መባቻ) = ((ዓመተ ምሕረት + መጠነ ራቢት) Mod 7)

                3. የበዓላትና አጽዋማት መወሰኛ ቀመር (ባሕረ ሐሳብ)
                - መደብ = ዓመተ ዓለም Mod 19
                - ወንበር = መደብ - 1
                - አበቅቴ = (ወንበር * 11) Mod 30
                - መጥቅዕ = 30 - አበቅቴ (ወይም (ወንበር * 19) Mod 30)
                - ተውሳክና መባጃ ሐመር ስሌት።

                4. የመጽሐፈ ሄኖክና የአቡሻኪር ምልከታ
                መጽሐፈ ሄኖክ የፀሐይን 6 መስኮቶችና የ364 ቀናት ዓመታዊ ዑደት ሲገልጽ፣ አቡሻኪር ደግሞ የጨረቃና ፀሐይ ዑደቶችን በስድሳዮች የጊዜ አሃዳት በጥንቃቄ ቀምሯል።
            """.trimIndent(),
            tags = "Bahre Hasab, Algorithms, Astronomy, Enoch, Abushakir, Computus",
            isFeatured = true,
            coverColorHex = "#B57C00",
            sourceUrl = "https://eathebook.org/",
            downloadUrl = "https://eathebook.org/"
        ),
        DocumentEntity(
            id = 3,
            title = "EAT The Book - Academic & Theological Library Repository",
            titleAmharic = "ኢትዮጵያ አካዳሚክና ቴኦሎጂካል መጻሕፍት (EAT The Book)",
            author = "EAT The Book Digital Collection",
            category = "Academic Research",
            language = "Amharic & English",
            year = 2024,
            pageCount = 500,
            description = "ሕጋዊ የኢትዮጵያ ኦርቶዶክስ ተዋሕዶ ቤተክርስቲያን፣ የታሪክ፣ የግዕዝ ቋንቋ እና የዘመን አቆጣጠር መጻሕፍትና ጥናቶች ዲጂታል ማከማቻ portal::",
            content = """
                ABOUT EAT THE BOOK PORTAL:
                EAT The Book (Ethiopian Academic & Theological Books) provides legal, open-access bibliographic resources, academic papers, and digitized manuscripts on Ethiopian calendar, Ge'ez language, Church history, and theological literature.
            """.trimIndent(),
            tags = "EAT The Book, Library, Ethiopian Books, Theological, Open Access",
            isFeatured = true,
            coverColorHex = "#0F6230",
            sourceUrl = "https://eathebook.org/",
            downloadUrl = "https://eathebook.org/"
        ),
        DocumentEntity(
            id = 4,
            title = "Bahre Hasab - Treatise on Ethiopian Calendar & Computus",
            titleAmharic = "ባሕረ ሐሳብ - የዘመን አቆጣጠርና የበዓላት ስሌት",
            author = "Abushakir Ibn al-Rahib (አቡሻኪር)",
            category = "Orthodox Books",
            language = "Ge'ez & Amharic",
            year = 1257,
            pageCount = 148,
            description = "The definitive historical text on the mathematical, astronomical, and theological computation of the Ethiopian calendar, leap years, and liturgical feast dates.",
            content = """
                CHAPTER 1: THE ERA OF CREATION (AMETE ALEM)
                The computation of time in the Ethiopian Church begins from Amete Alem (5500 BC). To find Amete Alem for any given Ethiopian year (E.Y.), add 5500 to the Ethiopian year.

                CHAPTER 2: THE FOUR EVANGELISTS (WENGELAWI)
                Every year is assigned to one of the four Evangelists in repeating order: Matthew, Mark, Luke, and John. Year of John is a leap year with 6 days in Pagume.

                CHAPTER 3: WENBER AND METKE
                The Metonic 19-year lunar cycle determines the calculation of Wenber, Abekt (Epact), and Metke. Through Metke, the date of Tsome Nenewe (Nineveh Fast) and Fasika (Easter) are determined with astronomical precision.
            """.trimIndent(),
            tags = "Bahre Hasab, Calendar, Abushakir, Fasting, Easter",
            isFeatured = true,
            coverColorHex = "#800020",
            sourceUrl = "https://eathebook.org/",
            downloadUrl = "https://www.ethiopianorthodox.org"
        ),
        DocumentEntity(
            id = 5,
            title = "Fundamentals of Ge'ez Grammar and Syntax",
            titleAmharic = "የግዕዝ ቋንቋ መመሪያ እና ሰዋስው",
            author = "Kefle Maryam & Academic Scholars",
            category = "Ge'ez Language",
            language = "Amharic",
            year = 1998,
            pageCount = 210,
            description = "Comprehensive guide to Ge'ez grammar, verb conjugation, nominal declension, and classical sentence structure for researchers and students.",
            content = """
                LESSON 1: THE FIDEL ALPHABET
                Ge'ez script consists of 26 base consonant characters, each modified into 7 vowel orders (ግዕዝ, ካዕብ, ሣልስ, ራብዕ, ኃምስ, ሳድስ, ሳብዕ).

                LESSON 2: NOUN DECLENSION AND GENDER
                Nouns in Ge'ez carry masculine and feminine gender forms. Construct state (አስሚክቲ) is used to indicate possession and relationships.

                LESSON 3: VERB CONJUGATION (ቀተለ System)
                The archetype verb is Qatala (ቀተለ - 'he killed'). Verbs are conjugated across Perfect (ኃላፊ), Imperfect (ካዕብ), and Subjunctive/Imperative modes.
            """.trimIndent(),
            tags = "Geez, Grammar, Language, Linguistics, Alphabet",
            isFeatured = false,
            coverColorHex = "#2E5A88",
            sourceUrl = "https://www.scribd.com/document/816711548/tig",
            downloadUrl = "https://eathebook.org/"
        ),
        DocumentEntity(
            id = 6,
            title = "Ethioconcord: Concordance of Ethiopian and Gregorian Calendars",
            titleAmharic = "የኢትዮጵያና የጎርጎርዮስ ካሌንደር ማሰላሰያ (Ethioconcord)",
            author = "Joseph Tubiana",
            category = "Academic Research",
            language = "English & French",
            year = 1965,
            pageCount = 320,
            description = "Scholarly concordance and mathematical conversion formulas between Ethiopian Calendar (E.C.) and Gregorian Calendar (G.C.) eras.",
            content = """
                PREFACE ON ETHIOPIAN CHRONOLOGY:
                Ethioconcord establishes direct date correspondence tables across centuries, addressing leap year shifts, Julian/Gregorian boundary conversions, and royal charter dates.
            """.trimIndent(),
            tags = "Tubiana, Ethioconcord, Chronology, Date Converter, OpenLibrary",
            isFeatured = false,
            coverColorHex = "#B71C1C",
            sourceUrl = "https://openlibrary.org/subjects/ethiopian_calendar",
            downloadUrl = "https://openlibrary.org/subjects/ethiopian_calendar"
        )
    )

    val INITIAL_SAINTS = listOf(
        SaintEntity(
            ethiopianMonth = 1, ethiopianDay = 1,
            nameEn = "New Year (Enkutatash) & St. John the Baptist",
            nameAm = "እንቁጣጣሽ እና ቅዱስ ዮሐንስ መጥምቅ",
            geezName = "ርእሰ ዐውደ ዓመት ወቅዱስ ዮሐንስ",
            description = "First day of the Ethiopian New Year and commemoration of St. John the Baptist.",
            biography = "Enkutatash marks the end of the rainy season and the renewal of spring flowers (Adey Abeba)."
        ),
        SaintEntity(
            ethiopianMonth = 1, ethiopianDay = 17,
            nameEn = "Feast of the Finding of the True Cross (Meskel)",
            nameAm = "መስቀል - ደመራ",
            geezName = "ዕለተ ረክበቱ ለቅዱስ መስቀል",
            description = "Commemoration of Queen Helena's discovery of the True Cross in Jerusalem.",
            biography = "Queen Helena lit a bonfire (Demera) whose smoke guided her to the location of the Holy Cross."
        ),
        SaintEntity(
            ethiopianMonth = 5, ethiopianDay = 11,
            nameEn = "St. Michael the Archangel",
            nameAm = "ቅዱስ ሚካኤል ሊቀ መላእክት",
            geezName = "ቅዱስ ሚካኤል ሊቀ መላእክት",
            description = "Monthly and annual principal feast of Archangel Michael.",
            biography = "Archangel Michael is venerated as the chief guardian, defender of God's people, and intercessor."
        ),
        SaintEntity(
            ethiopianMonth = 5, ethiopianDay = 21,
            nameEn = "St. Mary (Astero'eyo Maryam)",
            nameAm = "አስተርኦዮ ማርያም",
            geezName = "እግዝእትነ ማርያም",
            description = "Commemoration of the repose and falling asleep of the Holy Virgin Mary.",
            biography = "Great feast honoring the Holy Mother of God across all Ethiopian Orthodox churches."
        ),
        SaintEntity(
            ethiopianMonth = 7, ethiopianDay = 27,
            nameEn = "St. Tekle Haymanot",
            nameAm = "አቡነ ተክለ ሃይማኖት",
            geezName = "አቡነ ተክለ ሃይማኖት",
            description = "Veneration of Abune Tekle Haymanot, legendary 13th-century monk and saint of Debre Libanos.",
            biography = "Abune Tekle Haymanot spent decades in solitary prayer and led the spiritual renewal of Ethiopia."
        ),
        SaintEntity(
            ethiopianMonth = 11, ethiopianDay = 7,
            nameEn = "Holy Trinity (Kidus Selassie)",
            nameAm = "ቅዱስ ሥላሴ",
            geezName = "በዓለ ቅዱስ ሥላሴ",
            description = "Feast of the Holy Trinity.",
            biography = "Celebrated on the 7th day of every Ethiopian month, with major annual celebration in Hamle."
        )
    )

    val INITIAL_FEASTS = listOf(
        FeastEntity(
            nameEn = "Enkutatash (Ethiopian New Year)",
            nameAm = "እንቁጣጣሽ (አዲስ ዓመት)",
            ethiopianMonth = 1, ethiopianDay = 1,
            isMovable = false,
            description = "First day of Meskerem celebrating the new year and arrival of spring flowers."
        ),
        FeastEntity(
            nameEn = "Finding of the True Cross (Meskel)",
            nameAm = "መስቀል",
            ethiopianMonth = 1, ethiopianDay = 17,
            isMovable = false,
            description = "Lighting of the Demera bonfire celebrating the discovery of the Cross."
        ),
        FeastEntity(
            nameEn = "Genna (Ethiopian Christmas)",
            nameAm = "ገና (ልደት)",
            ethiopianMonth = 4, ethiopianDay = 29,
            isMovable = false,
            description = "Nativity of Jesus Christ celebrated on Tahsas 29 (or Tahsas 28 in leap years)."
        ),
        FeastEntity(
            nameEn = "Timket (Ethiopian Epiphany)",
            nameAm = "ጥምቀት",
            ethiopianMonth = 5, ethiopianDay = 11,
            isMovable = false,
            description = "Baptism of Jesus Christ in the Jordan River, celebrated with Tabot processions to water bodies."
        )
    )
}
