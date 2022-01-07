package com.drindrin.todolist.userinfo

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.drindrin.todolist.R
import com.drindrin.todolist.databinding.FragmentUserInfoBinding
import com.drindrin.todolist.models.UserInfo
import com.drindrin.todolist.network.Api
import com.drindrin.todolist.network.SHARED_PREF_TOKEN_KEY
import com.drindrin.todolist.utils.KeyboardUtils.Companion.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import com.google.modernstorage.mediastore.FileType
import com.google.modernstorage.mediastore.MediaStoreRepository
import com.google.modernstorage.mediastore.SharedPrimary
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

import java.util.*

class UserInfoFragment : Fragment() {

    // créer une uri pour sauvegarder l'image, dans onViewCreated
    private lateinit var photoUri: Uri
    private val viewModel: UserInfoViewModel by viewModels()

    private val mediaStore by lazy { MediaStoreRepository(activity!!) }

    private var _binding: FragmentUserInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            val userInfo = Api.userWebService.getInfo().body()!!
            binding.editEmailTxtview.setText(userInfo.email)
            binding.editLastnameTxtview.setText(userInfo.lastName)
            binding.editFirstnameTxtview.setText(userInfo.firstName)

            binding.takePictureButton.setOnClickListener {
                launchCameraWithPermission()
            }

            binding.uploadImageButton.setOnClickListener {
                launchGallery()
            }

            binding.updateInfoBtn.setOnClickListener {
                val email = binding.editEmailTxtview.text.toString()
                val lastname = binding.editLastnameTxtview.text.toString()
                val firstname = binding.editFirstnameTxtview.text.toString()

                if (email.isEmpty() || lastname.isEmpty() || firstname.isEmpty()) {
                    Toast.makeText(context, "Veuillez tout remplir", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Informations modifiées avec succès", Toast.LENGTH_LONG).show()
                    viewModel.updateUserInfo(UserInfo(email, firstname, lastname, userInfo.avatar))
                    hideKeyboard()
                    binding.editEmailTxtview.clearFocus()
                    binding.editLastnameTxtview.clearFocus()
                    binding.editFirstnameTxtview.clearFocus()
                }
            }

            binding.disconnectBtn.setOnClickListener {
                val sharedPrefs = context!!.getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
                sharedPrefs.edit().remove(SHARED_PREF_TOKEN_KEY).commit()
                findNavController().navigate(R.id.action_userInfoFragment_to_authenticationFragment)

            }
        }

        lifecycleScope.launchWhenStarted {
            photoUri = mediaStore.createMediaUri(
                filename = "picture-${UUID.randomUUID()}.jpg",
                type = FileType.IMAGE,
                location = SharedPrimary
            ).getOrThrow()
        }
        lifecycleScope.launch {
            viewModel.userInfo.collectLatest{   userInfo ->
                binding.imageView.load(userInfo.avatar) {
                    error(R.drawable.ic_launcher_background)
                }
            }
        }
    }

    // register
   private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { accepted ->
            if (accepted) handleImage(photoUri)
            else Snackbar.make(binding.root, "Échec!", Snackbar.LENGTH_LONG)
    }
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { URI ->
        if (URI != null) handleImage(URI)

        else Snackbar.make(binding.root, "Échec!", Snackbar.LENGTH_LONG)

    }


    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                launchCamera()
            } else {
                showExplanation()
            }
        }


    private fun launchCameraWithPermission() {
        val camPermission = Manifest.permission.CAMERA
        val permissionStatus = ContextCompat.checkSelfPermission(activity!!, camPermission)
        val isAlreadyAccepted = permissionStatus == PackageManager.PERMISSION_GRANTED
        val isExplanationNeeded = shouldShowRequestPermissionRationale(camPermission)
        when {
            isAlreadyAccepted -> {
                launchCamera()
            }
            isExplanationNeeded -> {
                showExplanation()
            }
            else -> {
                cameraPermissionLauncher.launch(camPermission)
            }
        }
    }

    private fun showExplanation() {
        // ici on construit une pop-up système (Dialog) pour expliquer la nécessité de la demande de permission
        AlertDialog.Builder(activity)
            .setMessage("🥺 On a besoin de la caméra, vraiment! 👉👈")
            .setPositiveButton("Bon, ok") { _, _ -> launchAppSettings() }
            .setNegativeButton("Nope") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun launchAppSettings() {
        // Cet intent permet d'ouvrir les paramètres de l'app (pour modifier les permissions déjà refusées par ex)
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", this.activity?.packageName, null)
        )
        // ici pas besoin de vérifier avant car on vise un écran système:
        startActivity(intent)
    }

    private fun handleImage(uri: Uri) {
        lifecycleScope.launch{
            viewModel.updateAvatar(convert(uri))
        }
    }

    private fun launchCamera() {
        cameraLauncher.launch(photoUri)
    }

    private fun launchGallery(){
        galleryLauncher.launch("image/*")
    }

    private fun convert(uri: Uri): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "temp.jpeg",
            body = this.activity?.contentResolver?.openInputStream(uri)!!.readBytes().toRequestBody()
        )
    }
}