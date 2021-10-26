package dev.vnu.survey.chooser

import java.io.Serializable

/** model danych dla pojedynczego quizu np. zestaw pytań
 *
 */
data class SurveyItem (
        var level: LevelEnum = LevelEnum.EASY,
        var lang: LangEnum = LangEnum.ANDROID,
        var questset: String = "") : Serializable
