package com.khedma.makhdomy.presentation.makhdommen_screen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.khedma.makhdomy.R
import com.khedma.makhdomy.databinding.FragmentMakhdommenListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MakhdommenListFragment : Fragment(), MakhdommenAdapter.OnItemClick {
    private val binding by lazy { FragmentMakhdommenListBinding.inflate(layoutInflater) }
    private val viewModel: MakhdomViewModel by activityViewModels()
    private val adapter: MakhdommenAdapter by lazy { MakhdommenAdapter(this) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
        observeMakhdommen()
    }

    private fun observeMakhdommen() {
        viewModel.makhdommen.observe(requireActivity()) {
            it?.let {
                Log.d("servants", it.map { it.id!!.toString() }.toString())
                adapter.submitList(it)
            }
        }
    }

    private fun setUpUi() {
        setUpRecyclerView()
        setUpAddMakhdomBtn()
    }

    private fun setUpRecyclerView() {
        binding.makhdommedRecyclerView.adapter = adapter
    }

    private fun setUpAddMakhdomBtn(){
        binding.addingMakhdomBtn.setOnClickListener {
            findNavController().navigate(R.id.action_makhdommenListFragment_to_basicDataMakhdomFragment)
        }
    }

    override fun onItemClick(id: Int) {
         findNavController().navigate(MakhdommenListFragmentDirections.actionMakhdommenListFragmentToMakhdomDetailsFragment(id)
             /*navOptions {
                 anim {
                     enter = android.R.animator.fade_out
                     exit = android.R.animator.fade_out
                 }
             }*/)
    }

}