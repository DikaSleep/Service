package com.example.apiserviceapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.apiserviceapp.databinding.ActivityMainBinding
import com.example.fundamentalapp.ApiService
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get
        binding.btnGet.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val result = ApiService.get("/posts/1")

                    binding.tvResponse.text = result
                }catch (e: Exception){
                    binding.tvResponse.text = e.message
                }
            }
        }

        //post
        binding.btnPost.setOnClickListener {

            val body = JSONObject().apply {
                put("Title", "Belajar Kotlin")
                put("Body","Belajar REST API")
                put("userId", 1)
            }

            lifecycleScope.launch {
                try {
                    val result = ApiService.post("/posts",body.toString())

                    binding.tvResponse.text = result
                }catch (e: Exception){
                    binding.tvResponse.text = e.message
                }
            }
        }

        binding.btnPut.setOnClickListener {

            val body = JSONObject().apply {
                put("id", 1)
                put("title", "Update Kotlin")
                put("body", "Belajar Http PUT")
                put("userId", 1)
            }
            lifecycleScope.launch {
                try{
                    val result = ApiService.put("/post/1", body.toString())

                    binding.tvResponse.text = result
                }catch (e: Exception){
                    binding.tvResponse.text = e.message
                }
            }
        }

        binding.btnDelete.setOnClickListener {
            lifecycleScope.launch {
                try{
                    val result = ApiService.delete("/posts/1")
                    binding.tvResponse.text = result

                }catch (e: Exception){
                    binding.tvResponse.text = e.message
                }
            }
        }
    }
}