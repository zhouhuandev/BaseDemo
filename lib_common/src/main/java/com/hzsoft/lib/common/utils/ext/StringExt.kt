package com.hzsoft.lib.common.utils.ext

/**
 * take if either non-null or not empty
 *
 * ```
 * var str: String? = null
 * …
 * str.takeIfNotEmpty()?.also { print(it) } // only print value if not null
 * ```
 */
fun String?.takeIfNotEmpty(): String? = this?.takeIf { it.isNotEmpty() }

/**
 * get or value specified if null
 *
 * Sample:
 * ```
 * var str: String? = null
 * …
 * str.orDefault("default").also { print(it) } // print 'default' if null
 * ```
 */
fun String?.orDefault(fallbackValue: String): String = this ?: fallbackValue

/**
 * run with default value provided if null
 *
 * Sample:
 * ```
 * var str: String? = null
 * …
 * str.orDefault { "" }.also { print(it) } // print empty string if null
 * ```
 */
inline fun String?.orDefault(fallbackValue: () -> String): String {
    return this ?: fallbackValue()
}

/**
 * Use value if not empty
 *
 * Sample:
 * ```
 * var str: String? = ""
 * …
 * str.useNotEmpty { print(it) } // print if str not empty
 * ```
 */
inline fun String?.useNotEmpty(block: (String) -> Unit) {
    this.takeIfNotEmpty()?.also(block)
}
