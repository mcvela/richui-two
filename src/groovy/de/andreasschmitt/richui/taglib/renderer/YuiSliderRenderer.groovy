package de.andreasschmitt.richui.taglib.renderer

import groovy.xml.MarkupBuilder
import de.andreasschmitt.richui.taglib.Resource

/**
 * @author Andreas Schmitt
 */
class YuiSliderRenderer extends AbstractRenderer {

	protected void renderTagContent(Map attrs, MarkupBuilder builder) throws RenderException {
		renderTagContent(attrs, null, builder)
	}

	protected void renderTagContent(Map attrs, Closure body, MarkupBuilder builder) throws RenderException {
		String valueId = "s" + getUniqueId()
		String inputId = "i" + getUniqueId()
		String inputName = "silder"
		String value = "0"

		if (attrs.valueId) {
			valueId = attrs.valueId
		}

		if (attrs.inputId) {
			inputId = attrs.inputId
		}

		if (attrs.inputName) {
			inputName = attrs.inputName
		}

		if (attrs.value) {
			value = attrs.value
		}

		builder.script(type: "text/javascript") {
			builder.yield("var slider = new Slider();\n", false)

			if (attrs.scaleFactor) {
				builder.yield("slider.setScaleFactor(${attrs.scaleFactor});\n", false)
			}

			if (attrs.tickSize) {
				builder.yield("slider.setTickSize(${attrs.tickSize});\n", false)
			}

			builder.yield("slider.setValueId(\"${valueId}\");\n", false)
			builder.yield("slider.setInputId(\"${inputId}\");\n", false)

			builder.yield("slider.init();\n", false)
		}

		builder."div"(id: "slider-bg-horizontal", tabindex: "-1", title: "Slider") {
			"div"(id: "slider-thumb") {
				 img(src: "http://developer.yahoo.com/yui/examples/slider/assets/thumb-n.gif")
			}
		}

		builder.input(autocomplete: "off", id: inputId, name: inputName, type: "text", value: value, "size": "4", maxlength: "4")
		builder.span(id: valueId, "")
	}

	protected List<Resource> getComponentResources(Map attrs, String resourcePath) throws RenderException {
		List<Resource> resources = []

		String yuiResourcePath = getResourcePath(resourcePath, attrs.remote != null)

		// CSS
		Resource css = new Resource(name: "slider.css")
		if (attrs.skin) {
			if (attrs.skin == "default") {
				css.builder.link(rel: "stylesheet", type: "text/css", href: "${resourcePath}/css/slider.css")
			}
			else {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				css.builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
			}
		}
		else {
			css.builder.link(rel: "stylesheet", type: "text/css", href: "${resourcePath}/css/slider.css")
		}
		resources << css

		// Yahoo dom event
		Resource yahooDomEvent = new Resource(name: "yui:yahoo-dom-event.js")
		yahooDomEvent.builder.script(type: "text/javascript", src: "${yuiResourcePath}/yahoo-dom-event/yahoo-dom-event.js", "")
		resources << yahooDomEvent

		// Yahoo animation min
		Resource yahooAnimationMin = new Resource(name: "yui:animation-min.js")
		yahooAnimationMin.builder.script(type: "text/javascript", src: "${yuiResourcePath}/animation/animation-min.js", "")
		resources << yahooAnimationMin

		// Yahoo drag drop min
		Resource yahooDragDropMin = new Resource(name: "yui:dragdrop-min.js")
		yahooDragDropMin.builder.script(type: "text/javascript", src: "${yuiResourcePath}/dragdrop/dragdrop-min.js", "")
		resources << yahooDragDropMin

		// Yahoo slider min
		Resource yahooSliderMin = new Resource(name: "yui:slider-min.js")
		yahooSliderMin.builder.script(type: "text/javascript", src: "${yuiResourcePath}/slider/slider-min.js", "")
		resources << yahooSliderMin

		// Slider js
		Resource sliderJavaScript = new Resource(name: "yui:slider.js")
		sliderJavaScript.builder.script(type: "text/javascript", src: "${resourcePath}/js/slider/slider.js", "")
		resources << sliderJavaScript

		return resources
	}

	protected void renderResourcesContent(Map attrs, MarkupBuilder builder, String resourcePath) throws RenderException {
		builder.yield("<!-- Slider -->", false)

		String yuiResourcePath = getResourcePath(resourcePath, attrs.remote != null)

		if (attrs.skin) {
			if (attrs.skin == "default") {
				builder.link(rel: "stylesheet", type: "text/css", href: "$resourcePath/css/slider.css")
			}
			else {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
			}
		}
		else {
			builder.link(rel: "stylesheet", type: "text/css", href: "$resourcePath/css/slider.css")
		}

		builder.script(type: "text/javascript", src: "$yuiResourcePath/yahoo-dom-event/yahoo-dom-event.js", "")
		builder.script(type: "text/javascript", src: "$yuiResourcePath/animation/animation-min.js", "")
		builder.script(type: "text/javascript", src: "$yuiResourcePath/dragdrop/dragdrop-min.js", "")
		builder.script(type: "text/javascript", src: "$yuiResourcePath/slider/slider-min.js", "")
		builder.script(type: "text/javascript", src: "$resourcePath/js/slider/slider.js", "")
	}
}
