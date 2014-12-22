package de.andreasschmitt.richui.taglib.renderer

import groovy.transform.CompileStatic
import de.andreasschmitt.richui.taglib.Resource

/**
 * @author Andreas Schmitt
 */
@CompileStatic
interface Renderer {
	String renderResources(Map attrs, String contextPath) throws RenderException
	String renderTag(Map attrs) throws RenderException
	String renderTag(Map attrs, Closure body) throws RenderException
	List<Resource> getResources(Map attrs, String contextPath) throws RenderException
}
