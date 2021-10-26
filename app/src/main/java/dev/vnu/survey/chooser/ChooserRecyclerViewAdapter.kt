package dev.vnu.survey.chooser

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import dev.vnu.survey.R

/** adapter okna wyboru quizu
 * ciągle sortowanie mapy quizów
 * nie za bardzo mozna sortowac przy aktualizacji danych - zamierzone
 *
 */
class ChooserRecyclerViewAdapter(private val quizzesMap: HashMap<String, SurveyItem>,
                                 private val onStartquizListener: ChooserFragment.OnStartQuizListener) : RecyclerView.Adapter<ChooserRecyclerViewAdapter.ViewHolder>(){


    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_quizitem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount()= quizzesMap.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sorted = quizzesMap.values.toList().sortedBy {surveyItem ->( surveyItem.level.ordinal + surveyItem.lang.ordinal*10) }
        holder.mItem = sorted[position]

        holder.levelImageView.setImageResource(sorted[position].level.image)
        holder.langImageView.setImageResource(sorted[position].lang.image)
        holder.quizTitle.text = getDoubleLineQuizTitle(sorted,position)

            holder.mView.setOnClickListener{
                onStartquizListener.onStartQuizSelected(holder.mItem, getSingleLineQuizTitle(sorted,position))
            }
    }
    private fun getSingleLineQuizTitle(sorted: List<SurveyItem>, position: Int) = "${sorted[position].lang.getString()} \n ${sorted[position].level.getString()}"

    private fun getDoubleLineQuizTitle(sorted: List<SurveyItem>, position: Int) = "${sorted[position].lang.getString()} \n ${sorted[position].level.getString()}"




    inner class ViewHolder(val mView: View): RecyclerView.ViewHolder(mView){
        val levelImageView = mView.findViewById<View>(R.id.levelImageView) as ImageView
        val langImageView = mView.findViewById<View>(R.id.langImageView) as ImageView
        val quizTitle = mView.findViewById<View>(R.id.quizTitle) as TextView

        lateinit var  mItem: SurveyItem
    }
}