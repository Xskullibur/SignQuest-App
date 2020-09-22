package sg.edu.nyp.signquest.modules

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.module_card.view.*
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.GameActivity
import sg.edu.nyp.signquest.game.gameobject.Module
import sg.edu.nyp.signquest.utils.ResourceManager

class ModuleAdapter(private val context: Context, private val datasource: List<Module>):
    BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return datasource.size
    }

    override fun getItem(position: Int): Module {
        return datasource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = inflater.inflate(R.layout.module_card, parent, false)

        val module = getItem(position)

        view.moduleId.text = module.id
        view.moduleTitle.text = module.title
        view.moduleDesc.text = module.description

        view.progressBar.setSegmentCount(module.steps.count())
        view.progressBar.setCompletedSegments(
            module.steps.filter {
                it.completed
            }.count()
        )

        view.moduleCard.setOnClickListener{ view ->

            val (_, step, glossary) = ResourceManager.findNext(module.id)

            if (glossary != null) {
                if (step != null) {
                    val action = MainModuleFragmentDirections.actionMainModuleFragmentToTutorialFragment(glossary, step, module.id)
                    Navigation.findNavController(view).navigate(action)
                }
            }
            else {

                // Get glossary for game
                val glossList = ResourceManager.getCompletedGlossary(module.id)
                if (glossList != null) {
                    val intent = GameActivity.createActivityIntent(context, glossList)
                    context.startActivity(intent)
                }

            }

        }

        return view
    }


}