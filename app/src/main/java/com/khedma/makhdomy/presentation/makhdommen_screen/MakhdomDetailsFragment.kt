package com.khedma.makhdomy.presentation.makhdommen_screen

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
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
import com.khedma.makhdomy.databinding.PhoneSelectionDialogBinding
import com.khedma.makhdomy.domain.model.Address
import com.khedma.makhdomy.domain.model.Makhdom
import com.khedma.makhdomy.presentation.utils.checkPhoneCallPermission
import com.khedma.makhdomy.presentation.utils.makePhoneCall
import com.khedma.makhdomy.presentation.utils.requestPermission
import com.khedma.makhdomy.presentation.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MakhdomDetailsFragment : Fragment(), PhoneSelectionAdapter.OnClickListener {

    private val binding by lazy { FragmentMakhdomDetailsBinding.inflate(layoutInflater) }
    private val viewModel: MakhdomViewModel by activityViewModels()
    private val args: MakhdomDetailsFragmentArgs by navArgs()
    private lateinit var makhdom: Makhdom
    private val phonesList: MutableList<Pair<String, String>> = mutableListOf()

    private val phonesAdapter by lazy {
        PhoneSelectionAdapter(
            requireContext(),
            phonesList,
            this
        )
    }
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
        setUpPhoneIconBtn()
        setUpMapImgButton()
        setUpUpdateBtn()
    }

    private fun presentMakhdomData() {
        viewModel.readMakhdomById(args.makhdomId).observe(requireActivity()) {
            it?.let {
                binding.makhdom = it
                binding.address = it.address
                makhdom = it
                setPhonesList(it.mobilePhones)
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


    private fun setPhonesList(phones: Map<String, String>?) {
        phones?.let {
            phonesList.clear()
            phonesList.addAll(it.toList())
            phonesAdapter.notifyDataSetChanged()
        }
    }


    private fun setUpPhoneIconBtn() {
        binding.phoneIcon.setOnClickListener {
            makhdom.mobilePhones?.let {
                showPhoneSelectionDialog()
            } ?: showToast(requireContext(), getString(R.string.not_phone_exist_label))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(requireActivity(), selectedPhone.second)
            }
        }
    }

    private fun showPhoneSelectionDialog() {

        val dialogBinding = PhoneSelectionDialogBinding.inflate(requireActivity().layoutInflater)
        dialogBinding.phonesGrid.adapter = phonesAdapter

        val dialog = AlertDialog.Builder(requireActivity()).apply {
            setView(dialogBinding.root)

        }.create()

        dialog.show()
    }

    private lateinit var selectedPhone: Pair<String, String>
    override fun onPhoneItemSelect(pair: Pair<String, String>) {
        selectedPhone = pair
        if (checkPhoneCallPermission(requireContext()))
            makePhoneCall(requireActivity(), pair.second)
        else
            requestPermission(this)

    }

}