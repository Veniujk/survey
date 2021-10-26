package dev.vnu.survey.chooser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dev.vnu.survey.R
import dev.vnu.survey.quiz.NewSurvey
import kotlinx.android.synthetic.main.fragment_quizitem_list.*

/** fragment wyboru quizu
 *
 */
class ChooserFragment : Fragment() {

    private var quizzesRef = FirebaseDatabase.getInstance().getReference("quizzes")

    private lateinit var onStartQuizListener: OnStartQuizListener


    private val quizzesMap : HashMap<String, SurveyItem> = HashMap()
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnStartQuizListener) {
            onStartQuizListener = context
    }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_quizitem_list, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpRecyclerView()
        setCommunication()
    }
//pobranie listy ankiet
    private fun setCommunication() {
        loader_quiz.visibility = View.VISIBLE
        quizzesRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                loader_quiz.visibility = View.GONE
                for (message in p0.children) {
                    val quizItem = message.getValue(SurveyItem::class.java)!!
                    quizzesMap.put(message.key!!, quizItem)
                }
                 quest_item_list.adapter.notifyDataSetChanged()

            }

        })
    }


    private fun setUpRecyclerView() {
        quest_item_list.layoutManager = GridLayoutManager(context, COLUMN_COUNT)
        quest_item_list.adapter = ChooserRecyclerViewAdapter(quizzesMap, onStartQuizListener)

    }

    interface OnStartQuizListener{
        fun onStartQuizSelected(quiz: SurveyItem, string:String)
    }

    companion object {
        private const val COLUMN_COUNT = 3
    }

}