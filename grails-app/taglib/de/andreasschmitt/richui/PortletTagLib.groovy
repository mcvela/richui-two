package de.andreasschmitt.richui

import de.andreasschmitt.richui.taglib.renderer.RenderException
import de.andreasschmitt.richui.taglib.renderer.Renderer

/**
 * @author Andreas Schmitt
 */
class PortletTagLib {

	static namespace = "richui"

	Renderer portletRenderer
	Renderer portletViewRenderer

	def portlet = {attrs, body ->

		if (!attrs?.action) {
			attrs.action = actionName
		}

		if (!attrs?.controller) {
			attrs.controller = controllerName
		}

		attrs.action = "${createLink('controller': attrs.controller, 'action': attrs.action)}"

		if (!attrs?.views) {
			throwTagError("Attribute views is required")
		}

		if (!attrs?.page) {
			attrs.page = "1"
		}

		try {
			out << portletRenderer.renderTag(attrs, body)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}

	def portletView = {attrs, body ->
		if (!attrs?.page) {
			attrs.page = "1"
		}

		try {
			out << portletViewRenderer.renderTag(attrs, body)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}
}
