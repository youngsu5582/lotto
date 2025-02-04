package member.domain.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "members")
class Member(
    @Id
    @GeneratedValue(generator = "UUID")
    private val id: UUID? = null,
    private val email: String,
    private val password: String
) {
    fun isNotEqualPassword(password: String): Boolean {
        return this.password != password
    }

    fun getId() = id ?: throw IllegalArgumentException("Not Exist Id")
    fun getEmail() = email
    fun getPassword() = password
}
