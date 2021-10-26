package dev.vnu.survey.profile

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_profile.*
import dev.vnu.survey.*
import dev.vnu.survey.BuildConfig
import dev.vnu.survey.R
import dev.vnu.survey.news.NewsItem
import dev.vnu.survey.news.NewsListFragment
import dev.vnu.survey.news.NewsListRecyclerViewAdapter
import dev.vnu.survey.quiz.NewSurvey

/**
 *PL Fragment profilu - Dla niezalogowanego proponuje logowanie lub wyświetla obcego, dla zalogowanego prezentuje swój profil
 */
class ProfileFragment : Fragment() {

    var currentUser: UserItem? = null

    var feedRef: Query? = null

    var respectsValue = 1

    val authListener: FirebaseAuth.AuthStateListener by lazy {
        FirebaseAuth.AuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser != null) {
                updateCurrentUser()
                updateFeedRefEventListener()
                updateLogon()
            } else {
                Log.d("NewsListFragment", "authListener - user:null")

            }
        }
    }

    val eventListener: ValueEventListener by lazy {
        object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                var pointsValue = 0
                respectsValue = 0
                for (it in p0.children) {
                    val feed = it.getValue(NewsItem::class.java)!!
                    mNewsMap.put(it.key!!, feed)
                    pointsValue += feed.points
                    respectsValue += 1 + it.child("respects").children.sumBy { it.getValue(Int::class.java)!! }
                }
                feed_recycler?.adapter?.notifyDataSetChanged()
                respects?.text = respectsValue.toString()
                points?.text = pointsValue.toString()
                loader_profil?.visibility = View.INVISIBLE
            }
        }
    }

    private fun updateFeedRefEventListener() {
        currentUser?.let {
            feedRef = FirebaseDatabase.getInstance().getReference("feeds").orderByChild("uid").equalTo(it.uid)
            loader_profil.visibility = View.VISIBLE
            feedRef?.addValueEventListener(eventListener)
        }
    }

    private val mNewsMap: HashMap<String, NewsItem> = hashMapOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpRecycler()
    }

    private fun setUpRecycler() {
        if (context is NewsListFragment.OnNewsInteractionListener) {
            feed_recycler.layoutManager = LinearLayoutManager(context)
            feed_recycler.adapter = NewsListRecyclerViewAdapter(mNewsMap, context as NewsListFragment.OnNewsInteractionListener)
        }
    }

    override fun onStop() {
        super.onStop()
        SApp.fAuth.removeAuthStateListener(authListener)
        //usunac listenera listy newsow
    }

    override fun onStart() {
        super.onStart()
        SApp.fAuth.addAuthStateListener(authListener)
        updateCurrentUser()
        updateFeedRefEventListener()
        updateLogon()

    }

    private fun updateCurrentUser() {
        currentUser = arguments?.get(USER) as? UserItem
        if (currentUser == null) {
            val firebase = SApp.fAuth.currentUser
            firebase?.let {
                currentUser = firebase.toUserItem()
            }
        }
    }

    private fun updateLogon() {
        when {
            currentUser == null -> {
                setUpViewsNotLogged()
                sign_in_button.setOnClickListener {
                    (activity as OnLogChangeListener).onLogIn()
                    loader_profil.visibility = View.VISIBLE
                }
            }
            currentUser != null -> setUpViewsLogged()
        }
        updateDebugFabLogout()
    }

    private fun updateDebugFabLogout() {

        if (BuildConfig.DEBUG) {
            val visibility = (currentUser != null && context is MainActivity)
            fab_debug_logout.visibility = if (visibility) View.VISIBLE else View.GONE
            fab_debug_logout.setOnClickListener{
                currentUser = null
                (activity as OnLogChangeListener).onLogout()
                updateLogon()
            }
        }
    }

    private fun setUpViewsNotLogged() {
        login_layout.visibility = View.VISIBLE
        feed_recycler.visibility = View.GONE
        respects.text = 0.toString()
        points.text = 0.toString()
        circleProfileImage.setImageDrawable(SApp.res.getDrawable(R.drawable.ic_anonym_face, null))
        collapsing_toolbar.title = SApp.res.getString(R.string.anonym_name)
    }

    private fun setUpViewsLogged() {
        login_layout.visibility = View.GONE
        feed_recycler.visibility = View.VISIBLE
        setUpUserData()
    }

    private fun setUpUserData() {
        collapsing_toolbar.title = currentUser!!.name
        Glide.with(this@ProfileFragment)
            .load(currentUser?.url)
            .into(circleProfileImage)
    }

    interface OnLogChangeListener {
        fun onLogout()
        fun onLogIn()
    }

    companion object {

        const val USER = "USER"

        fun newInstance(user: UserItem): ProfileFragment {
            val frament = ProfileFragment()
            val bundle = Bundle()
            bundle.putSerializable(USER, user)
            frament.arguments = bundle
            return frament
        }
    }
}