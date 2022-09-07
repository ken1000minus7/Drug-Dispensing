package org.hmispb.drugdispensing.model

data class LoginResponse(
    val dataHeading: List<String> = listOf("hospitalCode","hospitalName","userId","UserName"),
    val dataType: List<String> = listOf("numeric","string","numeric","string"),
    val dataValue: List<List<String>>?
)