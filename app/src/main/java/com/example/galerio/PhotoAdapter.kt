import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.galerio.R
import com.example.galerio.data.model.Photo
import java.text.SimpleDateFormat
import java.util.*

class PhotoAdapter(private val photos: List<Photo>) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val titleView: TextView = view.findViewById(R.id.titleView)
        val dateView: TextView = view.findViewById(R.id.dateView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = photos[position]
        holder.titleView.text = photo.title
        holder.dateView.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(photo.dateTaken))
        Glide.with(holder.imageView.context).load(Uri.parse(photo.uri)).into(holder.imageView)
    }

    override fun getItemCount() = photos.size
}