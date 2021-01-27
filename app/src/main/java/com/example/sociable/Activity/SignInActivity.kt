package com.example.sociable.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import android.widget.VideoView
import com.example.sociable.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {
    private var videoBG: VideoView? = null
    var mMediaPlayer: MediaPlayer? = null
    var mCurrentVideoPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        btSingIn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        btLogInButton.setOnClickListener {
            loginUser()
        }
// Hook up the VideoView to our UI.
        videoBG = findViewById<View>(R.id.videoView) as VideoView

        // Build your video Uri
        val uri = Uri.parse("android.resource://" // First start with this,
                + packageName // then retrieve your package name,
                + "/" // add a slash,
                + R.raw.video7) // and then finally add your video resource. Make sure it is stored
        // in the raw folder.

        // Set the new Uri to our VideoView
        videoBG!!.setVideoURI(uri)
        // Start the VideoView
        videoBG!!.start()

        // Set an OnPreparedListener for our VideoView. For more information about VideoViews,
        // check out the Android Docs: https://developer.android.com/reference/android/widget/VideoView.html
        videoBG!!.setOnPreparedListener { mediaPlayer ->
            mMediaPlayer = mediaPlayer
            // We want our video to play over and over so we set looping to true.
            mMediaPlayer!!.isLooping = true
            // We then seek to the current posistion if it has been set and play the video.
            if (mCurrentVideoPosition != 0) {
                mMediaPlayer!!.seekTo(mCurrentVideoPosition)
                mMediaPlayer!!.start()
            }
        }
    }

    /*================================ Important Section! ================================
    We must override onPause(), onResume(), and onDestroy() to properly handle our
    VideoView.
     */
    override fun onPause() {
        super.onPause()
        // Capture the current video position and pause the video.
        mCurrentVideoPosition = mMediaPlayer!!.currentPosition
        videoBG!!.pause()
    }

    override fun onResume() {
        super.onResume()
        // Restart the video when resuming the Activity
        videoBG!!.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        // When the Activity is destroyed, release our MediaPlayer and set it to null.
        mMediaPlayer!!.release()
        mMediaPlayer = null
    }

    private fun loginUser()
    {

        val email = etEmailAddress.text.toString()
        val password = password_login.text.toString()

        when
        {

            TextUtils.isEmpty(email) ->  Toast.makeText(this, "email is required", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(password) ->  Toast.makeText(this, "password is required", Toast.LENGTH_LONG).show()

            else ->
            {

                val progressDialog = ProgressDialog(this@SignInActivity)
                progressDialog.setTitle("Login")
                progressDialog.setMessage("Please wait, this may take a while...")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful)
                        {
                            progressDialog.dismiss()

                            val intent = Intent(this@SignInActivity, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }
                        else
                        {
                            val  message = task.exception!!.toString()
                            Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
                            mAuth.signOut()
                            progressDialog.dismiss()
                        }
                    }

            }

        }

    }

    override fun onStart() {
        super.onStart()

        if(FirebaseAuth.getInstance().currentUser != null)
        {
            val intent = Intent(this@SignInActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }


}
