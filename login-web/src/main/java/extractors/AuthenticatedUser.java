package extractors;

import ninja.params.WithArgumentExtractor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ninja extractor for loged in user
 * @author henrik
 */
@WithArgumentExtractor(AuthenticatedUserExtractor.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface AuthenticatedUser {
}
