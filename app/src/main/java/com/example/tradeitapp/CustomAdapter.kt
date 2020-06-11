package com.example.tradeitapp


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class CustomAdapter(var context: Context, var data:ArrayList<UserClass>):BaseAdapter() {
    private class ViewHolder(row:View?){
        var mTxtName:TextView
        var mPersonPic:ImageView
        init {
            this.mTxtName = row?.findViewById(R.id.mContactName) as TextView
            this.mPersonPic = row?.findViewById(R.id.mImgContactPic) as ImageView
        }
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view:View?
        var viewHolder:ViewHolder
        if (convertView == null){
            var layout = LayoutInflater.from(context)
            view = layout.inflate(R.layout.item_layout,parent,false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        }else{
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        var item:UserClass = getItem(position) as UserClass
        viewHolder.mTxtName.text = item.username
        viewHolder.mPersonPic.setImageResource(item.profileimageurl!!)
        return view as View
    }

    override fun getItem(position: Int): Any {
        return  data.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.count()
    }
}