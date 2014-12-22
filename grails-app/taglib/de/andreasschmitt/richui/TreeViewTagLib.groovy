package de.andreasschmitt.richui

import de.andreasschmitt.richui.taglib.renderer.RenderException
import de.andreasschmitt.richui.taglib.renderer.Renderer

/**
 * @author Andreas Schmitt
 */
class TreeViewTagLib {

	static namespace = "richui"

	Renderer treeViewRenderer

	def treeView = {attrs ->
		try {
			attrs.xml = new XmlSlurper().parseText(attrs.xml)
		}
		catch (e) {
			log.error("Error parsing xml", e)
			return
		}

		try {
			out << treeViewRenderer.renderTag(attrs)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}
}
