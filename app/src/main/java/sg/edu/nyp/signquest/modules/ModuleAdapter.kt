package sg.edu.nyp.signquest.modules

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_custom_dialog.view.*
import kotlinx.android.synthetic.main.module_card.view.*
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.`object`.Module

class ModuleAdapter(private val context: Context, private val datasource: ArrayList<Module>):
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

        return view
    }


}