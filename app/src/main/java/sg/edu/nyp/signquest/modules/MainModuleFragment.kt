package sg.edu.nyp.signquest.modules

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_main_module.*
import sg.edu.nyp.signquest.R
import sg.edu.nyp.signquest.game.ARGS_GAME_MODULE_ID
import sg.edu.nyp.signquest.game.GameActivity
import sg.edu.nyp.signquest.game.NEXT
import sg.edu.nyp.signquest.game.gameobject.Module
import sg.edu.nyp.signquest.game.view.CustomPlayDialogFragment
import sg.edu.nyp.signquest.tutorial.ModuleViewModel
import sg.edu.nyp.signquest.tutorial.OnFinished
import sg.edu.nyp.signquest.tutorial.PracticeFragment
import sg.edu.nyp.signquest.utils.ResourceManager


/**
 * A simple [Fragment] subclass.
 * Use the [MainModuleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainModuleFragment : Fragment(), ModuleAdapter.OnSelectedListener, OnFinished {

    private lateinit var moduleAdapter: ModuleAdapter

    private lateinit var startActivityForResult: ActivityResultLauncher<Intent>

    private val viewModel: ModuleViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.listener = this

        startActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val intent = it.data
                val value = intent?.getBooleanExtra(NEXT, false)
                val moduleId = intent?.getStringExtra(ARGS_GAME_MODULE_ID)

                if(value != null && value && moduleId != null){
                    val module = ResourceManager.getModule(moduleId)
                    next(module)
                }

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moduleAdapter = ModuleAdapter(this.requireContext(), ResourceManager.data)
        moduleAdapter.listener = this
        moduleList.adapter = moduleAdapter

        mainModule_topAppBar.setNavigationOnClickListener {
            it.findNavController().popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_module, container, false)
    }

    private fun next(module: Module) {
        if (!module.isCompleted) {
            val step = module.nextIncompleteStep()
            val gloss = step?.nextIncompleteGloss()

            if (step != null && gloss != null) {
                // Navigate to Tutorial
                val action =
                    MainModuleFragmentDirections.actionMainModuleFragmentToTutorialFragment(
                        gloss,
                        step,
                        module.id
                    )
                findNavController().navigate(action)
            } else {
                // Navigate to Quiz
                /// Get glossary for game
                val glossList = ResourceManager.getCompletedGlossary(module.id)

                if (glossList != null) {

                    val fragmentManager = this.requireActivity().supportFragmentManager.beginTransaction()
                    val fragment = CustomPlayDialogFragment.newInstance(
                        title = "Quiz Time!",
                        subtitle = "${glossList.first()} - ${glossList.last()}"
                    ){ dialog ->

                        val intent =
                            GameActivity.createActivityIntent(this.requireContext(), glossList, module.id, true)
                        startActivityForResult.launch(intent)
                        dialog.dismiss()
                    }

                    fragment.show(fragmentManager, CustomPlayDialogFragment.TAG)
                }
            }
        }
    }

    override fun finish(module: Module) {
        next(module)
    }

    override fun onSelect(module: Module) {
        next(module)
    }

}