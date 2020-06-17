package anbara.ayoub.drawing.adapter

import anbara.ayoub.drawing.ImageActivity
import anbara.ayoub.drawing.MainActivity
import anbara.ayoub.drawing.R
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_view.view.*


import java.io.File








const val IMAGE_PATH = "image_path"

class DrawAdapter(private val context: Context, private val imageList: ArrayList<String>,private val activity: MainActivity) : RecyclerView.Adapter<DrawAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val path = imageList[holder.adapterPosition]
        Glide.with(context).load(path).into(holder.drawImage)
        holder.drawImage.setOnClickListener {
            val intent = Intent(context, ImageActivity::class.java)
            intent.putExtra(IMAGE_PATH, path)
            context.startActivity(intent)
        }
        holder.fab_delete.setOnClickListener {
           // Toast.makeText(context,"hdhdhd",Toast.LENGTH_LONG).show()

            val file = File(path)
            file.delete()
            imageList.removeAt(position)
            //context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(File(path))))
           // imageList.clear()
            //imageList.addAll(imageList)
            //this.notifyDataSetChanged()
            activity.recreate() //notifyDataSetChanged خدامة على
         //  notifyItemRemoved(holder.adapterPosition+1)
            //notifyItemRangeRemoved(0, position);

//Toast.makeText(context,"position= "+position+"holder.adapterPosition "+holder.adapterPosition,Toast.LENGTH_LONG).show()

        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val drawImage: ImageView = itemView.image_draw
        val fab_delete: FloatingActionButton = itemView.floatingBtn_delete
    }

    fun addItem(uri: Uri) {
        imageList.add(uri.toString())
        notifyItemInserted(imageList.size - 1)
    }
}