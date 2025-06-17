package com.mera.babycare

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageButton
import android.widget.TextView

class ReportActivity : AppCompatActivity() {
    private lateinit var foodButton: ImageButton
    private lateinit var sleepButton: ImageButton
    private lateinit var relatoryTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_report)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main2)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar os elementos
        foodButton = findViewById(R.id.foodButton)
        sleepButton = findViewById(R.id.sleepButton)
        relatoryTitle = findViewById(R.id.relatoryTitle)

        // Configurar listeners de clique
        foodButton.setOnClickListener {
            setSelectedButton(true)
        }

        sleepButton.setOnClickListener {
            setSelectedButton(false)
        }

        // Definir estado inicial (foodButton selecionado por padrão)
        setSelectedButton(true)
    }

    private fun setSelectedButton(isFoodSelected: Boolean) {
        if (isFoodSelected) {
            foodButton.setImageResource(R.drawable.relatory_selected)
            sleepButton.setImageResource(R.drawable.relatory_unselected)
            relatoryTitle.text = "Histórico de Refeições"
            // Aqui você pode adicionar lógica para mostrar o conteúdo de comida
        } else {
            foodButton.setImageResource(R.drawable.relatory_unselected)
            sleepButton.setImageResource(R.drawable.relatory_selected)
            relatoryTitle.text = "Análise de Sono"
            // Aqui você pode adicionar lógica para mostrar o conteúdo de sono
        }
    }
}