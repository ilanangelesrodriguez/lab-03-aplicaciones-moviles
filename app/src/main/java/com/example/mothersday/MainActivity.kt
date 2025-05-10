package com.example.mothersday

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var etMomName: TextInputEditText
    private lateinit var btnGreet: MaterialButton
    private lateinit var tvGreeting: TextView
    private lateinit var cardGreeting: CardView
    private lateinit var ratingLove: RatingBar
    private lateinit var btnShowHearts: MaterialButton
    private lateinit var llHearts: LinearLayout
    private lateinit var sbFlower: SeekBar
    private lateinit var ivFlower: ImageView
    private lateinit var ivFlowerEffect: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar vistas
        etMomName = findViewById(R.id.etMomName)
        btnGreet = findViewById(R.id.btnGreet)
        tvGreeting = findViewById(R.id.tvGreeting)
        cardGreeting = findViewById(R.id.cardGreeting)
        ratingLove = findViewById(R.id.ratingLove)
        btnShowHearts = findViewById(R.id.btnShowHearts)
        llHearts = findViewById(R.id.llHearts)
        sbFlower = findViewById(R.id.sbFlower)
        ivFlower = findViewById(R.id.ivFlower)
        ivFlowerEffect = findViewById(R.id.ivFlowerEffect)

        // Iniciar animación de rotación para el efecto de la flor
        startFlowerEffectAnimation()

        // 1) Saludo personalizado con animación
        btnGreet.setOnClickListener {
            val name = etMomName.text.toString().trim().ifEmpty { "Mamá" }

            // Mensajes aleatorios para mamá
            val messages = arrayOf(
                "¡Feliz Día de la Madre, $name! 🌷 Gracias por tu amor infinito.",
                "Para la mejor mamá del mundo: $name ❤️ ¡Te quiero muchísimo!",
                "$name, eres mi inspiración y mi fuerza. ¡Feliz día! 🌹",
                "Cada día agradezco tenerte como mamá, $name. ¡Te adoro! 💖",
                "No hay palabras para expresar cuánto te quiero, $name. ¡Feliz día! 🌺"
            )

            tvGreeting.text = messages.random()

            // Mostrar la tarjeta de saludo con animación
            if (cardGreeting.visibility == View.GONE) {
                cardGreeting.visibility = View.VISIBLE
                animateCardAppearance(cardGreeting)
            } else {
                // Animar cambio de texto
                animateTextChange(tvGreeting)
            }
        }

        // 2) Mostrar corazones según rating con animación
        btnShowHearts.setOnClickListener {
            llHearts.removeAllViews()
            val count = ratingLove.rating.toInt()

            // Colores para los corazones
            val heartColors = arrayOf(
                "#FF4081", "#E91E63", "#D81B60",
                "#C2185B", "#AD1457", "#880E4F"
            )

            // Añadir corazones con animación
            for (i in 0 until count) {
                val heart = ImageView(this).apply {
                    setImageResource(R.drawable.ic_favorite)

                    // Tamaño aleatorio para variedad
                    val baseSize = resources.getDimensionPixelSize(R.dimen.heart_size)
                    val randomFactor = (0.8 + Math.random() * 0.4).toFloat() // 0.8 a 1.2
                    val size = (baseSize * randomFactor).toInt()

                    layoutParams = LinearLayout.LayoutParams(size, size).apply {
                        setMargins(8, 0, 8, 0)
                    }

                    // Color aleatorio de la paleta
                    setColorFilter(android.graphics.Color.parseColor(heartColors[i % heartColors.size]))

                    // Inicialmente invisible para la animación
                    alpha = 0f
                    scaleX = 0f
                    scaleY = 0f
                }

                llHearts.addView(heart)

                // Animar aparición con delay según posición
                heart.postDelayed({
                    animateHeartAppearance(heart)
                }, (i * 150).toLong())
            }

            // Vibrar el botón
            animateButtonPress(btnShowHearts)
        }

        // 3) Escalando Flor con efectos visuales
        sbFlower.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, prog: Int, fromUser: Boolean) {
                // prog va de 0 a 200; 100 = tamaño original
                val scale = prog / 100f

                // Animar el cambio de escala suavemente
                val scaleXAnim = ObjectAnimator.ofFloat(ivFlower, "scaleX", scale)
                val scaleYAnim = ObjectAnimator.ofFloat(ivFlower, "scaleY", scale)

                val animSet = AnimatorSet()
                animSet.playTogether(scaleXAnim, scaleYAnim)
                animSet.duration = 100
                animSet.start()

                // Ajustar también el efecto
                ivFlowerEffect.scaleX = scale * 1.2f
                ivFlowerEffect.scaleY = scale * 1.2f

                // Ajustar la opacidad del efecto según el tamaño
                ivFlowerEffect.alpha = 0.1f + (scale * 0.3f)
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // Efecto al comenzar a tocar
                ivFlower.animate().rotation(5f).setDuration(300).start()
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // Efecto al soltar
                ivFlower.animate().rotation(0f).setDuration(500).start()

                // Pequeña animación de rebote
                val bounceAnim = ObjectAnimator.ofFloat(ivFlower, "translationY", 0f, -20f, 0f)
                bounceAnim.duration = 500
                bounceAnim.interpolator = AccelerateDecelerateInterpolator()
                bounceAnim.start()
            }
        })
    }

    // Animación continua para el efecto de la flor
    private fun startFlowerEffectAnimation() {
        val rotateAnim = ObjectAnimator.ofFloat(ivFlowerEffect, "rotation", 0f, 360f)
        rotateAnim.duration = 20000 // 20 segundos por rotación
        rotateAnim.repeatCount = ObjectAnimator.INFINITE
        rotateAnim.interpolator = AccelerateDecelerateInterpolator()
        rotateAnim.start()
    }

    // Animación para la aparición de la tarjeta
    private fun animateCardAppearance(card: CardView) {
        card.alpha = 0f
        card.translationY = 50f
        card.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(500)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }

    // Animación para cambio de texto
    private fun animateTextChange(textView: TextView) {
        textView.animate()
            .alpha(0f)
            .setDuration(150)
            .withEndAction {
                // El texto ya se ha cambiado en el listener
                textView.animate()
                    .alpha(1f)
                    .setDuration(300)
                    .start()
            }
            .start()
    }

    // Animación para la aparición de corazones
    private fun animateHeartAppearance(heart: ImageView) {
        val scaleX = ObjectAnimator.ofFloat(heart, "scaleX", 0f, 1.2f, 1f)
        val scaleY = ObjectAnimator.ofFloat(heart, "scaleY", 0f, 1.2f, 1f)
        val alpha = ObjectAnimator.ofFloat(heart, "alpha", 0f, 1f)

        val animSet = AnimatorSet()
        animSet.playTogether(scaleX, scaleY, alpha)
        animSet.duration = 400
        animSet.interpolator = AccelerateDecelerateInterpolator()
        animSet.start()
    }

    // Animación para el botón al presionarlo
    private fun animateButtonPress(button: Button) {
        val scaleDown = ObjectAnimator.ofFloat(button, "scaleX", 1f, 0.9f)
        val scaleUp = ObjectAnimator.ofFloat(button, "scaleX", 0.9f, 1f)

        val animSet = AnimatorSet()
        animSet.play(scaleDown).before(scaleUp)
        animSet.duration = 100
        animSet.start()
    }
}