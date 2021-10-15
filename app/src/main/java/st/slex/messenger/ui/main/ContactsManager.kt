package st.slex.messenger.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI
import android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER
import android.provider.ContactsContract.Contacts.DISPLAY_NAME
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import st.slex.messenger.data.contacts.FirebaseContactModel
import st.slex.messenger.utilites.PERMISSION_REQUEST
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@ExperimentalCoroutinesApi
interface ContactsManager {

    suspend fun setContacts(): Flow<List<FirebaseContactModel>>

    class Base @Inject constructor(
        private val activity: MainActivity
    ) : ContactsManager {

        @SuppressLint("Range")
        override suspend fun setContacts(): Flow<List<FirebaseContactModel>> =
            flow {
                val list = mutableListOf<FirebaseContactModel>()
                checkPermission(Manifest.permission.READ_CONTACTS).collect { permission ->
                    if (permission) {
                        val cursor =
                            activity.contentResolver.query(CONTENT_URI, null, null, null, null)
                        cursor?.let { item ->
                            while (item.moveToNext()) {
                                val username =
                                    item.getString(item.getColumnIndexOrThrow(DISPLAY_NAME))
                                val phone = item.getString(item.getColumnIndexOrThrow(NUMBER))
                                val setPhone = phone.replace(Regex("[\\s,-]"), "")
                                val contactModel =
                                    FirebaseContactModel(phone = setPhone, full_name = username)
                                list.add(contactModel)
                            }
                        }
                        cursor?.close()
                        emit(list)
                    } else emit(list)
                }
            }.catch {
                currentCoroutineContext().cancel(CancellationException(it))
            }

        private suspend fun checkPermission(permission: String): Flow<Boolean> = flow {
            if (ContextCompat.checkSelfPermission(activity, permission) == PERMISSION_GRANTED) {
                emit(true)
            } else {
                ActivityCompat.requestPermissions(activity, arrayOf(permission), PERMISSION_REQUEST)
                emit(false)
            }
        }

    }
}