package com.mera.babycare

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries


class ReportActivity : AppCompatActivity() {
    private lateinit var foodButton: ImageButton
    private lateinit var sleepButton: ImageButton
    private lateinit var relatoryTitle: TextView
    private lateinit var sleepScroll: ScrollView
    private lateinit var databaseHelper: Database
    private lateinit var dbManager: DataBaseManager
    private lateinit var graph: GraphView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_report)

        // Inicializar Banco de Dados
        databaseHelper = Database(this)
        dbManager = DataBaseManager(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main2)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar os elementos
        foodButton = findViewById(R.id.foodButton)
        sleepButton = findViewById(R.id.sleepButton)
        relatoryTitle = findViewById(R.id.relatoryTitle)
        sleepScroll = findViewById(R.id.sleepScroll)
        graph = findViewById(R.id.sleepgraph);

        // Configurar listeners de clique
        foodButton.setOnClickListener {
            setSelectedButton(true)
        }

        sleepButton.setOnClickListener {
            setSelectedButton(false)
            loadSleepSessions()
        }


        // Definir estado inicial (foodButton selecionado por padrão)
        setSelectedButton(true)
    }



    private fun loadSleepSessions() {
        val db = databaseHelper.readableDatabase

        // Query all sleep sessions
        val cursor = db.query(
            "sleep_sessions",
            arrayOf("id", "baby_id", "start_time", "end_time", "is_nap", "notes", "created_at"),
            null, null, null, null, "created_at DESC"
        )

        Log.d("SleepSessions", "Found ${cursor.count} sleep sessions")

    }

    private fun setSelectedButton(isFoodSelected: Boolean) {
        if (isFoodSelected) {
            foodButton.setImageResource(R.drawable.relatory_selected)
            sleepButton.setImageResource(R.drawable.relatory_unselected)
            relatoryTitle.text = "Histórico de Refeições"
            sleepScroll.visibility = View.GONE
            // Aqui você pode adicionar lógica para mostrar o conteúdo de comida
        } else {
            foodButton.setImageResource(R.drawable.relatory_unselected)
            sleepButton.setImageResource(R.drawable.relatory_selected)
            relatoryTitle.text = "Análise de Sono"

            val series = LineGraphSeries(
                arrayOf(
                    DataPoint(1.0, 5.0),
                    DataPoint(2.0, 3.0),
                    DataPoint(3.0, 2.0),
                    DataPoint(4.0, 6.0),
                    DataPoint(5.0, 6.0),
                    DataPoint(6.0, 6.0),
                    DataPoint(7.0, 6.0),
                )
            )
            graph.addSeries(series)
            series.color = Color.parseColor("#FFCBC5")

            val weekdays = arrayOf("", "S", "T", "Q", "Q", "S", "S", "D") // Index 0 is empty

            graph.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {
                override fun formatLabel(value: Double, isValueX: Boolean): String {
                    return if (isValueX) {
                        val index = value.toInt()
                        if (index in 1..7) weekdays[index] else ""
                    } else {
                        super.formatLabel(value, isValueX)
                    }
                }
            }
            /*graph.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {
                private val weekdays = arrayOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sab", "Dom")

                override fun formatLabel(value: Double, isValueX: Boolean): String {
                    return if (isValueX) {
                        if (value >= 0 && value < weekdays.size) {
                            weekdays[value.toInt()]
                        } else {
                            ""
                        }
                    } else {
                        super.formatLabel(value, isValueX)
                    }
                }
            }*/
            graph.viewport.setMinX(0.5)  // Small margin before first point
            graph.viewport.setMaxX(7.5)  // Small margin before first point
            graph.viewport.isXAxisBoundsManual = true
            graph.viewport.isYAxisBoundsManual = true
            graph.gridLabelRenderer.padding = 32
            //graph.gridLabelRenderer.setHorizontalLabelsAngle(45)
            graph.gridLabelRenderer.numHorizontalLabels = 8
            graph.gridLabelRenderer.verticalAxisTitle = "Horas"
            graph.gridLabelRenderer.verticalAxisTitleTextSize = 30f // in SP
            graph.gridLabelRenderer.verticalAxisTitle
            graph.gridLabelRenderer.verticalAxisTitleColor = Color.BLACK
            graph.gridLabelRenderer.gridStyle = GridLabelRenderer.GridStyle.HORIZONTAL

            sleepScroll.visibility = View.VISIBLE
            // Aqui você pode adicionar lógica para mostrar o conteúdo de sono
        }
    }
}