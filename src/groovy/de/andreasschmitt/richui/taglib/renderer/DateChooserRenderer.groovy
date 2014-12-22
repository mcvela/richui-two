package de.andreasschmitt.richui.taglib.renderer

import groovy.xml.MarkupBuilder

import java.text.DateFormat
import java.text.SimpleDateFormat

import de.andreasschmitt.richui.taglib.Resource

/**
 * @author Andreas Schmitt
 */
class DateChooserRenderer extends AbstractRenderer {

	protected void renderTagContent(Map attrs, MarkupBuilder builder) throws RenderException {
		renderTagContent(attrs, null, builder)
	}

	protected void renderTagContent(Map attrs, Closure body, MarkupBuilder builder) throws RenderException {
		String id = "c" + getUniqueId()
		String inputId = "i" + getUniqueId()

		if (!attrs.id) {
			attrs.id = attrs.name
		}

		if (attrs.inputId) {
			inputId = attrs.inputId
		}

		if (!attrs.'class') {
			attrs.'class' = ""
		}

		if (!attrs.style) {
			attrs.style = ""
		}

		if (!attrs.timezone) {
			attrs.timezone = TimeZone.getDefault()
		}

		String formattedValue = ""
		String day = ""
		String month = ""
		String year = ""
		String hour = "00"
		String minute = "00"

		if (attrs.value) {
			try {
				DateFormat fmt = new SimpleDateFormat(attrs.format)
				fmt.setTimeZone(attrs.timezone)
				formattedValue = fmt.format(attrs.value)

				Calendar cal = new GregorianCalendar(attrs.timezone)
				cal.setTime(attrs.value)
				day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH))
				month = Integer.toString(cal.get(Calendar.MONTH)+1)
				year = Integer.toString(cal.get(Calendar.YEAR))

				hour = Integer.toString(cal.get(Calendar.HOUR_OF_DAY))
				if (hour == "0") {
					hour = "00"
				}

				minute = Integer.toString(cal.get(Calendar.MINUTE))
				if (minute == "0") {
					minute = "00"
				}
			}
			catch (e) {
				log.error("Error formatting date", e)
			}
		}

		//Default HTML attributes
		Map htmlAttributes = ["class": "${attrs.'class'}", style: "${attrs.style}", type:"text", name: "${inputId}", id: "${inputId}", value: "${formattedValue}"]

		//Add additional attributes
		attrs.each { key, value ->
			if (key.startsWith("html:")) {
				htmlAttributes[key.replace("html:", "")] = value
			}
		}

		builder.input(htmlAttributes)
		builder.div("id": id, "class": "datechooser yui-skin-sam", "")

		builder.script(type: "text/javascript") {
			builder.yield("	var dateChooser = new DateChooser();\n", false)
			builder.yield("	dateChooser.setDisplayContainer(\"$id\");\n", false)
			builder.yield("	dateChooser.setInputId(\"${inputId}\");\n", false)
			builder.yield("	dateChooser.setStructId(\"${attrs.id}\");\n", false)
			builder.yield("	dateChooser.setFormat(\"${attrs.format}\");\n", false)
			if (attrs.locale) {
				builder.yield("	dateChooser.setLocale(\"${attrs.locale}\");\n", false)
			}

			// Add callbackHandler for focus, blur and change
			if (attrs.onFocus) {
				builder.yield("  dateChooser.setFocusCallback(\"${attrs.onFocus}\");\n", false)
			}
			if (attrs.onBlur) {
				builder.yield("  dateChooser.setBlurCallback(\"${attrs.onBlur}\");\n", false)
			}
			if (attrs.onChange) {
				builder.yield("  dateChooser.setChangeCallback(\"${attrs.onChange}\");\n", false)
			}

			if (attrs.navigator) {
				builder.yield("	dateChooser.setNavigator(${attrs.navigator});\n", false)
			}

			if (attrs.firstDayOfWeek) {
				Map days = [su: 0, mo: 1, tu: 2, we: 3, th: 4, fr: 5, sa: 6]

				if (days.containsKey(attrs.firstDayOfWeek.toLowerCase())) {
					String dayOfWeek = days[attrs.firstDayOfWeek.toLowerCase()]
					builder.yield("	dateChooser.setFirstDayOfWeek(\"${dayOfWeek}\");\n", false)
				}
			}
			builder.yield("	dateChooser.init();\n", false)
		}

		builder.input(type: "hidden", name: "${attrs.name}", id: "${attrs.id}", value: "date.struct")

		if (attrs.time) {
			builder.input("class": "${attrs.hourClass}", style: "${attrs.hourStyle}", type: "text", name: "${attrs.name}_hour", id: "${attrs.id}_hour", value: hour)
			builder.yield(":", false)
			builder.input("class": "${attrs.minuteClass}", style: "${attrs.minuteStyle}", type: "text", name: "${attrs.name}_minute", id: "${attrs.id}_minute", value: minute)
		}

		builder.input(type: "hidden", name: "${attrs.name}_day", id: "${attrs.id}_day", value: day)
		builder.input(type: "hidden", name: "${attrs.name}_month", id: "${attrs.id}_month", value: month)
		builder.input(type: "hidden", name: "${attrs.name}_year", id: "${attrs.id}_year", value: year)
	}

	protected List<Resource> getComponentResources(Map attrs, String resourcePath) throws RenderException {
		List<Resource> resources = []

		String yuiResourcePath = getResourcePath(resourcePath, attrs.remote != null)

		// CSS
		Resource css = new Resource()
		if (attrs.skin) {
			if (attrs.skin == "default") {
				css.builder.link(rel: "stylesheet", type: "text/css", href: "${resourcePath}/css/datechooser.css")
				css.name = "${resourcePath}/css/datechooser.css"
			}
			else {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				css.builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
				css.name = "${applicationResourcePath}/css/${attrs.skin}.css"
			}
		}
		else {
			css.builder.link(rel: "stylesheet", type: "text/css", href: "${resourcePath}/css/datechooser.css")
			css.name = "${resourcePath}/css/datechooser.css"
		}
		resources << css

		// Calendar css
		Resource calendarCss = new Resource(name: "${yuiResourcePath}/calendar/assets/calendar.css")
		calendarCss.builder.link(rel: "stylesheet", type: "text/css", href: "${yuiResourcePath}/calendar/assets/calendar.css", "")
		resources << calendarCss

		// Calendar skin css
		Resource calendarSkinCss = new Resource(name: "${yuiResourcePath}/calendar/assets/skins/sam/calendar.css")
		calendarSkinCss.builder.link(rel: "stylesheet", type: "text/css", href: "${yuiResourcePath}/calendar/assets/skins/sam/calendar.css")
		resources << calendarSkinCss

		// Yahoo dom event
		Resource yahooDomEvent = new Resource(name: "${yuiResourcePath}/yahoo-dom-event/yahoo-dom-event.js")
		yahooDomEvent.builder.script(type: "text/javascript", src: "${yuiResourcePath}/yahoo-dom-event/yahoo-dom-event.js", "")
		resources << yahooDomEvent

		// Date chooser
		Resource dateChooser = new Resource(name: "${resourcePath}/js/datechooser/datechooser.js")
		dateChooser.builder.script(type: "text/javascript", src: "${resourcePath}/js/datechooser/datechooser.js", "")
		resources << dateChooser

		// Yahoo calendar min
		Resource yahooCalendarMin = new Resource(name: "${yuiResourcePath}/calendar/calendar-min.js")
		yahooCalendarMin.builder.script(type: "text/javascript", src: "${yuiResourcePath}/calendar/calendar-min.js", "")
		resources << yahooCalendarMin

		return resources
	}

	protected void renderResourcesContent(Map attrs, MarkupBuilder builder, String resourcePath) throws RenderException {
		builder.yield("<!-- DateChooser -->", false)

		String yuiResourcePath = getResourcePath(resourcePath, attrs.remote != null)

		if (attrs.skin) {
			if (attrs.skin == "default") {
				builder.link(rel: "stylesheet", type: "text/css", href: "$resourcePath/css/datechooser.css")
			}
			else {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
			}
		}
		else {
			builder.link(rel: "stylesheet", type: "text/css", href: "$resourcePath/css/datechooser.css")
		}

		builder.link(rel: "stylesheet", type: "text/css", href: "$yuiResourcePath/calendar/assets/calendar.css")
		builder.link(rel: "stylesheet", type: "text/css", href: "$yuiResourcePath/calendar/assets/skins/sam/calendar.css")
		builder.script(type: "text/javascript", src: "$yuiResourcePath/yahoo-dom-event/yahoo-dom-event.js", "")
		builder.script(type: "text/javascript", src: "$resourcePath/js/datechooser/datechooser.js", "")
		builder.script(type: "text/javascript", src: "$yuiResourcePath/calendar/calendar-min.js", "")
	}
}
