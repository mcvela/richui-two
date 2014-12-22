package de.andreasschmitt.richui.taglib.renderer

import groovy.xml.MarkupBuilder
import de.andreasschmitt.richui.taglib.Resource

/**
 * @author Andreas Schmitt
 */
class GlowSliderRenderer extends AbstractRenderer {

	protected void renderTagContent(Map attrs, MarkupBuilder builder) throws RenderException {
		renderTagContent(attrs, null, builder)
	}

	protected void renderTagContent(Map attrs, Closure body, MarkupBuilder builder) throws RenderException {
		String id = "s" + getUniqueId()
		String inputId = "i" + getUniqueId()
		String inputName = "silder"

		if (!attrs.id) {
			attrs.id = id
		}

		if (!attrs.inputId) {
			attrs.inputId = inputId
		}

		if (!attrs.inputName) {
			attrs.inputName = inputName
		}

		if (!attrs.value) {
			attrs.value = ""
		}

		Map internalAttributes = [id: true, controller: true, action: true, inputId: true, inputName: true, value: true, "class": true, style: true]
		Map htmlAttributes = ["class": "${attrs.'class'}", type: "hidden", id: "${attrs.inputId}", name: "${attrs.inputName}", style: "${attrs.style}", value: "${attrs.value}"]
		Map configAttributes = [bindTo: "#${attrs.inputId}", 'step': "10", min: "0", max: "100"]

		//Add additional attributes
		attrs.each { key, value ->
			if (key.startsWith("html:")) {
				htmlAttributes[key.replace("html:", "")] = value
			}
			else {
				configAttributes[key] = value
			}
		}

		builder."div"(id: "${attrs.id}") {
			input(htmlAttributes)
		}

		//Attribute transformer mapping for certain attributes
		AttributeTransformer attributeTransformer = new AttributeTransformer()
		attributeTransformer.registerTransformer("bindTo", AttributeTransformer.stringTransformer)

		builder.script(type: "text/javascript") {
			builder.yield(" var slider${id} = new glow.widgets.Slider('#${attrs.id}', {\n", false)

			int i = 0
			configAttributes.each {key, value ->
				if (!internalAttributes.containsKey(key) && value != null) {
					if (i >= 1) {
						builder.yield(",\n", false)
					}
					builder.yield("	${key} :  ${attributeTransformer.transform(key, value)}", false)
					i += 1
				}
			}

			builder.yield(" });\n", false)
		}
	}

	protected List<Resource> getComponentResources(Map attrs, String resourcePath) throws RenderException {
		List<Resource> resources = []

		// Glow core
		Resource glowCore = new Resource(name: "${resourcePath}/js/glow/1.5.1/core/core.js")
		glowCore.builder.script(type: "text/javascript", src: "${resourcePath}/js/glow/1.5.1/core/core.js", "")
		resources << glowCore

		// Glow widgets
		Resource glowWidgets = new Resource(name: "${resourcePath}/js/glow/1.5.1/widgets/widgets.js")
		glowWidgets.builder.script(type: "text/javascript", src: "${resourcePath}/js/glow/1.5.1/widgets/widgets.js", "")
		resources << glowWidgets

		// Glow widget css
		Resource glowWidgetsCss = new Resource(name: "${resourcePath}/js/glow/1.5.1/widgets/widgets.css")
		glowWidgetsCss.builder.link(rel: "stylesheet", type: "text/css", href: "${resourcePath}/js/glow/1.5.1/widgets/widgets.css")
		resources << glowWidgetsCss

		return resources
	}

	protected void renderResourcesContent(Map attrs, MarkupBuilder builder, String resourcePath) throws RenderException {
		builder.yield("<!-- Slider -->", false)

		String yuiResourcePath = getResourcePath(resourcePath, attrs.remote != null)

		/*
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
		*/

		builder.script(type: "text/javascript", src: "$resourcePath/js/glow/1.5.1/core/core.js", "")
		builder.script(type: "text/javascript", src: "$resourcePath/js/glow/1.5.1/widgets/widgets.js", "")
		builder.link(rel: "stylesheet", type: "text/css", href: "$resourcePath/js/glow/1.5.1/widgets/widgets.css")
	}
}
