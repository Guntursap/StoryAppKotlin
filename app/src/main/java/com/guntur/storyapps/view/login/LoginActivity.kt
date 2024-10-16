package com.guntur.storyapps.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.guntur.storyapps.databinding.ActivityLoginBinding
import com.guntur.storyapps.view.ViewModelFactory
import com.guntur.storyapps.view.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
        setupAnimation()
    }

    private fun setupViewModel() {
        loginViewModel.loading.observe(this){
            showLoading(it)
        }
        loginViewModel.loginResponse.observe(this){
            if (it.error == true){
                AlertDialog.Builder(this).apply {
                    setTitle("Peringatan")
                    setMessage(it.message)
                    setPositiveButton("Lanjut"){_,_ ->
                        finish()
                    }
                    create()
                    show()
                }
            }else{
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                ViewModelFactory.clearInstance()
                AlertDialog.Builder(this).apply {
                    setTitle("Peringatan")
                    setMessage("Berhasil Masuk")
                    setPositiveButton("Lanjut"){_,_ ->
                        startActivity(intent)
                        finish()
                    }
                    create()
                    show()

                }
            }
        }
    }

    private fun setupAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(500)
        val emailEdit = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passEdit = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val button = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(1000)

        AnimatorSet().apply {
            playSequentially(title, message, emailEdit, passEdit, button)
            start()
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        with(binding){
            loginButton.setOnClickListener {
                loginViewModel.saveSession(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }
    }
    private fun showLoading(isLoading : Boolean){
        binding.loader.isVisible = isLoading
    }

}