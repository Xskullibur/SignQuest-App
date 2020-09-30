package sg.edu.nyp.signquest.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import sg.edu.nyp.signquest.game.gameobject.Gloss
import sg.edu.nyp.signquest.game.gameobject.PlayerToSignWordQuestion

class PlayerToSignWordViewModel : GameExpandedAppBarViewModel() {

    val gloss: LiveData<Gloss?>
        get() = Transformations.map(question){
        (it as? PlayerToSignWordQuestion)?.glossToBeAnswered
    }

    var currentIndex = -1
    var numberOfCorrect = 0

    fun haveNextIndex(): Boolean{
        val glossLength = (question.value as PlayerToSignWordQuestion).glossToBeAnswered.value.length
        return currentIndex < glossLength - 1
    }

    fun nextIndex(){
        currentIndex++
    }

    private val _circleProgressBarState: MutableList<CircleProgressBarState> = mutableListOf()

    fun getCircleProgressBarStateForIndex(index: Int): CircleProgressBarState{
//        val glossLength = gloss.value!!.value.length
//        check(index > glossLength){"Index size of $index cannot be greater than gloss length of $glossLength"}
        return if(index < _circleProgressBarState.size){
            //Been created
            _circleProgressBarState[index]
        }else{
            //Create a new circle progress bar state
            val circleProgressBarState = CircleProgressBarState()
            _circleProgressBarState.add(circleProgressBarState)
            circleProgressBarState
        }
    }


}