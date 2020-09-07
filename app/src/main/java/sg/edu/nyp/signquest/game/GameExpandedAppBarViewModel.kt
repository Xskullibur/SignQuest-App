package sg.edu.nyp.signquest.game

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.lang.IllegalStateException

class GameExpandedAppBarViewModel: ViewModel()  {

    private val _currentMilliseconds: MutableLiveData<Long> = MutableLiveData(60)
    private val currentMilliseconds: LiveData<Long> get() = _currentMilliseconds
    val currentSecondsString = Transformations.map(currentMilliseconds){(it/1000).toString()}

    private var timer: CountDownTimer? = null
    private var gameCountDownTimer: GameCountDownTimer? = null

    val timerIsStarted get() = timer != null

    fun startCountDownTimer(totalMillisSeconds: Long, countDownInterval: Long){
        if(timerIsStarted) throw IllegalStateException("Timer has already been started")
        timer = object: CountDownTimer(totalMillisSeconds, countDownInterval){
            override fun onTick(millisUntilFinished: Long) {

                _currentMilliseconds.postValue(millisUntilFinished)

                gameCountDownTimer?.onTick(millisUntilFinished)
            }

            override fun onFinish() {
                gameCountDownTimer?.onFinish()
            }
        }.start()
    }

     fun resetCountDownTimer(){
        if(!timerIsStarted) throw IllegalStateException("Timer has not been started")
        timer!!.cancel()

        timer = null
    }

    fun setGameCountDownTimer(gameCountDownTimer: GameCountDownTimer){
        this.gameCountDownTimer = gameCountDownTimer
    }



}