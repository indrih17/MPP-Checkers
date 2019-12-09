package com.kubsu.checkers.data

sealed class Failure {
    object IncorrectMove : Failure()
}