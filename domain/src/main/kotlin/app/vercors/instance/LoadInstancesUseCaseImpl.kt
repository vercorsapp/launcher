/*
 * Copyright (c) 2024 skyecodes
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package app.vercors.instance

import app.vercors.notification.Notification
import app.vercors.notification.NotificationLevel
import app.vercors.notification.NotificationManager
import app.vercors.notification.NotificationText
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*

private val logger = KotlinLogging.logger {}

internal class LoadInstancesUseCaseImpl(
    private val externalScope: CoroutineScope,
    private val instanceRepository: InstanceRepository,
    private val notificationManager: NotificationManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : LoadInstancesUseCase {
    override suspend operator fun invoke() {
        externalScope.launch {
            withContext(ioDispatcher) {
                instanceRepository.loadInstances().collect {
                    when (it) {
                        is InstanceResult.Error -> {
                            logger.error(it.error) { "An error occurred while loading an instance" }
                            notificationManager.sendNotification(
                                Notification(
                                    level = NotificationLevel.ERROR,
                                    text = NotificationText.Template.Error,
                                    args = arrayOf(it.error.localizedMessage)
                                )
                            )
                        }

                        is InstanceResult.Warn -> {
                            logger.warn { "Could not find instance at location ${it.path}" }
                            notificationManager.sendNotification(
                                Notification(
                                    level = NotificationLevel.WARN,
                                    text = NotificationText.Template.InstanceNotFound,
                                    args = arrayOf(it)
                                )
                            )
                        }

                        else -> { // Nothing needs to be done, state is automatically updated
                        }
                    }
                }
            }
        }.join()
    }
}

