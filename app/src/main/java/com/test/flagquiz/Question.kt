package com.test.flagquiz

data class Question (

    //each question will require
    val id: Int,
    val question: String,
    val image: Int,
    val optionOne: String,
    val optionTwo: String,
    val optionThree: String,
    val optionFour: String,
    val correctionAnswer: Int
)