package com.example.tradeitapp

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_messages.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class NewMessagesActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_messages)

        supportActionBar?.title="Select User"

        var users:ArrayList<com.example.tradeitapp.UserClass> = ArrayList()
        var myAdapter = CustomAdapter(applicationContext,users)
        var progress = ProgressDialog(this)
        progress.setTitle("Loading")
        progress.setMessage("Please wait...")

        //Access the table in the database

        var my_db = FirebaseDatabase.getInstance().reference.child("Names")
        //Start retrieving data
        progress.show()
        my_db.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                //Get the data and put it on the arraylist users
                users.clear()
                for (snap in p0.children){
                    var person = snap.getValue(UserClass::class.java)
                    users.add(person!!)
                }
                //Notify the adapter that data has changed
                myAdapter.notifyDataSetChanged()
                progress.dismiss()
            }

            override fun onCancelled(p0: DatabaseError) {
                progress.dismiss()
                Toast.makeText(applicationContext,"DB Locked",Toast.LENGTH_LONG).show()
            }
        })

        recyclerView_newmessages.adapter = myAdapter

    }


}

class UserItem(val user:User): Item<ViewHolder>(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
//will be called in our list for each of our user
        viewHolder.itemView.username_textview_new_message.text=user.username

//        Picasso.get().load(user.profileimageurl).into(viewHolder.itemView.imageView_new_message)
        Picasso.get().load(R.mipmap.icon_login).into(viewHolder.itemView.imageView_new_message)
    }
    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }
}

//this is tedious work as you try to display list of users
//instead install some android libraries

//class CustomAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
//{
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//}
