package com.example.sociable.Activity

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.sociable.R
import com.hitomi.cmlibrary.CircleMenu
import com.hitomi.cmlibrary.OnMenuSelectedListener
import com.hitomi.cmlibrary.OnMenuStatusChangeListener


class MainActivity : AppCompatActivity() {

    private var circleMenu: CircleMenu? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        circleMenu = findViewById<View>(R.id.circle_menu) as CircleMenu
        circleMenu!!.setMainMenu(
            Color.parseColor("#CDCDCD"),
            R.drawable.ic_menu,
            R.drawable.ic_close
        )
            .addSubMenu(Color.parseColor("#258CFF"), R.drawable.ic_home)
            .addSubMenu(Color.parseColor("#30A400"), R.drawable.ic_search)
            .addSubMenu(Color.parseColor("#FF4B32"), R.drawable.ic_music)
            .addSubMenu(Color.parseColor("#8A39FF"), R.drawable.ic_person)
            .addSubMenu(Color.parseColor("#FF6A00"), R.drawable.ic_add)
            .setOnMenuSelectedListener(OnMenuSelectedListener { })
            .setOnMenuStatusChangeListener(object : OnMenuStatusChangeListener {
                override fun onMenuOpened() {}
                override fun onMenuClosed() {}
            })
    }

   fun onMenuOpened(featureId: Int, menu: Menu?): Comparable<Boolean> {
        circleMenu!!.openMenu()
        return super.onMenuOpened(featureId, menu!!)
    }

    override fun onBackPressed() {
        circleMenu!!.closeMenu()
    }
}

