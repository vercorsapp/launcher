package com.skyecodes.snowball.ui.util

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.Density
import com.skyecodes.snowball.service.CacheService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xml.sax.InputSource
import java.io.File
import java.io.IOException
import java.net.URL
import kotlin.io.path.createParentDirectories
import kotlin.io.path.exists
import kotlin.io.path.outputStream

@Composable
fun AsyncImage(
    key: String,
    url: String,
    painterFor: @Composable (ImageBitmap) -> Painter,
    contentDescription: String,
    defaultImage: ImageBitmap? = null,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val image: ImageBitmap? by produceState(defaultImage) {
        value = withContext(Dispatchers.IO) {
            try {
                val u = URL(url)
                val file = CacheService.cacheDir.resolve("image").resolve(key + "." + File(u.file).extension)
                if (file.exists()) {
                    loadImageBitmap(file.toFile())
                } else {
                    u.openStream().use { input ->
                        file.createParentDirectories().outputStream().use { output -> input.copyTo(output) }
                        loadImageBitmap(url)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    if (image != null) {
        Image(
            painter = painterFor(image!!),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
    }
}

/* Loading from file with java.io API */

fun loadImageBitmap(file: File): ImageBitmap =
    file.inputStream().buffered().use(::loadImageBitmap)

fun loadSvgPainter(file: File, density: Density): Painter =
    file.inputStream().buffered().use { androidx.compose.ui.res.loadSvgPainter(it, density) }

fun loadXmlImageVector(file: File, density: Density): ImageVector =
    file.inputStream().buffered().use { androidx.compose.ui.res.loadXmlImageVector(InputSource(it), density) }

/* Loading from network with java.net API */

fun loadImageBitmap(url: String): ImageBitmap =
    URL(url).openStream().buffered().use(::loadImageBitmap)

fun loadSvgPainter(url: String, density: Density): Painter =
    URL(url).openStream().buffered().use { androidx.compose.ui.res.loadSvgPainter(it, density) }

fun loadXmlImageVector(url: String, density: Density): ImageVector =
    URL(url).openStream().buffered().use { androidx.compose.ui.res.loadXmlImageVector(InputSource(it), density) }

