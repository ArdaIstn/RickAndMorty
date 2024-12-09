package com.example.rickandmorty.common

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import java.text.SimpleDateFormat
import java.util.Locale

fun ImageView.loadImage(url: String) {
    Glide.with(this.context).load(url).transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
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

fun Fragment.navigateTo(action: NavDirections) {
    findNavController().navigate(action)
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}