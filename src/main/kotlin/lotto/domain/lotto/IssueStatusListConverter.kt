package lotto.domain.lotto

import jakarta.persistence.Converter

@Converter
class IssueStatusListConverter : EnumListToStringConverter<IssueStatus>(IssueStatus::class.java)

