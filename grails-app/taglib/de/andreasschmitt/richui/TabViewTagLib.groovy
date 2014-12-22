package de.andreasschmitt.richui

import de.andreasschmitt.richui.taglib.renderer.RenderException
import de.andreasschmitt.richui.taglib.renderer.Renderer

/**
 * @author Andreas Schmitt
 */
class TabViewTagLib {

	static namespace = "richui"

	Renderer tabViewRenderer
	Renderer tabLabelsRenderer
	Renderer tabLabelRenderer
	Renderer tabContentsRenderer
	Renderer tabContentRenderer

	def tabView = {attrs, body ->
		try {
			out << tabViewRenderer.renderTag(attrs, body)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}

	def tabLabels = { attrs, body ->
		try {
			out << tabLabelsRenderer.renderTag(attrs, body)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}

	def tabContents = { attrs, body ->
		try {
			out << tabContentsRenderer.renderTag(attrs, body)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}

	def tabContent = { attrs, body ->
		try {
			out << tabContentRenderer.renderTag(attrs, body)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}

	def tabLabel = { attrs ->
		try {
			out << tabLabelRenderer.renderTag(attrs)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}
}
