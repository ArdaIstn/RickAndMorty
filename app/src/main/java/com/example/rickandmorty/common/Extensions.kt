package com.example.rickandmorty.common

import android.widget.ImageView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale

fun ImageView.loadImage(url: String) {
    Glide.with(this.context).load(url).into(this)
}

fun List<String>.extractIds(): String {
    return this.joinToString(",") { it.split("/").last() }
}

fun String.extractId(): String {
    return this.split("/").last()
}

fun String.formatDateString(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale.getDefault())
    val date = inputFormat.parse(this)
    return date?.let { outputFormat.format(it) } ?: this
}