package com.skyecodes.snowball.ui.home


import androidx.compose.animation.core.*
import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skyecodes.snowball.AsyncImage
import com.skyecodes.snowball.data.app.Mod
import com.skyecodes.snowball.loadImageBitmap
import com.skyecodes.snowball.openURL
import com.skyecodes.snowball.ui.Theme
import java.net.URI

@Composable
fun HomeSection(headerTitle: String, mods: List<Mod>) {
    Column(Modifier.fillMaxWidth().background(MaterialTheme.colors.background).padding(8.dp)) {
        Text(
            text = headerTitle,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(Modifier.padding(4.dp))
        Box {
            val listState = rememberLazyListState()

            LazyRow(
                modifier = Modifier.padding(bottom = 10.dp),
                state = listState
            ) {
                if (mods.isNotEmpty()) {
                    items(mods) {
                        ModCard(it)
                    }
                } else {
                    items(3) {
                        FakeModCard()
                    }
                }
            }

            HorizontalScrollbar(
                modifier = Modifier.align(Alignment.BottomStart).fillMaxWidth(),
                adapter = rememberScrollbarAdapter(listState)
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ModCard(mod: Mod) {
    Card(
        modifier = Modifier.requiredSize(200.dp, 250.dp).padding(4.dp),
        shape = RoundedCornerShape(15.dp),
        onClick = { if (mod.url.isNotEmpty()) openURL(URI(mod.url)) }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                load = { loadImageBitmap(mod.logoUrl!!) },
                painterFor = { remember { BitmapPainter(it) } },
                contentDescription = "${mod.name} thumbnail",
                modifier = Modifier.size(128.dp)
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    mod.name,
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 16.sp,
                    maxLines = 3,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Clip
                )
            }
        }
    }
}

@Composable
private fun BoxScope.FakeModCard() {
    val limit = 1.5f
    val transition = rememberInfiniteTransition()
    val progressAnimated by transition.animateFloat(
        initialValue = -limit,
        targetValue = limit,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Card(
        modifier = Modifier.requiredSize(200.dp, 250.dp).padding(4.dp).align(Alignment.Center).drawWithCache {
            val width = size.width
            val height = size.height

            val offset = width * progressAnimated

            val brush = Brush.linearGradient(
                colors = listOf(Theme.currentPalette.surface1, Theme.currentPalette.surface0),
                start = Offset(offset, 0f),
                end = Offset(offset + width, height)
            )

            onDrawWithContent {
                drawRoundRect(
                    brush = brush,
                    blendMode = BlendMode.SrcIn,
                    cornerRadius = CornerRadius(22f)
                )
            }
        },
        shape = RoundedCornerShape(15.dp)
    ) {}
}