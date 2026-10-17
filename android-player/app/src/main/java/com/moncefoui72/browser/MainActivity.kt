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
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector

class MainActivity : AppCompatActivity() {
    private lateinit var player: SimpleExoPlayer
    private lateinit var trackSelector: DefaultTrackSelector
    private lateinit var fabDownload: FloatingActionButton
    private var downloadQueueCount = 0
    private lateinit var qualitiesList: LinearLayout

    // مثال لروابط لكل جودة. في التطبيق الحقيقي تحصل هذه الروابط من الـ API أو تحليل الصفحة
    private val qualities = mapOf(
        "720P" to "https://storage.example.com/video_720.m3u8",
        "480P" to "https://storage.example.com/video_480.m3u8",
        "360P" to "https://storage.example.com/video_360.m3u8",
        "250P" to "https://storage.example.com/video_250.m3u8"
    )
    private var selectedQuality = "720P"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create a DefaultTrackSelector to support HLS/DASH track selection
        trackSelector = DefaultTrackSelector(this)
        trackSelector.setParameters(trackSelector.buildUponParameters().setDisableAdaptiveSelection(false))

        // init player with the track selector
        player = SimpleExoPlayer.Builder(this).setTrackSelector(trackSelector).build()
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
        // If the source is HLS/DASH (manifest), we can instruct the track selector to prefer a maximum video size
        if (url.endsWith(".m3u8") || url.contains(".m3u8") || url.endsWith(".mpd")) {
            // set max video size based on quality choice to influence adaptive selection
            applyHlsQuality(q)
            player.setMediaItem(MediaItem.fromUri(Uri.parse(url)))
            player.prepare()
            player.play()
        } else {
            // direct file per-quality (mp4...) -> just load that URL
            player.setMediaItem(MediaItem.fromUri(Uri.parse(url)))
            player.prepare()
            player.play()
        }
    }

    private fun applyHlsQuality(q: String) {
        // map quality to a maximum allowed video size (width x height)
        val paramsBuilder = trackSelector.buildUponParameters()
        when (q) {
            "720P" -> paramsBuilder.setMaxVideoSize(1280, 720)
            "480P" -> paramsBuilder.setMaxVideoSize(854, 480)
            "360P" -> paramsBuilder.setMaxVideoSize(640, 360)
            "250P" -> paramsBuilder.setMaxVideoSize(426, 240)
            else -> paramsBuilder.clearVideoSizeConstraints()
        }
        // Allow adaptive switching up to the max size
        paramsBuilder.setForceHighestSupportedBitrate(false)
        trackSelector.setParameters(paramsBuilder)
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
