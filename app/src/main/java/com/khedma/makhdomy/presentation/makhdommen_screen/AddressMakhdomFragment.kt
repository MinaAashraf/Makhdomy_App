package com.khedma.makhdomy.presentation.makhdommen_screen

import android.Manifest
import android.app.Activity
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.khedma.makhdomy.R
import com.khedma.makhdomy.databinding.BottomSheetLayoutBinding
import com.khedma.makhdomy.databinding.FragmentAddressMakhdomBinding
import com.khedma.makhdomy.domain.model.Address
import com.khedma.makhdomy.presentation.utils.hide
import com.khedma.makhdomy.presentation.utils.show
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddressMakhdomFragment : Fragment() {

    private val binding by lazy { FragmentAddressMakhdomBinding.inflate(layoutInflater) }

    private val viewModel: MakhdomViewModel by activityViewModels()

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var locationSettingsBuilder: LocationSettingsRequest.Builder

    @Inject
    lateinit var locationSettingsClient: SettingsClient

    private var isGpsOpen = false

    private var addressLat: String? = null
    private var addressLng: String? = null

    private val resolutionForResult = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                isGpsOpen = true
                getCurrentLocation()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.preparedMakhdom.address.apply {
            this?.let {
                binding.address = it
                if (it.lat != null && it.lng != null)
                    binding.getLocationAxisBtn.text = "${it.lat} - ${it.lng}"
            }
        }
        setUpUi()
    }

    private fun setUpUi() {
        setUpNextBtn()
        setUpLastBtn()
        handleAddingLocationLatLngBtn()
        handleSaveExitBtn()
    }

    private fun handleAddingLocationLatLngBtn() {
        binding.getLocationAxisBtn.setOnClickListener {
            showBottomSheetDialog()
        }
    }

    private fun setUpNextBtn() {
        binding.nextPageBtn.setOnClickListener {
            saveAddressData()
            findNavController().navigate(R.id.action_addressMakhdomFragment_to_phoneDataFragment)
        }
    }

    private fun saveAddressData() {
        val homeNum = binding.homeNumField.editText!!.text.toString().ifEmpty { null }
        val floorNum = binding.floorNumField.editText!!.text.toString().ifEmpty { null }
        val apartmentNum = binding.apartmentNumField.editText!!.text.toString().ifEmpty { null }
        val areaName = binding.areaNameField.editText!!.text.toString().ifEmpty { null }
        val streetName = binding.streetNameField.editText!!.text.toString().ifEmpty { null }
        val motfare3From = binding.motfare3StreetField.editText!!.text.toString().ifEmpty { null }
        val anotherAddressData =
            binding.otherAddressDetailsField.editText!!.text.toString().ifEmpty { null }

        val address = Address(
            homeNum = homeNum,
            floorNum = floorNum,
            apartmentNum = apartmentNum,
            areaName = areaName,
            streetName = streetName,
            motafre3From = motfare3From,
            anotherAddressData = anotherAddressData,
            lat = addressLat,
            lng = addressLng
        )

        viewModel.preparedMakhdom.apply {
            this.address = address
            this.addressArea = areaName
            this.streetName = streetName
            this.motafare3From = motfare3From
        }
    }

    private fun setUpLastBtn() {
        binding.lastPageBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun handleSaveExitBtn() {
        if (viewModel.updatingState)
            binding.saveExitBtn.show()
        else
            binding.saveExitBtn.hide()

        binding.saveExitBtn.setOnClickListener {
            saveAddressData()
            viewModel.updateMakhdom()
            findNavController().popBackStack(R.id.makhdomDetailsFragment, false)
        }
    }


    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            getCurrentLocation()
        }

    private fun requestLocationPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }


    private fun getCurrentLocation() {
        if (checkLocationPermission()) {
            if (!isGpsOpen) {
                checkGps(
                    resolutionForResult = resolutionForResult,
                    locationSettingsBuilder = locationSettingsBuilder,
                    locationSettingsClient = locationSettingsClient
                )
            } else {
                fusedLocationProviderClient.getCurrentLocation(
                    LocationRequest.PRIORITY_HIGH_ACCURACY,
                    object : CancellationToken() {
                        override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken =
                            CancellationTokenSource().token

                        override fun isCancellationRequested(): Boolean = false
                    }).addOnSuccessListener {
                    it?.let {
                        bottomSheetLayoutBinding.apply {
                            latTextField.editText!!.setText(it.latitude.toString())
                            lngTextField.editText!!.setText(it.longitude.toString())
                        }
                    } ?: run { Log.d("lat lng", "null location") }
                }

            }
        } else
            requestLocationPermission()
    }


    private fun checkGps(
        resolutionForResult: ActivityResultLauncher<IntentSenderRequest>,
        locationSettingsBuilder: LocationSettingsRequest.Builder,
        locationSettingsClient: SettingsClient
    ) {
        val task: Task<LocationSettingsResponse> =
            locationSettingsClient.checkLocationSettings(locationSettingsBuilder.build())
        task.addOnSuccessListener {
            isGpsOpen = true
            getCurrentLocation()
        }
        // if gps is closed -> display request dialogue to open gps
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    val intentSenderRequest = IntentSenderRequest
                        .Builder(exception.resolution).build()
                    resolutionForResult.launch(intentSenderRequest)
                } catch (sendEx: IntentSender.SendIntentException) {
                }
            }
        }
    }

    private lateinit var bottomSheet: BottomSheetDialog
    private lateinit var bottomSheetLayoutBinding: BottomSheetLayoutBinding
    private fun showBottomSheetDialog() {
        bottomSheetLayoutBinding = BottomSheetLayoutBinding.inflate(layoutInflater)
        bottomSheet = BottomSheetDialog(requireContext()).apply {
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setContentView(bottomSheetLayoutBinding.root)
            show()
        }
        handleBottomSheetInputs()

    }

    private fun hideBottomSheet() {
        bottomSheet.hide()
    }

    private fun handleBottomSheetInputs() {

        bottomSheetLayoutBinding.automaticGpsBtn.setOnClickListener {
            getCurrentLocation()
        }
        bottomSheetLayoutBinding.apply {

            setLatLngInitialValueIfExist()

            saveBtn.setOnClickListener {
                if (!verifyBottomSheetInputs())
                    return@setOnClickListener
                else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.location_added_succesfully_msg),
                        Toast.LENGTH_SHORT
                    ).show()
                    saveBottomSheetInputsLatLngTemporally()
                    hideBottomSheet()
                }
            }

        }
    }

    private fun saveBottomSheetInputsLatLngTemporally() {
        addressLat = bottomSheetLayoutBinding.latTextField.editText!!.text.toString()
        addressLng = bottomSheetLayoutBinding.lngTextField.editText!!.text.toString()
        binding.getLocationAxisBtn.text = "$addressLat - $addressLng"
    }

    private fun setLatLngInitialValueIfExist() {
        bottomSheetLayoutBinding.latTextField.editText!!.setText(
            addressLat ?: viewModel.preparedMakhdom.address?.lat
        )
        bottomSheetLayoutBinding.lngTextField.editText!!.setText(
            addressLat ?: viewModel.preparedMakhdom.address?.lng
        )
    }

    private fun verifyBottomSheetInputs(): Boolean {
        var isVerified = true
        if (bottomSheetLayoutBinding.latTextField.editText!!.text.isEmpty()) {
            isVerified = false
            bottomSheetLayoutBinding.latTextField.helperText = getString(R.string.latitude_err_msg)
        }
        if (bottomSheetLayoutBinding.lngTextField.editText!!.text.isEmpty()) {
            isVerified = false
            bottomSheetLayoutBinding.lngTextField.helperText = getString(R.string.latitude_err_msg)
        }
        return isVerified
    }

}
