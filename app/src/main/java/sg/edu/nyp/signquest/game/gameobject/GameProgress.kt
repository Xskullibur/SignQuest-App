package sg.edu.nyp.signquest.game.gameobject

import java.io.Serializable

class GameProgress: Serializable {
    //List of questions available
    lateinit var questions: List<Question>

    //The index of where the current question is being answered
    var currentlyQuestionIndex: Int = 0
        private set

    //Returns the current question that is being answered
    val currentQuestion: Question
        get() = questions.elementAt(currentlyQuestionIndex)

    //Returns the next question to be answered, if the current question is the last question, this
    //function will return null
    val nextQuestion: Question?
        get() = if(questions.size > currentlyQuestionIndex)
            questions.elementAt(currentlyQuestionIndex+1)
        else null

    //Returns the total amount of questions available
    val totalAmountOfQuestion get() = questions.count()

    var score: Int = 0

    constructor(questions: List<Question>){
        this.questions = questions
    }

//    constructor(parcel: Parcel){
//        val questions = mutableListOf<Question>()
//        parcel.readList(questions, Question::class.java.classLoader)
//        this.questions = questions
//        currentlyProcessIndex = parcel.readInt()
//    }

    /**
     * Move on to the next question
     */
    fun nextQuestion(): Boolean{
        return if(totalAmountOfQuestion-1 > currentlyQuestionIndex){
            currentlyQuestionIndex++
            true
        }else{
            false
        }
    }

//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeList(questions)
//        parcel.writeInt(currentlyProcessIndex)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<GameProgress> {
//        override fun createFromParcel(parcel: Parcel): GameProgress {
//            return GameProgress(parcel)
//        }
//
//        override fun newArray(size: Int): Array<GameProgress?> {
//            return arrayOfNulls(size)
//        }
//    }

}