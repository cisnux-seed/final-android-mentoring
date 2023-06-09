package dev.cisnux.dicodingmentoring.ui.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import coil.ImageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.transform.CircleCropTransformation
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import dev.cisnux.dicodingmentoring.R
import dev.cisnux.dicodingmentoring.data.repositories.TokenMessagingRepository
import dev.cisnux.dicodingmentoring.domain.models.UpdateMessagingToken
import dev.cisnux.dicodingmentoring.domain.repositories.AuthRepository
import dev.cisnux.dicodingmentoring.ui.MainActivity
import dev.cisnux.dicodingmentoring.ui.navigation.URI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CloudMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var tokenMessagingRepository: TokenMessagingRepository

    @Inject
    lateinit var authRepository: AuthRepository

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    override fun onNewToken(token: String) {
        serviceScope.launch {
            authRepository.currentUser().collectLatest {
                if (it.uid.isNotBlank()) {
                    val updateMessagingToken = UpdateMessagingToken(
                        userId = it.uid,
                        deviceToken = token
                    )
                    tokenMessagingRepository.updateDeviceToken(
                        updateMessagingToken
                    )
                }
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val type = remoteMessage.data["type"]
        type?.let {

            if (type == "mentoring") {
                val mentoringId = remoteMessage.data["mentoringId"]!!
                val fullName = remoteMessage.data["fullName"]!!
                val title = remoteMessage.data["title"]!!
                val description = remoteMessage.data["description"]!!
                val photoProfile = remoteMessage.data["photoProfile"]
                photoProfile?.let {
                    sendMentoringNotificationWithPhotoProfile(
                        mentoringId,
                        fullName,
                        title,
                        description,
                        photoProfile
                    )
                } ?: sendMentoringNotification(
                    mentoringId,
                    fullName,
                    title,
                    description,
                )
            } else if (type == "chat") {
                val roomChatId = remoteMessage.data["roomChatId"]!!
                val fullName = remoteMessage.data["fullName"]!!
                val message = remoteMessage.data["message"]!!
                val photoProfile = remoteMessage.data["photoProfile"]

                photoProfile?.let {
                    sendChatNotificationWithPhotoProfile(
                        roomChatId,
                        fullName,
                        message,
                        photoProfile
                    )
                } ?: sendChatNotification(
                    roomChatId, fullName, message
                )
            }
        }
    }

    private fun sendMentoringNotification(
        mentoringId: String,
        fullName: String,
        title: String,
        description: String
    ) {
        val context = applicationContext
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            "$URI/mentoring/$mentoringId".toUri(),
            context,
            MainActivity::class.java
        ).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val deepLinkPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(deepLinkIntent)
            getPendingIntent(
                REQUEST_CODE,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE
                else PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val notificationBuilder = NotificationCompat.Builder(
            applicationContext,
            MENTORING_NOTIFICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(fullName)
            .setStyle(NotificationCompat.BigTextStyle().bigText(description))
            .setContentIntent(deepLinkPendingIntent)
            .setAutoCancel(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                MENTORING_NOTIFICATION_CHANNEL_ID,
                MENTORING_NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationBuilder.setChannelId(MENTORING_NOTIFICATION_CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(MENTORING_NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun sendMentoringNotificationWithPhotoProfile(
        mentoringId: String,
        fullName: String,
        photoProfile: String,
        title: String,
        description: String
    ) {
        serviceScope.launch {
            val context = applicationContext
            val deepLinkIntent = Intent(
                Intent.ACTION_VIEW,
                "$URI/mentoring/$mentoringId".toUri(),
                context,
                MainActivity::class.java
            ).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_SINGLE_TOP
            }

            val loader = ImageLoader(applicationContext)
            val request = ImageRequest.Builder(applicationContext)
                .data(photoProfile)
                .allowHardware(false)
                .transformations(CircleCropTransformation())
                .build()

            val deepLinkPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(deepLinkIntent)
                getPendingIntent(
                    REQUEST_CODE,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE
                    else PendingIntent.FLAG_UPDATE_CURRENT
                )
            }

            val notification = when (val state = loader.execute(request)) {
                is SuccessResult -> {
                    val bitmapPhotoProfile = (state.drawable as BitmapDrawable).bitmap

                    NotificationCompat.Builder(
                        applicationContext,
                        MENTORING_NOTIFICATION_CHANNEL_ID
                    )
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(title)
                        .setContentText(fullName)
                        .setLargeIcon(bitmapPhotoProfile)
                        .setStyle(NotificationCompat.BigTextStyle().bigText(description))
                        .setContentIntent(deepLinkPendingIntent)
                        .setAutoCancel(true)
                }

                is ErrorResult -> {
                    NotificationCompat.Builder(
                        applicationContext,
                        MENTORING_NOTIFICATION_CHANNEL_ID
                    )
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(title)
                        .setContentText(fullName)
                        .setStyle(NotificationCompat.BigTextStyle().bigText(description))
                        .setContentIntent(deepLinkPendingIntent)
                        .setAutoCancel(true)
                }
            }


            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    MENTORING_NOTIFICATION_CHANNEL_ID,
                    MENTORING_NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
                notification.setChannelId(MENTORING_NOTIFICATION_CHANNEL_ID)
                notificationManager.createNotificationChannel(channel)
            }
            notificationManager.notify(MENTORING_NOTIFICATION_ID, notification.build())
        }
    }

    private fun sendChatNotification(
        roomChatId: String,
        fullName: String,
        message: String,
    ) {
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            "$URI/chat?roomChatId=$roomChatId".toUri(),
            applicationContext,
            MainActivity::class.java
        )

        val deepLinkPendingIntent: PendingIntent = TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(deepLinkIntent)
            getPendingIntent(
                REQUEST_CODE,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE
                else PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val notificationBuilder = NotificationCompat.Builder(
            applicationContext,
            CHAT_NOTIFICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(fullName)
            .setContentText(message)
            .setContentIntent(deepLinkPendingIntent)
            .setAutoCancel(true)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHAT_NOTIFICATION_CHANNEL_ID,
                CHAT_NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationBuilder.setChannelId(CHAT_NOTIFICATION_CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(CHAT_NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun sendChatNotificationWithPhotoProfile(
        roomChatId: String,
        fullName: String,
        message: String,
        photoProfile: String
    ) {
        serviceScope.launch {
            val loader = ImageLoader(applicationContext)
            val request = ImageRequest.Builder(applicationContext)
                .data(photoProfile)
                .allowHardware(false)
                .transformations(CircleCropTransformation())
                .build()


            val deepLinkIntent = Intent(
                Intent.ACTION_VIEW,
                "$URI/chat?roomChatId=$roomChatId".toUri(),
                applicationContext,
                MainActivity::class.java
            )
            val deepLinkPendingIntent: PendingIntent =
                TaskStackBuilder.create(applicationContext).run {
                    addNextIntentWithParentStack(deepLinkIntent)
                    getPendingIntent(
                        REQUEST_CODE,
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE
                        else PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }

            val notification = when (val state = loader.execute(request)) {
                is SuccessResult -> {
                    val bitmapPhotoProfile = (state.drawable as BitmapDrawable).bitmap

                    NotificationCompat.Builder(
                        applicationContext,
                        CHAT_NOTIFICATION_CHANNEL_ID
                    )
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(fullName)
                        .setContentText(message)
                        .setLargeIcon(bitmapPhotoProfile)
                        .setContentIntent(deepLinkPendingIntent)
                        .setAutoCancel(true)
                }

                is ErrorResult -> {
                    NotificationCompat.Builder(
                        applicationContext,
                        CHAT_NOTIFICATION_CHANNEL_ID
                    )
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(fullName)
                        .setContentText(message)
                        .setContentIntent(deepLinkPendingIntent)
                        .setAutoCancel(true)
                }
            }
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHAT_NOTIFICATION_CHANNEL_ID,
                    CHAT_NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
                notification.setChannelId(CHAT_NOTIFICATION_CHANNEL_ID)
                notificationManager.createNotificationChannel(channel)
            }
            notificationManager.notify(CHAT_NOTIFICATION_ID, notification.build())
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }

    companion object {
        private const val MENTORING_NOTIFICATION_ID = 1
        private const val MENTORING_NOTIFICATION_CHANNEL_ID = "Mentoring Channel"
        private const val MENTORING_NOTIFICATION_CHANNEL_NAME = "Mentoring Notification"
        private const val CHAT_NOTIFICATION_ID = 2
        private const val CHAT_NOTIFICATION_CHANNEL_ID = "Chat Channel"
        private const val CHAT_NOTIFICATION_CHANNEL_NAME = "Chat Notification"
        private const val REQUEST_CODE = 239
    }
}
