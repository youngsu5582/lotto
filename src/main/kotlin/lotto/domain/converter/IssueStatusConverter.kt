package lotto.domain.converter

import jakarta.persistence.Converter
import lotto.domain.entity.IssueStatus

@Converter
class IssueStatusConverter : EnumToStringConverter<IssueStatus>(IssueStatus::class.java)
