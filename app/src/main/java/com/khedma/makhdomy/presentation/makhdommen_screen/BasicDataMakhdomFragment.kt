package com.khedma.makhdomy.presentation.makhdommen_screen

import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.khedma.makhdomy.MainActivity
import com.khedma.makhdomy.R
import com.khedma.makhdomy.databinding.FragmentBasicDataMakhdomBinding
import com.khedma.makhdomy.domain.model.Makhdom
import com.khedma.makhdomy.presentation.utils.hide
import com.khedma.makhdomy.presentation.utils.show
import com.khedma.makhdomy.presentation.utils.showToast
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
        (requireActivity() as MainActivity).supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true) // Optional: Show the Up button
        }
        requireActivity().title = getString(R.string.basic_data_toolbar_title)
        setHasOptionsMenu(true)
        handleMakhdomInitialValueIFExist()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("fragment: ", "basic fragment, ${args.makhdomId}")
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
            pickMedia.launch(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
            )
        }
    }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val uri = result.data?.data
            uri?.let {
                val bitmap = decodeSampledBitmapFromResource(it, 320, 320)
                binding.pictureView.setImageBitmap(bitmap)
                viewModel.preparedMakhdom.picture = bitmap
                if (viewModel.preparedMakhdom.isSynchronized)
                    viewModel.preparedMakhdom.isPictureUpdated = true
            }
        }

    private fun getBitmapFromUri(imageUri: Uri): Bitmap? {
        var bitmap: Bitmap? = null
        val contentResolver: ContentResolver = requireActivity().contentResolver
        try {
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            } else {
                val source: ImageDecoder.Source =
                    ImageDecoder.createSource(contentResolver, imageUri)
                ImageDecoder.decodeBitmap(source)
            }
            bitmap = Bitmap.createScaledBitmap(
                bitmap!!,
                (bitmap.width * 0.3).toInt(),
                (bitmap.height * 0.3).toInt(),
                true
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.d("bitmap", bitmap.toString())

        return bitmap
    }

    private fun resizeBitmap(bitmap: Bitmap?, newWidth: Int, newHeight: Int): Bitmap? {
        if (bitmap == null)
            return bitmap
        // Calculate the scaling factors for width and height
        val widthScale = newWidth.toFloat() / bitmap.width
        val heightScale = newHeight.toFloat() / bitmap.height

        // Calculate the scale factor while maintaining the aspect ratio
        val scaleFactor = minOf(widthScale, heightScale)

        // Calculate the new dimensions based on the aspect ratio
        val scaledWidth = (bitmap.width * scaleFactor).toInt()
        val scaledHeight = (bitmap.height * scaleFactor).toInt()

        // Create a Matrix to perform the scaling
        val matrix = Matrix()
        matrix.postScale(scaleFactor, scaleFactor)

        // Create a new Bitmap with the desired dimensions

        var resizedBitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)
        // Create a Canvas and draw the scaled Bitmap onto the new Bitmap
        val canvas = Canvas(resizedBitmap)
        canvas.drawBitmap(bitmap, matrix, null)

        return resizedBitmap
    }

    private fun decodeSampledBitmapFromResource(
        imageUri: Uri,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap? {
        // First decode with inJustDecodeBounds=true to check dimensions
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeStream(
                requireActivity().contentResolver.openInputStream(imageUri),
                null,
                this
            )

            // Calculate inSampleSize
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

            // Decode bitmap with inSampleSize set
            inJustDecodeBounds = false

            BitmapFactory.decodeStream(
                requireActivity().contentResolver.openInputStream(imageUri),
                null,
                this
            )
        }
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int
    ): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight
                && halfWidth / inSampleSize >= reqWidth
            ) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
                true
            }

            else -> return super.onContextItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

}

