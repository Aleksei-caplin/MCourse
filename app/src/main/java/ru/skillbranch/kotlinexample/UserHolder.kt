package ru.skillbranch.kotlinexample

object UserHolder {
    private val map = mutableMapOf<String, User>()

    fun resetMap() {
        map.clear()
    }

    fun registerUser (
        fullName: String,
        email:String,
        password: String
    ): User{
        return User.makeUser(fullName, email = email, password = password)
            .also { user -> map[user.login] = user}
    }

    fun loginUser(login: String, password: String) : String? {
        val user = map[login.trim()] ?: map[login.replace("[^+\\d]".toRegex(), "")]
        return user?.run {
            if(checkPassword(password)) this.userInfo
            else null
        }
    }

    fun registerUserByPhone(fullName: String, rawPhone: String): User {
        return User.makeUser(fullName, phone = rawPhone)
            .also {user ->
                val pattern = "^[+]*[0-9]?[ ]?[(]?[0-9]{1,4}[)]?[ ]?[0-9]{3}[- ]?[0-9]{2}[- ]?[0-9]{2}\$".toRegex()
                if (pattern.matches(rawPhone)) {
                    if (map.containsKey(user.login.trim())) {
                        throw IllegalArgumentException("A user with this phone already exists")
                    } else {
                        map[user.login.trim()] = user
                    }
                } else {
                    throw IllegalArgumentException("Enter a valid phone number starting with a + and containing 11 digits")
                }
            }
    }

    fun requestAccessCode(login: String) {
        val user = map[login.trim()] ?: map[login.replace("[^+\\d]".toRegex(), "")]
        user?.newAuthorizationCode()
    }
}