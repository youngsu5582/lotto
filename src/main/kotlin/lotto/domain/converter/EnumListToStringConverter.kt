package lotto.domain.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
abstract class EnumListToStringConverter<T : Enum<T>>(
    private val enumClass: Class<T>,
) : AttributeConverter<List<T>, String> {
    override fun convertToDatabaseColumn(attribute: List<T>?): String {
        return attribute?.joinToString(",") { it.name } ?: ""
    }

    override fun convertToEntityAttribute(dbData: String?): List<T> {
        return dbData?.split(",")?.map { enumName ->
            java.lang.Enum.valueOf(enumClass, enumName.trim())
        } ?: emptyList()
    }
}
