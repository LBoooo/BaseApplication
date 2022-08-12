package com.hinacle.base.datastore

@Suppress("UNCHECKED_CAST")
inline fun <reified T> saveDataStore(block: () -> Map<String, T>) {
    block.invoke().forEach {
        when (val data = it.value) {
            is Boolean -> {
                DataStoreUtils.putBoolean(it.key, data)
            }
            is String -> {
                DataStoreUtils.putString(it.key, data)
            }
            is Int -> {
                DataStoreUtils.putInt(it.key, data)
            }
            is Long -> {
                DataStoreUtils.putLong(it.key, data)
            }
            is Float -> {
                DataStoreUtils.putFloat(it.key, data)
            }
            is Set<*> -> {
                DataStoreUtils.putStringSet(it.key, data as Set<String>)
            }
        }
    }
}

inline fun <reified T : Any> getDataStore(key: String, block: T.() -> Unit) {
    when (T::class) {
        Boolean::class -> {
            (DataStoreUtils.getBoolean(key) as T).block()
        }
        String::class -> {
            (DataStoreUtils.getString(key) as T).block()
        }
        Int::class -> {
            (DataStoreUtils.getInt(key) as T).block()
        }
        Long::class -> {
            (DataStoreUtils.getLong(key) as T).block()
        }
         Float::class -> {
            (DataStoreUtils.getFloat(key) as T).block()
        }
        Set::class -> {
            (DataStoreUtils.getStringSet( key, emptySet()) as T).block()
        }
    }
}

