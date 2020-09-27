package sg.edu.nyp.signquest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import sg.edu.nyp.signquest.modules.MainModuleFragment
import sg.edu.nyp.signquest.tutorial.OnFinished
import sg.edu.nyp.signquest.tutorial.PracticeFragment
import sg.edu.nyp.signquest.utils.ResourceManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ResourceManager.loadResources(this)
    }
}