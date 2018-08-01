package ru.barabo.carwash.gui

import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ru.barabo.ftpclient.FileNameType
import ru.barabo.ftpclient.FileTypeFtp
import ru.barabo.ftpclient.FtpObject
import ru.barabo.carwash.R


class FtpListFragment : Fragment() {

    private var ftpRecyclerView: RecyclerView? = null

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val viewFragment = inflater.inflate(R.layout.ftp_list_fragment, container, false)!!

        AsyncFtp().execute()

        ftpRecyclerView = viewFragment.findViewById(R.id.ftpRecyclerView)

        ftpRecyclerView?.layoutManager = LinearLayoutManager(activity)

        ftpRecyclerView?.adapter = FtpAdapter()

        return viewFragment
    }

    private inner class FtpHolder(inflater: LayoutInflater, parent: ViewGroup) :
            RecyclerView.ViewHolder(inflater.inflate(R.layout.ftp_item_list, parent, false)) {

        private var fileName: TextView = itemView.findViewById(R.id.fileName)

        private var fileImage: ImageView = itemView.findViewById(R.id.fileImage)

        fun bindData(itemData: FileNameType) {

            fileName.text = itemData.first


            fileImage.setImageResource(
                    if(itemData.second == FileTypeFtp.DIRECTORY) R.drawable.folder
                    else R.drawable.file
            )
        }
    }

    private inner class FtpAdapter : RecyclerView.Adapter<FtpHolder>() {

        private var selectPosition: Int = -1

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FtpHolder {

            val inFlater = LayoutInflater.from(activity)

            return FtpHolder(inFlater, parent)
        }

        override fun getItemCount(): Int   = FtpObject.getRootCountFiles()

        override fun onBindViewHolder(holder: FtpHolder, position: Int) {

            holder.itemView.setBackgroundColor( backgroundColor(position) )

            holder.itemView.setOnClickListener {
                val oldPosition = selectPosition
                selectPosition = position
                notifyItemChanged(oldPosition)
                notifyItemChanged(position)
            }

            val itemData = FtpObject.getActiveDirItemByIndex(position)

            holder.bindData(itemData)
        }

        private fun backgroundColor(position: Int): Int =
                if(position == selectPosition) Color.parseColor("#000000")
                else Color.parseColor("#ffffff")
    }

    private inner class AsyncFtp : AsyncTask<Unit, Unit, Unit>() {

        override fun doInBackground(vararg params: Unit?) {

            FtpObject.initActiveDir()
        }

        override fun onPostExecute(result: Unit?) {

            ftpRecyclerView?.adapter?.notifyDataSetChanged()
        }
    }
}