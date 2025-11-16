package com.erdemgozen.hw1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.erdemgozen.hw1.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase)))
    }

    private lateinit var binding: ActivityMainBinding
    private val REQUEST_SECOND = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink)
        binding.txtTitle.startAnimation(blinkAnimation)

        binding.btnLanguage.setOnClickListener { changeLanguage() }
        val moneyString = intent.getStringExtra("EXTRA_MONEY").toString()

        binding.btnStart.setOnClickListener {
            val moneyInput = binding.txtMoney.text.toString().trim()
            val money = moneyInput.toDoubleOrNull()

            if (money == null || money <= 0) {

                Toast.makeText(this, "Please enter a valid money amount!", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, SecondActivity::class.java)

                intent.putExtra("EXTRA_MONEY", money.toString())
                startActivity(intent)
            }
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.iconHome -> {

                    true
                }
                R.id.iconList -> {

                    val intent = Intent(this, SecondActivity::class.java)
                    val money = binding.txtMoney.text.toString().toDoubleOrNull() ?: 0.0
                    intent.putExtra("EXTRA_MONEY", money.toString())

                    startActivity(intent)
                    true
                }
                R.id.iconCart -> {
                    if (CartManager.getCartItems().isEmpty()){
                        val intent = Intent(this, EmptyCartActivity::class.java)
                        startActivity(intent)
                    }else{
                        val intent = Intent(this, ThirdActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }
                else -> false
            }
        }
    }


    @Deprecated("Deprecated in Android 11+, but fine for assignment")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_SECOND && resultCode == RESULT_OK && data != null) {
            val message = data.getStringExtra("BACK_MESSAGE")
            if (!message.isNullOrEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun displayToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun changeLanguage() {
        val currentLang = LocaleHelper.getLanguage(this)
        val newLang = if (currentLang == "tr") "en" else "tr"
        LocaleHelper.setLocale(this, newLang)
        displayToast(resources.getString(R.string.toast_language_changed))
        recreate()
    }


}
