package lotto.domain.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
abstract class EnumToStringConverter<T : Enum<T>>(
    private val enumClass: Class<T>,
) : AttributeConverter<T, String> {
    override fun convertToDatabaseColumn(attribute: T?): String {
        return attribute?.name ?: ""
    }

    override fun convertToEntityAttribute(dbData: String?): T {
        return java.lang.Enum.valueOf(
            enumClass,
            dbData?.trim() ?: throw IllegalArgumentException("Not Exist Enum $dbData")
        )
    }
}
