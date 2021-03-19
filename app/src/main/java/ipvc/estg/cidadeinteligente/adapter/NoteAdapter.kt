package ipvc.estg.cidadeinteligente.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.cidadeinteligente.R
import ipvc.estg.cidadeinteligente.entities.Notes
import ipvc.estg.cidadeinteligente.viewmodel.NoteViewModel

class NoteAdapter internal constructor(context: Context, private val listener: OnDeleteClickListener, private val listener2: OnUpdateClickListener) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>(){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notes = emptyList<Notes>()


    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val noteItemView: TextView = itemView.findViewById(R.id.note)
        val idItemView: TextView = itemView.findViewById(R.id.idHidden)
        val tituloItemView: TextView = itemView.findViewById(R.id.titulo)
        val trashItemView: ImageButton = itemView.findViewById(R.id.imageDelete)
        val editItemView: ImageButton = itemView.findViewById(R.id.imageEdit)

        init {
            trashItemView.setOnClickListener(this)
            editItemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.imageDelete->{
                    listener.onDeleteClick(idItemView.text.toString().toInt())
                }
                R.id.imageEdit->{
                    listener2.onUpdateClick(idItemView.text.toString().toInt(), tituloItemView.text.toString(), noteItemView.text.toString())
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerline, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = notes[position]
        holder.noteItemView.text = current.notes
        holder.tituloItemView.text = current.titulo
        holder.idItemView.text = current.id.toString()

    }

    internal fun setNotes(notes: List<Notes>){
        this.notes = notes
        notifyDataSetChanged()
    }

    interface OnDeleteClickListener{
        fun onDeleteClick(id :Int)
    }

    interface OnUpdateClickListener{
        fun onUpdateClick(id:Int, titulo:String, notes:String)
    }

}