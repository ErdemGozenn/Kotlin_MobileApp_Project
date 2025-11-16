package com.erdemgozen.hw1

import android.app.Activity
import android.app.Person
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.erdemgozen.hw1.databinding.ActivitySecondBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class SecondActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase)))
    }

    private lateinit var binding: ActivitySecondBinding
    private var money: Double = 0.0
    companion object {
        private const val KEY_CURRENT_MONEY = "CURRENT_MONEY"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null ) {
           money = savedInstanceState.getDouble(KEY_CURRENT_MONEY)
        } else {

            val moneyString = intent.getStringExtra("EXTRA_MONEY")
            money = moneyString?.toDoubleOrNull() ?: 0.0
        }

            binding.txtMessage.text = "Let's find a house appropriate to your money: ${money.toLong()} TL"



        // --- City list ---
        val cities = listOf(
            CityData("Ankara", 5700000.0),
            CityData("Istanbul", 8250000.0),
            CityData("Izmir", 4800000.0),
            CityData("Adana", 3100000.0)
        )

        // --- Spinner setup ---
        val cityNamesFromXml = resources.getStringArray(R.array.city_names)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cityNamesFromXml)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMood.adapter = adapter

        // --- On selection change ---
        binding.spinnerMood.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long
            ) {
                val selectedCity = cities[position]
                when (selectedCity.cityName) {
                    "Ankara" -> binding.img.setImageResource(R.drawable.ankara)
                    "Istanbul" -> binding.img.setImageResource(R.drawable.istanbul)
                    "Izmir" -> binding.img.setImageResource(R.drawable.izmir)
                    "Adana" -> binding.img.setImageResource(R.drawable.adana)
                }
                binding.txtCost.text = "${selectedCity.cityCost} TL"
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // --- Brightness SeekBar ---
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                val alphaValue = (progress * 2.55).toInt().coerceIn(0, 255)
                binding.root.background.alpha = alphaValue

                val brightness = alphaValue
                binding.txtBright.setTextColor(Color.rgb(brightness, brightness, brightness))
            }

            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })

        binding.bottomNavigation.setOnItemSelectedListener {
            item ->
            when(item.itemId) {
                R.id.iconHome -> {
                    val navigatorIntent = Intent()
                    val intent = Intent(this, MainActivity::class.java)
                    setResult(Activity.RESULT_OK, intent)
                    finish()

                    true
                }
                R.id.iconList -> {
                    true
                }
                R.id.iconCart -> {
                    if (CartManager.getCartItems().isEmpty()){
                        val intent = Intent(this, EmptyCartActivity::class.java)
                        startActivity(intent)
                    }else{
                        val intent = Intent(this, ThirdActivity::class.java)
                        intent.putExtra("EXTRA_MONEY", money.toString())
                        startActivity(intent)
                    }
                    true
                }
                else -> false
            }
        }
        // --- Back button ---
        binding.btnBack.setOnClickListener {
            val backIntent = Intent()

            backIntent.putExtra("EXTRA_RETURN_MESSAGE", getString(R.string.back_message))


            setResult(Activity.RESULT_OK, backIntent)
            finish()
        }
        // --- Buy button ---
        binding.btnBuy.setOnClickListener {
            val selectedCity = cities[binding.spinnerMood.selectedItemPosition]
            showConfirmationDialog(selectedCity)
        }

        binding.btnChangeLang.setOnClickListener { changeLanguage() }
    }

    private fun showConfirmationDialog(city: CityData) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_title))
            .setMessage("${getString(R.string.dialog_message)}\n\n${city.cityName} -> ${city.cityCost} TL")
            .setPositiveButton("ONAYLA") { _, _ ->
                if (money >= city.cityCost) {
                    Toast.makeText(this, "Added into Cart!", Toast.LENGTH_SHORT).show()
                    CartManager.addHouse(city)
                    money -= city.cityCost
                    binding.txtMessage.text = "Let's find a house appropriate to your money: ${money.toLong()} TL"

                } else {
                    Toast.makeText(this, "Not enough money to buy a house in ${city.cityName}!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("İPTAL", null)
            .show()
    }


    private fun changeLanguage() {
        val currentLang = LocaleHelper.getLanguage(this)
        val newLang = if (currentLang == "tr") "en" else "tr"
        LocaleHelper.setLocale(this, newLang)
        Toast.makeText(this, getString(R.string.toast_language_changed), Toast.LENGTH_SHORT).show()
        recreate()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putDouble(KEY_CURRENT_MONEY, money)
    }
}
