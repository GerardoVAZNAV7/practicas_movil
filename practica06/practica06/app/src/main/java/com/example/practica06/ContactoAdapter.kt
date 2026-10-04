package com.example.practica06

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter: conecta la colección de datos con las vistas del RecyclerView
class ContactoAdapter(
    private val contactos: List<Contacto>,
    private val onItemClick: (Contacto) -> Unit // callback del clic
) : RecyclerView.Adapter<ContactoAdapter.ContactoViewHolder>() {

    // ViewHolder: guarda las referencias a las vistas de UN ítem para reutilizarlas
    class ContactoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvTelefono: TextView = view.findViewById(R.id.tvTelefono)
    }

    // Se llama solo cuando se necesita crear un ítem nuevo (infla el XML)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contacto, parent, false)
        return ContactoViewHolder(view)
    }

    // Se llama cada vez que un ítem se muestra: aquí se "rellena" con los datos
    override fun onBindViewHolder(holder: ContactoViewHolder, position: Int) {
        val contacto = contactos[position]
        holder.tvNombre.text = contacto.nombre
        holder.tvTelefono.text = "Tel: ${contacto.telefono}"
        holder.itemView.setOnClickListener { onItemClick(contacto) }
    }

    override fun getItemCount(): Int = contactos.size
}
