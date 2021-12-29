package com.vladmarkovic.sample.main_presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vladmarkovic.sample.shared_presentation.util.navigate
import com.vladmarkovic.sample.shared_presentation.util.setContainerContentView

/** Main holder activity, holding feature fragments within R.id.container. */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContainerContentView()

        if (savedInstanceState == null) {
            navigate { toFeed() }
        }
    }
}
