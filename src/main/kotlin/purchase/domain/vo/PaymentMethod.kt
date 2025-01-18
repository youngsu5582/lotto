package purchase.domain.vo

enum class PaymentMethod(private val ln: List<String>) {
    CARD(listOf("CARD", "카드", "Tarjeta")),
    CASH(listOf("CASH", "현금", "Efectivo")),
    SIMPLE_PAY(listOf("SIMPLE_PAY", "간편결제")), ;

    companion object {
        private val IDENTIFY: Map<String, PaymentMethod> =
            entries.flatMap { method ->
                method.ln.map { alias -> alias.uppercase() to method }
            }.toMap()

        fun get(s: String) = IDENTIFY[s.uppercase()]
    }
}
