package common.web

import org.springframework.core.annotation.AliasFor
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@RequestMapping(
    method = [RequestMethod.GET]
)
annotation class Get(
    @get:AliasFor(annotation = RequestMapping::class, attribute = "path")
    val path: String = "/"
)
