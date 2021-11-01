package com.jfsb.myvie.first

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.jfsb.myvie.databinding.ActivityLoginBinding
import com.jfsb.myvie.main.MainActivity

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth

    private lateinit var txtEmail:String
    private lateinit var txtPassword:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        if(mAuth.currentUser!=null){
            startApp()
        }

        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.tvSignUpSecond.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener{
            txtEmail = binding.etLoginEmail.toString()
            txtPassword = binding.etLoginPassword.text.toString()

            if (txtEmail.isNotEmpty() && txtPassword.isNotEmpty()){
                logUser()
            }
            else{
                Toast.makeText(this,"Favor de ingresar todos los datos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logUser(){
        mAuth.signInWithEmailAndPassword(txtEmail,txtPassword).addOnCompleteListener(){ task ->
            if(task.isSuccessful){
                startApp()
                this.finish()
            } else{
                Toast.makeText(this,"Verificar los datos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startApp(){
        val intentProfile = Intent(this, MainActivity::class.java).apply {}
        startActivity(intentProfile)
        // Utils.openProfile(FirebaseDatabase.getInstance().reference, "Users", mAuth.currentUser!!.uid, requireActivity(), MainActivity(), true)
    }
}