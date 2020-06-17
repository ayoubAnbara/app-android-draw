package anbara.ayoub.drawing

import anbara.ayoub.drawing.adapter.IMAGE_PATH
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : AppCompatActivity() {
    private lateinit var mInterstitialAd: InterstitialAd
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_image)
        showMyInterstitialAd()
        val path = intent.getStringExtra(IMAGE_PATH)
        Glide.with(this).load(path).into(image_view)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        }
    }
    fun showMyInterstitialAd(){
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = getString(R.string.interstitial_id)
        mInterstitialAd.loadAd(AdRequest.Builder()
                .tagForChildDirectedTreatment(true)
                .build())
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                mInterstitialAd.loadAd(AdRequest.Builder().tagForChildDirectedTreatment(true).build())
            }
        }
    }
}
