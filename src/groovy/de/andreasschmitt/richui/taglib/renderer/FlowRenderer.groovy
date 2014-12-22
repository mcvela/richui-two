package de.andreasschmitt.richui.taglib.renderer

import groovy.xml.MarkupBuilder
import de.andreasschmitt.richui.taglib.Resource

/**
 * @author Andreas Schmitt
 */
class FlowRenderer extends AbstractRenderer {

	protected void renderTagContent(Map attrs, MarkupBuilder builder) throws RenderException {
		renderTagContent(attrs, null, builder)
	}

	protected void renderTagContent(Map attrs, Closure body, MarkupBuilder builder) throws RenderException {
		//Seems to be a bug in protoflow
		String id = "protoflow" //"f" + getUniqueId()

		if (!attrs.caption) {
			attrs.caption = "false"
		}

		if (!attrs.reflection) {
			attrs.reflection = "true"
		}

		if (!attrs.onClickScroll) {
			attrs.onClickScroll = "true"
		}

		if (!attrs.startIndex) {
			attrs.startIndex = "2"
		}

		if (!attrs.slider) {
			attrs.slider = "true"
		}

		builder."div"(id: id) {
			builder.yield("${body.call()}", false)
		}

		builder.script(type: "text/javascript") {
			builder.yield("	Event.observe(window, 'load', function() {\n", false)
			builder.yield("		new ProtoFlow(\$('${id}'), {\n", false)
			builder.yield("			startIndex: ${attrs.startIndex},\n", false)
			builder.yield("			slider: ${attrs.slider},\n", false)
			builder.yield("			captions: ${attrs.caption},\n" , false)
			builder.yield("			useReflection: ${attrs.reflection},\n", false)
			builder.yield("			enableOnClickScroll: ${attrs.onClickScroll}\n", false)
			builder.yield("		});\n", false)
			builder.yield("	});\n", false)
		}
	}

	protected List<Resource> getComponentResources(Map attrs, String resourcePath) throws RenderException {
		List<Resource> resources = []

		// CSS
		Resource css = new Resource()
		if (attrs.skin) {
			if (attrs.skin == "default") {
				css.builder.link(rel: "stylesheet", type: "text/css", href: "${resourcePath}/js/flow/protoFlow.css")
				css.name = "${resourcePath}/js/flow/protoFlow.css"
			}
			else {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				css.builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
				css.name = "${applicationResourcePath}/css/${attrs.skin}.css"
			}
		}
		else {
			css.builder.link(rel: "stylesheet", type: "text/css", href: "${resourcePath}/js/flow/protoFlow.css")
			css.name = "${resourcePath}/js/flow/protoFlow.css"
		}
		resources << css

		// Prototype
		Resource prototype = new Resource(name: "${resourcePath}/js/flow/lib/prototype.js")
		prototype.builder.script(type: "text/javascript", src: "${resourcePath}/js/flow/lib/prototype.js", "")
		resources << prototype

		// Scriptaculous
		Resource scriptaculous = new Resource(name: "${resourcePath}/js/flow/lib/scriptaculous.js")
		scriptaculous.builder.script(type: "text/javascript", src: "${resourcePath}/js/flow/lib/scriptaculous.js", "")
		resources << scriptaculous

		// Reflection
		Resource reflection = new Resource(name: "${resourcePath}/js/reflection/reflection.js")
		reflection.builder.script(type: "text/javascript", src: "${resourcePath}/js/reflection/reflection.js", "")
		resources << reflection

		// Proto flow
		Resource protoFlow = new Resource(name: "${resourcePath}/js/flow/protoFlow.js")
		protoFlow.builder.script(type: "text/javascript", src: "${resourcePath}/js/flow/protoFlow.js", "")
		resources << protoFlow

		return resources
	}

	protected void renderResourcesContent(Map attrs, MarkupBuilder builder, String resourcePath) throws RenderException {
		builder.yield("<!-- Flow -->", false)

		if (attrs.skin) {
			if (attrs.skin == "default") {
				builder.link(rel: "stylesheet", type: "text/css", href: "$resourcePath/js/flow/protoFlow.css")
			}
			else {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
			}
		}
		else {
			builder.link(rel: "stylesheet", type: "text/css", href: "$resourcePath/js/flow/protoFlow.css")
		}

		builder.script(type: "text/javascript", src: "$resourcePath/js/flow/lib/prototype.js", "")
		builder.script(type: "text/javascript", src: "$resourcePath/js/flow/lib/scriptaculous.js", "")
		builder.script(type: "text/javascript", src: "$resourcePath/js/reflection/reflection.js", "")
		builder.script(type: "text/javascript", src: "$resourcePath/js/flow/protoFlow.js", "")
	}
}
