package ipvc.estg.cidadeinteligente.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.cidadeinteligente.R
import ipvc.estg.cidadeinteligente.dataclasses.Notes

class LineAdapter(val list:ArrayList<Notes>):RecyclerView.Adapter<LineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineViewHolder {

        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recyclerline, parent, false)
        return LineViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LineViewHolder, position: Int) {
        val currentNote = list[position]

        holder.note.text = currentNote.notes
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class LineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val note: TextView = itemView.findViewById(R.id.note)
}