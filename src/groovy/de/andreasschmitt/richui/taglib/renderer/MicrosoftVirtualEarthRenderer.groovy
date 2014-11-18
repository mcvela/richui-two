/**
 * 
 */
package de.andreasschmitt.richui.taglib.renderer

import groovy.xml.MarkupBuilder
import de.andreasschmitt.richui.taglib.Resource

/**
 * @author andreas
 *
 */
public class MicrosoftVirtualEarthRenderer extends AbstractRenderer {

	protected void renderTagContent(Map attrs, MarkupBuilder builder) throws RenderException {
		renderTagContent(attrs, null, builder)
	}
	
	protected void renderTagContent(Map attrs, Closure body, MarkupBuilder builder) throws RenderException {
		String id = "m" + RenderUtils.getUniqueId()
		
		if(!attrs?.id){
			attrs.id = id
		}
		
		if(!attrs?.'class'){
			attrs.'class' = ""
		}
		
		if(!attrs?.style){
			attrs.style = ""
		}
		
		if(!attrs?.mapIntegrationVar){
			attrs.mapIntegrationVar = "map" + RenderUtils.getUniqueId()
		}
		
		if(!attrs?.zoomLevel){
			attrs.zoomLevel = 5
		}
		
		//Default HTML attributes
		Map htmlAttributes = [id: attrs?.id, "class": "${attrs?.mapStyleClass}", style: "${attrs?.mapStyle}"]

		//Add additional attributes
		attrs.each { key, value ->
			if(key.startsWith("html:")){
				htmlAttributes[key.replace("html:", "")] = value
			}
		}		
		
		builder.div(htmlAttributes)			
		builder.script(type: "text/javascript"){			
			builder.yield("	var ${attrs.mapIntegrationVar} = new VEMap('${attrs.id}');\n", false)

			// Control size
			if(attrs?.small){
				builder.yield("	${attrs.mapIntegrationVar}.SetDashboardSize(VEDashboardSize.Small);", false)
			}
			else if(attrs?.tiny){
				builder.yield("	${attrs.mapIntegrationVar}.SetDashboardSize(VEDashboardSize.Tiny);", false)
			}
			else {
				builder.yield("	${attrs.mapIntegrationVar}.SetDashboardSize(VEDashboardSize.Normal);", false)	
			}
			
			builder.yield("	 function addMarker(latitude, longitude, draggable, description){\n", false)   
			builder.yield("	 	var marker = new VEShape(VEShapeType.Pushpin, new VELatLong(latitude, longitude));\n", false)
			builder.yield("	    marker.SetDescription(description);\n", false)
			builder.yield("	    ${attrs.mapIntegrationVar}.AddShape(marker);\n", false)  
			builder.yield("	 }\n", false) 

			if("${attrs?.lat}" == "" && "${attrs?.lng}" == ""){
				if(attrs?.markers?.size() > 0){
					Map marker = attrs.markers[0]
					String latitude = marker.latitude
					String longitude = marker.longitude
					builder.yield("	${attrs.mapIntegrationVar}.LoadMap(new VELatLong(${latitude}, ${longitude}), ${attrs.zoomLevel} ,'h' ,false);\n", false)
					builder.yield("	addMarker(${latitude}, ${latitude}, false, '');\n", false)
				}
				else {
					builder.yield("	${attrs.mapIntegrationVar}.LoadMap();\n", false)
				}
			}
			else {
				builder.yield("	${attrs.mapIntegrationVar}.LoadMap(new VELatLong(${attrs.lat}, ${attrs.lng}), ${attrs.zoomLevel} ,'h' ,false);\n", false)
				builder.yield("	addMarker(${attrs.lat}, ${attrs.lng}, false, '');\n", false)
			}
			
			//Markers
			if(attrs?.markers){
				attrs.markers.each {
					try {
						builder.yield("	addMarker(${it.latitude}, ${it.longitude}, ${it.draggable}, '${it.description}');\n", false)
					}
					catch(exception) {
						println exception
					}
				}	
			}
			
			builder.yield("	${attrs.mapIntegrationVar}.SetMapStyle(VEMapStyle.Road);\n", false)
		}
	}
	
	protected List<Resource> getComponentResources(Map attrs, String resourcePath) throws RenderException {
		List<Resource> resources = []

		if(!attrs?.version){
			attrs.version = "6.2"
		}
		
		// CSS 
		Resource css = new Resource()
		
		def cssBuilder = css.getBuilder()		
		if(attrs?.skin){
			if(attrs.skin == "default"){
				
			}
			else {
				String applicationResourcePath = RenderUtils.getApplicationResourcePath(resourcePath)
				cssBuilder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
				css.name = "${applicationResourcePath}/css/${attrs.skin}.css"
			}
		}
		else {
				
		}
		resources.add(css)
		
		// Java script 
		Resource javaScript = new Resource()
		javaScript.name = "http://dev.virtualearth.net/mapcontrol/mapcontrol.ashx?v=${attrs.version}"
		
		def javaScriptBuilder = javaScript.getBuilder()
		javaScriptBuilder.script(type: "text/javascript", src: "http://dev.virtualearth.net/mapcontrol/mapcontrol.ashx?v=${attrs.version}", "")
		
		resources.add(javaScript)
		
		return resources
	}
	
	protected void renderResourcesContent(Map attrs, MarkupBuilder builder, String resourcePath) throws RenderException {			
		builder.yield("<!-- Virtual Earth -->", false)
		
		if(!attrs?.version){
			attrs.version = "6.2"
		}
		
		if(attrs?.skin){
			if(attrs.skin == "default"){
				//builder.link(rel: "stylesheet", type: "text/css", href: "$resourcePath/css/virtualearth.css")
			}
			else {
				String applicationResourcePath = RenderUtils.getApplicationResourcePath(resourcePath)
				builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
			}
		}
		else {
			//builder.link(rel: "stylesheet", type: "text/css", href: "$resourcePath/css/virtualearth.css")	
		}
		
		builder.script(type: "text/javascript", src: "http://dev.virtualearth.net/mapcontrol/mapcontrol.ashx?v=${attrs.version}", "")
	}
	
}
