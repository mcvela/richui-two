package de.andreasschmitt.richui

import de.andreasschmitt.richui.taglib.renderer.RenderException
import de.andreasschmitt.richui.taglib.renderer.Renderer

/**
 * @author Andreas Schmitt
 */
class AutoCompleteTagLib {

	static namespace = "richui"

	Renderer autoCompleteRenderer

	def autoComplete = {attrs ->

		if (!attrs?.name) {
			throwTagError("Attribute name is required.")
		}

		if (!attrs?.action) {
			attrs.action = actionName
		}

		if (!attrs?.controller) {
			attrs.controller = controllerName
		}

		if (!attrs.action.contains("/")) {
			attrs.action = "${createLink('controller': attrs.controller, 'action': attrs.action)}"
		}

		if (!attrs?.id) {
			attrs.id = attrs.name
		}

		if (!attrs."class") {
			attrs."class" = ""
		}

		if (!attrs.style) {
			attrs.style = ""
		}

		if (!attrs?.shadow) {
			attrs.shadow = false
		}
		else {
			attrs.shadow = attrs.shadow == "true"
		}

		if (!attrs?.minQueryLength) {
			attrs.minQueryLength = 0
		}

		if (!attrs?.queryDelay) {
			attrs.queryDelay = 0
		}

		if (!attrs?.value) {
			attrs.value = ""
		}

		if (!attrs?.title) {
			attrs.title = ""
		}

		try {
			out << autoCompleteRenderer.renderTag(attrs)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}
}
