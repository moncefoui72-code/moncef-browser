package com.moncefoui72.browser

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import android.widget.LinearLayout
import android.widget.TextView
import android.view.View
import android.widget.Button
import com.google.android.exoplayer2.ui.PlayerView

class MainActivity : AppCompatActivity() {
    private lateinit var player: SimpleExoPlayer
    private lateinit var fabDownload: FloatingActionButton
    private var downloadQueueCount = 0
    private lateinit var qualitiesList: LinearLayout

    // مثال لروابط لكل جودة. في التطبيق الحقيقي تحصل هذه الروابط من الـ API أو تحليل الصفحة
    private val qualities = mapOf(
        "720P" to "https://storage.example.com/video_720.mp4",
        "480P" to "https://storage.example.com/video_480.mp4",
        "360P" to "https://storage.example.com/video_360.mp4",
        "250P" to "https://storage.example.com/video_250.mp4"
    )
    private var selectedQuality = "720P"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // init player
        player = SimpleExoPlayer.Builder(this).build()
        val playerView = findViewById<PlayerView>(R.id.playerView)
        playerView.player = player

        qualitiesList = findViewById(R.id.qualitiesList)
        buildQualityList()

        // play initial
        playQuality(selectedQuality)

        fabDownload = findViewById(R.id.fabDownload)
        fabDownload.setOnClickListener {
            enqueueDownload(qualities[selectedQuality]!!)
        }
        updateFabBadge()
    }

    private fun buildQualityList() {
        qualitiesList.removeAllViews()
        for ((q, url) in qualities) {
            val item = layoutInflater.inflate(android.R.layout.simple_list_item_1, qualitiesList, false) as TextView
            item.text = q
            item.textSize = 18f
            item.setPadding(24, 24, 24, 24)
            item.setOnClickListener {
                selectedQuality = q
                playQuality(q)
                highlightSelected(item)
            }
            qualitiesList.addView(item)
            if (q == selectedQuality) highlightSelected(item)
        }
    }

    private fun highlightSelected(view: TextView) {
        // تبسيط: لون الخلفية
        for (i in 0 until qualitiesList.childCount) {
            val v = qualitiesList.getChildAt(i) as TextView
            v.setBackgroundColor(0) // clear
        }
        view.setBackgroundColor(getColor(android.R.color.holo_blue_light))
    }

    private fun playQuality(q: String) {
        val url = qualities[q]!!
        player.setMediaItem(MediaItem.fromUri(Uri.parse(url)))
        player.prepare()
        player.play()
    }

    override fun onStop() {
        super.onStop()
        player.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    // ---------- تنزيل عبر WorkManager بسيط ----------
    private fun enqueueDownload(url: String) {
        val data = androidx.work.Data.Builder().putString("url", url).build()
        val request = OneTimeWorkRequestBuilder<DownloadWorker>().setInputData(data).build()
        WorkManager.getInstance(this).enqueue(request)
        downloadQueueCount++
        updateFabBadge()
    }

    private fun updateFabBadge() {
        // إنشاء badge بسيط فوق الFAB
        val badge = BadgeDrawable.create(this)
        if (downloadQueueCount > 0) {
            badge.isVisible = true
            badge.number = downloadQueueCount
            BadgeUtils.attachBadgeDrawable(badge, fabDownload, findViewById(android.R.id.content))
        } else {
            badge.isVisible = false
        }
    }
}
