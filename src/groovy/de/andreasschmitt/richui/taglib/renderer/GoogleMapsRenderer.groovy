package de.andreasschmitt.richui.taglib.renderer

import groovy.xml.MarkupBuilder
import de.andreasschmitt.richui.taglib.Resource

/**
 * @author Andreas Schmitt
 */
class GoogleMapsRenderer extends AbstractRenderer {

	protected void renderTagContent(Map attrs, MarkupBuilder builder) throws RenderException {
		renderTagContent(attrs, null, builder)
	}

	protected void renderTagContent(Map attrs, Closure body, MarkupBuilder builder) throws RenderException {
		String mapId = "map" + getUniqueId()

		if (attrs.mapId) {
			mapId = attrs.mapId
		}

		String dirId = "d" + getUniqueId()

		builder.script(type: "text/javascript") {
			builder.yield("//<![CDATA[\n", false)
			builder.yield("var $attrs.mapIntegrationVar;\n", false)

			if (!(attrs.immediate && attrs.immediate == "true")) {
				builder.yield("addEvent(window, \"load\", \n", false)
				builder.yield("function () {\n", false)
			}

			builder.yield("	" + attrs.mapIntegrationVar + " = new GoogleMapIntegration();\n", false)
			builder.yield("	" + attrs.mapIntegrationVar + ".setDirectionsId(\"${dirId}\");\n", false)
			builder.yield("	" + attrs.mapIntegrationVar + ".setDraggable($attrs.draggable);\n", false)
			builder.yield("	" + attrs.mapIntegrationVar + ".setLatitudeId(\"${attrs.latId}\");\n", false)
			builder.yield("	" + attrs.mapIntegrationVar + ".setLongitudeId(\"${attrs.lngId}\");\n", false)
			builder.yield("	" + attrs.mapIntegrationVar + ".setMapId(\"$mapId\");\n", false)
			builder.yield("	" + attrs.mapIntegrationVar + ".setZoomLevel(${attrs.zoomLevel});\n", false)
			builder.yield("	" + attrs.mapIntegrationVar + ".setShowStartMarker(${attrs.showStartMarker});\n", false)
			if (attrs.small && attrs.small == "true") {
				builder.yield("	" + attrs.mapIntegrationVar + ".setSmallMap(${attrs.small});\n", false)
			}

			//Markers
			if (attrs.markers) {
				attrs.markers.each {
					try {
						builder.yield("	" + attrs.mapIntegrationVar + ".addMarker(new GLatLng($it.latitude, $it.longitude), $it.draggable, '$it.description');\n", false)
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
					builder.yield("	" + attrs.mapIntegrationVar + ".load(new GLatLng(${latitude}, ${longitude}));\n", false)
				}
				else {
					builder.yield("	" + attrs.mapIntegrationVar + ".load();\n", false)
				}
			}
			else {
				builder.yield("	" + attrs.mapIntegrationVar + ".load(new GLatLng($attrs.lat, $attrs.lng));\n", false)
			}

			if (!(attrs.immediate && attrs.immediate == "true")) {
				builder.yield("}\n", false)
				builder.yield(", false);\n", false)
			}

			builder.yield("window.onunload = function () {\n", false)
			builder.yield("	GUnload();\n", false)
			builder.yield("}\n", false)
			builder.yield("//]]>\n", false)
		}

		builder.div("class": attrs.'class', style: attrs.style) {
		    if (attrs.search && attrs.search == "true") {
				search(builder, attrs, attrs.mapIntegrationVar)
		    }

			//Map div
			div(id:"$mapId", style: "$attrs.mapStyle", "class": "$attrs.mapStyleClass", "")

			if (attrs.route && attrs.route == "true") {
				calculateRoute(builder, attrs, attrs.mapIntegrationVar)
				drivingDirections(builder, attrs, dirId)
			}
		}
	}

	private void drivingDirections(builder, attrs, dirId) {
		builder."div"(id: "$dirId", style: "${attrs.dirStyle}", "class": "${attrs.dirClass}", "")
	}

	private void calculateRoute(builder, attrs, mapIntegrationVar) {
		builder.p {
			form {
				label("for": "start", "${attrs.routeStart}:")
				input(id: "${mapIntegrationVar}start", type: "text", name: "start")
				label("for": "destination","${attrs.routeDestination}:")
				input(id: "${mapIntegrationVar}destination", type: "text", name: "destination")
				input(type: "button", name: "route", value: "${attrs.routeOk}", onclick: "javascript: ${mapIntegrationVar}.setDirections(document.getElementById('${mapIntegrationVar}start').value, document.getElementById('${mapIntegrationVar}destination').value, '${attrs.directionsLocale}');")
				input(type: "button", name: "route_clear", value: "${attrs.routeClear}", onclick: "javascript: ${mapIntegrationVar}.clearDirections(); javascript: document.getElementById('${mapIntegrationVar}start').value = ''; javascript: document.getElementById('${mapIntegrationVar}destination').value = '';")
			}
		}
	}

	private void search(builder, attrs, mapIntegrationVar) {
		builder.p {
			form {
				label("for": "${mapIntegrationVar}search", "${attrs.searchSearch}:")
				input(id: "${mapIntegrationVar}search", type: "text", name: "search", style: "")
				input(type: "button", name: "searchbutton", onclick: "javascript: ${mapIntegrationVar}.showAddress(document.getElementById('${mapIntegrationVar}search').value);", value: "${attrs.searchOk}")
			}
		}
	}

	protected List<Resource> getComponentResources(Map attrs, String resourcePath) throws RenderException {
		List<Resource> resources = []

		if (!attrs.key) {
			throw new RenderException("Attribute 'key' is required")
		}

		if (!attrs.version) {
			attrs.version = "2.x"
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

		// Java script
		Resource javaScript = new Resource(name: "http://maps.google.com/maps?file=api&amp;v=${attrs.version}&key=${attrs.key}")
		javaScript.builder.script(type: "text/javascript", src:"http://maps.google.com/maps?file=api&amp;v=${attrs.version}&key=${attrs.key}", "")
		resources << javaScript

		// Gmap
		Resource gmap = new Resource(name: "${resourcePath}/js/googlemaps/gmap.js")
		gmap.builder.script(type: "text/javascript", src:"${resourcePath}/js/googlemaps/gmap.js", "")
		resources << gmap

		// Util
		Resource util = new Resource(name: "${resourcePath}/js/util/util.js")
		util.builder.script(type: "text/javascript", src:"${resourcePath}/js/util/util.js", "")
		resources << util

		return resources
	}

	protected void renderResourcesContent(Map attrs, MarkupBuilder builder, String resourcePath) throws RenderException {
		if (!attrs.key) {
			throw new RenderException("Attribute 'key' is required")
		}

		if (!attrs.version) {
			attrs.version = "2.x"
		}

		if (attrs.skin) {
			if (attrs.skin != "default") {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
			}
		}

		builder.script(type: "text/javascript", src:"http://maps.google.com/maps?file=api&amp;v=${attrs.version}&key=${attrs.key}", "")
		builder.script(type: "text/javascript", src:"$resourcePath/js/googlemaps/gmap.js", "")
		builder.script(type: "text/javascript", src:"$resourcePath/js/util/util.js", "")
	}
}
