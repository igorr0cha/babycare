package com.mera.babycare

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val layoutEmail = findViewById<TextInputLayout>(R.id.layout_email)

        val icon = ContextCompat.getDrawable(this, R.drawable.mail_icon)
        if (icon != null) {
            val resizedIcon = resizeDrawable(icon, 20, 20)
            layoutEmail.setStartIconDrawable(resizedIcon)
        }

        val layoutPassword = findViewById<TextInputLayout>(R.id.layout_password)

        val icon_password = ContextCompat.getDrawable(this, R.drawable.password_icon)

        if (icon_password != null) {
            val resizedIcon = resizeDrawable(icon_password, 20, 20)
            layoutPassword.setStartIconDrawable(resizedIcon)
        }
    }

    fun resizeDrawable(drawable: Drawable, width: Int, height: Int): Drawable {
        val bitmap = (drawable as BitmapDrawable).bitmap
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
        return BitmapDrawable(resources, resizedBitmap)
    }
}