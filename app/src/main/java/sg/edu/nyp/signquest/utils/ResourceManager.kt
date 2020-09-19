package sg.edu.nyp.signquest.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.gameobject.Glossary
import sg.edu.nyp.signquest.game.gameobject.Module
import sg.edu.nyp.signquest.game.gameobject.Step
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

    fun findNext(moduleId: String): Triple<Module?, Step?, Glossary?> {
        val module = data.find { it.id == moduleId }
        val step = module?.steps?.find { !it.completed }
        val glossary = step?.glossary?.find { !it.completed }

        return Triple(module, step, glossary)
    }

}