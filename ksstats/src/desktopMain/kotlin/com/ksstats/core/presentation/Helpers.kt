package com.ksstats.core.presentation

fun getOvers(balls: Int, ballsPerOver: Int) : String {
    val completedOvers = balls/ballsPerOver
    val ballsLeft = balls % ballsPerOver
    return "$completedOvers.$ballsLeft"
}
