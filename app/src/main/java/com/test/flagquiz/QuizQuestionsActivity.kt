package com.test.flagquiz

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_quiz_questions.*

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {
    private var mCurrentPosition: Int = 1
    private var mQuestionsList: ArrayList<Question>? = null //not initialised
    private var mSelectedOptionPosition: Int = 0
    private var mCorrectAnswers: Int = 0
    private var mUserName: String ? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        mUserName = intent.getStringExtra(Constants.USER_NAME)//get name from mainActivity

        mQuestionsList = Constants.getQuetions()

        setQuestion()

        //wait for click
        tv_option_one.setOnClickListener(this)
        tv_option_two.setOnClickListener(this)
        tv_option_three.setOnClickListener(this)
        tv_option_four.setOnClickListener(this)
        btn_submit.setOnClickListener(this)
    }

    private fun setQuestion(){
        val question = mQuestionsList!!.get(mCurrentPosition -1)

        defaultOptionsView()//reset view

        //Check if at the end of quiz
        if(mCurrentPosition == mQuestionsList!!.size){
            btn_submit.text = "Finish"
        }else{
            btn_submit.text = "Submit"
        }

        //assign the progress bar value
        progressBar.progress = mCurrentPosition
        tv_progress.text = "$mCurrentPosition" + "/" + progressBar.max

        //set the question text
        tv_question.text = question!!.question

        //set image
        iv_image.setImageResource(question.image)

        //set option text
        tv_option_one.text = question.optionOne
        tv_option_two.text = question.optionTwo
        tv_option_three.text = question.optionThree
        tv_option_four.text = question.optionFour
    }

    private fun defaultOptionsView(){
        val options = ArrayList<TextView>()

        options.add(0, tv_option_one)
        options.add(1, tv_option_two)
        options.add(2, tv_option_three)
        options.add(3, tv_option_four)

        for (option in options){
            //set default look
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this, R.drawable.default_option_border_bg
            )
        }

    }


    //once option clicked do
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_option_one ->{selectedOptionView(tv_option_one, 1)}
            R.id.tv_option_two ->{selectedOptionView(tv_option_two, 2)}
            R.id.tv_option_three ->{selectedOptionView(tv_option_three, 3)}
            R.id.tv_option_four ->{selectedOptionView(tv_option_four, 4)}
            R.id.btn_submit ->{
                if(mSelectedOptionPosition == 0){
                    mCurrentPosition++ //if 0 move to next Q

                    when{
                        mCurrentPosition <= mQuestionsList!!.size -> {
                            setQuestion()
                        }else ->{
                        val intent = Intent(this, ResultActivity::class.java )
                        //pass on info to next activity (result)
                        intent.putExtra(Constants.USER_NAME, mUserName)
                        intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                        intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionsList!!.size)

                        startActivity(intent)
                        finish()//close activity
                    }
                    }

                 //assign the colours
                }else{
                    val question = mQuestionsList?.get(mCurrentPosition -1)
                    if(question!!.correctionAnswer != mSelectedOptionPosition){
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                    }else{
                        mCorrectAnswers++ //increase count of correct answers
                    }
                    answerView(question.correctionAnswer, R.drawable.correct_option_border_bg)

                    //When at last question change text of button
                    if(mCurrentPosition == mQuestionsList!!.size){
                        btn_submit.text = "Finish"
                    }else{
                        btn_submit.text = "Go to next question"
                    }
                    mSelectedOptionPosition = 0 //move to next Q
                }
            }
        }
    }

    //assign the right colours to answers
    private fun answerView(answer: Int, drawableView: Int){
        when(answer){
            1 -> {tv_option_one.background = ContextCompat.getDrawable(this, drawableView)}
            2 -> {tv_option_two.background = ContextCompat.getDrawable(this, drawableView)}
            3 -> {tv_option_three.background = ContextCompat.getDrawable(this, drawableView)}
            4 -> {tv_option_four.background = ContextCompat.getDrawable(this, drawableView)}
        }
    }

    private fun selectedOptionView(tv: TextView, selectedOptionNum: Int){
        defaultOptionsView()//reset to default view

        mSelectedOptionPosition = selectedOptionNum

        //change to selected view
        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this, R.drawable.selected_option_border_bg
        )

    }

}