package com.mazzampr.bwamovie.sign.sigin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mazzampr.bwamovie.home.HomeActivity
import com.mazzampr.bwamovie.R
import com.mazzampr.bwamovie.sign.signup.SignUpActivity
import com.mazzampr.bwamovie.utils.Preferences
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : AppCompatActivity() {
    lateinit var iUsername:String
    lateinit var iPassword:String

    lateinit var mFirebaseInstance: FirebaseDatabase
    lateinit var mDatabase : DatabaseReference
    lateinit var preference : Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        mFirebaseInstance = Firebase.database("https://bwa-mov-74782-default-rtdb.asia-southeast1.firebasedatabase.app/")
        mDatabase = mFirebaseInstance.getReference("User")

        preference = Preferences(this)
        preference.setValues("onboarding", "1")
        if(preference.getValues("status").equals("1")) {
            finishAffinity()

            var goHome = Intent(this@SignInActivity, HomeActivity::class.java)
            startActivity(goHome)
        }

        btn_home.setOnClickListener {
            iUsername = et_username.text.toString()
            iPassword = et_password.text.toString()

            if(iUsername.equals("")) {
                et_username.error = "Silakan tulis kembali username anda"
                et_username.requestFocus()
            } else if(iPassword.equals("")) {
            et_password.error = "Silakan tulis kembali username anda"
            et_password.requestFocus()
            } else {
                pushLogin(iUsername, iPassword)
            }
        }

        btn_daftar.setOnClickListener {
            var goSignUp = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(goSignUp)
        }
    }

    private fun pushLogin(iUsername: String, iPassword: String) {
        mDatabase.child(iUsername).addValueEventListener(object : ValueEventListener {

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@SignInActivity, databaseError.message,
                    Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var user = dataSnapshot.getValue(User::class.java)
                if(user==null) {
                    Toast.makeText(this@SignInActivity, "Username tidak ditemukan",
                        Toast.LENGTH_LONG).show()
                } else {

                    if(user.password.equals(iPassword)) {
                        preference.setValues("nama", user.nama.toString())
                        preference.setValues("username", user.username.toString())
                        preference.setValues("url", user.url.toString())
                        preference.setValues("email", user.email.toString())
                        preference.setValues("saldo", user.saldo.toString())
                        preference.setValues("status", "1")

                        var intent = Intent(this@SignInActivity, HomeActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@SignInActivity, "Password Salah!",
                            Toast.LENGTH_LONG).show()

                    }

                }
            }


        })
    }
}