package de.andreasschmitt.richui.taglib.renderer

import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import groovy.xml.MarkupBuilder

import java.util.regex.Matcher
import java.util.regex.Pattern

import org.apache.commons.codec.digest.DigestUtils
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import de.andreasschmitt.richui.taglib.Resource

/**
 * @author Andreas Schmitt
 */
@CompileStatic
abstract class AbstractRenderer implements Renderer {

	private static final String YUI_PATH = "http://yui.yahooapis.com"
	private static final String LATEST_VERSION = "2.8.0"

	protected Logger log = LoggerFactory.getLogger(getClass())

	GrailsApplication grailsApplication

	String renderResources(Map attrs, String contextPath) throws RenderException {
		renderWithMarkupBuilder(attrs) { MarkupBuilder builder ->
			String resourcePath = getResourcePath("Richui", contextPath)
			renderResourcesContent attrs ?: [:], builder, resourcePath
		}
	}

	List<Resource> getResources(Map attrs, String contextPath) throws RenderException {
		try {
			String resourcePath = getResourcePath("Richui", contextPath)
			getComponentResources attrs ?: [:], resourcePath
		}
		catch (e) {
			throw new RenderException("Error rendering resources with attrs: $attrs", e)
		}
	}

	String renderTag(Map attrs) throws RenderException {
		renderWithMarkupBuilder(attrs) { MarkupBuilder builder ->
			renderTagContent attrs ?: [:], builder
		}
	}

	String renderTag(Map attrs, Closure body) throws RenderException {
		renderWithMarkupBuilder(attrs) { MarkupBuilder builder ->
			renderTagContent attrs ?: [:], body, builder
		}
	}

	protected String renderWithMarkupBuilder(Map attrs, Closure c) throws RenderException {
		try {
			StringWriter writer = new StringWriter()
			def builder = new MarkupBuilder(writer)

			c(builder)

			writer.flush()
			writer
		}
		catch (e) {
			throw new RenderException("Error rendering with attrs: $attrs", e)
		}
	}

	protected abstract void renderTagContent(Map attrs, MarkupBuilder builder) throws RenderException
	protected abstract void renderTagContent(Map attrs, Closure body, MarkupBuilder builder) throws RenderException
	protected abstract void renderResourcesContent(Map attrs, MarkupBuilder builder, String resourcePath) throws RenderException
	protected abstract List<Resource> getComponentResources(Map attrs, String resourcePath) throws RenderException

	// YUIUtils
	@CompileStatic(TypeCheckingMode.SKIP)
	protected String getResourcePath(String version = LATEST_VERSION, String resourcePath, boolean remote) {
		if (remote || grailsApplication.config?.richui?.serve?.resource?.files?.remote) {
			return getYuiResourcePath(version)
		}
		return resourcePath + "/js/yui"
	}

	private String getYuiResourcePath() {
		getYuiResourcePath(LATEST_VERSION)
	}

	private String getYuiResourcePath(String version) {
		return "${YUI_PATH}/${version}/build"
	}

	// RenderUtils

	protected String getUniqueId() {
		DigestUtils.md5Hex UUID.randomUUID().toString()
	}

	protected String getResourcePath(String pluginName, String contextPath) {
		//TODO find a more efficient way to retrieve plugin version getPlugin(name).version did not work
		//def plugin = Holders?.pluginManager?.allPlugins.find { it.name == pluginName.toLowerCase() }
		//String pluginVersion = plugin?.version

		//The above version doesn't work on Jetty so for now an ugly approach will be used
		String pluginVersion = "0.8"

		"${contextPath}/plugins/${pluginName.toLowerCase()}-$pluginVersion"
	}

	protected String getApplicationResourcePath(String pluginResourcePath) {
		try {
			Pattern pattern = Pattern.compile("(.*)/plugins.*")
			Matcher matcher = pattern.matcher(pluginResourcePath)
			if (matcher.matches()) {
				return matcher.group(1)
			}
		}
		catch (e) {
			return ""
		}
	}
}
