package com.vladmarkovic.sample.common.navigation.screen.compose.model

import androidx.compose.material.ScaffoldState
import androidx.navigation.NavHostController

data class ComposeNavArgs(val navController: NavHostController, val scaffoldState: ScaffoldState)
