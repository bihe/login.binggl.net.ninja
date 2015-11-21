package extractors;

import net.binggl.login.common.models.User;
import ninja.Context;
import ninja.params.ArgumentExtractor;

/**
 * extract the authenticated user from the session
 * @author henrik
 */
public class AuthenticatedUserExtractor implements ArgumentExtractor<User> {
    @Override
    public User extract(Context context) {
        return null;
    }

    @Override
    public Class<User> getExtractedType() {
        return User.class;
    }

    @Override
    public String getFieldName() {
        return null;
    }
}
