package org.eigengo.fiteye.meal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.{MenuItem, Menu}
import android.widget.Toast
import com.google.android.glass.view.WindowUtils
import org.eigengo.fiteye.R

class MenuLogActivity extends Activity {

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)

    getWindow.requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS)
    setContentView(R.layout.capture)
  }

//  override def onAttachedToWindow(): Unit = {
//    super.onAttachedToWindow()
//
//    openOptionsMenu()
//  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.log_meal, menu)
    true
  }

  override def onCreatePanelMenu(featureId: Int, menu: Menu): Boolean = {
    if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS) {
      getMenuInflater.inflate(R.menu.log_meal, menu)
      true
    } else super.onCreatePanelMenu(featureId, menu)
  }

  override def onMenuItemSelected(featureId: Int, item: MenuItem): Boolean = item.getItemId match {
    case R.id.log_meal_speak   => Toast.makeText(getApplicationContext, "Listen to the user for voice input", Toast.LENGTH_LONG).show(); true
    case R.id.log_meal_barcode => getApplication.startActivity(new Intent(this, classOf[LogActivity])); true
    case R.id.log_meal_picture => Toast.makeText(getApplicationContext, "Will take a picture, attempt to classify it on the server", Toast.LENGTH_LONG).show(); true
    case _                     => super.onOptionsItemSelected(item)
  }
}
