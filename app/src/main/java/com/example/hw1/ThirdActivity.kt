package com.erdemgozen.hw1

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.erdemgozen.hw1.databinding.ActivityThirdBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class ThirdActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase)))
    }

    private lateinit var binding: ActivityThirdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_third)


        val moneyString = intent.getStringExtra("EXTRA_MONEY")
        val money = moneyString?.toDoubleOrNull() ?: 0.0


        val city = "Unknown"

        binding.money = money.toString()



        val cartItemsText = CartManager.getCartItems().joinToString(separator = "\n") {
            "${it.cityName} - ${it.cityCost} TL" // CityData.kt'deki toString'e bağlı
        }
        binding.textView.text = cartItemsText

        // Snackbar mesajını düzeltme
        Snackbar.make(binding.root, "You have ${money.toFloat()} TL.", Snackbar.LENGTH_LONG).show()

        binding.btnLang.setOnClickListener { changeLanguage() }


        // 'btnShow' butonuna tıklandığında bu kod çalışacak
        binding.btnShow.setOnClickListener {
            showCustomMoodDialog(money, city)
        }


        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) { // 'item.id' değil, 'item.itemId'
                R.id.iconHome -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("EXTRA_MONEY", money.toString())
                    finish()
                    true
                }
                R.id.iconList -> {
                    val intent = Intent(this, SecondActivity::class.java)

                    intent.putExtra("EXTRA_MONEY", money.toString())
                    finish()
                    true
                }
                R.id.iconCart -> {
                    // Zaten buradayız
                    true
                }
                else -> false
            }
        }
    }

    private fun showCustomMoodDialog(money: Double?, city: String?) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_mood_summary)

        val txtMessage = dialog.findViewById<TextView>(R.id.dialogMessage)


        val btnConfirm = dialog.findViewById<Button>(R.id.btnDialogConfirm)

        txtMessage.text = "\n" + CartManager.getCartCost() + "TL"

        // 'btnShow' değil, 'btnConfirm' olmalı
        btnConfirm.setOnClickListener {
            dialog.dismiss()
            val resultIntent = Intent()

            resultIntent.putExtra("EXTRA_RETURN_MESSAGE", "Glad to know you like $city with $money money!")

            setResult(Activity.RESULT_OK, resultIntent)
            CartManager.clearCart()
            finish()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun changeLanguage() {
        val currentLang = LocaleHelper.getLanguage(this)
        val newLang = if (currentLang == "tr") "en" else "tr"

        LocaleHelper.setLocale(this, newLang)
        Toast.makeText(this, resources.getString(R.string.toast_language_changed), Toast.LENGTH_SHORT).show()
        recreate()
    }
}