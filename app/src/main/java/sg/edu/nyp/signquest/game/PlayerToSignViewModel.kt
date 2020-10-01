package sg.edu.nyp.signquest.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import sg.edu.nyp.signquest.game.gameobject.Gloss
import sg.edu.nyp.signquest.game.gameobject.PlayerToSignQuestion

class PlayerToSignViewModel : GameExpandedAppBarViewModel() {
    val gloss: LiveData<Gloss?> get() = Transformations.map(question){
        (it as? PlayerToSignQuestion)?.glossToBeAnswered
    }
    var completed = false
}