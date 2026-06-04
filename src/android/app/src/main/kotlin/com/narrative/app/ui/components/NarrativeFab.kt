package com.narrative.app.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.narrative.app.ui.theme.Primary

@Composable
fun NarrativeFab(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        containerColor = Primary,
        contentColor = Color.White,
    ) {
        Icon(Icons.Default.Add, contentDescription = "Nueva card")
    }
}
