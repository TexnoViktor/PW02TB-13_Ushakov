package com.example.pw02tb_13ushakov

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import android.widget.Button


class ResultActivity : AppCompatActivity() {

    data class Fuel(
        val name: String,
        val lowerHeatingValue: Double,  // Q_r_i - Нижча теплота згоряння, МДж/кг
        val ashContent: Double,          // A_r - Вміст золи, %
        val volatileAshContent: Double,  // a_вин - частка золи, що виходить у вигляді леткої золи
        val collectionEfficiency: Double, // n_зу - Ефективність очищення димових газів від твердих частинок
        val combustibleContent: Double    // Г_вин - масовий вміст горючих речовин у викидах твердих частинок, %
    ) {
        // Константа ефективності золовловлення
        private val ashRemovalEfficiency = 0.985

        // Розрахунок коефіцієнта емісії K_тв
        fun calculateEmissionCoefficient(): Double {
            return (1e6 / lowerHeatingValue) * volatileAshContent *
                    (ashContent / (100 - combustibleContent)) * (1 - collectionEfficiency)
        }

        // Розрахунок валового викиду E_тв
        fun calculateGrossEmission(bi: Double): Double {
            val k_tv = calculateEmissionCoefficient()
            return 1e-6 * k_tv * lowerHeatingValue * bi // результат у тоннах
        }
    }
    // Клас для опису пального
    private val fuels = arrayOf(
        Fuel("Вугілля", lowerHeatingValue = 20.47, ashContent = 25.20, volatileAshContent = 0.8, collectionEfficiency = 0.985, combustibleContent = 1.5),
        Fuel("Мазут", lowerHeatingValue = 40.40, ashContent = 0.15, volatileAshContent = 1.0, collectionEfficiency = 0.985, combustibleContent = 0.0),
        Fuel("Газ", lowerHeatingValue = 33.08, ashContent = 0.0, volatileAshContent = 0.0, collectionEfficiency = 0.985, combustibleContent = 0.0)
    )

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Отримуємо дані з Intent
        val fuelTypeIndex = intent.getIntExtra("fuelType", 0)
        val fuelQuantity = intent.getDoubleExtra("fuelQuantity", 0.0)

        // Отримуємо відповідне паливо з масиву
        val selectedFuel = fuels[fuelTypeIndex]

        // Розраховуємо коефіцієнт емісії
        val emissionCoefficient = selectedFuel.calculateEmissionCoefficient()

        // Розраховуємо валовий викид
        val grossEmission = selectedFuel.calculateGrossEmission(fuelQuantity)

        // Виводимо результат
        val resultTextView = findViewById<TextView>(R.id.resultTextView)
        resultTextView.text = """
            Тип палива: ${selectedFuel.name}
            Коефіцієнт емісії (K_тв): ${String.format("%.3f", emissionCoefficient)} г/ГДж
            Валовий викид (E_тв): ${String.format("%.3f", grossEmission)} т
        """.trimIndent()

        // Обробка натискання кнопки "Назад"
        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            finish() // Закриває дану активність і повертає на попередній екран
        }
    }
}

