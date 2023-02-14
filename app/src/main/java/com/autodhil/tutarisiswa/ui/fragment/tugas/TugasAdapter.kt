package com.autodhil.tutarisiswa.ui.fragment.tugas

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.RecyclerView
import com.autodhil.tutarisiswa.databinding.ItemTugasBinding
import com.autodhil.tutarisiswa.model.Tugas
import com.autodhil.tutarisiswa.ui.activity.minggu2.Minggu2Activity
import com.autodhil.tutarisiswa.utils.local.SessionManager


@UnstableApi class TugasAdapter(
    val list : ArrayList<Tugas>,
    val context: Context
) : RecyclerView.Adapter<TugasAdapter.ViewHolder>(){


    @UnstableApi inner class ViewHolder (private  var binding : ItemTugasBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bind (tugasData: Tugas, session: SessionManager){


            binding.tvJudul.text = tugasData.title
            if (session.username.equals("admin")){

                binding.root.setOnClickListener {
                    if (!session.usernameSiswa.isNullOrEmpty()){
                        tugasData.username = session.usernameSiswa.toString()
                        context.startActivity(Intent(context,Minggu2Activity::class.java)
                            .putExtra(Minggu2Activity.KEY_TUGAS,tugasData))
                    }else{
                        Toast.makeText(context, "Silahkan Pilih Siswa", Toast.LENGTH_SHORT).show()
                    }


                }

            }else{
                tugasData.username = session.username.toString()
                binding.root.setOnClickListener {

                    context.startActivity(Intent(context,Minggu2Activity::class.java)
                        .putExtra(Minggu2Activity.KEY_TUGAS,tugasData))
                }
            }




            binding.executePendingBindings()


        }
    }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemTugasBinding.inflate(layoutInflater,parent,false)
        return  ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val session = SessionManager(context)
        val item = list.get(position)

        holder.bind(item,session)



    }
}