package org.eigengo.fiteye

import android.app.Activity
import android.util.Log
import android.view.{MenuItem, Menu}

class MenuActivity extends Activity {
  override def onAttachedToWindow(): Unit = {
    super.onAttachedToWindow()
    openOptionsMenu()
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    val inflater = getMenuInflater
    inflater.inflate(R.menu.fiteye, menu)
    true
  }


  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    Log.v("MenuActivity", "Selected")

    super.onOptionsItemSelected(item)
  }

  override def onOptionsMenuClosed(menu: Menu): Unit = finish()
}
