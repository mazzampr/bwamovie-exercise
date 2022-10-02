package com.mazzampr.bwamovie.sign.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mazzampr.bwamovie.R
import com.mazzampr.bwamovie.sign.PhotoUIActivity
import com.mazzampr.bwamovie.sign.sigin.User
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    lateinit var sUsername:String
    lateinit var sPassword:String
    lateinit var sNama:String
    lateinit var sEmail:String

    lateinit var mFirebaseInstance: FirebaseDatabase
    lateinit var mDatabase: DatabaseReference
    lateinit var mDatabaseReference : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mFirebaseInstance = Firebase.database("https://bwa-mov-74782-default-rtdb.asia-southeast1.firebasedatabase.app/")
        mDatabase = Firebase.database.getReference()
        mDatabaseReference = mFirebaseInstance.getReference("User")

        btn_lanjutkan.setOnClickListener {
            sUsername = et_username.text.toString()
            sPassword = et_password.text.toString()
            sNama = et_name.text.toString()
            sEmail = et_email.text.toString()

            //Validation
            if (sUsername.equals("")) {
                et_username.error = "Silakan isi username anda"
                et_username.requestFocus()
            } else if (sPassword.equals("")) {
                et_password.error = "Silakan isi username anda"
                et_password.requestFocus()
            } else if (sNama.equals("")) {
                et_name.error = "Silakan isi username anda"
                et_name.requestFocus()
            } else if (sEmail.equals("")) {
                et_email.error = "Silakan isi username anda"
                et_email.requestFocus()
            } else {
                saveUsername(sUsername, sPassword, sNama, sEmail)
            }
        }
    }

    private fun saveUsername(sUsername: String, sPassword: String, sNama: String, sEmail: String) {
        var user = User()
        user.email = sEmail
        user.username = sUsername
        user.nama = sNama
        user.password = sPassword

        if (sUsername != null) {
            checkingUsername(sUsername, user)
        }
    }

    private fun checkingUsername(sUsername: String, data: User) {
        mDatabaseReference.child(sUsername).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@SignUpActivity, ""+databaseError.message,
                    Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var user = dataSnapshot.getValue(User::class.java)
                if(user == null) {
                    mDatabaseReference.child(sUsername).setValue(data)
                    
                    var goSignUpPhotoScreen = Intent(this@SignUpActivity, PhotoUIActivity::class.java).putExtra("nama", data?.nama)
                    startActivity(goSignUpPhotoScreen)
                } else {
                    Toast.makeText(this@SignUpActivity, "Username sudah digunakan",
                        Toast.LENGTH_LONG).show()
                }
            }



        } )
    }
}