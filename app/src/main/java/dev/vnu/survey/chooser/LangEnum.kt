package dev.vnu.survey.chooser

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import dev.vnu.survey.R
import dev.vnu.survey.SApp

enum class LangEnum(@StringRes val label: Int,
                    @DrawableRes val image: Int) {
    ANDROID(R.string.lang_android, R.drawable.ic_language_android),
    KOTLIN(R.string.lang_kotlin, R.drawable.ic_language_kotlin),
    JAVA(R.string.lang_java, R.drawable.ic_language_java);

    fun getString() =
        SApp.res.getString(this.label)
}