package com.yjotdev.clasificarpeces.presentation.utils

import java.util.Locale

object Helper {
    fun getDeviceLanguage(): String = Locale.getDefault().language
}