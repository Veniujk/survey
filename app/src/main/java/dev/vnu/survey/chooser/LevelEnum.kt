package dev.vnu.survey.chooser

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import dev.vnu.survey.R
import dev.vnu.survey.SApp

/**mapowanie poziomów trudności i towarzyszące im zasoby
 *
 */
enum class LevelEnum(@StringRes val label: Int,
                     @DrawableRes val image: Int) {
    EASY (R.string.level_easy,R.drawable.ic_level_easy),
    AVERAGE (R.string.level_average, R.drawable.ic_level_average),
    HARD (R.string.level_hard, R.drawable.ic_level_hard);

    fun getString() =
            SApp.res.getString(this.label)
}