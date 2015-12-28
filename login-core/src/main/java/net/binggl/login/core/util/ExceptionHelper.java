package net.binggl.login.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @see https://gist.github.com/jfager/9317201
 * 
 * <pre>
 * {@code
 * import static net.binggl.login.core.util.ExceptionHelper.wrap;
 * 
 * String foo = wrapEx(() -> {
 *     if(new Random().nextBoolean()) {
 *         throw new Exception("Look ma, no try/catch!");
 *     } else {
 *         return "Cool";
 *     }
 * });
 * }
 * </pre> 
 */
public class ExceptionHelper {
    
	private static final Logger logger = LoggerFactory.getLogger(ExceptionHelper.class);
	
    public interface Block {
        void go() throws Exception;
    }
    
    public interface NoArgFn<T> {
        T go() throws Exception;
    }
    
    //And because these overload it only looks like I'm importing one name.
    public static void wrapEx(Block t) {
        try {
            t.go();
        } catch(Exception e) {
        	logger.error("Got exception {}, stack: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
    
    public static <T> T wrap(NoArgFn<T> f) {
        try {
            return f.go();
        } catch(Exception e) {
        	logger.error("Got exception {}, stack: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}