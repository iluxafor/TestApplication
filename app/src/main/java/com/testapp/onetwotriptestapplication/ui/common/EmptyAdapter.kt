package com.testapp.onetwotriptestapplication.ui.common

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.testapp.onetwotriptestapplication.R

/**
 * Created by Ilya Bakerin on 12/10/18.
 */
class EmptyAdapter(@DrawableRes private val iconResource: Int, @StringRes private val stringResource: Int) :
    RecyclerView.Adapter<EmptyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_empty, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.messageView.setText(stringResource)
        holder.messageView.setCompoundDrawablesWithIntrinsicBounds(
            null,
            VectorDrawableCompat.create(
                holder.itemView.resources,
                iconResource, null
            ), null, null
        )
    }

    override fun getItemCount(): Int {
        return 1
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var messageView: TextView = itemView.findViewById<View>(R.id.adapter_empty_message_view) as TextView
    }
}