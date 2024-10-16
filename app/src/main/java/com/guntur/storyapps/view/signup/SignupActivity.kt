package com.guntur.storyapps.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.guntur.storyapps.R
import com.guntur.storyapps.databinding.ActivitySignupBinding
import com.guntur.storyapps.view.ViewModelFactory

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    private val signupViewModel by viewModels<SignupViewModel>{
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
        setupAnimation()
    }

    private fun setupViewModel() {
        signupViewModel.loading.observe(this){
            showLoading(it)
        }
        signupViewModel.signupUser.observe(this){
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
                AlertDialog.Builder(this).apply {
                    setTitle("Selamat")
                    setMessage("Pendaftaran Berhasil")
                    setPositiveButton("Lanjut"){_,_ ->
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
        val nameEdit = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val emailEdit = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passEdit = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val sign = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)
        AnimatorSet().apply {
            playSequentially(title, nameEdit, emailEdit, passEdit, sign)
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
        binding.signupButton.setOnClickListener {
            if (binding.nameEditText.text.toString().isEmpty()){
                binding.nameEditTextLayout.error = getString(R.string.isEmpty_name)
            }else{
                signupViewModel.signup(
                    binding.nameEditText.text.toString(),
                    binding.emailEditText.text.toString(),
                    binding.passwordEditText.text.toString()
                )
            }
        }
    }
    private fun showLoading(isLoading: Boolean){
        binding.loader.isVisible = isLoading
    }
}