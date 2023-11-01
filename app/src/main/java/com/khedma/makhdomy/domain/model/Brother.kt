package com.khedma.makhdomy.domain.model


data class Brother(
    val name: String? = null,
    val type: String? = null,
    val age: String? = null,
    val study: String? = null
) {
    override fun toString(): String {
        var stringBrother = ""
        stringBrother += name
        stringBrother += " - $type"
        if (!age.isNullOrEmpty())
            stringBrother += " - $age سنوات"
        if (!study.isNullOrEmpty())
            stringBrother += " - $study"
        return stringBrother
    }
}
