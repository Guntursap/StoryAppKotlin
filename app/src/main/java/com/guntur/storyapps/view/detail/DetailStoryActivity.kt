package com.guntur.storyapps.view.detail

import android.content.Intent.EXTRA_USER
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.guntur.storyapps.databinding.ActivityDetailStoryBinding
import com.guntur.storyapps.view.ViewModelFactory

class DetailStoryActivity : AppCompatActivity() {

    private val detailModel by viewModels<DetailStoryViewModel>{
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        val id = intent.getStringExtra(EXTRA_USER).toString()
        detailModel.loading.observe(this){
            showLoading(it)
        }
        detailModel.getAllDetailStory(id)
        detailModel.detailStory.observe(this){
            binding.apply {
                nameEditText.text = it.name
                descTextView.text = it.description
                Glide.with(applicationContext)
                    .load(it.photoUrl)
                    .into(binding.imageView)
            }
        }

    }
    private fun showLoading(isLoading: Boolean){
        binding.loader.isVisible = isLoading
    }
}