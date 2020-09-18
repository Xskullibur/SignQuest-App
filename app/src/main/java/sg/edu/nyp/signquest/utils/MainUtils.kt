package sg.edu.nyp.signquest.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.`object`.Glossary
import sg.edu.nyp.signquest.game.`object`.Module
import sg.edu.nyp.signquest.game.`object`.Step
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList


class MainUtils(val context: Context) {

    companion object {
        lateinit var data: ArrayList<Module>
        fun findNext(moduleId: String): Triple<Module?, Step?, Glossary?> {
            val module = data.find { it.id == moduleId }
            val step = module?.steps?.find { !it.completed }
            val glossary = step?.glossary?.find { !it.completed }

            return Triple(module, step, glossary)
        }
    }

    init {
        // read your json file into an array
        val reader = InputStreamReader(context.resources.openRawResource(R.raw.modules))

        val moduleListType = object : TypeToken<ArrayList<Module>>() {}.type
        data = Gson().fromJson(reader, moduleListType)
    }

}