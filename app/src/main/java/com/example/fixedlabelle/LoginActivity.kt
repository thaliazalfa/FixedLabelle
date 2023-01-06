package com.example.fixedlabelle

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.fixedlabelle.API.RetrofitHelper
import com.example.fixedlabelle.API.UserApi
import com.example.fixedlabelle.API.Users
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    lateinit var btnLogin : Button
    lateinit var btnMoveToSignUp : TextView
    lateinit var etEmail : EditText
    lateinit var etPassword : EditText

    val apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inh1YXdqbGFramNrdGN6eHZhaW1vIiwicm9sZSI6ImFub24iLCJpYXQiOjE2NzE5NDk0ODgsImV4cCI6MTk4NzUyNTQ4OH0.GJQ0y2G4zZuaNLIvDdnBdqyLRMuDUHJ6otjo5Xui9ZI"
    val token = "Bearer $apiKey"

    val userApi = RetrofitHelper.getInstance().create(UserApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setFullScreen(window)
        lightStatusBar(window)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        btnLogin = findViewById(R.id.button_login)
        btnMoveToSignUp = findViewById(R.id.txt_Move_To_SignUp)
        etEmail = findViewById(R.id.usn_login)
        etPassword = findViewById(R.id.password_login)

        btnLogin.setOnClickListener {
            signIn(etEmail.text.toString(), etPassword.text.toString())

        }

        btnMoveToSignUp.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signIn(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {

            val data = Users(email = email, password = password)
            val response = userApi.signIn(token = token, apiKey = apiKey, data = data)

            val bodyResponse = if (response.code() == 200) {
                response.body()?.string()
            } else {
                response.errorBody()?.string().toString()
            }

            var failed = false
            val jsonResponse = JSONObject(bodyResponse)
            if (jsonResponse.keys().asSequence().toList().contains("error")) {
                failed = true
            }

            var msg = ""
            if (!failed) {
                var email = jsonResponse.getJSONObject("user").get("email").toString()
                msg = "Successfully login! Welcome back: $email"

                val sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE)
                var editor = sharedPreferences.edit()
                editor.putString("email", email)
                editor.commit()

            } else {
                msg += jsonResponse.get("error_description").toString()
            }

            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(
                    applicationContext,
                    msg,
                    Toast.LENGTH_SHORT
                ).show()

                if (!failed){
                    goToHomepage()
                }
            }

        }
    }

    private fun goToHomepage() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}