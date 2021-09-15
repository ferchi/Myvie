package com.jfsb.myvie.first

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jfsb.myvie.R
import com.jfsb.myvie.databinding.ActivityLoginBinding
import com.jfsb.myvie.main.MainActivity

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.tvSignUp.setOnClickListener {
            var intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.tvSignUpSecond.setOnClickListener {
            var intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener{
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }
}