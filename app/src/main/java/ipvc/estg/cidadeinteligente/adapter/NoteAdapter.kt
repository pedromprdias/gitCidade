package ipvc.estg.cidadeinteligente.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.cidadeinteligente.R
import ipvc.estg.cidadeinteligente.entities.Notes
import ipvc.estg.cidadeinteligente.viewmodel.NoteViewModel

class NoteAdapter internal constructor(context: Context) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>(){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notes = emptyList<Notes>()

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val noteItemView: TextView = itemView.findViewById(R.id.note)
        val deleteButton: ImageButton = itemView.findViewById(R.id.imageDelete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerline, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = notes[position]
        holder.noteItemView.text = current.notes


    }

    internal fun setNotes(notes: List<Notes>){
        this.notes = notes
        notifyDataSetChanged()
    }

}