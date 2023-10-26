package com.khedma.makhdomy.presentation.makhdommen_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.khedma.makhdomy.R
import com.khedma.makhdomy.databinding.FragmentMakhdommenListBinding
import com.khedma.makhdomy.presentation.utils.hide
import com.khedma.makhdomy.presentation.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MakhdommenListFragment : Fragment(), MakhdommenAdapter.OnItemClick {
    private val binding by lazy { FragmentMakhdommenListBinding.inflate(layoutInflater) }
    private val viewModel: MakhdomViewModel by activityViewModels()
    private val adapter: MakhdommenAdapter by lazy { MakhdommenAdapter(this) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
        observeMakhdommen()
    }

    private fun observeMakhdommen() {
        viewModel.makhdommen.observe(requireActivity()) {
            it?.let {
                adapter.submitList(it)
                if (it.isEmpty())
                    binding.emptyIcon.show()
                else
                    binding.emptyIcon.hide()
            }
        }
    }

    private fun setUpUi() {
        setUpRecyclerView()
        setUpSearchView()
        setUpAddMakhdomBtn()
        handleRecyclerViewScrolling()
    }

    private fun setUpRecyclerView() {
        binding.makhdommedRecyclerView.adapter = adapter
    }

    private fun setUpAddMakhdomBtn() {
        binding.addingMakhdomBtn.setOnClickListener {
            findNavController().navigate(R.id.action_makhdommenListFragment_to_basicDataMakhdomFragment)
        }
    }

    override fun onItemClick(id: Int) {
        findNavController().navigate(
            MakhdommenListFragmentDirections.actionMakhdommenListFragmentToMakhdomDetailsFragment(id)
            /*navOptions {
                anim {
                    enter = android.R.animator.fade_out
                    exit = android.R.animator.fade_out
                }
            }*/
        )
    }

    private fun handleRecyclerViewScrolling() {
        binding.makhdommedRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && binding.addingMakhdomBtn.visibility == View.VISIBLE)
                    binding.addingMakhdomBtn.visibility = View.GONE
                else if (dy <= 0 && binding.addingMakhdomBtn.visibility == View.GONE)
                    binding.addingMakhdomBtn.visibility = View.VISIBLE
            }
        })
    }

    private fun setUpSearchView() {
        binding.searchView.onActionViewExpanded()
        binding.searchView.clearFocus()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean = false

            override fun onQueryTextChange(keyWord: String?): Boolean {
                keyWord?.let {
                    viewModel.search(it)
                }
                return true
            }

        })
    }

}