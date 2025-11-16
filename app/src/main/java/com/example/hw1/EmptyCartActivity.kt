package com.erdemgozen.hw1

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.erdemgozen.hw1.databinding.ActivityEmptyCartBinding
import com.erdemgozen.hw1.databinding.ActivityEmptyCartBinding.*
import com.erdemgozen.hw1.databinding.ActivityMainBinding
import com.erdemgozen.hw1.databinding.ActivitySecondBinding
import com.erdemgozen.hw1.LocaleHelper

class EmptyCartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmptyCartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmptyCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()


        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.iconHome -> {
                    val intent = Intent(this, MainActivity::class.java)
                    finish()
                    true
                }
                R.id.iconList -> {

                    val intent = Intent(this, SecondActivity::class.java)
                    finish()
                    true
                }
                R.id.iconCart -> {
                    true
                }
                else -> false
            }
        }

        }
    private fun changeLanguage() {
        val currentLang = LocaleHelper.getLanguage(this)
        val newLang = if (currentLang == "tr") "en" else "tr"
        LocaleHelper.setLocale(this, newLang)
        recreate()
    }

    }

