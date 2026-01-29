package org.prauga.measure.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.prauga.measure.ui.theme.BackgroundDark
import org.prauga.measure.ui.theme.GridColor
import org.prauga.measure.ui.theme.LevelGreen
import org.prauga.measure.ui.theme.LevelOrange
import kotlin.math.abs
import kotlin.math.asin
import kotlin.math.sqrt


@Composable
fun LevelComponent(
    tiltX: Float,
    tiltY: Float,
    modifier: Modifier = Modifier
) {
    val animatedX by animateFloatAsState(
        targetValue = tiltX,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "ballX"
    )
    val animatedY by animateFloatAsState(
        targetValue = tiltY,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "ballY"
    )

    val distanceFromCenter = sqrt(animatedX * animatedX + animatedY * animatedY)
    val isLevel = distanceFromCenter < 0.03f

    val ballColor by animateColorAsState(
        targetValue = if (isLevel) LevelGreen else LevelOrange,
        label = "ballColor"
    )

    val totalDegrees =
        Math.toDegrees(asin(distanceFromCenter.coerceIn(0f, 1f).toDouble())).toFloat()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val gridSpacing = 40f

            // vertical grid
            var x = 0f
            while (x <= size.width) {
                drawLine(
                    color = GridColor,
                    start = Offset(x, 0f),
                    end = Offset(x, size.height),
                    strokeWidth = 1f
                )
                x += gridSpacing
            }

            // horizontal grid
            var y = 0f
            while (y <= size.height) {
                drawLine(
                    color = GridColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1f
                )
                y += gridSpacing
            }

            val centerX = size.width / 2
            val centerY = size.height / 2
            val ballRadius = 300f
            val maxOffset = size.width / 3

            // Calculate ball separation based on tilt
            val separation = distanceFromCenter * maxOffset

            // Direction of tilt
            val dirX = if (distanceFromCenter > 0.001f) animatedX / distanceFromCenter else 0f
            val dirY = if (distanceFromCenter > 0.001f) animatedY / distanceFromCenter else 0f

            val ball1X = centerX - dirX * separation
            val ball1Y = centerY + dirY * separation

            val ball2X = centerX + dirX * separation
            val ball2Y = centerY - dirY * separation

            // ball shadows
            drawCircle(
                color = Color.Black.copy(alpha = 0.2f),
                radius = ballRadius,
                center = Offset(ball1X + 6f, ball1Y + 6f)
            )
            drawCircle(
                color = Color.Black.copy(alpha = 0.2f),
                radius = ballRadius,
                center = Offset(ball2X + 6f, ball2Y + 6f)
            )

            // Draw ball 1
            val gradient1 = Brush.radialGradient(
                colors = listOf(
                    ballColor.copy(alpha = 0.5f),
                    ballColor.copy(alpha = 0.35f),
                    ballColor.copy(alpha = 0.2f)
                ),
                center = Offset(ball1X - ballRadius * 0.3f, ball1Y - ballRadius * 0.3f),
                radius = ballRadius * 1.5f
            )
            drawCircle(
                brush = gradient1,
                radius = ballRadius,
                center = Offset(ball1X, ball1Y)
            )

            // Draw ball 2
            val gradient2 = Brush.radialGradient(
                colors = listOf(
                    ballColor.copy(alpha = 0.5f),
                    ballColor.copy(alpha = 0.35f),
                    ballColor.copy(alpha = 0.2f)
                ),
                center = Offset(ball2X - ballRadius * 0.3f, ball2Y - ballRadius * 0.3f),
                radius = ballRadius * 1.5f
            )
            drawCircle(
                brush = gradient2,
                radius = ballRadius,
                center = Offset(ball2X, ball2Y)
            )
        }

        Text(
            text = "${abs(totalDegrees).toInt()}°",
            color = ballColor,
            fontSize = 96.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}