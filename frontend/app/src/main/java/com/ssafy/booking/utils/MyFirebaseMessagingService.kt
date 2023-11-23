package com.ssafy.booking.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ssafy.booking.R
import com.ssafy.booking.ui.MainActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {
    /** 푸시 알림으로 보낼 수 있는 메세지는 2가지
     * 1. Notification: 앱이 실행중(포그라운드)일 떄만 푸시 알림이 옴
     * 2. Data: 실행중이거나 백그라운드(앱이 실행중이지 않을때) 알림이 옴 -> TODO: 대부분 사용하는 방식 */

    private val TAG = "DEVICE_TOKEN"

    /** Token 생성 메서드(FirebaseInstanceIdService 사라짐) */
    override fun onNewToken(token: String) {
        Log.d(TAG, "new Token: $token")

        // 토큰 값을 따로 저장
        val pref = this.getSharedPreferences("token", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("token", token).apply()
        editor.commit()
        Log.i(TAG, "성공적으로 토큰을 저장함")
    }

    /** Token 가져오기 */
    companion object {
        fun getFirebaseToken(onTokenReceived: (String) -> Unit) {
            FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                Log.d("DEVICE_TOKEN", "token=$token")
                onTokenReceived(token) // 콜백을 통해 토큰을 반환합니다.
            }
        }
    }

// 		  //동기방식
//        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//                if (!task.isSuccessful) {
//                    Log.d(TAG, "Fetching FCM registration token failed ${task.exception}")
//                    return@OnCompleteListener
//                }
//                var deviceToken = task.result
//                Log.e(TAG, "token=${deviceToken}")
//            })
//    }

    /** 메시지 수신 메서드(포그라운드) */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.from)

        // Notification 메시지를 수신할 경우
        // remoteMessage.notification?.body!! 여기에 내용이 저장되있음
//        Log.d(TAG, "Notification Message Body: " + remoteMessage.notification?.body!!)

        if (remoteMessage.notification != null) {
            // 알림생성
            sendNotification(remoteMessage)
            Log.d(TAG, "${remoteMessage.notification?.title}")
            Log.d(TAG, "${remoteMessage.notification?.body}")
        } else {
            Log.e(TAG, "data가 비어있습니다. 메시지를 수신하지 못했습니다.")
        }
    }

    /** 알림 생성 메서드 */
    private fun sendNotification(remoteMessage: RemoteMessage) {
        // channel 설정
        val channelId = "C206" // 알림 채널 이름
        val channelName = "Booking"
        val channelDescription = "북킹 알림"
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) // 알림 소리
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // 오레오 버전 이후에는 채널이 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH // 중요도 (HIGH: 상단바 표시 가능)
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }
            notificationManager.createNotificationChannel(channel)
        }

        // RequestCode, Id를 고유값으로 지정하여 알림이 개별 표시
        val uniId: Int = (System.currentTimeMillis() / 7).toInt()
        // 일회용 PendingIntent : Intent 의 실행 권한을 외부의 어플리케이션에게 위임
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            remoteMessage.data.forEach { entry ->
                putExtra(entry.key, entry.value)
            }
        }

        // 23.05.22 Android 최신버전 대응 (FLAG_MUTABLE, FLAG_IMMUTABLE)
        // PendingIntent.FLAG_MUTABLE은 PendingIntent의 내용을 변경할 수 있도록 허용, PendingIntent.FLAG_IMMUTABLE은 PendingIntent의 내용을 변경할 수 없음
        val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_ONE_SHOT
        }

        val pendingIntent = PendingIntent.getActivity(this, uniId, intent, pendingIntentFlags)
        // 알림에 대한 UI 정보, 작업
        val notificationBuilder = NotificationCompat.Builder(this, channelId).apply {
            setPriority(NotificationCompat.PRIORITY_HIGH)
            setSmallIcon(R.mipmap.ic_launcher) // 적절한 알림 아이콘으로 교체해야 합니다.
            setContentTitle(remoteMessage.notification?.title.toString())
            setContentText(remoteMessage.notification?.body.toString())
            setAutoCancel(true)
            setSound(soundUri)
            setContentIntent(pendingIntent)
        }

        notificationManager.notify(uniId, notificationBuilder.build())
    }
}

/**
"notificationBuilder" 알림 생성시 여러가지 옵션을 이용해 커스텀 가능.
setSmallIcon : 작은 아이콘 (필수)
setContentTitle : 제목 (필수)
setContentText : 내용 (필수)ㅁㄴ
setColor : 알림내 앱 이름 색
setWhen : 받은 시간 커스텀 ( 기본 시스템에서 제공합니다 )
setShowWhen : 알림 수신 시간 ( default 값은 true, false시 숨길 수 있습니다 )
setOnlyAlertOnce : 알림 1회 수신 ( 동일 아이디의 알림을 처음 받았을때만 알린다, 상태바에 알림이 잔존하면 무음 )
setContentTitle : 제목
setContentText : 내용
setFullScreenIntent : 긴급 알림 ( 자세한 설명은 아래에서 설명합니다 )
setTimeoutAfter : 알림 자동 사라지기 ( 지정한 시간 후 수신된 알림이 사라집니다 )
setContentIntent : 알림 클릭시 이벤트 ( 지정하지 않으면 클릭했을때 아무 반응이 없고 setAutoCancel 또한 작동하지 않는다 )
setLargeIcon : 큰 아이콘 ( mipmap 에 있는 아이콘이 아닌 drawable 폴더에 있는 아이콘을 사용해야 합니다. )
setAutoCancel : 알림 클릭시 삭제 여부 ( true = 클릭시 삭제 , false = 클릭시 미삭제 )
setPriority : 알림의 중요도를 설정 ( 중요도에 따라 head up 알림으로 설정할 수 있는데 자세한 내용은 밑에서 설명하겠습니다. )
setVisibility : 잠금 화면내 알림 노출 여부
Notification.VISIBILITY_PRIVATE : 알림의 기본 정보만 노출 (제목, 타이틀 등등)
Notification.VISIBILITY_PUBLIC : 알림의 모든 정보 노출
Notification.VISIBILITY_SECRET : 알림의 모든 정보 비노출
 */
