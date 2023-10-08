package com.khedma.makhdomy.presentation.makhdommen_screen

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.khedma.makhdomy.R
import com.khedma.makhdomy.databinding.FragmentBasicDataMakhdomBinding
import com.khedma.makhdomy.domain.model.Makhdom
import com.khedma.makhdomy.presentation.hide
import com.khedma.makhdomy.presentation.show
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BasicDataMakhdomFragment : Fragment() {

    private val binding by lazy { FragmentBasicDataMakhdomBinding.inflate(layoutInflater) }

    private val viewModel: MakhdomViewModel by activityViewModels()

    private val args: BasicDataMakhdomFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        handleMakhdomInitialValueIFExist()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpPictureImage()
        setUpNextBtn()
        setUpUi()
    }

    private fun setUpUi() {
        setUpPictureImage()
        setUpNextBtn()
        handleSaveExitBtn()
    }

    private fun setUpNextBtn() {
        binding.nextPageBtn.setOnClickListener {
            saveBasicData()
            findNavController().navigate(R.id.action_basicDataMakhdomFragment_to_addressMakhdomFragment)
        }
    }

    private fun saveBasicData() {
        val name = binding.nameTextField.editText!!.text.toString()
        val date = binding.birthDateField.editText!!.text.toString()
        val birthLocation = binding.birthLocationField.editText!!.text.toString()
        viewModel.preparedMakhdom.apply {
            this.name = name
            this.birthDate = date
            this.birthLocation = birthLocation
        }
    }

    private fun setUpPictureImage() {
        binding.pictureView.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                binding.pictureView.setImageURI(it)
                viewModel.preparedMakhdom.picture = getBitmapFromUri(it)
            }
        }

    private fun getBitmapFromUri(imageUri: Uri): Bitmap? {
        var bitmap: Bitmap? = null
        val contentResolver: ContentResolver = requireActivity().contentResolver
        try {
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            } else {
                val source = ImageDecoder.createSource(contentResolver, imageUri)
                ImageDecoder.decodeBitmap(source)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }

    private fun handleMakhdomInitialValueIFExist() {
        if (viewModel.updatingState) {
            binding.makhdom = viewModel.preparedMakhdom
            return
        }
        if (args.makhdomId != -1) {
            viewModel.readMakhdomById(args.makhdomId).observe(requireActivity()) {
                it?.let {
                    viewModel.updatingState = true
                    viewModel.preparedMakhdom = it
                    binding.makhdom = viewModel.preparedMakhdom
                    binding.saveExitBtn.show()
                }
            }
        } else {
            binding.saveExitBtn.hide()
        }
    }

    private fun handleSaveExitBtn() {
        binding.saveExitBtn.setOnClickListener {
            saveBasicData()
            viewModel.updateMakhdom()
            findNavController().popBackStack()
        }
    }


}