package de.andreasschmitt.richui.image

import groovy.transform.CompileStatic

/**
 * @author Andreas Schmitt
 */
@CompileStatic
class ImageCreationException extends RuntimeException {
	ImageCreationException(String message, Throwable cause) {
		super(message, cause)
	}
}
