package org.prauga.measure

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.prauga.pvot.designsystem.components.navigation.PvotNavBar
import com.prauga.pvot.designsystem.components.navigation.PvotTabItem
import org.prauga.measure.screens.LevelScreen
import org.prauga.measure.screens.MeasureScreen


@Composable
fun MeasureAppNavBar() {
    var selectedTab by remember { mutableIntStateOf(0) }

    val tabs = listOf(
        PvotTabItem(
            iconRes = R.drawable.ic_measure,
            labelRes = R.string.tab_measure,
            contentDescriptionRes = R.string.cd_measure
        ),
        PvotTabItem(
            iconRes = R.drawable.ic_level,
            labelRes = R.string.tab_level,
            contentDescriptionRes = R.string.cd_level
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            PvotNavBar(
                selectedTab = selectedTab,
                onTabClick = { selectedTab = it },
                tabs = tabs
            )
        }
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(MaterialTheme.colorScheme.background)

        when (selectedTab) {
            0 -> MeasureScreen(modifier = contentModifier)
            1 -> LevelScreen(modifier = contentModifier)
        }
    }
}