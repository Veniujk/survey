package dev.vnu.survey.summary

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import dev.vnu.survey.BaseActivity
import kotlinx.android.synthetic.main.fragment_newsitem.*
import kotlinx.android.synthetic.main.result_activity.*
import dev.vnu.survey.MainActivity.Companion.USER_NAME
import dev.vnu.survey.MainActivity.Companion.USER_URL
import dev.vnu.survey.SApp
import dev.vnu.survey.R
import dev.vnu.survey.news.NewsItem
import dev.vnu.survey.quiz.QuizActivity.Companion.POINTS
import dev.vnu.survey.quiz.QuizActivity.Companion.QUIZ_NAME
import dev.vnu.survey.quiz.QuizActivity.Companion.SUCCESS_SUMMARY
import kotlinx.android.synthetic.main.fragment_newsitem.respects
import kotlinx.android.synthetic.main.fragment_profile.*
import java.lang.Exception

/**
 *PL Aktywność podsumowania quizu. Obsługuje "przypadkowe logowanie" by zachować postępy
 *EN Quiz summary activity. Handle "accidential login" to save score
 */
class SummaryActivity : BaseActivity() {
    //region intent extras
    private val quiz_name by lazy { intent.extras.get(QUIZ_NAME) as String }
    private val success_summary by lazy { intent.extras.get(SUCCESS_SUMMARY) as String }
    private val pointed by lazy { intent.extras.get(POINTS) as Int }
    private val user_name by lazy { intent.extras.get(USER_NAME) as? String }
    private val user_url by lazy { intent.extras.get(USER_URL) as? String }
    //endregion

    //region window init
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_activity)
        setUpViews()
    }

    private fun setUpViews() {
        title_caption.text = success_summary
        quizTitle.text = quiz_name
        pointsText.text = pointed.toString()
        respects.text = 1.toString()
        time.text = "00m"

        setUserName()
        setUserImage()
        setAddComment()

        likesImage.isEnabled = false

        setUpOkButton()
        setUpCloseButton()
    }

    private fun setUserImage() {
        if (!user_url.isNullOrEmpty()) {
            Glide.with(this)
                .load(user_url)
                .into(circleImageProfile)
        }
    }

    private fun setUserName() {
        if (!user_name.isNullOrEmpty()) {
            name.text = user_name
        }
    }

    //endregion

    //region comment
    private fun setAddComment() {
        add_comment.visibility = View.VISIBLE
        comment.visibility = View.GONE
        add_comment.setOnClickListener { v -> showEditComment() }
    }

    private fun showEditComment() {
        add_comment.visibility = View.GONE
        edit_comment.visibility = View.VISIBLE
    }
    //endregion

    //region public
    private fun setUpOkButton() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            ok.setOnClickListener { v -> goToPublish() }
        } else {
            ok.text = SApp.res.getString(R.string.not_logged_news)
            ok.setOnClickListener { logIn() }
        }
    }

    override fun onLogInSuccess() {
        goToPublish()
    }

    override fun onLogInFailue(exception: Exception?) {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private fun goToPublish() {
        val intent = Intent().apply {
            putExtra(NEW_FEED, NewsItem().apply {
                comment = edit_comment.text.toString()
                points = pointed
                quiz = quiz_name
                timeMilis = System.currentTimeMillis()
            })
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun setUpCloseButton() {
        close_btn.setOnClickListener { v ->
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
    //endregion

    companion object {
        const val NEW_FEED = "newFeed"
    }
}