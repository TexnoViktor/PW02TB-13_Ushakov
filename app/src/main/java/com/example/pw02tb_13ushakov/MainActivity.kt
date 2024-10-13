package com.example.pw02tb_13ushakov

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Знаходимо всі необхідні елементи інтерфейсу за допомогою findViewById
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val inputQuantity = findViewById<EditText>(R.id.inputQuantity)
        val calculateButton = findViewById<Button>(R.id.calculateButton)

        // Обробка натискання кнопки "Розрахувати"
        calculateButton.setOnClickListener {
            // Отримуємо індекс вибраного палива
            val selectedFuelIndex = when (radioGroup.checkedRadioButtonId) {
                R.id.radio_fuel_1 -> 0 // Вугілля
                R.id.radio_fuel_2 -> 1 // Мазут
                R.id.radio_fuel_3 -> 2 // Газ
                else -> -1
            }

            if (selectedFuelIndex == -1) {
                Toast.makeText(this, "Будь ласка, виберіть тип палива", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Отримуємо значення маси/об'єму палива, введені користувачем
            val userInput = inputQuantity.text.toString().toDoubleOrNull()

            if (userInput == null || userInput <= 0.0) {
                Toast.makeText(this, "Будь ласка, введіть коректне значення маси/об'єму палива", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Передаємо дані у інше активіті
            val intent = Intent(this, ResultActivity::class.java).apply {
                putExtra("fuelType", selectedFuelIndex)
                putExtra("fuelQuantity", userInput)
            }

            startActivity(intent)
        }
    }
}
