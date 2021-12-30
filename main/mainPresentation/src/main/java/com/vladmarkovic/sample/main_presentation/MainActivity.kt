package com.vladmarkovic.sample.main_presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vladmarkovic.sample.shared_presentation.briefaction.BriefActionViewModel
import com.vladmarkovic.sample.shared_presentation.navigation.HomeScreen
import com.vladmarkovic.sample.shared_presentation.util.actionViewModels
import com.vladmarkovic.sample.shared_presentation.util.setContainerContentView
import dagger.hilt.android.AndroidEntryPoint

/** Main holder activity, holding feature fragments within R.id.container. */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel : BriefActionViewModel by actionViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContainerContentView()

        if (savedInstanceState == null) {
            viewModel.navigateTo(HomeScreen.FEED)
        }
    }
}
