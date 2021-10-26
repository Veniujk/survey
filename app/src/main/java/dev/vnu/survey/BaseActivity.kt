package dev.vnu.survey

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.Exception

/**
 *PL Bazowa aktywność - rozszerzać oknami na których ma znaczenie zmiana logowania
 *EN Base activity - extend when there is login change on window
 */
abstract class BaseActivity : AppCompatActivity() {

    private val baseAuthStateListener: FirebaseAuth.AuthStateListener by lazy {
        FirebaseAuth.AuthStateListener { firebaseAuth ->
            SApp.fUser = firebaseAuth.currentUser
        }
    }

    override fun onResume() {
        super.onResume()
        SApp.fAuth.addAuthStateListener(baseAuthStateListener)
    }

    override fun onPause() {
        super.onPause()
        SApp.fAuth.removeAuthStateListener(baseAuthStateListener)
    }

    fun logIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        startActivityForResult(mGoogleSignInClient.signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {

            val taks = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {

                val account = taks.getResult(ApiException::class.java)

                if (account != null) {

                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                SApp.fAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, {
                        task ->
                        if (task.isSuccessful) {
                        onLogInSuccess()
                        } else {
                        onLogInFailue(task.exception)
                        }
                    })}
            } catch (e: ApiException) {
                Log.w("BASE_ACTIVITY", "Google sign in failed")
            }
        }
    }


    open fun onLogInFailue(exception: Exception?) {
        Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
        Log.d("BASE_ACTIVITY", "log in failed")
    }


    open fun onLogInSuccess() {
        Log.d("BASE_ACTIVITY", "log in success")
    }

    companion object {
        const val RC_SIGN_IN = 12412
    }
}