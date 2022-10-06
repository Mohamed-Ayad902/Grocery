package com.example.grocery.di

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.Settings
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import com.example.grocery.R
import com.example.grocery.other.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Named

@Module
@InstallIn(FragmentComponent::class)
object DialogModule {

    @FragmentScoped
    @Provides
    @Named(Constants.PERMISSION_DIALOG)
    fun providePermissionDialog(@ApplicationContext context: Context): Dialog {
        val dialog = Dialog(context)
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.permission_dialog)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val btnSettings = findViewById<Button>(R.id.btn_settings)
            val btnCancel = findViewById<ImageView>(R.id.btn_close)

            btnCancel.setOnClickListener { hide() }
            btnSettings.setOnClickListener {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
            }
            create()
        }
        return dialog
    }

    @FragmentScoped
    @Provides
    @Named(Constants.ENABLE_GPS_DIALOG)
    fun provideEnableGpsDialog(@ApplicationContext context: Context): Dialog {
        val dialog = AlertDialog.Builder(context).let { builder ->
            builder.setPositiveButton("Ok", null)
            builder.create()
        }
        return dialog
    }

}