package com.mera.babycare

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.*

class ReportFoodActivity : AppCompatActivity() {

    private lateinit var container: LinearLayout
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_report)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main2)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        container = findViewById(R.id.foodItemsContainer) // Certifique-se que este ID existe no XML

        // Adiciona itens de exemplo
        addSampleItems()
    }

    private fun addSampleItems() {
        // Item 1
        addMealItem(
            dayLabel = "Hoje",
            startTime = System.currentTimeMillis() - 6 * 60 * 60 * 1000,
            endTime = System.currentTimeMillis(),
            volumeMl = 350,
            showDelete = true
        )

        // Item 2
        addMealItem(
            dayLabel = "Hoje",
            startTime = System.currentTimeMillis() - 4 * 60 * 60 * 1000,
            endTime = System.currentTimeMillis() - 2 * 60 * 60 * 1000,
            volumeMl = 500,
            showDelete = true
        )

        // Item 3
        addMealItem(
            dayLabel = "Ontem",
            startTime = System.currentTimeMillis() - 28 * 60 * 60 * 1000,
            endTime = System.currentTimeMillis() - 26 * 60 * 60 * 1000,
            volumeMl = 20000,
            showDelete = false
        )
    }

    private fun addMealItem(dayLabel: String, startTime: Long, endTime: Long, volumeMl: Int, showDelete: Boolean) {
        // Verifica se já existe cabeçalho para este dia
        var dayHeader: TextView? = null
        for (i in 0 until container.childCount) {
            val view = container.getChildAt(i)
            if (view is TextView && view.tag == "day_header_$dayLabel") {
                dayHeader = view
                break
            }
        }

        // Adiciona cabeçalho se não existir
        if (dayHeader == null) {
            dayHeader = TextView(this).apply {
                text = dayLabel
                textSize = 16f
                setTextColor(Color.BLACK)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 24.dpToPx(), 0, 8.dpToPx())
                }
                tag = "day_header_$dayLabel"
            }
            container.addView(dayHeader)
        }

        // Infla o layout do item exatamente como você forneceu
        val mealItem = LayoutInflater.from(this).inflate(R.layout.activity_item_food_report, container, false)

        // Configura os textos
        val startTimeText = timeFormat.format(Date(startTime))
        val endTimeText = timeFormat.format(Date(endTime))
        val duration = (endTime - startTime) / (60 * 1000) // em minutos

        mealItem.findViewById<TextView>(R.id.time_range).text =
            "$startTimeText às $endTimeText - ${duration / 60}h e ${duration % 60}min"

        mealItem.findViewById<TextView>(R.id.volume).text = "${volumeMl}ml"

        // Configura o botão de deletar
        val deleteButton = mealItem.findViewById<ImageView>(R.id.delete_button)
        if (showDelete) {
            deleteButton.visibility = View.VISIBLE
            deleteButton.setOnClickListener {
                container.removeView(mealItem)
                checkAndRemoveDayHeader(dayLabel)
            }
        } else {
            deleteButton.visibility = View.GONE
        }

        // Adiciona o item após o cabeçalho do dia
        val dayHeaderIndex = container.indexOfChild(dayHeader)
        container.addView(mealItem, dayHeaderIndex + 1)
    }

    private fun checkAndRemoveDayHeader(dayLabel: String) {
        var hasItemsForDay = false
        val dayHeader = container.findViewWithTag<TextView>("day_header_$dayLabel")

        // Verifica se ainda existem itens para este dia
        for (i in 0 until container.childCount) {
            val view = container.getChildAt(i)
            if (view.tag == "day_header_$dayLabel") continue

            if (i > container.indexOfChild(dayHeader)) {
                if (view is TextView && view.tag.toString().startsWith("day_header_")) break
                hasItemsForDay = true
                break
            }
        }

        if (!hasItemsForDay && dayHeader != null) {
            container.removeView(dayHeader)
        }
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
}