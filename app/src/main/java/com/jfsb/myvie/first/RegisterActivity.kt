package com.jfsb.myvie.first

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jfsb.myvie.R
import com.jfsb.myvie.databinding.ActivityLoginBinding
import com.jfsb.myvie.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.tvLogin.setOnClickListener {
            this.finish()
        }
    }
}