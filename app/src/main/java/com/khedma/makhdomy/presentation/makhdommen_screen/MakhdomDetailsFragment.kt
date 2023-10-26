package com.khedma.makhdomy.presentation.makhdommen_screen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.khedma.makhdomy.R
import com.khedma.makhdomy.databinding.FragmentMakhdomDetailsBinding
import com.khedma.makhdomy.domain.model.Address
import com.khedma.makhdomy.domain.model.Makhdom
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MakhdomDetailsFragment : Fragment() {
    private val binding by lazy { FragmentMakhdomDetailsBinding.inflate(layoutInflater) }
    private val viewModel: MakhdomViewModel by activityViewModels()
    private val args: MakhdomDetailsFragmentArgs by navArgs()
    private lateinit var makhdom: Makhdom
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presentMakhdomData()
        setUpMapImgButton()
        setUpUpdateBtn()
    }

    private fun presentMakhdomData() {
        viewModel.readMakhdomById(args.makhdomId).observe(requireActivity()) {
            it?.let {
                binding.makhdom = it
                binding.address = it.address
                makhdom = it
            }
        }
    }

    private fun setUpUpdateBtn() {
        binding.updateBtn.setOnClickListener {
            findNavController().navigate(
                MakhdomDetailsFragmentDirections.actionMakhdomDetailsFragmentToBasicDataMakhdomFragment(
                    args.makhdomId
                )
            )
        }
    }

    private fun setUpMapImgButton() {
        binding.mapIcon.setOnClickListener {
            makhdom.address?.let {
                goToLocationOnMap(it, makhdom.name ?: "")
            } ?: kotlin.run {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.inavailable_msg), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun goToLocationOnMap(address: Address, makhdomName: String) {

        val myLatitude = address.lat
        val myLongitude = address.lng
        val labelLocation = makhdomName

        if (myLatitude == null || myLongitude == null) {
            Toast.makeText(
                requireContext(),
                getString(R.string.map_is_notavaialbe_msg),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val mapIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("geo:<$myLatitude>,<$myLongitude>?q=<$myLatitude>,<$myLongitude>($labelLocation)")
        );

        mapIntent.resolveActivity(requireActivity().packageManager)?.let {
            startActivity(mapIntent)
        }
    }

}