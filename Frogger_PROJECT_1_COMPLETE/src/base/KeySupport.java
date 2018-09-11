package base;

/** Enables support for keyboard actions
 * @author Rohyl Joshi
 *
 */
public interface KeySupport {
	/** Raised when a key is pressed on the keyboard
	 * @param key Integer value of pressed key (ASCII)
	 * @param c Java character representation of pressed key
	 */
    void onKeyPress(int key, char c);
}