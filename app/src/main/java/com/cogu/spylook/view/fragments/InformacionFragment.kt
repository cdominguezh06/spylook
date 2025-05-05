package com.cogu.spylook.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.cogu.spylook.R
import com.cogu.spylook.model.entity.Contacto

class InformacionFragment(private val contacto: Contacto) : Fragment() {
    private var edadContent: TextView? = null
    private var nickContent: TextView? = null
    private var fechaContent: TextView? = null
    private var ciudadContent: TextView? = null
    private var estadoContent: TextView? = null
    private val paisImg: ImageView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_informacion, container, false)
        edadContent = fragment.findViewById<TextView>(R.id.edadContent)
        nickContent = fragment.findViewById<TextView>(R.id.nickContent)
        fechaContent = fragment.findViewById<TextView>(R.id.fechaContent)
        ciudadContent = fragment.findViewById<TextView>(R.id.ciudadContent)
        estadoContent = fragment.findViewById<TextView>(R.id.estadoContent)

        edadContent!!.setText(contacto.edad.toString())
        nickContent!!.setText(contacto.alias)
        fechaContent!!.setText(contacto.fechaNacimiento.toString())
        ciudadContent!!.setText(contacto.ciudad)
        estadoContent!!.setText(contacto.estado)
        return fragment
    }
}
