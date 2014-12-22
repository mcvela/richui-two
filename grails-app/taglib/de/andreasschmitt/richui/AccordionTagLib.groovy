package de.andreasschmitt.richui

import de.andreasschmitt.richui.taglib.renderer.RenderException
import de.andreasschmitt.richui.taglib.renderer.Renderer

/**
 * @author Andreas Schmitt
 */
class AccordionTagLib {

	static namespace = "richui"

	Renderer accordionRenderer
	Renderer accordionItemRenderer

	def accordion = {attrs, body ->
		try {
			out << accordionRenderer.renderTag(attrs, body)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}

	def accordionItem = {attrs, body ->
		try {
			out << accordionItemRenderer.renderTag(attrs, body)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}
}
