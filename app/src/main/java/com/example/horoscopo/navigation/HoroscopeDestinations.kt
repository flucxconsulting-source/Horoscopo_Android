package com.example.horoscopo.navigation

object HoroscopeDestinations {
    const val Home = "home"
    const val SignIdArg = "signId"
    const val Detail = "detail/{$SignIdArg}"

    fun detailRoute(signId: String): String = "detail/$signId"
}
