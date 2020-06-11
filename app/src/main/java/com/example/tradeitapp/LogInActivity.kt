package com.example.tradeitapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_log_in.*

class LogInActivity : AppCompatActivity() {
val mAuth=FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)


        val login_btn=findViewById<View>(R.id.mBtnLogin)

        login_btn.setOnClickListener(View.OnClickListener {
            view-> login()
        })
        val register_textview=findViewById<View>(R.id.mTvRegister)
         register_textview.setOnClickListener(View.OnClickListener {
             view-> register()
         })


    }

    private fun login(){
        val emailtxt=findViewById<View>(R.id.mEdtEmail_Login) as TextView
        val passwordtxt=findViewById<View>(R.id.mEdtEmail_Login) as TextView

        var email=mEdtEmail_Login.text.toString()
        var password=mEdtPassword_Login.text.toString()
        var progress=ProgressDialog(this)
        progress.setTitle("Logging In...")
        progress.setMessage("Please wait...")

        if(!email.isEmpty() && !password.isEmpty())
        {
            progress.show()
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this,
                OnCompleteListener { task->
//check on the authenticity of this code after you're done
                    if(task.isSuccessful){
                        var intent=Intent(this,LatestMessagesActivity::class.java)
                        intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or (Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        //...........................................................
                        progress.dismiss()
                        clear()
                        Toast.makeText(this,"Login successful",Toast.LENGTH_LONG).show()
                    }
                })

        }
       // if(email.isEmpty() or password.isEmpty()){
           // progress.dismiss()
           // Toast.makeText(this,"Fill in all credetials",Toast.LENGTH_LONG).show()
      //  }
        else{
            progress.dismiss()
            Toast.makeText(this,"Error...Login failed",Toast.LENGTH_LONG).show()
        }


    }

    private fun register(){
        var progress=ProgressDialog(this)
        progress.setTitle("Loading")
        progress.setMessage("Please Wait...")
        progress.show()
        startActivity(Intent(this,MainActivity::class.java))
        progress.dismiss()

    }

    private fun clear(){

        mEdtEmail_Login.setText("")
        mEdtPassword_Login.setText("")
    }
}
