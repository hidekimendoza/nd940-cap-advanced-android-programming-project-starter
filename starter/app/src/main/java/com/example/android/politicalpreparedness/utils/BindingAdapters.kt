package com.udacity.project4.utils


import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.domain.ElectionDomainModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("listData")
    fun bindRecyclerView(recyclerView: RecyclerView, data: List<ElectionDomainModel>?) {
        if(! data.isNullOrEmpty()){
            val adapter = recyclerView.adapter as ElectionListAdapter
            adapter.submitList(data)
        }
    }

@BindingAdapter("electionDate")
fun TextView.displayDate(date: Date){
    val formatter = SimpleDateFormat("EE MM-dd HH:mm:ss zz yyyy", Locale.US)
    formatter.timeZone = TimeZone.getTimeZone("EDT")
    val result = formatter.format(date)
    this.text = result
}