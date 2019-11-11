package com.arafat.arafate_com.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import com.arafat.arafate_com.R
import com.facebook.login.LoginResult
import java.util.*
import android.content.Intent
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginManager
import org.json.JSONObject
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class AuthActivity : AppCompatActivity() {

    private val RC_SIGN_IN: Int = 100
    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var btnFbLogin : RelativeLayout
    private lateinit var btnGoogleLOgin : RelativeLayout
    private lateinit var callbackManager : CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)


        initFB()
        initGoogle()


        btnFbLogin = findViewById(R.id.rlFBLogin)
        btnFbLogin.setOnClickListener {

            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"))
        }

        btnGoogleLOgin = findViewById(R.id.rlGoogleLogin)
        btnGoogleLOgin.setOnClickListener {
            GOOGLEsignIn()
        }
    }

    private fun initGoogle() {

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val account : GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)

    }

    private fun initFB() {

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                Log.d("Success", "Login")

                val request : GraphRequest = GraphRequest.newMeRequest(result!!.accessToken
                ) { obj, response ->
                    Log.d("LoginActivity", response.toString())
                    Log.d("LoginActivity", obj.toString())

                    val email = obj?.getString("email")
                    val name = obj?.getString("name")

                    Log.d("me::", email+"--"+name+"--")
                }

                val parameters = Bundle()
                parameters.putString("fields", "id,name,email,gender,birthday")
                request.setParameters(parameters);
                request.executeAsync()

            }

            override fun onCancel() {
                Toast.makeText(applicationContext,"Login Cancel", Toast.LENGTH_LONG).show()

            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(applicationContext, error?.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode === RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }else{

            callbackManager.onActivityResult(requestCode, resultCode, data)

        }
    }


    private fun GOOGLEsignIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            val email = account?.email
            val name = account?.displayName
            val photo = account?.photoUrl

            Log.d("details", email+"--"+name+"--"+photo)

            //updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("signInResult:", ""+ e.statusCode)
            //updateUI(null)
        }

    }
}
