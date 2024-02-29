package online.k0ras1k.pulse.data.enums

enum class ValidationStatus(val text: String?) {

    ACCEPTED(""),
    INVALID_LENGTH("Недопустимая длина."),
    INVALID_FORMAT("Несоответствует паттерну."),
    NOT_GOOD_PASSWORD("Недостаточно \"надежный\" пароль."),
    COUNTRY_CODE_NOT_FOUND("Страна с указанным кодом не найдена."),
    TOO_LENGHT_AVATAR_LINK_SIZE("Длина ссылки на аватар пользователя превышает допустимый лимит."),

}