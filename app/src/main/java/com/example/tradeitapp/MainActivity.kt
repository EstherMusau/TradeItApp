package com.example.tradeitapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
     val mAuth= FirebaseAuth.getInstance()
    lateinit var mDatabase:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val regBtn=findViewById<View>(R.id.mBtnRegister) as Button

        val txt_Login=findViewById<View>(R.id.mTxtLogin) as TextView
        mDatabase= FirebaseDatabase.getInstance().getReference("Names")

val profile_pic=findViewById<View>(R.id.mBtn_Profilepic_Register) as Button


        profile_pic.setOnClickListener(View.OnClickListener{
            view-> profilepic()
        } )
        regBtn.setOnClickListener (View.OnClickListener  {
             view ->  register()
        }
        )
        txt_Login.setOnClickListener(View.OnClickListener{
            view-> login()
        })
    }
    private fun register(){
        val emailtxt=findViewById<View>(R.id.mEdtEmail_Register) as EditText
        val passwordtxt=findViewById<View>(R.id.mEdtPassword_Register) as EditText
        val usernametxt=findViewById<View>(R.id.mEdtName_Register) as EditText


        var username=usernametxt.text.toString()
        var email=emailtxt.text.toString()
        var password=passwordtxt.text.toString()
        var progress=ProgressDialog(this)
        progress.setTitle("Registering...")
        progress.setMessage("Please wait.")

        if(!username.isEmpty() && !email.isEmpty() && !password.isEmpty()){
        progress.show()
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this,
                OnCompleteListener { task ->

                    if(task.isSuccessful){
                        var user=mAuth.currentUser
                        var uid=user!!.uid.toString()
                        mDatabase.child(uid).child("Names").setValue(username)
                        progress.dismiss()
                        Toast.makeText(this,"Registration is successful",Toast.LENGTH_LONG).show()

                        var intent=Intent(this,LatestMessagesActivity::class.java)
                        intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)

                        uploadimagetoFirebaseStorage()

                    }
                    else{
                        progress.dismiss()
                        Toast.makeText(this,"Error...Registration Failed",Toast.LENGTH_LONG).show()
                    }

                    })
                }
        if(username.isEmpty()&& email.isEmpty()&&password.isEmpty()){
            progress.dismiss()
            Toast.makeText(this,"Fill in your credentials",Toast.LENGTH_LONG).show()


        }


    }
    private fun login(){
        val intent=Intent(this,LogInActivity::class.java)
        startActivity(intent)
    }
    private fun profilepic(){

        val intent=Intent(Intent.ACTION_PICK)
        intent.type="image/+"
        startActivityForResult(intent,0)
    }
 var selectedPhotoUri: Uri?=null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==0 && resultCode==Activity.RESULT_OK && data !=null) {
            //proceed to check what image was selected...
            selectedPhotoUri=data.data
            val bitmap=MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            mCircleImageView_Register.setImageBitmap(bitmap)

            mBtn_Profilepic_Register.alpha=0f
            //val bitmapDrawable=BitmapDrawable(bitmap)
           // mBtn_Profilepic_Register.setBackgroundDrawable(bitmapDrawable)

        }
    }
    private fun uploadimagetoFirebaseStorage(){

        if(selectedPhotoUri==null) return

        val filename=UUID.randomUUID().toString()
        val ref=FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)

            .addOnSuccessListener {
                Log.d("MainActivity","Uploaded photo successfully: ${it.metadata?.path}")

                //retrieve file location
                ref.downloadUrl.addOnSuccessListener {
                    it.toString()
                    Log.d("MainActivity", "File Location: $it")
                    //....................................

                    saveUsertoFirebaseDatabase(it.toString())
                }
            }

    }
    //function to save all of user details to Firebase database
    private fun saveUsertoFirebaseDatabase(profileimageurl: String){
        val uid=FirebaseAuth.getInstance().uid ?:""
       val ref= FirebaseDatabase.getInstance().getReference("/Names/ $uid")
        val user=User(uid,mEdtName_Register.text.toString(),profileimageurl)
        ref.setValue(user)

    }
}

class User(val uid: String, val username: String, val profileimageurl:String){
    constructor(): this("","","")
}
