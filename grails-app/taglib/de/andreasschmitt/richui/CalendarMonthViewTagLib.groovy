package de.andreasschmitt.richui

import de.andreasschmitt.richui.taglib.renderer.RenderException
import de.andreasschmitt.richui.taglib.renderer.Renderer

/**
 * @author Andreas Schmitt
 */
class CalendarMonthViewTagLib {

	static namespace = "richui"

	Renderer calendarMonthViewRenderer
	def messageSource

	def calendarMonthView = {attrs ->

		if (!attrs?.action) {
			attrs.action = actionName
		}

		if (!attrs?.controller) {
			attrs.controller = controllerName
		}

		if (!attrs?.dayController) {
			attrs.dayController = controllerName
		}

		if (!attrs?.weekController) {
			attrs.weekController = controllerName
		}

		if (attrs?.weekAction) {
			attrs.weekUrl = "${createLink(controller: attrs.weekController, action: attrs.weekAction)}"
		}
		if (attrs?.dayAction) {
			attrs.dayUrl = "${createLink(controller: attrs.dayController, action: attrs.dayAction)}"
		}
		if (attrs?.createLink && attrs?.createLink == "true") {
			String action = attrs.action

			// Domain specific controller mapping
			if (attrs.controller instanceof Map) {
				Map itemUrls = [:]

				attrs.controller.each { key, value ->
					String controller = value //attrs.controller.get(domain.toString())

					// Domain specific action mapping
					if (attrs.action instanceof Map && attrs.action.containsKey(key)) {
						action = attrs.action[key]
					}

					itemUrls.put(key, "${createLink(controller: controller, action: action, id: 'itemId')}")
				}
				attrs.itemUrls = itemUrls
			}
			else {
				attrs.itemUrl = "${createLink(controller: attrs.controller, action: attrs.action, id: 'itemId')}"
			}
		}

		try {
			attrs.week = messageSource.getMessage("default.week", null, request?.locale)
			attrs.weekDays = [:]
			['monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday'].each { String day ->
				attrs.weekDays[day] = messageSource.getMessage("default." + day, null, request?.locale)
			}
		}
		catch (e) {
			log.error("Error retrieving messages", e)
		}

		try {
			out << calendarMonthViewRenderer.renderTag(attrs)
		}
		catch (RenderException e) {
			log.error e.message, e
		}
	}
}
