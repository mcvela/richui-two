package de.andreasschmitt.richui

import de.andreasschmitt.richui.taglib.renderer.RenderException
import de.andreasschmitt.richui.taglib.renderer.Renderer

class CheckedTreeViewTagLib {

	static namespace = "richui"

	Renderer checkedTreeViewRenderer

	def checkedTreeView = { attrs ->
		try {
			attrs.xml = new XmlSlurper().parseText(attrs.xml)
		}
		catch (e) {
			log.error("Error parsing xml", e)
			return
		}

		try {
			out << checkedTreeViewRenderer.renderTag(attrs)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}
}
