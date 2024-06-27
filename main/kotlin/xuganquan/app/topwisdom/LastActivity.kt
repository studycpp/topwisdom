package xuganquan.app.topwisdom

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import xuganquan.app.topwisdom.databinding.ActivityLastBinding


class LastActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLastBinding
    private lateinit var webView:WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_last)

        val bundle=this.intent.getBundleExtra("id_revelation")

        var str=bundle?.getString("id_revelation")


        webView=findViewById<WebView>(R.id.idLastWebView)

        //webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)///!!!!!!!!!
        ///webView.setBackgroundColor(Color.parseColor("#000000"))
        val webSettings= webView.getSettings()
        webSettings.setJavaScriptEnabled(true)  //////////////////
        webSettings.setBuiltInZoomControls(true)
        webSettings.setDisplayZoomControls(false)
        webSettings.setSupportZoom(true)

        webSettings.setDomStorageEnabled(true)////这个代码是关键，这个让webView设置支持DomStorage。
        webSettings.setDatabaseEnabled(true) //////////////////

        webSettings.setLoadWithOverviewMode(true)
        webSettings.setUseWideViewPort(true)


        if (str!=null)
            webView.loadUrl(str)
        else
            webView.loadUrl("""https://www.alipay.com/""")
        /*
        val vedioStr=bundle?.getString("id_revelation")
        val vv=findViewById<VideoView>(R.id.idLastVideoView)

        vv.setVideoURI(Uri.parse(vedioStr))
        val mediaController = MediaController(this@LastActivity)
        vv.setMediaController(mediaController)
        vv.requestFocus()
        vv.start() //播放   ，不播放的话，需要手动点击视频的三角图标来播放
        */


    }


    override fun onDestroy() {
        super.onDestroy()
        webView.destroy()
    }

}