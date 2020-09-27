package sg.edu.nyp.signquest.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import sg.edu.nyp.signquest.game.gameobject.Gloss
import sg.edu.nyp.signquest.game.gameobject.MCQQuestion

class MCQQuestionViewModel: GameExpandedAppBarViewModel()  {

    val mcqQuestion: LiveData<MCQQuestion?> = Transformations.map(question){
        it as? MCQQuestion
    }

    val glossToBeAnswered: LiveData<Gloss> =
        Transformations.map(question) {
            it.glossToBeAnswered
        }
    val otherGlossaryChoice: LiveData<Set<Gloss>?> = Transformations.map(mcqQuestion){
        it?.otherGlossaryChoice
    }



}