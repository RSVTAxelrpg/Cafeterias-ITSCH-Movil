package mx.tecnm.cdhidalgo.cafeterias_itsch

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Pair
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView

class Splash : AppCompatActivity() {

    private lateinit var logo: ImageView

    private lateinit var animacion: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        logo = findViewById(R.id.ivLogoXML)
        animacion = AnimationUtils.loadAnimation(this, R.anim.anim_splash)

        logo.startAnimation(animacion)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                val intent = Intent(this, Home::class.java)
                val transicion = ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    Pair(logo, "logo_transicionXML")
                )
                finish()
                startActivity(intent, transicion.toBundle())
            }, 2000
        )
    }
}