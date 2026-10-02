package com.lanshare.app

import android.content.Context
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicReference

object WallpaperStore {
    private val cache = AtomicReference<Map<String, android.graphics.Bitmap>>(emptyMap())
    private val names = AtomicReference<List<String>>(emptyList())

    fun load(ctx: Context): Map<String, android.graphics.Bitmap> {
        val cur = cache.get()
        if (cur.isNotEmpty()) return cur
        val result = try {
            val list = ctx.assets.list("wallpapers")?.sorted() ?: emptyList()
            names.set(list)
            list.associateWith { n ->
                ctx.assets.open("wallpapers/" + n).use { android.graphics.BitmapFactory.decodeStream(it) }
            }.filterValues { it != null }.mapValues { it.value!! }
        } catch (e: Exception) { emptyMap() }
        cache.set(result)
        return result
    }

    fun names(): List<String> = names.get()
}

@Composable
fun WallpaperCarousel(
    intervalMs: Long = 10000L,
    overlayAlpha: Float = 0.55f,
    content: @Composable () -> Unit
) {
    val ctx = LocalContext.current
    val dark = isSystemInDarkTheme()
    var bitmaps by remember { mutableStateOf<List<android.graphics.Bitmap>>(emptyList()) }
    var index by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        bitmaps = withContext(Dispatchers.IO) {
            WallpaperStore.load(ctx).values.toList()
        }
    }

    LaunchedEffect(bitmaps.size) {
        if (bitmaps.size <= 1) return@LaunchedEffect
        while (true) {
            delay(intervalMs)
            index = (index + 1) % bitmaps.size
        }
    }

    Box(Modifier.fillMaxSize()) {
        if (bitmaps.isNotEmpty()) {
            val current = bitmaps[index]
            androidx.compose.animation.Crossfade(
                targetState = current,
                animationSpec = tween(1400, easing = FastOutSlowInEasing),
                label = "wp"
            ) { bmp ->
                // Ken Burns 缓动
                val infinite = rememberInfiniteTransition(label = "kb")
                val s by infinite.animateFloat(
                    initialValue = 1.0f,
                    targetValue = 1.08f,
                    animationSpec = infiniteRepeatable(
                        tween(14000, easing = LinearEasing),
                        RepeatMode.Reverse
                    ),
                    label = "scale"
                )
                Image(
                    bitmap = bmp.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                        .androidx.compose.ui.draw.scale(s),
                    contentScale = ContentScale.Crop
                )
            }
        }
        // 遮罩渐变，保证内容可读
        Box(
            Modifier.fillMaxSize().background(
                Brush.verticalGradient(
                    listOf(
                        (if (dark) Color(0xFF0E1014) else Color(0xFFFFFFFF)).copy(alpha = overlayAlpha),
                        (if (dark) Color(0xFF0E1014) else Color(0xFFF7F8FC)).copy(alpha = overlayAlpha + 0.1f),
                        (if (dark) Color(0xFF0E1014) else Color(0xFFF7F8FC)).copy(alpha = 0.95f)
                    )
                )
            )
        )
        content()
    }
}
