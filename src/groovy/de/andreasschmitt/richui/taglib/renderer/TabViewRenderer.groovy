package de.andreasschmitt.richui.taglib.renderer

import groovy.xml.MarkupBuilder
import de.andreasschmitt.richui.taglib.Resource

/**
 * @author Andreas Schmitt
 */
class TabViewRenderer extends AbstractRenderer {

	protected void renderTagContent(Map attrs, MarkupBuilder builder) throws RenderException {
		renderTagContent(attrs, null, builder)
	}

	protected void renderTagContent(Map attrs, Closure body, MarkupBuilder builder) throws RenderException {
		builder.script(type: "text/javascript") {
			builder.yield("	var tabView = new YAHOO.widget.TabView(\"${attrs.id}\");\n", false)

			if (attrs.event) {
				builder.yield("	function handleClick(e) {", false)
				builder.yield("        ${attrs.event};", false)
				builder.yield("    } ", false)
				builder.yield("    tabView.addListener('activeTabChange', handleClick);", false)
			}
		}

		builder."div"("class": "yui-skin-sam") {
			"div"("class": "yui-navset yui-navset-top", "id": attrs.id) {
				builder.yield("${body.call()}", false)
			}
		}
	}

	protected List<Resource> getComponentResources(Map attrs, String resourcePath) throws RenderException {
		List<Resource> resources = []

		String yuiResourcePath = getResourcePath(resourcePath, attrs.remote != null)

		// CSS
		Resource css = new Resource()

		def cssBuilder = css.builder
		if (attrs.skin) {
			if (attrs.skin == "default") {
				cssBuilder.link(rel: "stylesheet", type: "text/css", href: "${yuiResourcePath}/fonts/fonts-min.css")
				cssBuilder.link(rel: "stylesheet", type: "text/css", href: "${yuiResourcePath}/tabview/assets/tabview-core.css")
				cssBuilder.link(rel: "stylesheet", type: "text/css", href: "${resourcePath}/css/tabView.css")
				css.name = "${yuiResourcePath}/fonts/fonts-min.css"
			}
			else {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				cssBuilder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
				css.name = "${applicationResourcePath}/css/${attrs.skin}.css"
			}
		}
		else {
			cssBuilder.link(rel: "stylesheet", type: "text/css", href: "${yuiResourcePath}/tabview/assets/skins/sam/tabview.css")
			css.name = "${yuiResourcePath}/tabview/assets/skins/sam/tabview.css"
		}

		resources << css

		// Yahoo dom event
		Resource yahooDomEvent = new Resource(name: "${yuiResourcePath}/yahoo-dom-event/yahoo-dom-event.js")
		yahooDomEvent.builder.script(type: "text/javascript", src: "${yuiResourcePath}/yahoo-dom-event/yahoo-dom-event.js", "")
		resources << yahooDomEvent

		// Yahoo element min
		Resource yahooElementMin = new Resource(name: "${yuiResourcePath}/element/element-min.js")
		yahooElementMin.builder.script(type: "text/javascript", src: "${yuiResourcePath}/element/element-min.js", "")
		resources << yahooElementMin

		// Yahoo connection min
		Resource connectionMin = new Resource(name: "${yuiResourcePath}/connection/connection-min.js")
		connectionMin.builder.script(type: "text/javascript", src: "${yuiResourcePath}/connection/connection-min.js", "")
		resources << connectionMin

		// Yahoo tab view min
		Resource yahooTabViewMin = new Resource(name: "${yuiResourcePath}/tabview/tabview-min.js")
		yahooTabViewMin.builder.script(type: "text/javascript", src: "${yuiResourcePath}/tabview/tabview-min.js", "")
		resources << yahooTabViewMin

		return resources
	}

	protected void renderResourcesContent(Map attrs, MarkupBuilder builder, String resourcePath) throws RenderException {
		builder.yield("<!-- TabView -->", false)

		String yuiResourcePath = getResourcePath(resourcePath, attrs.remote != null)

		if (attrs.skin) {
			if (attrs.skin == "default") {
				builder.link(rel: "stylesheet", type: "text/css", href: "$yuiResourcePath/fonts/fonts-min.css")
				builder.link(rel: "stylesheet", type: "text/css", href: "$yuiResourcePath/tabview/assets/tabview-core.css")
				builder.link(rel: "stylesheet", type: "text/css", href: "$resourcePath/css/tabView.css")
			}
			else {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
			}
		}
		else {
			builder.link(rel: "stylesheet", type: "text/css", href: "$yuiResourcePath/tabview/assets/skins/sam/tabview.css")
		}

		builder.script(type: "text/javascript", src: "$yuiResourcePath/yahoo-dom-event/yahoo-dom-event.js", "")
		builder.script(type: "text/javascript", src: "$yuiResourcePath/element/element-min.js", "")
		builder.script(type: "text/javascript", src: "$yuiResourcePath/connection/connection-min.js", "")
		builder.script(type: "text/javascript", src: "$yuiResourcePath/tabview/tabview-min.js", "")
	}
}
