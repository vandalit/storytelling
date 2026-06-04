package com.narrative.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val NarrativeTypography = Typography(
    headlineLarge  = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 28.sp, letterSpacing = (-0.5).sp),
    headlineMedium = TextStyle(fontWeight = FontWeight.Bold,      fontSize = 22.sp),
    titleLarge     = TextStyle(fontWeight = FontWeight.Bold,      fontSize = 18.sp),
    titleMedium    = TextStyle(fontWeight = FontWeight.SemiBold,  fontSize = 15.sp),
    bodyLarge      = TextStyle(fontWeight = FontWeight.Normal,    fontSize = 15.sp, lineHeight = 22.sp),
    bodyMedium     = TextStyle(fontWeight = FontWeight.Normal,    fontSize = 13.sp, lineHeight = 20.sp),
    labelSmall     = TextStyle(fontWeight = FontWeight.Bold,      fontSize = 10.sp, letterSpacing = 0.8.sp),
)
