package com.lanshare.app

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.net.Inet4Address
import java.net.NetworkInterface

data class ShareUiState(
    val running: Boolean = false,
    val url: String = "",
    val port: Int = 8080,
    val files: List<FileItem> = emptyList(),
)

data class FileItem(val name: String, val size: Long, val mtime: Long)

class ShareViewModel(app: Application) : AndroidViewModel(app) {
    private val _ui = MutableStateFlow(ShareUiState())
    val ui: StateFlow<ShareUiState> = _ui.asStateFlow()

    private var server: FileShareServer? = null
    private var cm: ConnectivityManager? = null
    private var cb: ConnectivityManager.NetworkCallback? = null

    private val storageDir: File
        get() = File(getApplication<Application>().getExternalFilesDir(null), "share").apply { mkdirs() }

    init { start(); watch() }

    fun start() {
        stop()
        val port = _ui.value.port
        val srv = FileShareServer(storageDir, port) { refresh() }
        try {
            srv.start()
            server = srv
            val ip = lanIp() ?: "127.0.0.1"
            _ui.value = _ui.value.copy(running = true, url = "http://$ip:$port")
            refresh()
        } catch (e: Exception) {
            _ui.value = _ui.value.copy(running = false)
        }
    }

    fun stop() { server?.stop(); server = null }

    private fun watch() {
        val ctx = getApplication<Application>()
        cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cb = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) { restart() }
            override fun onLost(network: Network) { restart() }
            override fun onLinkPropertiesChanged(network: Network, lp: LinkProperties) { restart() }
        }.also { cm?.registerDefaultNetworkCallback(it) }
    }

    private fun restart() { viewModelScope.launch { delay(800); start() } }

    private fun lanIp(): String? {
        try {
            for (nif in NetworkInterface.getNetworkInterfaces()) {
                if (!nif.isUp || nif.isLoopback) continue
                for (a in nif.inetAddresses) {
                    if (!a.isLoopbackAddress && a is Inet4Address) {
                        val ip = a.hostAddress ?: continue
                        if (ip.startsWith("192.168.") || ip.startsWith("10.") || ip.startsWith("172."))
                            return ip
                    }
                }
            }
        } catch (_: Exception) {}
        return null
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = storageDir.listFiles()?.filter { !it.name.startsWith(".") }
                ?.map { FileItem(it.name, it.length(), it.lastModified()) }
                ?.sortedByDescending { it.mtime } ?: emptyList()
            _ui.value = _ui.value.copy(files = list)
        }
    }

    fun delete(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            File(storageDir, name).takeIf { it.exists() }?.delete()
            refresh()
        }
    }

    override fun onCleared() {
        cb?.let { cm?.unregisterNetworkCallback(it) }
        stop()
        super.onCleared()
    }
}
