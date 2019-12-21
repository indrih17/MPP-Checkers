package com.kubsu.checkers

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class GameType : Parcelable {
    @Parcelize
    object HumanVsAi : GameType()

    @Parcelize
    object HumanVsHuman : GameType()

    @Parcelize
    object AiVsAi : GameType()
}