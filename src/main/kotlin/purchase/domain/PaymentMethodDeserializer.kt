package purchase.domain

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import purchase.domain.vo.PaymentMethod

class PaymentMethodDeserializer : JsonDeserializer<PaymentMethod>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): PaymentMethod {
        return PaymentMethod.get(p.text) ?: throw NoSuchElementException("메소드에 해당하는 값이 없습니다")
    }
}
