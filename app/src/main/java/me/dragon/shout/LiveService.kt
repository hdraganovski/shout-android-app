package me.dragon.shout

import com.google.gson.Gson
import okhttp3.*

class LiveService private constructor() {
    val callbackInterfaces = mutableListOf<Listener>()
    lateinit var client: OkHttpClient
    lateinit var ws: WebSocket
    lateinit var listener: WebSocketListener
    var gson = Gson()

    fun start() {
        client = OkHttpClient()
        val request = Request.Builder().url(wsURL).build()
        listener = object: WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)

                try {
                    val liveServiceData = gson.fromJson(text, LiveServiceData::class.java)
                    callbackInterfaces.forEach {
                        it.onReceive(liveServiceData)
                    }
                }
                catch(ignore: Throwable) {
                    callbackInterfaces.forEach {
                        it.onReceive(text)
                    }
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                callbackInterfaces.forEach {
                    it.onError(t.localizedMessage)
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                callbackInterfaces.forEach {
                    it.onError(reason)
                }
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                callbackInterfaces.forEach {
                    it.onError(reason)
                }
            }
        }

        ws = client.newWebSocket(request, listener)
    }

    fun addListener(l: Listener) {
        callbackInterfaces.add(l)
    }

    fun removeListener(l: Listener?) {
        if(l != null){
            callbackInterfaces.remove(l)
        }
        else {
            callbackInterfaces.removeAll { true }
        }
    }

    fun end() {
        ws.close(200, "Ok")
        client.dispatcher().executorService().shutdown()
        removeListener(null)
    }

    interface Listener {
        fun onReceive(str: String)
        fun onReceive(obj: LiveServiceData)
        fun onError(str: String)
    }

    companion object {
        @JvmStatic
        val instance = LiveService()
        val wsURL = "ws://shout-live-service.herokuapp.com/ws"
    }
}

data class LiveServiceData(
    var lon: Double,
    var lat: Double,
    var topics: List<String>,
    var title: String
)
