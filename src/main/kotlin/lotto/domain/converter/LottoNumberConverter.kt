package lotto.domain.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class LottoNumbersConverter : AttributeConverter<List<Int>, String> {

    override fun convertToDatabaseColumn(attribute: List<Int>?): String {
        return attribute?.joinToString(",") ?: ""
    }

    override fun convertToEntityAttribute(dbData: String?): List<Int> {
        return dbData?.split(",")?.map { it.trim().toInt() } ?: emptyList()
    }
}
