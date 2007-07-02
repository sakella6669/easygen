package org.easygen.core.generators.velocity;

/**
 * String stack implementation. Used in velocity templates to simulate
 * call stack for recursive macro.
 * Separator for the string stack is '|' 
 */
public class VelocityStackUtils {

	public static final char STACK_SEPARATOR = '|';
	private String stack = "";
	/**
	 * 
	 */
	public VelocityStackUtils() {
	}

	/**
	 * Simply concat newItem to the current String stack
	 * @param newItem
	 */
	public void push(String newItem) {
		stack = newItem + STACK_SEPARATOR + stack;
	}

	/**
	 * Pop the last added item using substring
	 * @return
	 */
	public String pop() {
		int secondItemPosition = stack.indexOf(STACK_SEPARATOR);
		if (secondItemPosition < 0) {
			return stack;
		}
		String item = stack.substring(0, secondItemPosition);
		stack = stack.substring(secondItemPosition+1);
		return item;
	}
}
