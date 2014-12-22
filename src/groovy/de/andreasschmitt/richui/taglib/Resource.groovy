package de.andreasschmitt.richui.taglib

import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder

/**
 * @author Andreas Schmitt
 */
@CompileStatic
class Resource {

	String name
	StringWriter writer = new StringWriter()
	MarkupBuilder builder = new MarkupBuilder(writer)

	String getData() {
		writer.flush()
		writer.toString()
	}

	String toString() {
		name
	}
}
