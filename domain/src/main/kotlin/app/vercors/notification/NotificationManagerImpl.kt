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

package app.vercors.notification

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

internal class NotificationManagerImpl : NotificationManager {
    private val _state: MutableStateFlow<List<Notification>> = MutableStateFlow(emptyList())
    override val state: StateFlow<List<Notification>> = _state
    override val current: List<Notification> = state.value

    override fun sendNotification(notification: Notification) {
        _state.update { it + notification }
    }

    override fun toggleNotificationReadStatus(id: Long) {
        _state.update { it.map { n -> if (n.id == id) n.copy(isRead = !n.isRead) else n } }
    }

    override fun clearNotification(id: Long) {
        _state.update { it.filter { n -> n.id != id } }
    }

    override fun clearAllNotifications() {
        _state.update { emptyList() }
    }

    override fun markAllNotificationsAsRead() {
        _state.update { it.map { n -> n.copy(isRead = !n.isRead) } }
    }
}