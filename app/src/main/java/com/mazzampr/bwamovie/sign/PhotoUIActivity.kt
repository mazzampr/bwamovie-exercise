package com.mazzampr.bwamovie.sign

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.mazzampr.bwamovie.home.HomeActivity
import com.mazzampr.bwamovie.R
import com.mazzampr.bwamovie.utils.Preferences
import kotlinx.android.synthetic.main.activity_photo_ui.*
import java.util.*

class PhotoUIActivity : AppCompatActivity(), PermissionListener {

    val REQUEST_IMAGE_PICTURE = 1
    var statusAdd:Boolean = false
    lateinit var filePath: Uri
    lateinit var storage : FirebaseStorage
    lateinit var storageReference : StorageReference
    lateinit var preference : Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_ui)

        preference = Preferences(this)
        storage = FirebaseStorage.getInstance()
        storageReference = storage.getReference()

        tv_hello.text = "Selamat Datang\n"+intent.getStringExtra("nama")

        iv_add.setOnClickListener {
            if(statusAdd) {
                statusAdd = false
                btn_save.visibility = View.VISIBLE
                iv_profile.setImageResource(R.drawable.user_pic)
            } else {
                Dexter.withActivity(this)
                    .withPermission(Manifest.permission.CAMERA)
                    .withListener(this)
                    .check()
            }
        }

        btn_upload_nanti.setOnClickListener {
            finishAffinity()
            var goHome = Intent(this@PhotoUIActivity, HomeActivity::class.java)
            startActivity(goHome)
        }

        btn_save.setOnClickListener {
            if (filePath != null) {
                var progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Uploading...")
                progressDialog.show()

                var ref = storageReference.child("images/"+UUID.randomUUID().toString())
                ref.putFile(filePath)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(this, "Uploaded", Toast.LENGTH_LONG).show()

                        ref.downloadUrl.addOnSuccessListener {
                            preference.setValues("url", it.toString())
                        }

                        finishAffinity()

                        var goHome = Intent(this@PhotoUIActivity, HomeActivity::class.java)
                        startActivity(goHome)
                    }

                    .addOnFailureListener {
                        progressDialog.dismiss()
                        Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                    }
                    .addOnProgressListener {
                        taskSnapshot ->  var progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                        progressDialog.setMessage("Upload "+progress.toInt()+" 5")
                    }

            } else {

            }
        }
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
            takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager) ?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_PICTURE)
            }
        }
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Toast.makeText(this@PhotoUIActivity, "Anda tidak bisa menambahkan foto profile",
            Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        Toast.makeText(this@PhotoUIActivity, "Tergesah? Kilk tombol upload nanti saja",
            Toast.LENGTH_LONG).show()
    }

    override fun onPermissionRationaleShouldBeShown(
        permission: PermissionRequest?,
        token: PermissionToken?
    ) {
        TODO("Not yet implemented")
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_PICTURE && resultCode == Activity.RESULT_OK) {
            var bitmap = data?.extras?.get("data") as Bitmap
            statusAdd = true

            filePath = data.getData()!!
            Glide.with(this)
                .load(bitmap)
                .apply(RequestOptions.circleCropTransform())
                .into(iv_profile)

            btn_save.visibility = View.VISIBLE
            iv_add.setImageResource(R.drawable.ic_delete)
        }
    }
}