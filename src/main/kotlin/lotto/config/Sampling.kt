package lotto.config

import org.springframework.stereotype.Indexed

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Indexed
annotation class Sampling {
}
