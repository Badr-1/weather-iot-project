@file:Suppress("DEPRECATION")

package com.iot.weather.utils

import android.os.AsyncTask
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class MailAPI(
    private val email: String, private val subject: String, private val message: String
) : AsyncTask<Void?, Void?, Void?>() {

    companion object {
        const val EMAIL = "rick.sanchez4044@gmail.com"
        const val PASSWORD = "jhhcdrfpyirztoeo"
    }

    private var session: Session? = null

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg p0: Void?): Void? {
        val properties = Properties()
        properties["mail.smtp.host"] = "smtp.gmail.com"
        properties["mail.smtp.socketFactory.port"] = "465"
        properties["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        properties["mail.smtp.auth"] = "true"
        properties["mail.smtp.port"] = "465"
        session = Session.getDefaultInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(EMAIL, PASSWORD)
            }
        })
        val mimeMessage = MimeMessage(session)
        try {
            mimeMessage.setFrom(InternetAddress(EMAIL))
            mimeMessage.addRecipients(Message.RecipientType.TO, InternetAddress(email).toString())
            mimeMessage.subject = subject
            mimeMessage.setText(message)
            Transport.send(mimeMessage)
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
        return null
    }

}