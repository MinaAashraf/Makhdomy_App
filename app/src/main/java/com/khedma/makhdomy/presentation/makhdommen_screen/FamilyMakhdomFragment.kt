package com.khedma.makhdomy.presentation.makhdommen_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.khedma.makhdomy.R
import com.khedma.makhdomy.databinding.FragmentFamilyMakhdomBinding
import com.khedma.makhdomy.presentation.utils.hide
import com.khedma.makhdomy.presentation.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FamilyMakhdomFragment : Fragment() {

    private val binding by lazy { FragmentFamilyMakhdomBinding.inflate(layoutInflater) }
    private val viewModel: MakhdomViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().title = getString(R.string.family_toolbar_title)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.makhdom = viewModel.preparedMakhdom
        setUpUi()
    }

    private fun setUpUi() {
        setUpLifeLevelsDropDownList()
        setUpNextBtn()
        setUpLastBtn()
        handleSaveExitBtn()
    }

    private fun setUpNextBtn() {
        binding.nextPageBtn.setOnClickListener {
            saveFamilyData()
            findNavController().navigate(R.id.action_familyMakhdomFragment_to_brothersDataFragment)
        }
    }

    private fun saveFamilyData() {
        val fatherJob = binding.fatherJobField.editText!!.text.toString()
        val motherJob = binding.motherJobField.editText!!.text.toString()
        val familyLifeLevel = binding.familyLifeLevelField.editText!!.text.toString()
        val familyLastVisitAttitude = binding.familyLastVisitAttitude.editText!!.text.toString()
        val familyChurchConnection = binding.familyChurchConnection.editText!!.text.toString()
        val familyNotes = binding.familyNotesField.editText!!.text.toString()

        viewModel.preparedMakhdom.apply {
            this.fatherJob = fatherJob
            this.motherJob = motherJob
            this.familyLifeLevel = familyLifeLevel
            this.familyLastVisitAttitude = familyLastVisitAttitude
            this.familyChurchConnection = familyChurchConnection
            this.familyNotes = familyNotes
        }
    }


    private fun setUpLastBtn() {
        binding.lastPageBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setUpLifeLevelsDropDownList() {
        val dropDownAdapter = ArrayAdapter(
            requireActivity(),
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.life_levels)
        )
        binding.familyLifeLevelAutoComplete.setAdapter(dropDownAdapter)
    }

    private fun handleSaveExitBtn() {
        if (viewModel.updatingState)
            binding.saveExitBtn.show()
        else
            binding.saveExitBtn.hide()

        binding.saveExitBtn.setOnClickListener {
            saveFamilyData()
            viewModel.updateMakhdom()
            findNavController().popBackStack(R.id.makhdomDetailsFragment, false)
        }
    }


}