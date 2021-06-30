package com.bottlerocketstudios.brarchitecture.ui.util

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter

/**
 * An [ArrayAdapter] that removes filtering performed by [android.widget.AutoCompleteTextView]
 *
 * Inspired by https://stackoverflow.com/questions/8512762/autocompletetextview-disable-filtering
 */
class NoFilterArrayAdapter<T : Any>(context: Context, resource: Int, private val objects: List<T>) : ArrayAdapter<T>(context, resource, objects) {

    private val filter: Filter = NoFilter()

    override fun getFilter(): Filter {
        return filter
    }

    private inner class NoFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val result = FilterResults()
            result.values = objects
            result.count = objects.size
            return result
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            notifyDataSetChanged()
        }
    }
}
