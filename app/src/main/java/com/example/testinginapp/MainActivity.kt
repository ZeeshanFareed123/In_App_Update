package com.example.testinginapp

import android.content.Intent
import android.content.IntentSender
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

class MainActivity : AppCompatActivity() {
    val appUpdateManager by lazy {AppUpdateManagerFactory.create(this)  }
    val UPDATE_REQUEST_CODE = 1661

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        callInAppUpdate()
    }

    override fun onResume() {
        super.onResume()

        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE,
                        this, UPDATE_REQUEST_CODE)
                }catch (e : IntentSender.SendIntentException){
                    e.printStackTrace()
                }
            }
        }
    }

    private fun callInAppUpdate() {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE,
                        this, UPDATE_REQUEST_CODE)
                    }catch (e : IntentSender.SendIntentException){
                       e.printStackTrace()
                    }
            }
        }.addOnFailureListener(){
            Log.d("ImmediateUpdateActivity", "Failed to check for update:$it")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(data == null) return
        if(requestCode == UPDATE_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show()
            finish()
        }
        else{
            Toast.makeText(this, "DownLoading Start", Toast.LENGTH_SHORT).show()
        }

    }


}