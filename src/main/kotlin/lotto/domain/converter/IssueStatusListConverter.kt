package lotto.domain.converter

import jakarta.persistence.Converter
import lotto.domain.entity.IssueStatus

@Converter
class IssueStatusListConverter : EnumListToStringConverter<IssueStatus>(IssueStatus::class.java)

