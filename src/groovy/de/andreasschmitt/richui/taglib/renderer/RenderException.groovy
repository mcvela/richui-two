package de.andreasschmitt.richui.taglib.renderer

import groovy.transform.CompileStatic

/**
 * @author Andreas Schmitt
 */
@CompileStatic
class RenderException extends RuntimeException {

	RenderException(String message) {
		super(message)
	}

	RenderException(String message, Throwable cause) {
		super(message, cause)
	}
}
