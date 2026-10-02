package com.lanshare.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LanShareTheme { App() }
        }
    }
}

enum class Screen { SPLASH, MAIN }

@Composable
fun App() {
    val vm: ShareViewModel = viewModel()
    var screen by remember { mutableStateOf(Screen.SPLASH) }

    // 背景层，全局共享（10 秒切换一张）
    WallpaperCarousel(intervalMs = 10000L, overlayAlpha = 0.6f) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                (fadeIn(tween(500)) + scaleIn(initialScale = 1.06f, animationSpec = tween(500)))
                    .togetherWith(fadeOut(tween(400)) + scaleOut(targetScale = 1.02f, animationSpec = tween(400)))
            },
            label = "screen"
        ) { s ->
            when (s) {
                Screen.SPLASH -> SplashScreen { screen = Screen.MAIN }
                Screen.MAIN -> ShareScreen(vm)
            }
        }
    }
}

@Composable
fun SplashScreen(onDone: () -> Unit) {
    val scale = remember { Animatable(0.3f) }
    val alpha = remember { Animatable(0f) }
    val rot = remember { Animatable(-20f) }
    val titleAlpha = remember { Animatable(0f) }
    val titleY = remember { Animatable(30f) }
    val subAlpha = remember { Animatable(0f) }

    val shimmer = rememberInfiniteTransition(label = "sh")
    val shX by shimmer.animateFloat(
        -0.4f, 1.4f,
        infiniteRepeatable(tween(2400, easing = LinearEasing), RepeatMode.Restart),
        label = "x"
    )
    val glow by shimmer.animateFloat(
        0.6f, 1f,
        infiniteRepeatable(tween(1500, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "g"
    )

    LaunchedEffect(Unit) {
        launch { alpha.animateTo(1f, tween(700)) }
        launch { scale.animateTo(1f, spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessLow)) }
        launch { rot.animateTo(0f, spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow)) }
        delay(300)
        launch { titleAlpha.animateTo(1f, tween(600)) }
        launch { titleY.animateTo(0f, spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessMediumLow)) }
        delay(350)
        launch { subAlpha.animateTo(1f, tween(500)) }
        delay(1100)
        onDone()
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(
            Modifier
                .size(300.dp)
                .scale(scale.value * glow)
                .alpha(0.25f * alpha.value)
                .background(
                    Brush.radialGradient(
                        listOf(MaterialTheme.colorScheme.primary.copy(alpha = 0.9f), Color.Transparent)
                    ),
                    CircleShape
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Box(
                Modifier
                    .size(140.dp)
                    .graphicsLayer {
                        scaleX = scale.value
                        scaleY = scale.value
                        rotationZ = rot.value
                        this.alpha = alpha.value
                    }
                    .shadow(28.dp, RoundedCornerShape(36.dp))
                    .clip(RoundedCornerShape(36.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("📡", fontSize = 60.sp)
                Box(
                    Modifier.fillMaxSize().background(
                        Brush.linearGradient(
                            listOf(Color.Transparent, Color.White.copy(alpha = 0.35f), Color.Transparent),
                            start = Offset(shX * 400f, -100f),
                            end = Offset(shX * 400f + 80f, 400f)
                        )
                    )
                )
            }

            Spacer(Modifier.height(32.dp))

            Text(
                "LanShare",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .graphicsLayer { this.alpha = titleAlpha.value }
                    .offset(y = titleY.value.dp),
                letterSpacing = 2.sp
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "局域网文件分享",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 4.sp,
                modifier = Modifier.graphicsLayer { this.alpha = subAlpha.value }
            )

            Spacer(Modifier.height(64.dp))

            Box(
                Modifier
                    .width(80.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.7f)
                        .graphicsLayer { this.alpha = alpha.value }
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0f),
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0f)
                                ),
                                start = Offset(shX * 160f, 0f),
                                end = Offset(shX * 160f + 80f, 0f)
                            )
                        )
                )
            }
        }
    }
}
