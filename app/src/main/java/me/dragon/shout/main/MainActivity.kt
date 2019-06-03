package me.dragon.shout.main

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import me.dragon.shout.LiveService
import me.dragon.shout.R
import okhttp3.*
import okio.ByteString


class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener, Toolbar.OnMenuItemClickListener {

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpUI()
        LiveService.instance.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        LiveService.instance.end()
    }

    private fun setUpUI() {
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        main_viewPager.adapter = viewPagerAdapter
        main_viewPager.addOnPageChangeListener(this)

        main_bar.setNavigationOnClickListener {
            MainModalDialogFragment.newInstance().show(supportFragmentManager, "dialog")
        }
        main_bar.setOnMenuItemClickListener(this)
    }

    override fun onPageScrollStateChanged(state: Int) {
        /* nothing */
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        /* nothing */
    }

    override fun onPageSelected(position: Int) {
        val fabListener = object: FloatingActionButton.OnVisibilityChangedListener() {
            override fun onHidden(fab: FloatingActionButton?) {
                super.onHidden(fab)
                when(position) {
                    0 -> {
                        main_bar.replaceMenu(R.menu.map_menu)
                        main_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                        main_bar.hideOnScroll = false
                    }
                    1 -> {
                        main_bar.replaceMenu(R.menu.list_menu)
                        main_bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                        main_bar.hideOnScroll = true
                    }
                }
                fab?.show()
            }
        }

        main_fab.hide(fabListener)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.action_map -> {
                main_viewPager.setCurrentItem(0, true)
                true
            }
            R.id.action_list_view -> {
                main_viewPager.setCurrentItem(1, true)
                true
            }
            R.id.action_refresh -> {
                Toast.makeText(this, "Refreshing", Toast.LENGTH_SHORT)
                    .show()
                true
            }
            R.id.action_filter -> {
                SearchBottomDialog.newInstance().show(supportFragmentManager, "filter")
                true
            }
            else -> false
        }
    }


    private inner class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> MapFragment.newInstance()
                1 -> DashboardFragment.newInstance()
                else -> throw IndexOutOfBoundsException("$position")
            }
        }

        override fun getCount(): Int = 2
    }

    private fun startListeningWS() {
        val client = OkHttpClient()
        val request = Request
            .Builder()
            .url("ws://192.168.100.5:8080/ws")
            .build()
        val listener = object: WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.d("WS", "Opened")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log.d("WS", "Received: $text")
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                onMessage(webSocket, bytes.hex())
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                Log.d("WS", "Closed: $code \n $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.e("WS", "Message: ${response?.message()}")
                Log.e("WS", "Body: ${response?.body()}")
                Log.e("WS", "Response:  $response")
                Log.e("WS", t.localizedMessage, t)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                Log.d("WS", "Closed: $code \n $reason")
            }
        }

        val ws = client.newWebSocket(request, listener)
    }
}
