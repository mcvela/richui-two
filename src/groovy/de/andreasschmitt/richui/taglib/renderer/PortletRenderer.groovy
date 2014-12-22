package de.andreasschmitt.richui.taglib.renderer

import groovy.xml.MarkupBuilder
import de.andreasschmitt.richui.taglib.Resource

/**
 * @author Andreas Schmitt
 */
class PortletRenderer extends AbstractRenderer {

	protected void renderTagContent(Map attrs, MarkupBuilder builder) throws RenderException {
		renderTagContent(attrs, null, builder)
	}

	protected void renderTagContent(Map attrs, Closure body, MarkupBuilder builder) throws RenderException {

		builder."div"() {
			builder.yield("${body.call()}", false)
		}

		if (!attrs.readOnly || attrs.readOnly == "false") {
			builder.script(type: "text/javascript") {
				builder.yield("	var slots = [], players = [],\n", false)
				builder.yield("	YUIEvent = YAHOO.util.Event, DDM = YAHOO.util.DDM;\n", false)

				builder.yield("	YUIEvent.onDOMReady(function() {\n", false)
				//	slots
				attrs.views.each { view ->
					builder.yield("		slots[${view}] = new YAHOO.util.DDTarget('slot_${attrs.page}_${view}', 'bottomslots');\n", false)
					builder.yield("		players[${view}] = new YAHOO.example.DDPlayer('${view}', 'bottomslots', {action: '${attrs.action}'});\n", false)
					builder.yield("	    slots[${view}].player = players[${view}];\n", false)
					builder.yield("	    players[${view}].slot = slots[${view}];\n", false)
				}

				// players
				builder.yield("	YUIEvent.on('ddmode', 'change', function(e) {\n", false)
				builder.yield("	YAHOO.util.DDM.mode = this.selectedIndex;\n", false)
				builder.yield("	   });\n", false)
				builder.yield("   });\n", false)
			}
		}
	}

	protected List<Resource> getComponentResources(Map attrs, String resourcePath) throws RenderException {
		List<Resource> resources = []

		String yuiResourcePath = getResourcePath(resourcePath, attrs.remote != null)

		// CSS
		Resource css = new Resource()
		if (attrs.skin) {
			if (attrs.skin == "default") {
				css.builder.link(rel: "stylesheet", type: "text/css", href: "${resourcePath}/css/portlet.css")
				css.name = "${resourcePath}/css/portlet.css"
			}
			else {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				css.builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
				css.name = "${applicationResourcePath}/css/${attrs.skin}.css"
			}
		}
		else {
			css.builder.link(rel: "stylesheet", type: "text/css", href: "${resourcePath}/css/portlet.css")
			css.name = "${resourcePath}/css/portlet.css"
		}

		resources << css

		// Yahoo dom event
		Resource yahooDomEvent = new Resource(name: "${yuiResourcePath}/yahoo-dom-event/yahoo-dom-event.js")
		yahooDomEvent.builder.script(type: "text/javascript", src: "${yuiResourcePath}/yahoo-dom-event/yahoo-dom-event.js", "")
		resources << yahooDomEvent

		// Yahoo drag drop min
		Resource yahooDragDropMin = new Resource(name: "${yuiResourcePath}/dragdrop/dragdrop-min.js")
		yahooDragDropMin.builder.script(type: "text/javascript", src: "${yuiResourcePath}/dragdrop/dragdrop-min.js", "")
		resources << yahooDragDropMin

		// Yahoo min
		Resource yahooMin = new Resource(name: "${yuiResourcePath}/yahoo/yahoo-min.js")
		yahooMin.builder.script(type: "text/javascript", src: "${yuiResourcePath}/yahoo/yahoo-min.js", "")
		resources << yahooMin

		// Yahoo event min
		Resource yahooEventMin = new Resource(name: "${yuiResourcePath}/event/event-min.js")
		yahooEventMin.builder.script(type: "text/javascript", src: "${yuiResourcePath}/event/event-min.js", "")
		resources << yahooEventMin

		// Yahoo connection min
		Resource yahooConnectionMin = new Resource(name: "${yuiResourcePath}/connection/connection-min.js")
		yahooConnectionMin.builder.script(type: "text/javascript", src: "${yuiResourcePath}/connection/connection-min.js", "")
		resources << yahooConnectionMin

		// Portlet
		Resource portlet = new Resource(name: "${resourcePath}/js/portlet/portlet.js")
		portlet.builder.script(type: "text/javascript", src: "${resourcePath}/js/portlet/portlet.js", "")
		resources << portlet

		return resources
	}

	protected void renderResourcesContent(Map attrs, MarkupBuilder builder, String resourcePath) throws RenderException {
		builder.yield("<!-- Portlet -->", false)

		String yuiResourcePath = getResourcePath(resourcePath, attrs.remote != null)

		if (attrs.skin) {
			if (attrs.skin == "default") {
				builder.link(rel: "stylesheet", type: "text/css", href: "$resourcePath/css/portlet.css")
			}
			else {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
			}
		}
		else {
			builder.link(rel: "stylesheet", type: "text/css", href: "$resourcePath/css/portlet.css")
		}

		builder.script(type: "text/javascript", src: "$yuiResourcePath/yahoo-dom-event/yahoo-dom-event.js", "")
		builder.script(type: "text/javascript", src: "$yuiResourcePath/dragdrop/dragdrop-min.js", "")
		builder.script(type: "text/javascript", src: "$yuiResourcePath/yahoo/yahoo-min.js", "")
		builder.script(type: "text/javascript", src: "$yuiResourcePath/event/event-min.js", "")
		builder.script(type: "text/javascript", src: "$yuiResourcePath/connection/connection-min.js", "")
		builder.script(type: "text/javascript", src: "$resourcePath/js/portlet/portlet.js", "")
	}
}
