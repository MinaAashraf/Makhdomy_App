package com.khedma.makhdomy.presentation.makhdommen_screen

import android.app.DatePickerDialog
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.khedma.makhdomy.R
import com.khedma.makhdomy.databinding.FragmentBasicDataMakhdomBinding
import com.khedma.makhdomy.domain.model.Makhdom
import com.khedma.makhdomy.presentation.utils.hide
import com.khedma.makhdomy.presentation.utils.show
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


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
        Log.d("fragment: ", "basic fragment, ${args.makhdomId.toString()}")
        setUpUi()
    }

    private fun setUpUi() {
        setUpPictureImage()
        setUpClassNameDropDownList()
        setUpBirthDateField()
        setUpNextBtn()
        handleSaveExitBtn()
    }

    private fun setUpNextBtn() {
        binding.nextPageBtn.setOnClickListener {
            if (saveBasicData()) {
                findNavController().navigate(R.id.action_basicDataMakhdomFragment_to_addressMakhdomFragment)
            }
        }
    }

    private fun saveBasicData(): Boolean {
        clearFieldsErrors()
        val name = binding.nameTextField.editText!!.text.toString()
        val date = binding.birthDateField.editText!!.text.toString()
        val birthLocation = binding.birthLocationField.editText!!.text.toString()
        val className = binding.classNameAutoComplete.text.toString()
        val classId = resources.getStringArray(R.array.classes_names).indexOf(className) + 1

        var isValid = true

        if (name.isEmpty()) {
            isValid = false
            binding.nameTextField.helperText = getString(R.string.name_err)
        }

        if (className.isEmpty()) {
            isValid = false
            binding.classNameField.helperText = getString(R.string.class_name_err_msg)
        }

        if (isValid) {
            viewModel.preparedMakhdom.apply {
                this.name = name
                this.className = className
                this.classId = classId
                this.birthDate = date
                this.birthLocation = birthLocation
            }

        }
        return isValid
    }

    private fun clearFieldsErrors() {
        binding.apply {
            nameTextField.helperText = null
            classNameField.helperText = null
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
            val liveData = viewModel.readMakhdomById(args.makhdomId)

            //observe only once
            liveData.observe(requireActivity(), object : Observer<Makhdom?> {
                override fun onChanged(makhdom: Makhdom?) {
                    makhdom?.let {
                        viewModel.updatingState = true
                        viewModel.preparedMakhdom = it
                        binding.makhdom = viewModel.preparedMakhdom
                        binding.saveExitBtn.show()
                        liveData.removeObserver(this)
                    }
                }
            })
        } else {
            binding.saveExitBtn.hide()
            Log.d("initial val: ", "${binding.makhdom} --- ${viewModel.preparedMakhdom}")
        }
    }

    private fun handleSaveExitBtn() {
        binding.saveExitBtn.setOnClickListener {
            if (binding.nameTextField.editText!!.text.isEmpty())
                binding.nameTextField.helperText = getString(R.string.name_err)
            else {
                saveBasicData()
                viewModel.updateMakhdom()
                findNavController().popBackStack()
            }
        }
    }

    private fun setUpClassNameDropDownList() {
        val dropDownAdapter = ArrayAdapter(
            requireActivity(),
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.classes_names)
        )
        binding.classNameAutoComplete.setAdapter(dropDownAdapter)
    }

    private fun setUpBirthDateField() {
        binding.birthDateField.editText!!.setOnClickListener {
            showDataPickerDialog { date: String -> setTextDateToDateField(date) }
        }
    }

    private val calender = Calendar.getInstance()
    private fun showDataPickerDialog(onDateSelected: (date: String) -> Unit) {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            android.R.style.Theme_Holo_Dialog,
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate =
                    Calendar.getInstance().apply { set(year, monthOfYear, dayOfMonth) }
                val formattedDate =
                    SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate.time)
                onDateSelected(formattedDate)
            },
            calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH),

        )
        datePickerDialog.show()
    }


    private fun setTextDateToDateField(date: String) =
        binding.birthDateField.editText!!.setText(date)


}