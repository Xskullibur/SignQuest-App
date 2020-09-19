package sg.edu.nyp.signquest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import sg.edu.nyp.signquest.utils.ResourceManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ResourceManager.loadResources(this)
    }
}