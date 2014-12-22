package de.andreasschmitt.richui.taglib.renderer

import groovy.xml.MarkupBuilder
import de.andreasschmitt.richui.taglib.Resource

/**
 * @author andreas
 */
class YahooMapsRenderer extends AbstractRenderer {

	protected void renderTagContent(Map attrs, MarkupBuilder builder) throws RenderException {
		renderTagContent(attrs, null, builder)
	}

	protected void renderTagContent(Map attrs, Closure body, MarkupBuilder builder) throws RenderException {
		String id = "m" + getUniqueId()

		if (!attrs.id) {
			attrs.id = id
		}

		if (!attrs.'class') {
			attrs.'class' = ""
		}

		if (!attrs.style) {
			attrs.style = ""
		}

		if (!attrs.mapIntegrationVar) {
			attrs.mapIntegrationVar = "map" + getUniqueId()
		}

		if (!attrs.zoomLevel) {
			attrs.zoomLevel = 5
		}

		//Default HTML attributes
		Map htmlAttributes = [id: attrs.id, "class": "${attrs.mapStyleClass}", style: "${attrs.mapStyle}"]

		//Add additional attributes
		attrs.each { key, value ->
			if (key.startsWith("html:")) {
				htmlAttributes[key.replace("html:", "")] = value
			}
		}

		builder.div(htmlAttributes)
		builder.script(type: "text/javascript") {
			builder.yield("	var ${attrs.mapIntegrationVar} = new YMap(document.getElementById('${attrs.id}'));\n", false)
			builder.yield("	${attrs.mapIntegrationVar}.addTypeControl();\n", false)
			builder.yield("	${attrs.mapIntegrationVar}.addZoomLong();\n", false)
			builder.yield("	${attrs.mapIntegrationVar}.addPanControl();\n", false)
			//YAHOO_MAP_SAT, YAHOO_MAP_HYB, YAHOO_MAP_REG
			builder.yield("	${attrs.mapIntegrationVar}.setMapType(YAHOO_MAP_REG);\n", false)

			builder.yield("	 function addMarker(latitude, longitude, draggable, description) {\n", false)
			builder.yield("	 	var pos = new YGeoPoint(latitude, longitude);\n", false)
			builder.yield("	 	var marker = new YMarker(pos);\n", false)
			builder.yield("	 	marker.addAutoExpand(description);\n", false)
			builder.yield("	 	YEvent.Capture(marker, EventsList.MouseClick,\n", false)
			builder.yield("	 	         function() {\n", false)
			builder.yield("	 	              marker.openSmartWindow(description);\n", false)
			builder.yield("	 	         });\n", false)
			builder.yield("	 	${attrs.mapIntegrationVar}.addOverlay(marker);\n", false)
			builder.yield("	 }\n", false)

			//Markers
			if (attrs.markers) {
				attrs.markers.each {
					try {
						builder.yield("	addMarker(${it.latitude}, ${it.longitude}, ${it.draggable}, '${it.description}');\n", false)
					}
					catch (e) {
						log.error e.message, e
					}
				}
			}

			if ("${attrs.lat}" == "" && "${attrs.lng}" == "") {
				if (attrs.markers?.size() > 0) {
					Map marker = attrs.markers[0]
					String latitude = marker.latitude
					String longitude = marker.longitude
					builder.yield("	var startPos = new YGeoPoint(${latitude}, ${longitude});\n", false)
					builder.yield("	${attrs.mapIntegrationVar}.drawZoomAndCenter(startPos, ${attrs.zoomLevel});\n", false)
					builder.yield("	${attrs.mapIntegrationVar}.addMarker(startPos);\n", false)
				}
			}
			else {
				builder.yield("	var startPos = new YGeoPoint(${attrs.lat}, ${attrs.lng});\n", false)
				builder.yield("	${attrs.mapIntegrationVar}.drawZoomAndCenter(startPos, ${attrs.zoomLevel});\n", false)
				builder.yield("	${attrs.mapIntegrationVar}.addMarker(startPos);\n", false)
			}
		}
	}

	protected List<Resource> getComponentResources(Map attrs, String resourcePath) throws RenderException {
		List<Resource> resources = []

		if (!attrs.key) {
			throw new RenderException("Attribute 'key' is required")
		}

		if (!attrs.version) {
			attrs.version = "3.8"
		}

		// CSS
		Resource css = new Resource()
		if (attrs.skin) {
			if (attrs.skin != "default") {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				css.builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
				css.name = "${applicationResourcePath}/css/${attrs.skin}.css"
			}
		}
		resources << css

		// Yahoo maps
		Resource yahooMaps = new Resource(name: "http://api.maps.yahoo.com/ajaxymap?v=${attrs.version}&appid=${attrs.key}")
		yahooMaps.builder.script(type: "text/javascript", src: "http://api.maps.yahoo.com/ajaxymap?v=${attrs.version}&appid=${attrs.key}", "")
		resources << yahooMaps

		return resources
	}

	protected void renderResourcesContent(Map attrs, MarkupBuilder builder, String resourcePath) throws RenderException {
		builder.yield("<!-- Yahoo Maps -->", false)

		if (!attrs.key) {
			throw new RenderException("Attribute 'key' is required")
		}

		if (!attrs.version) {
			attrs.version = "3.8"
		}

		if (attrs.skin) {
			if (attrs.skin == "default") {
				//builder.link(rel: "stylesheet", type: "text/css", href: "$resourcePath/css/yahoomaps.css")
			}
			else {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
			}
		}
		else {
			//builder.link(rel: "stylesheet", type: "text/css", href: "$resourcePath/css/yahoomaps.css")
		}

		builder.script(type: "text/javascript", src: "http://api.maps.yahoo.com/ajaxymap?v=${attrs.version}&appid=${attrs.key}", "")
	}
}
