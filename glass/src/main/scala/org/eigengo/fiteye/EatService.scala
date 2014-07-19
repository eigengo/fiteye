package org.eigengo.fiteye

import android.app.Service
import android.content.Intent
import android.os.IBinder

class EatService extends Service {

  override def onBind(intent: Intent): IBinder = {
    null
  }

  override def onStartCommand(intent: Intent, flags: Int, startId: Int): Int = {
    val intent = new Intent(this, classOf[meal.MenuLogActivity])
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    getApplication.startActivity(intent)

    Service.START_STICKY
  }

  override def onDestroy(): Unit = super.onDestroy()
}
