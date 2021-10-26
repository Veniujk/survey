package dev.vnu.survey.quiz

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import dev.vnu.survey.BaseActivity
import dev.vnu.survey.R
import dev.vnu.survey.chooser.LangEnum
import dev.vnu.survey.chooser.LevelEnum
import dev.vnu.survey.chooser.SurveyItem
import kotlinx.android.synthetic.main.fragment_newsurvey.*

class NewSurvey: BaseActivity() {
    lateinit var questset: EditText
    lateinit var question1: EditText
    lateinit var q1: EditText
    lateinit var q2: EditText
    lateinit var q3: EditText
    lateinit var question2: EditText
    lateinit var q21: EditText
    lateinit var q22: EditText
    lateinit var q23: EditText
    lateinit var question3: EditText
    lateinit var q31: EditText
    lateinit var q32: EditText
    lateinit var q33: EditText
    lateinit var question4: EditText
    lateinit var q41: EditText
    lateinit var q42: EditText
    lateinit var q43: EditText
    lateinit var question5: EditText
    lateinit var q51: EditText
    lateinit var q52: EditText
    lateinit var q53: EditText
    lateinit var next1: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_newsurvey)
        questset = findViewById(R.id.questset)
        question1 = findViewById(R.id.question1)
        q1 = findViewById(R.id.q1)
        q2 = findViewById(R.id.q2)
        q3 = findViewById(R.id.q3)
        question2 = findViewById(R.id.question2)
        q21 = findViewById(R.id.q21)
        q22 = findViewById(R.id.q22)
        q23 = findViewById(R.id.q23)
        question3 = findViewById(R.id.question3)
        q31 = findViewById(R.id.q31)
        q32 = findViewById(R.id.q32)
        q33 = findViewById(R.id.q33)
        question4 = findViewById(R.id.question4)
        q41 = findViewById(R.id.q41)
        q42 = findViewById(R.id.q42)
        q43 = findViewById(R.id.q43)
        question5 = findViewById(R.id.question5)
        q51 = findViewById(R.id.q51)
        q52 = findViewById(R.id.q52)
        q53 = findViewById(R.id.q53)
        next1 = findViewById(R.id.next1)

        next1.setOnClickListener{
            saveIcon()
            saveSurvey1()
            saveSurvey2()
            saveSurvey3()
            saveSurvey4()
            saveSurvey5()


        }
    }
    private fun saveIcon(){

        val questset = questset.text.toString().trim()
        var level: LevelEnum = LevelEnum.HARD
        var lang: LangEnum = LangEnum.ANDROID

        val NewIconRef = FirebaseDatabase.getInstance().getReference("quizzes/")
        val sid = NewIconRef.push().key
        val addIcon = SurveyItem(level, lang, questset)
        NewIconRef.child(sid.toString()).setValue(addIcon).addOnCompleteListener{}
    }
    private fun saveSurvey1(){
        val questset = questset.text.toString().trim()
        val ask = question1.text.toString().trim()
        if(ask.isEmpty()){
            question1.error= "Proszę wpisać pytanie!!!"
            return
        }
        val positive = q1.text.toString().trim()
        if(positive.isEmpty()){
            q1.error= "Proszę uzupełnić odpowiedź!!!"
            return
        }
        val false1 = q2.text.toString().trim()
        if(false1.isEmpty()){
            q2.error= "Proszę uzupełnić odpowiedź!!!"
            return
        }
        val false2 = q3.text.toString().trim()

        if(false2.isEmpty()){
            q3.error= "Proszę uzupełnić odpowiedź!!!"
            return
        }


        val NewSurveyRef = FirebaseDatabase.getInstance().getReference().child("questions").child(questset).child("quest1")
        val qid = NewSurveyRef.push().key
        val addquestion = QuestionItem(ask, positive, false1, false2)

        NewSurveyRef.setValue(addquestion).addOnCompleteListener{
            Toast.makeText(applicationContext,"Ankieta prawidłowo dodana", Toast.LENGTH_LONG).show()
        }
    }

private fun saveSurvey2(){
    val questset = questset.text.toString().trim()
    val ask = question2.text.toString().trim()
    if(ask.isEmpty()){
        question1.error= "Proszę wpisać pytanie!!!"
        return
    }
    val positive = q21.text.toString().trim()
    if(positive.isEmpty()){
        q21.error= "Proszę uzupełnić odpowiedź!!!"
        return
    }
    val false1 = q22.text.toString().trim()
    if(false1.isEmpty()){
        q22.error= "Proszę uzupełnić odpowiedź!!!"
        return
    }
    val false2 = q23.text.toString().trim()

    if(false2.isEmpty()){
        q23.error= "Proszę uzupełnić odpowiedź!!!"
        return
    }

    val NewSurveyRef = FirebaseDatabase.getInstance().getReference("questions/" + questset + "/quest2")

    val qid = NewSurveyRef.push().key
    val addquestion = QuestionItem(ask, positive, false1, false2)

    NewSurveyRef.setValue(addquestion).addOnCompleteListener{
        Toast.makeText(applicationContext,"Ankieta prawidłowo dodana", Toast.LENGTH_LONG).show()
    }
}
    private fun saveSurvey3(){
        val questset = questset.text.toString().trim()
        val ask = question3.text.toString().trim()
        if(ask.isEmpty()){
            question3.error= "Proszę wpisać pytanie!!!"
            return
        }
        val positive = q31.text.toString().trim()
        if(positive.isEmpty()){
            q31.error= "Proszę uzupełnić odpowiedź!!!"
            return
        }
        val false1 = q32.text.toString().trim()
        if(false1.isEmpty()){
            q32.error= "Proszę uzupełnić odpowiedź!!!"
            return
        }
        val false2 = q33.text.toString().trim()

        if(false2.isEmpty()){
            q33.error= "Proszę uzupełnić odpowiedź!!!"
            return
        }

        val NewSurveyRef = FirebaseDatabase.getInstance().getReference("questions/" + questset + "/quest3")

        val qid = NewSurveyRef.push().key
        val addquestion = QuestionItem(ask, positive, false1, false2)

        NewSurveyRef.setValue(addquestion).addOnCompleteListener{
            Toast.makeText(applicationContext,"Ankieta prawidłowo dodana", Toast.LENGTH_LONG).show()
        }
    }
    private fun saveSurvey4(){
        val questset = questset.text.toString().trim()
        val ask = question4.text.toString().trim()
        if(ask.isEmpty()){
            question4.error= "Proszę wpisać pytanie!!!"
            return
        }
        val positive = q41.text.toString().trim()
        if(positive.isEmpty()){
            q41.error= "Proszę uzupełnić odpowiedź!!!"
            return
        }
        val false1 = q42.text.toString().trim()
        if(false1.isEmpty()){
            q42.error= "Proszę uzupełnić odpowiedź!!!"
            return
        }
        val false2 = q43.text.toString().trim()

        if(false2.isEmpty()){
            q43.error= "Proszę uzupełnić odpowiedź!!!"
            return
        }

        val NewSurveyRef = FirebaseDatabase.getInstance().getReference("questions/" + questset + "/quest4")

        val qid = NewSurveyRef.push().key
        val addquestion = QuestionItem(ask, positive, false1, false2)

        NewSurveyRef.setValue(addquestion).addOnCompleteListener{
            Toast.makeText(applicationContext,"Ankieta prawidłowo dodana", Toast.LENGTH_LONG).show()
        }
    }
    private fun saveSurvey5(){
        val questset = questset.text.toString().trim()
        val ask = question5.text.toString().trim()
        if(ask.isEmpty()){
            question5.error= "Proszę wpisać pytanie!!!"
            return
        }
        val positive = q51.text.toString().trim()
        if(positive.isEmpty()){
            q51.error= "Proszę uzupełnić odpowiedź!!!"
            return
        }
        val false1 = q52.text.toString().trim()
        if(false1.isEmpty()){
            q52.error= "Proszę uzupełnić odpowiedź!!!"
            return
        }
        val false2 = q53.text.toString().trim()

        if(false2.isEmpty()){
            q53.error= "Proszę uzupełnić odpowiedź!!!"
            return
        }

        val NewSurveyRef = FirebaseDatabase.getInstance().getReference("questions/" + questset + "/quest5")

        val qid = NewSurveyRef.push().key
        val addquestion = QuestionItem(ask, positive, false1, false2)

        NewSurveyRef.setValue(addquestion).addOnCompleteListener{
            Toast.makeText(applicationContext,"Ankieta prawidłowo dodana", Toast.LENGTH_LONG).show()
        }
    }

}



