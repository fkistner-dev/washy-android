package com.kilomobi.washy.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.kilomobi.washy.R
import kotlinx.android.synthetic.main.add_photo_layout.*
import java.io.IOException
import java.util.*

class AddPhotoFragment : BaseEmptyFragment() {

    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    companion object {
        private const val PICK_IMAGE_REQUEST = 71
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_photo_layout, container, false)

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        view.findViewById<MaterialButton>(R.id.btn_choose_image).setOnClickListener { launchGallery() }
        view.findViewById<MaterialButton>(R.id.btn_upload_image).setOnClickListener { uploadImage() }

        return view
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICK_IMAGE_REQUEST)
    }

    private fun uploadImage(){
        if (filePath != null){
            val ref = storageReference?.child("uploads/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)

            val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    addUploadRecordToDb(downloadUri.toString())
                } else {
                    // Handle failures
                }
            }?.addOnFailureListener{

            }
        } else{
            Toast.makeText(requireContext(), getString(R.string.no_picture_taken), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, filePath)
                uploadImage.setImageBitmap(bitmap)
                uploadImage.imageTintList = null
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun addUploadRecordToDb(uri: String){
        val db = FirebaseFirestore.getInstance()

        val data = HashMap<String, Any>()
        data["imageUrl"] = uri
        if (FirebaseAuth.getInstance().currentUser != null) {
            data["user"] = FirebaseAuth.getInstance().currentUser?.uid!!
        }

        db.collection("posts")
            .add(data)
            .addOnSuccessListener {
                Snackbar.make(requireView(), getString(R.string.picture_upload_success), Snackbar.LENGTH_LONG).show()
                findNavController().popBackStack()
            }
            .addOnFailureListener { e ->
                Snackbar.make(requireView(), getString(R.string.picture_upload_error), Snackbar.LENGTH_LONG).show()
            }
    }
}