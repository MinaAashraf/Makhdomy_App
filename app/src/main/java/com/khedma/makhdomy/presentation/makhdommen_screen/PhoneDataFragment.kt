package com.khedma.makhdomy.presentation.makhdommen_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.khedma.makhdomy.R
import com.khedma.makhdomy.databinding.FragmentPhoneDataBinding
import com.khedma.makhdomy.presentation.utils.hide
import com.khedma.makhdomy.presentation.utils.show
import com.khedma.makhdomy.presentation.utils.showToast
import com.khedma.makhdomy.presentation.utils.validatePhoneNum
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PhoneDataFragment : Fragment(), PhonesAdapter.OnClickListener {

    private val binding by lazy { FragmentPhoneDataBinding.inflate(layoutInflater) }

    private val viewModel: MakhdomViewModel by activityViewModels()


    private val phonesAdapter by lazy {
        PhonesAdapter(
            requireContext(),
            viewModel.phonesList,
            this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    private fun setUpUi() {
        setUpSavingPhoneBtn()
        setUpNextBtn()
        setUpLastBtn()
        setUpPhonesGrid()
        handleSaveExitBtn()
    }


    private fun setUpPhonesGrid() {
        viewModel.preparedMakhdom.mobilePhones?.let {
            if (viewModel.phones == null) {
                viewModel.phones = mutableMapOf<String, String>().apply { this.putAll(it) }
                viewModel.phonesList.clear()
                viewModel.phonesList.addAll(it.toList())
            }
        }
        //  phones = viewModel.preparedMakhdom.mobilePhones
        /*     phones?.let {
                 phonesList.clear()
                 phonesList.addAll(it.toList())
             }*/
        binding.phonesGrid.isExpanded = true
        binding.phonesGrid.adapter = phonesAdapter
    }

    private fun setUpSavingPhoneBtn() {
        binding.savePhoneBtn.setOnClickListener {
            val phoneOwner = binding.phoneOwnerField.editText!!.text.toString()
            val phoneNum = binding.phoneField.editText!!.text.toString()
            if (!validatePhoneNum(binding.phoneField, requireContext()))
                return@setOnClickListener
            val key =
                if (phoneOwner.isEmpty()) "موبايل ${viewModel.phones?.size?.plus(1) ?: 1}" else phoneOwner

            viewModel.phones?.let {
                it[key] = phoneNum
            }
                ?: kotlin.run {
                    viewModel.phones =
                        mutableMapOf<String, String>().apply { this[key] = phoneNum }
                }
            viewModel.phonesList.add(Pair(key, phoneNum))
            phonesAdapter.notifyDataSetChanged()
            clearFields()
            showToast(requireContext(), getString(R.string.phone_added_successfully_msg))
        }
    }


    private fun setUpNextBtn() {
        binding.nextPageBtn.setOnClickListener {
            findNavController().navigate(R.id.action_phoneDataFragment_to_familyMakhdomFragment)
        }
    }

    private fun setUpLastBtn() {
        binding.lastPageBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun clearFields() {
        binding.phoneField.editText?.setText("")
        binding.phoneOwnerField.editText?.setText("")
    }

    override fun onRemoveClick(key: String, position: Int) {
        viewModel.phones?.remove(key)
        viewModel.phonesList.removeAt(position)
        phonesAdapter.notifyDataSetChanged()
    }

    private fun handleSaveExitBtn() {

        if (viewModel.updatingState)
            binding.saveExitBtn.show()
        else
            binding.saveExitBtn.hide()

        binding.saveExitBtn.setOnClickListener {
            viewModel.preparedMakhdom.mobilePhones = viewModel.phones
            viewModel.updateMakhdom()
            findNavController().popBackStack(R.id.makhdomDetailsFragment, false)
        }
    }

}