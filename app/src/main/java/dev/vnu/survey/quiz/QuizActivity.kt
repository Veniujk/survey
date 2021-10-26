package dev.vnu.survey.quiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import android.widget.Toast
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand
import com.kofigyan.stateprogressbar.StateProgressBar
import dev.vnu.survey.BaseActivity
import dev.vnu.survey.MainActivity.Companion.QUIZ
import dev.vnu.survey.MainActivity.Companion.QUIZ_SET
import dev.vnu.survey.MainActivity.Companion.TITLE
import kotlinx.android.synthetic.main.quiz_activity.*
import dev.vnu.survey.SApp
import dev.vnu.survey.R
import dev.vnu.survey.chooser.SurveyItem
import java.util.*

/**
 *PL Aktywność quizu. W tym oknie odbywa się quiz - bez przechodzenia do innych okien aż do podsumowania.
 *PL Logika podmienia pytania i odpowiedzi w ramach okna, nie przebudowuje go.
 */
class QuizActivity : BaseActivity() {

    private val questionList by lazy { intent.extras.get(QUIZ_SET) as ArrayList<QuestionItem> }
    private val quizTitle by lazy { intent.extras.get(TITLE) as String }
    private val quiz by lazy { intent.extras.get(QUIZ) as SurveyItem }

    val successArray: BooleanArray by lazy { BooleanArray(questionList.size) }

    private val quizItereator by lazy { questionList.iterator() }

    private lateinit var currentQuestionItem: QuestionItem

    private var currentPositive = 0
    private var currentNumber: Int = 0
        get() = if (field < 5) field else 4

    /**
     *PL Odlicza do końca czasu na odpowiedź
     *EN Counts down to answer time ends
     */
    var countDown = getCountDownTimer()

    /**
     *PL Odlicza do następnego pytania, tzn. pomiędzy odpowiedzią a prezentowaniem nowego pytania
     *EN Counts to next question, i.e. between answer and presenting new question
     */
    var prepareNext = getPrepareNextTimer()
    private var countDownRemain: Long = COUNTDOWNREMAIN
    private var prepareNextRemain: Long = PREPARENEXTREMAIN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_activity)

        quizLogo.setImageResource(quiz.lang.image)
        levelImageView.setImageResource(quiz.level.image)
        nextQuestion()
    }

    private fun nextQuestion() {
        if (quizItereator.hasNext()) {
            currentQuestionItem = quizItereator.next()
            currentPositive = Random().nextInt(3) + 1
            progress.setCurrentStateNumber(StateProgressBar.StateNumber.values()[currentNumber])
            questionText.niceSetText(currentQuestionItem.ask)
            setUpButtons()
            countDown.start()
        } else {
            returnResultFromQuiz()
        }
    }


    private fun setUpButtons() {
        ans_a.setOnClickListener(onChoiceListener(false))
        ans_b.setOnClickListener(onChoiceListener(false))
        ans_c.setOnClickListener(onChoiceListener(false))
        when (currentPositive) {
            1 -> {
                ans_a.niceSetText(currentQuestionItem.positive)
                ans_a.setOnClickListener(onChoiceListener(true))
                ans_b.niceSetText(currentQuestionItem.false2)
                ans_c.niceSetText(currentQuestionItem.false1)
            }
            2 -> {
                ans_a.niceSetText(currentQuestionItem.false2)
                ans_b.niceSetText(currentQuestionItem.positive)
                ans_b.setOnClickListener(onChoiceListener(true))
                ans_c.niceSetText(currentQuestionItem.false1)
            }
            3 -> {
                ans_a.niceSetText(currentQuestionItem.false2)
                ans_b.niceSetText(currentQuestionItem.false1)
                ans_c.niceSetText(currentQuestionItem.positive)
                ans_c.setOnClickListener(onChoiceListener(true))
            }
        }

    }

    private fun onChoiceListener(isPositive: Boolean): View.OnClickListener {
        return View.OnClickListener {
            successArray[currentNumber] = isPositive

            countDown.cancel()

//kolorowanie odpowiedzi

         if (!isPositive) {
               // setButtonsColorBrand(DefaultBootstrapBrand.DANGER)
            }

            when (currentPositive) {
                1 -> ans_a.bootstrapBrand = DefaultBootstrapBrand.SUCCESS
                2 -> ans_b.bootstrapBrand = DefaultBootstrapBrand.SUCCESS
                3 -> ans_c.bootstrapBrand = DefaultBootstrapBrand.SUCCESS
            }
            setButtonsClickable(false)

            resetNextTimer()
            prepareNext.start()
        }
    }

    private fun setButtonsClickable(clickable: Boolean) {
        ans_a.isClickable = clickable
        ans_b.isClickable = clickable
        ans_c.isClickable = clickable
    }

    private fun setButtonsColorBrand(brand: DefaultBootstrapBrand) {
        ans_a.bootstrapBrand = brand
        ans_b.bootstrapBrand = brand
        ans_c.bootstrapBrand = brand
    }

    private fun TextView.niceSetText(string: String) {
        if (currentNumber > 0) {
            val anim = AlphaAnimation(1.0f, 0.0f)
            anim.duration = 200
            anim.repeatCount = 1
            anim.repeatMode = Animation.REVERSE
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                    this@niceSetText.text = string
                }

                override fun onAnimationEnd(animation: Animation?) {}
                override fun onAnimationStart(animation: Animation?) {}
            })
            this.startAnimation(anim)
        } else {
            this.text = string
        }
    }


    private fun getCountDownTimer(): CountDownTimer {
        return object : CountDownTimer(COUNTDOWNREMAIN, 100) {
            override fun onFinish() {
                resetNextTimer()
                successArray[currentNumber] = false
                setButtonsColorBrand(DefaultBootstrapBrand.WARNING)
                setButtonsClickable(false)

                prepareNext.start()
                Toast.makeText(this@QuizActivity, SApp.res.getString(R.string.time_is_up), Toast.LENGTH_SHORT).show()
            }

            override fun onTick(remain: Long) {
                countDownRemain = remain
                timeLeftProgress.progressValue = remain / 1000f
            }
        }

    }

    private fun resetNextTimer() {
        countDownRemain = COUNTDOWNREMAIN
        prepareNextRemain = PREPARENEXTREMAIN
        prepareNext = getPrepareNextTimer()
    }


    private fun getPrepareNextTimer(): CountDownTimer {
        return object : CountDownTimer(PREPARENEXTREMAIN, 10) {
            override fun onFinish() {
                resetCountDownTimer()
                setButtonsClickable(true)
                setButtonsColorBrand(DefaultBootstrapBrand.SECONDARY)
                currentNumber++
                nextQuestion()
            }

            override fun onTick(remain: Long) {
                prepareNextRemain = remain
                timeLeftProgress.progressValue = 40 - remain.toFloat() / 50
            }

        }

    }

    private fun resetCountDownTimer() {
        countDownRemain = COUNTDOWNREMAIN
        prepareNextRemain = PREPARENEXTREMAIN
        countDown = getCountDownTimer()
    }

    override fun onPause() {
        super.onPause()
        countDown.cancel()
        prepareNext.cancel()
    }

    override fun onResume() {
        super.onResume()
        if (!countDownRemain.equals(COUNTDOWNREMAIN)) {
            countDown = getCountDownTimer()
            countDown.start()
        }
        if (!prepareNextRemain.equals(PREPARENEXTREMAIN)) {
            prepareNext = getPrepareNextTimer()
            prepareNext.start()
        }
    }


    private fun returnResultFromQuiz() { //todo
        val intent = Intent().apply {
            putExtra(QUIZ_NAME, quizTitle)

            var results = ""

            for(question in questionList) {
                var i = 0
                var values = getRandomPercentages()
                results += question.ask + "\n"
                results += question.false1 + " " + values.get(i++) + "%\n"
                results += question.false2 + " " + values.get(i++) + "%\n"
                results += question.positive + " " + values.get(i++) + "%\n\n"
            }
            putExtra(SUCCESS_SUMMARY, results)
            putExtra(POINTS, successArray.count({ it }) * (quiz.level.ordinal + 1) * 39)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun getRandomPercentages(): MutableList<Int> {
        var pool = 100

        var values = mutableListOf<Int>()
        var rand = (30..60).random()
        pool -= rand
        values.add(rand)

        rand = (0..25).random()
        if (rand < pool )
            pool -= rand
        else {
            rand = pool
            pool = 0
        }
        values.add(rand)

        rand = (0..15).random()
        if (rand < pool )
            pool -= rand
        else {
            rand = pool
            pool = 0
        }

        if (pool > 0)
            rand += pool
        values.add(rand)

        return values
    }

    companion object {
        const val QUIZ_NAME = "QUIZNAME"
        const val SUCCESS_SUMMARY = "SUCCESS_SUMMARY"
        const val POINTS = "POINTS"

        const val COUNTDOWNREMAIN = 40000L
        const val PREPARENEXTREMAIN = 2000L
    }
}