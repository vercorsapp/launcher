package app.vercors.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import app.vercors.*
import app.vercors.common.AppAsyncImage
import app.vercors.common.IconTextButton
import app.vercors.common.appAnimateContentSize
import app.vercors.project.ProjectData
import compose.icons.FeatherIcons
import compose.icons.feathericons.Calendar
import compose.icons.feathericons.Download
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.stringResource
import vercors.app.generated.resources.Res
import vercors.app.generated.resources.install
import vercors.app.generated.resources.pack
import kotlin.math.roundToInt

@Composable
fun HomeProjectsSectionContent(
    projects: List<ProjectData>?,
    onProjectClick: (ProjectData) -> Unit,
    onProjectInstallClick: (ProjectData) -> Unit
) {
    var count by rememberSaveable { mutableStateOf(0) }
    val localDensity = LocalDensity.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(UI.largePadding),
        modifier = Modifier.fillMaxWidth().onGloballyPositioned {
            count = localDensity.run { ((it.size.width.toDp() - 80.dp) / 400.dp).roundToInt() }
        }
    ) {
        if (projects == null) {
            for (i in 0 until count) {
                FakeHomeCard { ProjectCardBox(it) }
            }
        } else if (projects.isNotEmpty()) {
            for (project in projects.take(count)) {
                ProjectCardContent(project, onProjectClick, onProjectInstallClick)
            }
        } else {
            Text("No projects found :(")
        }
    }
}

@Composable
private fun RowScope.ProjectCardBox(
    modifier: Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    content: @Composable () -> Unit = {}
) {
    Card(modifier = modifier.weight(1f).aspectRatio(1.5f), shape = shape) {
        content()
    }
}

@Composable
private fun RowScope.ProjectCardContent(
    project: ProjectData,
    onProjectClick: (ProjectData) -> Unit,
    onProjectInstallClick: (ProjectData) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    ProjectCardBox(
        modifier = Modifier.hoverable(interactionSource)
            .pointerHoverIcon(PointerIcon.Hand).clickable { onProjectClick(project) }
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.align(Alignment.BottomCenter).height(maxHeight - 80.dp).fillMaxWidth()) {
                project.imageUrl?.let { url ->
                    AppAsyncImage(
                        model = imageRequest("project/${project.provider}/${project.slug}/image", url),
                        contentDescription = "${project.name} image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } ?: Image(
                    bitmap = imageResource(Res.drawable.pack),
                    contentDescription = "${project.name} image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Row(
                    modifier = Modifier.align(Alignment.BottomEnd).padding(start = 10.dp, end = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProjectChip(
                        name = "Downloads",
                        icon = FeatherIcons.Download,
                        text = project.downloads.readable()
                    )
                    ProjectChip(
                        name = "Last updated",
                        icon = FeatherIcons.Calendar,
                        text = project.lastUpdated.readable()
                    )
                }
            }

            Column(
                modifier = Modifier.background(LocalPalette.current.surface1).align(Alignment.TopCenter).fillMaxWidth()
                    .appAnimateContentSize().applyIf(isHovered) { fillMaxHeight() }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(80.dp).padding(UI.mediumLargePadding),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(UI.mediumPadding)
                ) {
                    Surface(
                        color = LocalPalette.current.surface2,
                        modifier = Modifier.fillMaxHeight().aspectRatio(1f),
                        shape = UI.defaultRoundedCornerShape,
                        elevation = 1.dp
                    ) {
                        project.logoUrl?.let { url ->
                            AppAsyncImage(
                                model = imageRequest("project/${project.provider}/${project.slug}/logo", url),
                                contentDescription = "${project.name} logo",
                                modifier = Modifier.fillMaxSize()
                            )
                        } ?: Image(
                            bitmap = imageResource(Res.drawable.pack),
                            contentDescription = "${project.name} logo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Column {
                        Text(
                            project.name,
                            style = MaterialTheme.typography.h6,
                            maxLines = 1,
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

                if (isHovered) {
                    Column(
                        modifier = Modifier.weight(1f).fillMaxWidth()
                    ) {
                        Text(
                            project.description,
                            style = MaterialTheme.typography.body2,
                            minLines = 4,
                            maxLines = 4,
                            modifier = Modifier.fillMaxWidth().padding(UI.mediumLargePadding),
                            overflow = TextOverflow.Ellipsis
                        )

                        Row(
                            modifier = Modifier.fillMaxSize().padding(bottom = UI.largePadding),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            IconTextButton(
                                onClick = { onProjectInstallClick(project) },
                                imageVector = FeatherIcons.Download,
                                text = stringResource(Res.string.install)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProjectChip(name: String, icon: ImageVector, text: String) {
    Card(
        modifier = Modifier.padding(bottom = UI.mediumPadding),
        backgroundColor = LocalPalette.current.surface1
    ) {
        Row(
            modifier = Modifier.padding(horizontal = UI.mediumPadding, vertical = UI.smallPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(UI.smallPadding)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = name,
                modifier = Modifier.size(UI.mediumIconSize)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.caption
            )
        }
    }
}
