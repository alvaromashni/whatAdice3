package br.com.whatadice.data

import android.content.Context
import android.util.Log
import br.com.whatadice.model.User
import org.json.JSONArray
import org.json.JSONObject
import br.com.whatadice.util.normalizeEmail

class UserStorage(context: Context) {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private companion object {
        const val KEY_USERS_JSON = "users_json"
        const val TAG = "UserStorage"
    }

    private fun key(email: String) = normalizeEmail(email)

    /* essa funcao le o json e retorn um hashmap com email -> user */
    private fun readUsers(): MutableMap<String, User>{
        val jsonStr = prefs .getString(KEY_USERS_JSON, null) ?: return mutableMapOf()
        return runCatching {
            val array = JSONArray(jsonStr)
            val map = mutableMapOf<String, User>()
            for (i in 0 until array.length()){
                val obj = array.getJSONObject(i)
                val user = User(
                    name = obj.getString("name"),
                    email = obj.getString("email"),
                    password = obj.getString("password")
                )
                map[key(user.email)] = user
            }
            map
        }.onFailure { e ->
            Log.e(TAG, "Falha ao parsear users_json: ${e.message}")
        }.getOrElse { mutableMapOf() }
    }
    /* essa aqui salva o map como jsonArray */
    private fun writeUsers(map: Map<String, User>){
        val array = JSONArray()
        map.values.forEach { user ->
            val obj = JSONObject().apply {
                put("name", user.name)
                put("email", user.email)
                put("password", user.password)
            }
            array.put(obj)
        }
        prefs.edit().putString(KEY_USERS_JSON, array.toString()).apply()
    }

    /* verifica se o usuario esta castradado no banco pelo email */
    fun userExists(email: String): Boolean = readUsers().containsKey(email)

    fun addUser(user: User): Boolean{
        val users = readUsers()
        if (users.containsKey(user.email)) return false
        users[user.email] = user
        writeUsers(users)
        return true
    }

    fun validateLogin(email: String, password: String): Boolean {
        val users = readUsers()
        val user = users[email] ?: return false
        return user.password == password
    }

    fun debugDump() {
        val users = readUsers()
        Log.d(TAG, "Users salvos (${users.size}):")
        users.forEach { (k, u) -> Log.d(TAG, " key=$k | displayEmail=${u.email}") }
    }

    fun clearAll() {
        prefs.edit().remove(KEY_USERS_JSON).apply()
    }

    fun getUser(email: String): User? = readUsers()[email]
}