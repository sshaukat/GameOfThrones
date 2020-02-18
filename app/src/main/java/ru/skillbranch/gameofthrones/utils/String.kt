package ru.skillbranch.gameofthrones.utils

fun String.lastNumbersOrEmpty(): String {
    return if (this.length>1)
        this.substring(this.lastIndexOf("/")+1)
    else ""
}

fun String.getHouseId(): String = this.substringBeforeLast(" of").substringAfterLast("House ").substringAfterLast(" ")