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
import com.skyecodes.snowball.service.StorageService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xml.sax.InputSource
import java.io.File
import java.io.IOException
import java.io.InputStream
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
                val file = StorageService.cacheDir.resolve("image").resolve(key + "." + File(u.file).extension)
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

fun loadImageBitmap(file: File): ImageBitmap = loadImageBitmap(file.inputStream())

fun loadSvgPainter(file: File, density: Density): Painter = loadSvgPainter(file.inputStream(), density)

fun loadXmlImageVector(file: File, density: Density): ImageVector = loadXmlImageVector(file.inputStream(), density)

/* Loading from network with java.net API */

fun loadImageBitmap(url: String): ImageBitmap = loadImageBitmap(URL(url).openStream())

fun loadSvgPainter(url: String, density: Density): Painter = loadSvgPainter(URL(url).openStream(), density)

fun loadXmlImageVector(url: String, density: Density): ImageVector = loadXmlImageVector(URL(url).openStream(), density)

fun loadImageBitmap(`is`: InputStream): ImageBitmap =
    `is`.buffered().use(::loadImageBitmap)

fun loadSvgPainter(`is`: InputStream, density: Density): Painter =
    `is`.buffered().use { androidx.compose.ui.res.loadSvgPainter(it, density) }

fun loadXmlImageVector(`is`: InputStream, density: Density): ImageVector =
    `is`.buffered().use { androidx.compose.ui.res.loadXmlImageVector(InputSource(it), density) }

