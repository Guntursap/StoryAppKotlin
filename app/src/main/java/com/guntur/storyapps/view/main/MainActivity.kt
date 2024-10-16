package com.guntur.storyapps.view.main


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.guntur.storyapps.R
import com.guntur.storyapps.databinding.ActivityMainBinding
import com.guntur.storyapps.view.ViewModelFactory
import com.guntur.storyapps.view.main.adapter.LoadingStateAdapter
import com.guntur.storyapps.view.main.adapter.StoryAdapter
import com.guntur.storyapps.view.map.MapsActivity
import com.guntur.storyapps.view.upload.UploadActivity
import com.guntur.storyapps.view.welcome.WelcomeActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvReview.layoutManager = LinearLayoutManager(this)
        setupAction()
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }else{
                setupViewData()
            }
        }
    }

    private fun setupViewData() {
        val adapter = StoryAdapter()
        binding.rvReview.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                adapter.retry()
            }
        )
        viewModel.getAllStories.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun setupAction() {
        binding.uploadButton.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.maps -> {
                val intent = Intent(this@MainActivity, MapsActivity::class.java)
                startActivity(intent)
            }
            R.id.logout -> {
                lifecycleScope.launch {
                    viewModel.logout()
                }
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}