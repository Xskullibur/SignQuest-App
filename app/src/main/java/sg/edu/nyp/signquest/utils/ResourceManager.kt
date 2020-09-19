package sg.edu.nyp.signquest.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.gameobject.Module
import java.io.InputStreamReader

/**
 * Load resources from 'raw' resource folder
 */
object ResourceManager {

    lateinit var data: List<Module>

    fun loadResources(context: Context){
        // read your json file into an array
        InputStreamReader(context.resources.openRawResource(R.raw.modules)).use {
            val moduleListType = object : TypeToken<ArrayList<Module>>() {}.type
            data = Gson().fromJson(it, moduleListType)
        }
    }

}