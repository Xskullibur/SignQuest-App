package sg.edu.nyp.signquest.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sg.edu.nyp.signquest.game.`object`.Gloss

class PlayerToSignViewModel : ViewModel() {

    private val _gloss = MutableLiveData<Gloss>()
    val gloss: LiveData<Gloss> get() = _gloss

    fun setGloss(gloss: Gloss){
        _gloss.value = gloss
    }


}