package sg.edu.nyp.signquest.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.`object`.Module
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList


class JsonUtils(val context: Context) {

    fun getModules(): ArrayList<Module> {

        // read your json file into an array
        val reader = InputStreamReader(context.resources.openRawResource(R.raw.modules))

        val moduleListType = object : TypeToken<ArrayList<Module>>() {}.type
        return Gson().fromJson<ArrayList<Module>>(reader, moduleListType)

    }

}