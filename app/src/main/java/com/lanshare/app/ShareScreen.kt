package com.lanshare.app

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareScreen(vm: ShareViewModel) {
    val ui by vm.ui.collectAsStateWithLifecycle()
    val ctx = LocalContext.current
    var showQr by remember { mutableStateOf(false) }
    var q by remember { mutableStateOf("") }
    var msg by remember { mutableStateOf<String?>(null) }
    val snack = remember { SnackbarHostState() }

    LaunchedEffect(msg) {
        msg?.let { snack.showSnackbar(it); msg = null }
    }

    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = { SnackbarHost(snack) },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
                ),
                title = {
                    Column {
                        Text("LanShare", fontWeight = FontWeight.Bold, fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary)
                        Text(ui.url.ifEmpty { "启动中…" }, fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1, overflow = TextOverflow.Ellipsis,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val c = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        c.setPrimaryClip(ClipData.newPlainText("url", ui.url))
                        msg = "地址已复制"
                    }) { Icon(Icons.Filled.ContentCopy, "复制") }
                    IconButton(onClick = { vm.refresh() }) {
                        Icon(Icons.Filled.Refresh, "刷新")
                    }
                }
            )
        }
    ) { pad ->
        LazyColumn(
            Modifier.fillMaxSize().padding(pad),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item { StatusCard(ui) { showQr = true } }

            item {
                Spacer(Modifier.height(8.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("文件", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        Spacer(Modifier.width(8.dp))
                        Surface(shape = RoundedCornerShape(50),
                            color = MaterialTheme.colorScheme.primaryContainer) {
                            Text("${ui.files.size}",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                                fontSize = 12.sp, fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary)
                        }
                    }
                    OutlinedTextField(
                        value = q, onValueChange = { q = it },
                        placeholder = { Text("搜索…", fontSize = 13.sp) },
                        singleLine = true, modifier = Modifier.width(150.dp),
                        shape = RoundedCornerShape(10.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp),
                        leadingIcon = { Icon(Icons.Filled.Search, null, Modifier.size(16.dp)) }
                    )
                }
            }

            val filtered = remember(ui.files, q) {
                if (q.isBlank()) ui.files
                else ui.files.filter { it.name.contains(q, ignoreCase = true) }
            }

            if (filtered.isEmpty()) {
                item { EmptyState(q.isNotBlank()) }
            } else {
                items(filtered, key = { it.name }) { f ->
                    FileRow(f, onDelete = { vm.delete(f.name) })
                }
            }
            item { Spacer(Modifier.height(60.dp)) }
        }
    }

    if (showQr && ui.url.isNotEmpty()) {
        QrDialog(ui.url) { showQr = false }
    }
}

@Composable
private fun StatusCard(ui: ShareUiState, onQrClick: () -> Unit) {
    var bmp by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    LaunchedEffect(ui.url) {
        if (ui.url.isEmpty()) return@LaunchedEffect
        bmp = withContext(Dispatchers.Default) { makeQr(ui.url, 400, 1) }
    }

    Card(
        Modifier.fillMaxWidth().shadow(8.dp, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        )
    ) {
        Column {
            Box(Modifier.fillMaxWidth().height(4.dp).background(
                Brush.horizontalGradient(listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.secondary,
                    MaterialTheme.colorScheme.primary
                ))
            ))
            Row(Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(92.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                        .clickable(onClick = onQrClick),
                    contentAlignment = Alignment.Center
                ) {
                    bmp?.let {
                        Image(it.asImageBitmap(), "二维码",
                            modifier = Modifier.fillMaxSize().padding(6.dp))
                    } ?: CircularProgressIndicator(Modifier.size(24.dp), strokeWidth = 2.dp)
                }
                Spacer(Modifier.width(16.dp))
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(9.dp).clip(CircleShape).background(
                            if (ui.running) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.error))
                        Spacer(Modifier.width(8.dp))
                        Text(if (ui.running) "服务运行中" else "服务已停止",
                            fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(ui.url.ifEmpty { "等待网络…" }, fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium, maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                    Spacer(Modifier.height(4.dp))
                    Text("扫码或输入地址 · ${ui.files.size} 个文件",
                        fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun FileRow(file: FileItem, onDelete: () -> Unit) {
    val icon = when {
        file.name.endsWith(".jpg", true) || file.name.endsWith(".png", true) ||
        file.name.endsWith(".jpeg", true) || file.name.endsWith(".webp", true) -> "🖼️"
        file.name.endsWith(".mp4", true) || file.name.endsWith(".mkv", true) -> "🎬"
        file.name.endsWith(".mp3", true) || file.name.endsWith(".wav", true) -> "🎵"
        file.name.endsWith(".pdf", true) -> "📄"
        file.name.endsWith(".zip", true) || file.name.endsWith(".rar", true) -> "📦"
        else -> "📎"
    }
    var pressed by remember { mutableStateOf(false) }
    val bg by animateColorAsState(
        if (pressed) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
        else MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        tween(180), label = "bg"
    )
    Card(
        Modifier.fillMaxWidth().clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) { pressed = !pressed },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = bg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(46.dp).clip(RoundedCornerShape(13.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center) { Text(icon, fontSize = 22.sp) }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(file.name, fontWeight = FontWeight.Medium, fontSize = 14.sp,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(3.dp))
                Text("${fmtSize(file.size)} · ${fmtTime(file.mtime)}",
                    fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.DeleteOutline, "删除",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
private fun EmptyState(searching: Boolean) {
    Column(Modifier.fillMaxWidth().padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(if (searching) "🔍" else "📂", fontSize = 40.sp)
        Spacer(Modifier.height(12.dp))
        Text(if (searching) "没有匹配的文件" else "还没有文件
从电脑浏览器上传试试",
            color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center)
    }
}

@Composable
private fun QrDialog(url: String, onDismiss: () -> Unit) {
    var bmp by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    LaunchedEffect(url) { bmp = withContext(Dispatchers.Default) { makeQr(url, 800, 2) } }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = onDismiss) { Text("关闭") } },
        title = { Text("扫码访问", fontWeight = FontWeight.Bold) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                bmp?.let {
                    Image(it.asImageBitmap(), null,
                        modifier = Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp)))
                } ?: CircularProgressIndicator()
                Spacer(Modifier.height(12.dp))
                Text(url, fontSize = 12.sp,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            }
        }
    )
}

private fun makeQr(text: String, size: Int, margin: Int): android.graphics.Bitmap? {
    return try {
        val h = mapOf(EncodeHintType.MARGIN to margin)
        val m = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size, h)
        val b = android.graphics.Bitmap.createBitmap(m.width, m.height,
            android.graphics.Bitmap.Config.ARGB_8888)
        for (x in 0 until m.width) for (y in 0 until m.height)
            b.setPixel(x, y, if (m[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
        b
    } catch (_: Exception) { null }
}

private fun fmtSize(n: Long): String {
    if (n < 1024) return "$n B"
    val u = arrayOf("KB", "MB", "GB", "TB")
    var v = n.toDouble(); var i = -1
    do { v /= 1024; i++ } while (v >= 1024 && i < u.size - 1)
    return if (v >= 100) "${v.toInt()} ${u[i]}" else String.format("%.1f %s", v, u[i])
}

private fun fmtTime(ms: Long): String {
    val d = (System.currentTimeMillis() - ms) / 1000
    return when {
        d < 60 -> "刚刚"
        d < 3600 -> "${d / 60} 分钟前"
        d < 86400 -> "${d / 3600} 小时前"
        d < 604800 -> "${d / 86400} 天前"
        else -> java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
            .format(java.util.Date(ms))
    }
}
