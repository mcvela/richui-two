package de.andreasschmitt.richui.taglib.renderer

import groovy.xml.MarkupBuilder
import de.andreasschmitt.richui.taglib.Resource

/*
*
* @author Andreas Schmitt
*/
public class LightBoxRenderer extends AbstractRenderer {

	protected void renderTagContent(Map attrs, MarkupBuilder builder) throws RenderException {
		renderTagContent(attrs, null, builder)
	}

	protected void renderTagContent(Map attrs, Closure body, MarkupBuilder builder) throws RenderException {		
		if(!attrs?.rel){
			if(attrs?.group){
				attrs.rel = "lightbox[${attrs.group}]"
			}
			else {
				attrs.rel = "lightbox"	
			}
		}
		
		builder."a"(attrs){
			builder.yield("${body.call()}", false)
		}
	}
	
	protected List<Resource> getComponentResources(Map attrs, String resourcePath) throws RenderException {
		List<Resource> resources = []
		
		// Prototype
		Resource prototype = new Resource()
		prototype.name = "${resourcePath}/js/lightbox/prototype.js"
		
		def prototypeBuilder = prototype.getBuilder()
		prototypeBuilder.script(type: "text/javascript", src: "${resourcePath}/js/lightbox/prototype.js", "")
		
		resources.add(prototype)
		
		// Scriptaculous
		Resource scriptaculous = new Resource()
		scriptaculous.name = "${resourcePath}/js/lightbox/scriptaculous.js?load=effects,builder"
		
		def scriptaculousBuilder = scriptaculous.getBuilder()
		scriptaculousBuilder.script(type: "text/javascript", src: "${resourcePath}/js/lightbox/scriptaculous.js?load=effects,builder", "")
		
		resources.add(scriptaculous)
		
		// Lightbox configuration
		Resource lightBoxConfig = new Resource()
		lightBoxConfig.name = "lightbox.config"
		
		Map configAttributes = [overlayOpacity: "0.8", animate: "true", resizeSpeed: "7",
			                        borderSize: "10", labelImage: "Image", labelOf: "of"]
			
		// Allow attribute overriding
		attrs.each { key, value ->
			if(configAttributes.containsKey(key)){
				configAttributes[key] = value
			}
		}
		
		def lightBoxConfigBuilder = scriptaculous.getBuilder()
		lightBoxConfigBuilder.script(type: "text/javascript"){
			lightBoxConfigbuilder.yield(" LightboxOptions = Object.extend({\n", false)
			lightBoxConfigbuilder.yield(" fileLoadingImage:        '${resourcePath}/js/lightbox/images/loading.gif',\n", false)     
			lightBoxConfigbuilder.yield(" fileBottomNavCloseImage: '${resourcePath}/js/lightbox/images/closelabel.gif',\n", false)
			lightBoxConfigbuilder.yield(" overlayOpacity: ${configAttributes.overlayOpacity},\n", false)
			lightBoxConfigbuilder.yield(" animate: ${configAttributes.animate},\n", false)
			lightBoxConfigbuilder.yield(" resizeSpeed: ${configAttributes.resizeSpeed},\n", false)
			lightBoxConfigbuilder.yield(" borderSize: ${configAttributes.borderSize},\n", false)
			lightBoxConfigbuilder.yield(" labelImage: '${configAttributes.labelImage}',\n", false)
			lightBoxConfigbuilder.yield(" labelOf: '${configAttributes.labelOf}'\n", false)
			lightBoxConfigbuilder.yield(" }, window.LightboxOptions || {});\n", false)
		}
		
		resources.add(lightBoxConfig)		
		
		// Lightbox JavaScript
		Resource lightBoxJavaScript = new Resource()
		lightBoxJavaScript.name = "${resourcePath}/js/lightbox/lightbox.js"
		
		def lightBoxJavaScriptBuilder = lightBoxJavaScript.getBuilder()
		lightBoxJavaScriptBuilder.script(type: "text/javascript", src: "${resourcePath}/js/lightbox/lightbox.js", "")
		
		resources.add(lightBoxJavaScript)		
		
		// Lightbox CSS
		Resource lightBoxCss = new Resource()
		lightBoxCss.name = "${resourcePath}/css/lightbox.css"
		
		def lightBoxCssBuilder = lightBoxCss.getBuilder()
		lightBoxCssBuilder.link(rel: "stylesheet", type: "text/css", href: "${resourcePath}/css/lightbox.css")
		
		resources.add(lightBoxCss)
		
		return resources
	}

	protected void renderResourcesContent(Map attrs, MarkupBuilder builder, String resourcePath) throws RenderException {			
		builder.yield("<!-- LightBox -->", false)
			

		builder.script(type: "text/javascript", src: "${resourcePath}/js/lightbox/prototype.js", "")
		builder.script(type: "text/javascript", src: "${resourcePath}/js/lightbox/scriptaculous.js?load=effects,builder", "")
		
		Map configAttributes = [overlayOpacity: "0.8", animate: "true", resizeSpeed: "7",
		                        borderSize: "10", labelImage: "Image", labelOf: "of"]
		
		// Allow attribute overriding
		attrs.each { key, value ->
			if(configAttributes.containsKey(key)){
				configAttributes[key] = value
			}
		}
		
		builder.script(type: "text/javascript"){
			builder.yield(" LightboxOptions = Object.extend({\n", false)
			builder.yield(" fileLoadingImage:        '${resourcePath}/js/lightbox/images/loading.gif',\n", false)     
			builder.yield(" fileBottomNavCloseImage: '${resourcePath}/js/lightbox/images/closelabel.gif',\n", false)
			builder.yield(" overlayOpacity: ${configAttributes.overlayOpacity},\n", false)
			builder.yield(" animate: ${configAttributes.animate},\n", false)
			builder.yield(" resizeSpeed: ${configAttributes.resizeSpeed},\n", false)
			builder.yield(" borderSize: ${configAttributes.borderSize},\n", false)
			builder.yield(" labelImage: '${configAttributes.labelImage}',\n", false)
			builder.yield(" labelOf: '${configAttributes.labelOf}'\n", false)
			builder.yield(" }, window.LightboxOptions || {});\n", false)
		}
		
		builder.script(type: "text/javascript", src: "${resourcePath}/js/lightbox/lightbox.js", "")
		builder.link(rel: "stylesheet", type: "text/css", href: "${resourcePath}/css/lightbox.css")
	}
	
}
