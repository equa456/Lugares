package com.lugares.ui.lugar

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lugares.R
import com.lugares.databinding.FragmentUpdateLugarBinding
import com.lugares.model.Lugar
import com.lugares.viewmodel.LugarViewModel
import android.Manifest


class UpdateLugarFragment : Fragment() {

    //Defino un argumento
    private  val args by navArgs<UpdateLugarFragmentArgs>()

    private lateinit var  lugarViewModel: LugarViewModel

    private var _binding: FragmentUpdateLugarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lugarViewModel = ViewModelProvider(this)[LugarViewModel::class.java]
        _binding= FragmentUpdateLugarBinding.inflate(inflater,container,false)

        //Se coloca la info del objeto lugar que me pasaron
        binding.etNombre.setText(args.lugar.nombre)
        binding.etTelefono.setText(args.lugar.telefono)
        binding.etCorreo.setText(args.lugar.correo)
        binding.etWeb.setText(args.lugar.web)

        binding.tvAltura.text=args.lugar.altura.toString()
        binding.tvLatitud.text=args.lugar.latitud.toString()
        binding.tvLongitud.text=args.lugar.longitud.toString()


        //Se agrega la funcion para actualizar un lugar
       binding.btActualizar.setOnClickListener{ updateLugar()}

        binding.btEmail.setOnClickListener{escribirCorreo()}
        binding.btPhone.setOnClickListener{llamarLugar()}
        binding.btWeb.setOnClickListener{verWeb()}
        /*binding.btWhatsapp.setOnClickListener{enviarWhatsapp()}
        binding.btLocation.setOnClickListener{verMapa()}*/

        //se indica que en esta pantalla se agrega una opciòn de menu
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun verWeb() {
        //se recupera el url del lugar
        val recurso = binding.etWeb.text.toString()
        if (recurso.isNotEmpty()){
            //se abre el sitio web
            val rutina = Intent(Intent.ACTION_VIEW, Uri.parse("http://$recurso"))
            startActivity(rutina) //levanta el browser y se ve el sitio web
        } else{
            Toast.makeText(
                requireContext(),
                getString(R.string.msg_datos),Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun llamarLugar(){
        //se recupera el telefono del lugar
        val recurso = binding.etTelefono.text.toString()
        if (recurso.isNotEmpty()){
            //se activa el correo
            val rutina = Intent(Intent.ACTION_CALL)
            rutina.data = Uri.parse("tel: $recurso")
            if(
                requireActivity().checkSelfPermission(Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED){
                //se solicitan los permisos
                requireActivity()
                    .requestPermissions(arrayOf(Manifest.permission.CALL_PHONE),105)
            } else{ //se tienen los permisos para llamar
                requireActivity().startActivity(rutina) //hace la llamada
            }
        } else{
            Toast.makeText(
                requireContext(),
                getString(R.string.msg_datos),Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun escribirCorreo() {

        //se recupera el correo del lugar
        val recurso = binding.etCorreo.text.toString()
        if (recurso.isNotEmpty()){
            //se activa el correo
            val rutina = Intent(Intent.ACTION_SEND)
            rutina.type="message/rfc822"
            rutina.putExtra(Intent.EXTRA_EMAIL, arrayOf(recurso))
            rutina.putExtra(Intent.EXTRA_SUBJECT,
            getString(R.string.msg_saludos)+" "+binding.etNombre.text)
            rutina.putExtra(Intent.EXTRA_TEXT,
            getString(R.string.msg_mensaje_correo))
            startActivity(rutina) //levanta el correo y lo presenta para modificar yu enviar
        } else{
            Toast.makeText(
                requireContext(),
                getString(R.string.msg_datos),Toast.LENGTH_SHORT
            ).show()
        }
    }


    //aca se genera el menu con el icono de borrar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu,menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //pregunto si se dio click para borrar
        if(item.itemId==R.id.menu_delete){
            deleteLugar()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteLugar(){
        val consulta = AlertDialog.Builder(requireContext())

        consulta.setTitle(R.string.delete)
        consulta.setMessage(getString(R.string.seguro)+ " ${args.lugar.nombre}?")


        //acciones a ejecutar si responde
        consulta.setPositiveButton(getString(R.string.si)){_,_ ->
            //borramos el lugar... si cosultar
            lugarViewModel.deleteLugar(args.lugar)
            findNavController().navigate(R.id.action_updateLugarFragment_to_nav_lugar)
        }

        consulta.setNegativeButton(getString(R.string.no)){_,_ ->}

        consulta.create().show()
    }


    private fun updateLugar() {
        val  nombre=binding.etNombre.text.toString()
        val  correo=binding.etCorreo.text.toString()
        val  telefono=binding.etTelefono.text.toString()
        val  web=binding.etWeb.text.toString()
        if(nombre.isNotEmpty()){
            val lugar = Lugar(args.lugar.id,nombre,correo,telefono,web,0.0,0.0,0.0,"","")
            lugarViewModel.saveLugar(lugar)
            Toast.makeText(requireContext(),getString(R.string.lugarAdded),Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addLugarFragment_to_nav_lugar)
        } else{
            Toast.makeText(requireContext(),getString(R.string.noData),Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}