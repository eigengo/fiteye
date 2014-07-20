package org.eigengo.fiteye.meal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.view.{MenuItem, Menu}
import android.widget.Toast
import com.google.android.glass.view.WindowUtils
import org.eigengo.fiteye.R

class MenuLogActivity extends Activity {
  final val SpeakRequestCode = 100
  final val BarcodeRequestCode = 101
  final val PictureRequestCode = 102

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
  }

  override def onAttachedToWindow(): Unit = openOptionsMenu()

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.log_meal, menu)
    true
  }

  override def onMenuItemSelected(featureId: Int, item: MenuItem): Boolean = {
    item.getItemId match {
      case R.id.log_meal_speak =>
        val intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say your meal")
        startActivityForResult(intent, SpeakRequestCode)
      case R.id.log_meal_barcode =>
        startActivityForResult(new Intent(this, classOf[BarcodeActivity]), BarcodeRequestCode)
      case R.id.log_meal_picture =>
        val intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, PictureRequestCode)
    }

    true
  }

  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit = requestCode match {
    case SpeakRequestCode   =>
      if (requestCode == SpeakRequestCode && resultCode == Activity.RESULT_OK) {
        val results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
        val spokenText = results.get(0)
        // Do something with spokenText.
        Toast.makeText(this, "I heard " + spokenText, Toast.LENGTH_SHORT).show()
        finish()
      }
    case BarcodeRequestCode =>
    case PictureRequestCode =>
      Toast.makeText(this, "I saw a kitten", Toast.LENGTH_SHORT).show()
      finish()
  }
}
