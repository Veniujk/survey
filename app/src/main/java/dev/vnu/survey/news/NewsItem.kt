package dev.vnu.survey.news

import android.util.Log
import java.io.Serializable

/**Model danych dla pojedynczego elementu nowin
 * Reprezentuje pojedynczy węzeł w feeds bazy danych firebase
 * Swiadoma denormalizacja bazy - optymalizacja pobieran postow, danych uzytkownikow w szczegolnosci
 */
data class NewsItem(
    var comment: String = "",
    var points: Int = 0,
    var quiz: String = "",
    var image: String = "",
    var user: String = "",
    var timeMilis: Long = 0,
    var uid: String = "",
    var respects: HashMap<String, Int> = hashMapOf()) : Serializable
