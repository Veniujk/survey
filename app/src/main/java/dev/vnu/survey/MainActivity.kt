package dev.vnu.survey

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import dev.vnu.survey.chooser.SurveyItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_quizitem_list.*
import kotlinx.android.synthetic.main.fragment_newsitem_list.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dev.vnu.survey.chooser.ChooserFragment
import dev.vnu.survey.news.NewsItem
import dev.vnu.survey.news.NewsListFragment
import dev.vnu.survey.profile.OtherProfileActivity
import dev.vnu.survey.profile.ProfileFragment
import dev.vnu.survey.profile.UserItem
import dev.vnu.survey.quiz.QuestionItem
import dev.vnu.survey.quiz.QuizActivity
import dev.vnu.survey.summary.SummaryActivity
import dev.vnu.survey.summary.SummaryActivity.Companion.NEW_FEED
import android.Manifest.permission
import android.Manifest.permission.RECORD_AUDIO
import android.Manifest.permission.VIBRATE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_PHONE_STATE
import android.Manifest.permission.INTERNET
import android.annotation.SuppressLint
import java.nio.file.Files.size
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.widget.Button
import android.widget.Toast
import dev.vnu.survey.quiz.NewSurvey
import java.util.*
import java.util.ArrayList as ArrayList2
import kotlin.collections.ArrayList as ArrayList1


/** głowna aktywność aplikacji, jest w niej view pager obslugiwany gestami i bottomnavigationviever
 *
 */
@SuppressLint("ByteOrderMark")
class MainActivity : BaseActivity(),
    ChooserFragment.OnStartQuizListener,
    NewsListFragment.OnNewsInteractionListener,
    ProfileFragment.OnLogChangeListener {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViewPager()

//        newsurvey.setOnClickListener{
//            val intentnewsurvey = Intent(this,NewSurvey::class.java)
//        startActivity(intentnewsurvey) }

//        newsurvey.setOnClickListener(object: View.OnClickListener{
//            override fun onClick(view: View): Unit {
//                val intentnewsurvey = Intent(this,NewSurvey::class.java)
//                startActivity(intentnewsurvey) }})

        val newSurvey2 = findViewById<Button>(R.id.newsurvey2)
        newSurvey2.setOnClickListener {
            val intentnewsurvey = Intent(this,NewSurvey::class.java)
            startActivity(intentnewsurvey)
        }



    }
    /*private val TAG = "PermissionDemo"
    private val RECORD_REQUEST_CODE2 = 101


    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.INTERNET)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied")
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.INTERNET),
            RECORD_REQUEST_CODE2)
    }
*/
   /* private fun checkPermissions(): Boolean {
      var permissions = arrayOf<String>(
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.VIBRATE,
            Manifest.permission.RECORD_AUDIO
            )

        var result: Int
        val listPermissionsNeeded = ArrayList()
        for (p in permissions) {
            result = ContextCompat.checkSelfPermission(this, p)
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p)
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toTypedArray(), 100)
            return false
        }
        return true
    }*/
    //region Ustawienia ViewPagera i bottomNavigation
    private fun setViewPager() {
        viewpager.adapter = getFragmentPagerAdapter()
        navigation.setOnNavigationItemSelectedListener(getBottomNavigationItemSelectedListener())
        viewpager.addOnPageChangeListener(getOnPageChangeListener())
        viewpager.offscreenPageLimit = 2

    }

    private fun getFragmentPagerAdapter() =
        object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int) = when (position) {
                FEED_ID -> NewsListFragment()
                CHOOSER_ID -> ChooserFragment()

                PROFILE_ID -> ProfileFragment()
                else -> {
                    Log.wtf("Fragment out of bounds", "How Came?!")
                    Fragment()
                }
            }

            override fun getCount() = 3

        }

    private fun getBottomNavigationItemSelectedListener() =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    viewpager.currentItem = 0
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    viewpager.currentItem = 1
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    viewpager.currentItem = 2
                    return@OnNavigationItemSelectedListener true
                }
                else -> false

            }
        }

    private fun getOnPageChangeListener() =
        object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                navigation.menu.getItem(position).isChecked = true
            }
        }


    //endregion


    //region Obsluga wynikow z okien
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when {
                (requestCode == QUIZ_ACT_REQ_CODE) -> {
                    navigateToSummaryActivity(data)
                }
                (requestCode == QUIZ_SUMMARY_RCODE) -> {
                    pushNewNews(data)
                }
            }
        }
    }
    /*﻿override fun onRequestPermissionsResult(requestCode: Int,
                                             permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            RECORD_REQUEST_CODE2 -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission has been denied by user")
                } else {
                    Log.i(TAG, "Permission has been granted by user")
                }
            }
        }
    }﻿*/
    /*private fun writeNewSurvey(level: String, lang: String, questset: String) {
        val survey = Survey(level, lang)
        SApp.fData.getReference("quizzes").child(questset).setValue(survey)

    }*/
    /* private fun pushNewQuestions(data: Intent?) {
        val questionItem = data!!.extras.get(NEW_FEED) as QuestionItem
        SApp.fData.getReference("questions").push().setValue(questionItem.apply {
            ask = SApp.fUser!!.ask
            q1 = SApp.fUser!!.q1
            false1 = SApp.fUser!!.false1
            q3 = SApp.fUser!!.q3
            user = SApp.fUser!!.displayName!!
        })
        viewpager.currentItem = 0
        getNewsListFragment().quest_item_list.smoothScrollToPosition(0)
    }
    */

    private fun pushNewNews(data: Intent?) {
        val feedItem = data!!.extras.get(NEW_FEED) as NewsItem
        SApp.fData.getReference("feeds").push().setValue(feedItem.apply {
            uid = SApp.fUser!!.uid
            image = SApp.fUser!!.photoUrl.toString()
            user = SApp.fUser!!.displayName!!
        })
        viewpager.currentItem = 0
        getNewsListFragment().feed_item_list.smoothScrollToPosition(0)
    }

    private fun navigateToSummaryActivity(data: Intent?) {
        val intent = Intent(this,SummaryActivity::class.java).apply {
            if (SApp.fUser != null) {
                data?.putExtra(USER_NAME, SApp.fUser?.displayName
                    ?: SApp.res.getString(R.string.anonym_name))
                data?.putExtra(USER_URL, SApp.fUser?.photoUrl.toString())
            }
            putExtras(data!!.extras)
        }
        startActivityForResult(intent, QUIZ_SUMMARY_RCODE)
    }
    //endregion

    //region Obsluga interfejsow z fragmentow
    private fun getChooserListFragment() =
        (supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + CHOOSER_ID) as ChooserFragment)

    private fun getNewsListFragment() =
        (supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + FEED_ID) as NewsListFragment)


//pobranie pytan do ankiet

    override fun onStartQuizSelected(quiz: SurveyItem, name: String) {
        getChooserListFragment().loader_quiz.visibility = View.VISIBLE

        SApp.fData.getReference("questions/${quiz.questset}").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                val quizset = ArrayList2<QuestionItem>()
                p0.children.map { it.getValue(QuestionItem::class.java) }.mapTo(quizset) { it!! }
                getChooserListFragment().loader_quiz.visibility = View.GONE
                navigateQuiz(quizset, name, quiz)
            }
        })
    }

    fun navigateQuiz(quizSet: ArrayList2<QuestionItem>, title: String, quiz: SurveyItem) {
        val intent = Intent(this, QuizActivity::class.java).apply {
            putExtra(QUIZ_SET, quizSet)
            putExtra(TITLE, title)
            putExtra(QUIZ, quiz)
        }
        startActivityForResult(intent, QUIZ_ACT_REQ_CODE)
    }


    override fun onUserSelected(user: UserItem, image: View) {
        val intent = Intent(this, OtherProfileActivity::class.java)
        intent.putExtra(USER_ITEM, user)
        val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, image, "circleProfileImageTransition")
        startActivity(intent, optionsCompat.toBundle())
    }

    override fun onLikeSelected(feedId: String, diff: Int) {
        if (SApp.fUser != null) {
            SApp.fData.getReference("feeds/$feedId/respects").updateChildren(mapOf(Pair(SApp.fUser?.uid, diff)))
                .addOnCompleteListener { Log.d("MainActivity", "Just liked $feedId, with $diff") }
        }
    }

    override fun onLogout() {
        SApp.fAuth.signOut()
        getNewsListFragment().feed_item_list.adapter.notifyDataSetChanged()
    }

    override fun onLogIn() {
        logIn()
    }

    //endregion
    companion object {
        const val FEED_ID = 0
        const val CHOOSER_ID = 1
        const val PROFILE_ID = 2

        const val QUIZ_SET = "quiz_set"
        const val TITLE = "TITLE"
        const val QUIZ = "QUIZ"

        const val USER_ITEM = "USER_ITEM"

        const val USER_NAME = "USER_NAME"
        const val USER_URL = "USER_URL"

        const val QUIZ_ACT_REQ_CODE = 324
        const val QUIZ_SUMMARY_RCODE = 2431
    }
}
