package com.skyecodes.snowball.ui.home


import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.skyecodes.snowball.data.app.Project
import com.skyecodes.snowball.openURL
import com.skyecodes.snowball.readable
import com.skyecodes.snowball.resourceAsStream
import com.skyecodes.snowball.ui.UI
import com.skyecodes.snowball.ui.util.AsyncImage
import com.skyecodes.snowball.ui.util.loadImageBitmap
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Calendar
import compose.icons.fontawesomeicons.solid.Download
import kotlinx.coroutines.*
import java.net.URI
import kotlin.coroutines.cancellation.CancellationException

@Composable
fun HomeSection(headerTitle: String, vararg providers: suspend () -> List<Project>) {
    var projects: List<Project> by rememberSaveable { mutableStateOf(emptyList()) }
    var rowSize by remember { mutableStateOf(0) }
    var fetchJobs: List<Deferred<List<Project>>> by rememberSaveable { mutableStateOf(emptyList()) }
    var joinJob: Job? by rememberSaveable { mutableStateOf(null) }
    val scope = rememberCoroutineScope()

    if (projects.isEmpty() && (fetchJobs.none { it.isActive } || joinJob?.isActive != true)) {
        fetchJobs = providers.map {
            scope.async {
                try {
                    it()
                } catch (e: CancellationException) {
                    emptyList()
                } catch (e: Exception) {
                    e.printStackTrace()
                    emptyList()
                }
            }
        }
        joinJob = scope.launch {
            projects = fetchJobs.awaitAll().map { it.toMutableList() }.let { lists ->
                buildList {
                    var curListIdx = 0
                    while (size < 10) {
                        val v = lists[curListIdx].removeFirst()
                        if (none { it.name == v.name }) {
                            add(v)
                            curListIdx++
                            if (curListIdx == lists.size) curListIdx = 0
                        }
                    }
                }
            }
        }
    }

    Column(Modifier.fillMaxWidth().background(MaterialTheme.colors.background).padding(UI.mediumPadding)) {
        Text(
            text = headerTitle,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.padding(UI.smallPadding))

        Row(
            horizontalArrangement = Arrangement.spacedBy(UI.largePadding),
            modifier = Modifier.fillMaxWidth().onGloballyPositioned { rowSize = (it.size.width - 80) / 450 }
        ) {
            if (projects.isNotEmpty()) {
                for (mod in projects.take(rowSize)) {
                    ModCard(mod)
                }
            } else {
                for (i in 0 until rowSize) {
                    FakeModCard()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RowScope.ModCard(project: Project) {
    Card(
        modifier = Modifier.weight(1f).aspectRatio(1.1f).pointerHoverIcon(PointerIcon.Hand),
        onClick = { if (project.url.isNotEmpty()) openURL(URI(project.url)) }
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().height(80.dp).padding(15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding)
            ) {
                Surface(
                    color = UI.colors.surface1,
                    modifier = Modifier.fillMaxHeight().aspectRatio(1f),
                    shape = UI.defaultCornerShape,
                    elevation = 1.dp
                ) {
                    project.logoUrl?.let { url ->
                        AsyncImage(
                            key = "logo-${project.key}",
                            url = url,
                            painterFor = { remember { BitmapPainter(it) } },
                            contentDescription = "${project.name} thumbnail",
                            modifier = Modifier.fillMaxSize()
                        )
                    } ?: Image(
                        bitmap = loadImageBitmap(resourceAsStream("/img/pack.jpg")),
                        contentDescription = "${project.name} thumbnail",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Column {
                    Text(
                        project.name,
                        style = MaterialTheme.typography.subtitle1,
                        maxLines = 1,
                        fontWeight = FontWeight.ExtraBold,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                                append("by ")
                            }
                            append(project.author)
                        },
                        style = MaterialTheme.typography.subtitle2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                project.imageUrl?.let { url ->
                    AsyncImage(
                        key = "image-${project.key}",
                        url = url,
                        painterFor = { remember { BitmapPainter(it) } },
                        contentDescription = "${project.name} image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } ?: Image(
                    bitmap = loadImageBitmap(resourceAsStream("/img/pack.jpg")),
                    contentDescription = "${project.name} image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Row(
                    modifier = Modifier.align(Alignment.BottomStart).padding(start = 10.dp, end = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProjectChip(
                        name = "Downloads",
                        icon = FontAwesomeIcons.Solid.Download,
                        text = project.downloads.readable()
                    )
                    ProjectChip(
                        name = "Last updated",
                        icon = FontAwesomeIcons.Solid.Calendar,
                        text = project.lastUpdated.readable()
                    )
                }
            }

            Text(
                project.description,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.fillMaxWidth().height(80.dp).padding(UI.mediumPadding),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }

    }
}

@Composable
private fun ProjectChip(name: String, icon: ImageVector, text: String) {
    Card(
        modifier = Modifier.padding(bottom = UI.mediumPadding),
        backgroundColor = UI.colors.surface1
    ) {
        Row(
            modifier = Modifier.padding(horizontal = UI.mediumPadding, vertical = UI.smallPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(UI.smallPadding)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = name,
                modifier = Modifier.size(UI.smallIconSize)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.caption,
                fontSize = UI.smallFontSize
            )
        }
    }
}

@Composable
private fun RowScope.FakeModCard() {
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
        modifier = Modifier.weight(1f).aspectRatio(1.1f).drawWithCache {
            val width = size.width
            val height = size.height

            val offset = width * progressAnimated

            val brush = Brush.linearGradient(
                colors = listOf(UI.colors.surface1, UI.colors.surface0),
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