package app.vercors.launcher.home.presentation.project

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import app.vercors.launcher.core.generated.resources.calendar_clock
import app.vercors.launcher.core.generated.resources.download
import app.vercors.launcher.core.generated.resources.install
import app.vercors.launcher.core.presentation.CoreDrawable
import app.vercors.launcher.core.presentation.CoreString
import app.vercors.launcher.core.presentation.ui.*
import app.vercors.launcher.home.presentation.HomeSectionItemUi
import app.vercors.launcher.project.generated.resources.pack
import app.vercors.launcher.project.presentation.ProjectDrawable
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun RowScope.HomeProjectCard(
    project: HomeSectionItemUi.Project,
    onProjectClick: (HomeSectionItemUi.Project) -> Unit,
    onProjectAction: (HomeSectionItemUi.Project) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    HomeProjectCardBox(
        onClick = { onProjectClick(project) },
        modifier = Modifier.hoverable(interactionSource)
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.align(Alignment.BottomCenter).height(maxHeight - 80.dp).fillMaxWidth()) {
                project.imageUrl?.let { url ->
                    AppAsyncImage(
                        model = appImageRequest("project/${project.id.provider.id}/${project.id.id}/image", url),
                        contentDescription = "${project.name} image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } ?: Image(
                    bitmap = imageResource(ProjectDrawable.pack),
                    contentDescription = "${project.name} image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Row(
                    modifier = Modifier.align(Alignment.BottomEnd).padding(start = 10.dp, end = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HomeProjectChip(
                        name = "Downloads",
                        icon = vectorResource(CoreDrawable.download),
                        text = project.downloadCount
                    )
                    HomeProjectChip(
                        name = "Last updated",
                        icon = vectorResource(CoreDrawable.calendar_clock),
                        text = LocalPrettyTime.current.format(project.lastUpdated)
                    )
                }
            }

            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.surface).align(Alignment.TopCenter)
                    .fillMaxWidth().appAnimateContentSize().thenIf(isHovered) { fillMaxHeight() }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(80.dp).padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceTint,
                        modifier = Modifier.fillMaxHeight().aspectRatio(1f),
                        shape = MaterialTheme.shapes.medium,
                        shadowElevation = 1.dp
                    ) {
                        project.iconUrl?.let { url ->
                            AppAsyncImage(
                                model = appImageRequest("project/${project.id.provider.id}/${project.id.id}/logo", url),
                                contentDescription = "${project.name} logo",
                                modifier = Modifier.fillMaxSize()
                            )
                        } ?: Image(
                            bitmap = imageResource(ProjectDrawable.pack),
                            contentDescription = "${project.name} logo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Column {
                        Text(
                            text = project.name,
                            style = MaterialTheme.typography.titleLarge,
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
                            style = MaterialTheme.typography.bodyMedium,
                            minLines = 3,
                            maxLines = 3,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                            overflow = TextOverflow.Ellipsis
                        )

                        Row(
                            modifier = Modifier.fillMaxSize().padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            AppIconButton(
                                onClick = { onProjectAction(project) },
                                icon = vectorResource(CoreDrawable.download),
                                text = stringResource(CoreString.install)
                            )
                        }
                    }
                }
            }
        }
    }
}