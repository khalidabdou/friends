package com.example.testfriends_jetpackcompose.util

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.testfriends_jetpackcompose.R
import com.example.testfriends_jetpackcompose.data.Question
import com.example.testfriends_jetpackcompose.data.User
import com.example.testfriends_jetpackcompose.util.Constant.Companion.ME
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Utils {

    companion object {
        fun isEmailValid(target: CharSequence?): Boolean {
            return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }

        fun convertToUser(userString: String): User {
            //val json = """{"id": "0", "token": "123445556", "username" : "abdellah","img" : "000" }"""
            val gson = Gson()
            return gson.fromJson(userString, User::class.java)
        }

        fun convertUserToJson(user: User): String {
            val gson = Gson()
            return gson.toJson(user)

        }

        fun compareResults(sender: String, myAnswers: String): Int {

            return 10
        }

        fun score(sender: String, myAnswers: String): String {
            if (myAnswers == "")
                return ""
            var res = 0
            val list1 = sender.split("*").toTypedArray()
            val list2 = myAnswers.split("*").toTypedArray()
            var i = 0
            for (item in list1) {
                if (item == list2[i])
                    res++
                i++
            }
            return "$res/${list1.size}"
        }

        fun generateId(username: String): String {
            return username.substring(0, 2)
        }

        val dynamicLink = Firebase.dynamicLinks.dynamicLink {
            //link = Uri.parse("https://testfriendss.herokuapp.com/ab18")
            domainUriPrefix = "https://testfriends.page.link"
            // Open links with this app on Android
            androidParameters { }
            // Open links with com.example.ios on iOS

        }
        val shortLinkTask = Firebase.dynamicLinks.shortLinkAsync(ShortDynamicLink.Suffix.SHORT) {
            //link = Uri.parse("https://testfriendss.herokuapp.com/ab18")
            domainUriPrefix = "https://testfriends.page.link"

        }.addOnSuccessListener {
            Log.d("dynamic_link", it.shortLink.toString())
        }.addOnCompleteListener {
            //Log.d("dynamic_link",it.shortLink.toString())
        }

        fun generateSharingLink(
            deepLink: Uri,
            getShareableLink: (String) -> Unit = {},
        ) {
            FirebaseDynamicLinks.getInstance().createDynamicLink().run {
                // What is this link parameter? You will get to know when we will actually use this function.
                link = deepLink
                // [domainUriPrefix] will be the domain name you added when setting up Dynamic Links at Firebase Console.
                // You can find it in the Dynamic Links dashboard.
                domainUriPrefix = Constant.PREFIX

                // Required
                androidParameters {
                    // build()
                }
                // Finally
                buildShortDynamicLink()
            }.addOnCompleteListener {
                // Pass the newly created dynamic link so that we retrieve and use it further for sharing via Intent.
                getShareableLink.invoke(it.result.shortLink.toString())
            }
        }


        fun copyTextToClipboard(text: String, context: Context) {
            val clipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", text)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_LONG).show()
        }

        fun shareChallenge(context: Context, link: String) {
            var textShare =
                context.getString(R.string.share_text).replace(
                    "www.example.com",
                    link
                )
            textShare = textShare.replace(
                "www.example.com",
                link
            )
            textShare += "*" + ME!!.inviteId + "*"

            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, textShare)
            shareIntent.flags = FLAG_ACTIVITY_NEW_TASK
            context.startActivity(Intent.createChooser(shareIntent, "SEND TO"))
        }


        @RequiresApi(Build.VERSION_CODES.M)
        fun hasConnection(application: Application): Boolean {
            val connectivityManager = application.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilites =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilites.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilites.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilites.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }

        fun stringToQuestionArrayList(question: String): List<Question> {
            var q = question
            val gson = Gson()
            val list: List<Question> =
                gson.fromJson(q, object : TypeToken<List<Question>>() {}.type)

            return list
        }

        fun openStore(context: Context) {
            val appPackageName = context.packageName // Replace with your own app package name
            val playStoreUrl = "https://play.google.com/store/apps/details?id=$appPackageName"
            val marketIntent = Intent(Intent.ACTION_VIEW, Uri.parse(playStoreUrl))
            context.startActivity(marketIntent)
        }


    }


}