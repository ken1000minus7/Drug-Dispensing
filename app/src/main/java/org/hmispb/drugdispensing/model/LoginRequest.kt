package org.hmispb.drugdispensing.model

data class LoginRequest(
    val userName: String,
    val password: String
)