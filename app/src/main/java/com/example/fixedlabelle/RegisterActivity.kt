package com.example.fixedlabelle

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.fixedlabelle.API.RetrofitHelper
import com.example.fixedlabelle.API.UserApi
import com.example.fixedlabelle.API.Users
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    lateinit var btnRegister : Button
    lateinit var btnMoveToSignIn : TextView
    lateinit var btnGender : RadioGroup
    lateinit var rbWedok : RadioButton
    lateinit var rbLanang : RadioButton
    lateinit var txtnama : EditText
    lateinit var txtusername : EditText
    lateinit var txtemail : EditText
    lateinit var txtpassword : EditText

    val apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inh1YXdqbGFramNrdGN6eHZhaW1vIiwicm9sZSI6ImFub24iLCJpYXQiOjE2NzE5NDk0ODgsImV4cCI6MTk4NzUyNTQ4OH0.GJQ0y2G4zZuaNLIvDdnBdqyLRMuDUHJ6otjo5Xui9ZI"
    val token = "Bearer $apiKey"

    val userApi = RetrofitHelper.getInstance().create(UserApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setFullScreen(window)
        lightStatusBar(window)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        btnRegister = findViewById(R.id.button_register)
        btnMoveToSignIn = findViewById(R.id.txt_Move_To_SignIn)
        btnGender = findViewById<RadioGroup>(R.id.rg_gender)
        rbWedok = findViewById(R.id.rb_wedok)
        rbLanang = findViewById(R.id.rb_lanang)
        txtnama = findViewById(R.id.txt_nama)
        txtusername = findViewById(R.id.txt_username)
        txtemail = findViewById(R.id.input_email)
        txtpassword = findViewById(R.id.txt_password)



        btnRegister.setOnClickListener {
            signUp(txtemail.text.toString(), txtpassword.text.toString())


        }

        btnMoveToSignIn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
    }
}

    private fun signUp(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {

            var data = Users(email = email, password = password)
            var response = userApi.signUp(token = token, apiKey = apiKey, data = data)

            val bodyResponse = if(response.code() == 200) {
                response.body()?.string()
            } else {
                response.errorBody()?.string().toString()
            }

            var failed = false
            val jsonResponse = JSONObject(bodyResponse)
            if(jsonResponse.keys().asSequence().toList().contains("error")) {
                failed = true
            }

            var msg = ""
            if (!failed) {
                msg = "Successfully sign up!"
                val sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE)


                var editor = sharedPreferences.edit()
                editor.putString("nama", txtnama.text.toString())
                editor.putString("username", txtusername.text.toString())
                editor.putBoolean("perempuan", rbWedok.isChecked)
                editor.putBoolean("laki-laki", rbLanang.isChecked)
                editor.commit()

            } else {
                var errorMessage = jsonResponse.get("error_description")
                msg += errorMessage
            }

            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(
                    applicationContext,
                    msg,
                    Toast.LENGTH_SHORT
                ).show()

                finish()
            }
        }
    }
}