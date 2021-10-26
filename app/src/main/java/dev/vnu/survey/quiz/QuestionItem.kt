package dev.vnu.survey.quiz


import java.io.Serializable

/** Model danych dla pojedynczego pytania dla pytania quizu
 *
 */
data class QuestionItem(
 //   var id: String,
    var ask: String = "",
    var positive: String = "",
    var false1: String = "",
    var false2: String = "") : Serializable