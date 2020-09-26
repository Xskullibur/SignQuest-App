package sg.edu.nyp.signquest.utils

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.GameActivity
import sg.edu.nyp.signquest.game.gameobject.Gloss
import sg.edu.nyp.signquest.game.gameobject.Glossary
import sg.edu.nyp.signquest.game.gameobject.Module
import sg.edu.nyp.signquest.game.gameobject.Step
import sg.edu.nyp.signquest.game.view.ConfettiType
import sg.edu.nyp.signquest.game.view.CustomDialogFragment
import sg.edu.nyp.signquest.game.view.CustomPlayDialogFragment
import sg.edu.nyp.signquest.modules.MainModuleFragment
import sg.edu.nyp.signquest.modules.MainModuleFragmentDirections
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

    fun getCompletedGlossary(moduleId: String): CharArray? {
        val module = data.find { it.id == moduleId }
        if (module != null) {

            val glossList: ArrayList<Glossary> = ArrayList()

            val notCompletedSteps = module.steps.find { !it.completed }
            if (notCompletedSteps != null) {

                // Add Not Completed Gloss
                glossList.addAll(notCompletedSteps.glossary)

                // Add Completed Gloss
                val completedSteps = module.steps.filter { it.completed }
                completedSteps.forEach {
                    glossList.addAll(it.glossary)
                }

                return glossList.map { it.value.toCharArray()[0] }.toCharArray()

            }

        }

        return null
    }

    fun navigateToNext(moduleId: String, sender: Fragment, context: Context) {
        val (nextModule, nextStep, nextGloss) = ResourceManager.findNext(moduleId)

        if (nextGloss != null && nextStep != null) {
            // Show Next Gloss
            val action = MainModuleFragmentDirections.actionMainModuleFragmentToTutorialFragment(nextGloss, nextStep, moduleId)
            sender.findNavController().navigate(action)
        }
        else if (nextModule != null && nextModule.id == moduleId) {

            val glossary = ResourceManager.getCompletedGlossary(moduleId)
            if (glossary != null) {
                val fragmentManager = sender.requireActivity().supportFragmentManager.beginTransaction()
                val fragment = CustomPlayDialogFragment.newInstance(
                    title = "Quiz Time!",
                    subtitle = "${glossary.first()} - ${glossary.last()}"
                ){

                    // Navigate to Game
                    val intent = GameActivity.createActivityIntent(context, glossary)
                    context.startActivity(intent)
                    it.dismiss()
                }

                fragment.show(fragmentManager, CustomPlayDialogFragment.TAG)
            }

            sender.findNavController().popBackStack()
            sender.findNavController().popBackStack()

        }
        else {
            // Navigate to Menu
            sender.findNavController().popBackStack(R.id.action_startFragment_to_mainModuleFragment, false)
        }

    }

    fun findNext(moduleId: String): Triple<Module?, Step?, Glossary?> {
        val module = data.find { it.id == moduleId }
        val step = module?.steps?.find { !it.completed }
        val glossary = step?.glossary?.find { !it.completed }

        return Triple(module, step, glossary)
    }

}
