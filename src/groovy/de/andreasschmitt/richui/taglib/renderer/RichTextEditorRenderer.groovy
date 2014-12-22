package de.andreasschmitt.richui.taglib.renderer

import groovy.xml.MarkupBuilder
import de.andreasschmitt.richui.taglib.Resource

/**
 * @author Andreas Schmitt
 */
class RichTextEditorRenderer extends AbstractRenderer {

	protected void renderTagContent(Map attrs, MarkupBuilder builder) throws RenderException {
		renderTagContent(attrs, null, builder)
	}

	protected void renderTagContent(Map attrs, Closure body, MarkupBuilder builder) throws RenderException {
		//Default HTML attributes
		Map htmlAttributes = [name: "${attrs.name}", id: "${attrs.id}", style: "width: ${attrs.width}px; height: ${attrs.height}px;"]

		//Add additional attributes
		attrs.each { key, value ->
			if (key.startsWith("html:")) {
				htmlAttributes[key.replace("html:", "")] = value
			}
		}

		builder.textarea(htmlAttributes, "${attrs.value}")

		builder.script(type: "text/javascript") {
			builder.yield("	tinyMCE.execCommand('mceAddControl', true, '${attrs.id}');\n", false)
		}
	}

	protected List<Resource> getComponentResources(Map attrs, String resourcePath) throws RenderException {
		List<Resource> resources = []

		// Tiny mce
		Resource tinyMce = new Resource(name: "${resourcePath}/js/tinymce/tiny_mce.js")
		tinyMce.builder.script(type: "text/javascript", src: "${resourcePath}/js/tinymce/tiny_mce.js", "")
		resources << tinyMce

		// Tiny mce
		Resource tinyMceInit = new Resource(name: "tinymceinit")
		def tinyMceInitBuilder = tinyMceInit.builder
		tinyMceInitBuilder.script(type: "text/javascript") {
			tinyMceInitBuilder.yield("tinyMCE.init({\n", false)
			tinyMceInitBuilder.yield("	mode : 'none',\n", false)

			if (!attrs.type || attrs.type == "medium") {
				tinyMceInitBuilder.yield("	theme : 'advanced',\n", false)
				tinyMceInitBuilder.yield("	theme_advanced_buttons1 : 'bold,italic,underline,separator,strikethrough,justifyleft,justifycenter,justifyright,justifyfull,bullist,numlist,undo,redo,link,unlink',\n", false)
				tinyMceInitBuilder.yield("	theme_advanced_buttons2 : '',\n", false)
				tinyMceInitBuilder.yield("	theme_advanced_buttons3 : '',\n", false)
				tinyMceInitBuilder.yield("	theme_advanced_toolbar_location : 'top',\n", false)
				tinyMceInitBuilder.yield("	theme_advanced_toolbar_align : 'left',\n", false)
				tinyMceInitBuilder.yield("	extended_valid_elements : 'a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]'\n", false)
			}
			else if (attrs.type == "simple") {
				tinyMceInitBuilder.yield("	theme : 'simple',\n", false)
				tinyMceInitBuilder.yield("	editor_selector : 'mceSimple'\n", false)
			}
			else if (attrs.type == "advanced") {
				tinyMceInitBuilder.yield("	theme : 'advanced',\n", false)
				tinyMceInitBuilder.yield("	theme_advanced_buttons1 : 'bold,italic,underline,separator,strikethrough,justifyleft,justifycenter,justifyright,justifyfull,|,styleselect,formatselect,fontselect,fontsizeselect,|,bullist,numlist,undo,redo,link,unlink',\n", false)
				tinyMceInitBuilder.yield("	theme_advanced_buttons2 : '',\n", false)
				tinyMceInitBuilder.yield("	theme_advanced_buttons3 : '',\n", false)
				tinyMceInitBuilder.yield("	theme_advanced_toolbar_location : 'top',\n", false)
				tinyMceInitBuilder.yield("	theme_advanced_toolbar_align : 'left',\n", false)
				tinyMceInitBuilder.yield("	extended_valid_elements : 'a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]'\n", false)
			}
			else if (attrs.type == "full") {
				tinyMceInitBuilder.yield("	theme : 'advanced',\n", false)
				tinyMceInitBuilder.yield("	plugins : 'safari,pagebreak,style,layer,table,save,advhr,advimage,advlink,emotions,iespell,inlinepopups,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template',\n", false)

				tinyMceInitBuilder.yield("	theme_advanced_buttons1 : 'save,newdocument,|,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,|,styleselect,formatselect,fontselect,fontsizeselect',\n", false)
				tinyMceInitBuilder.yield("	theme_advanced_buttons2 : 'cut,copy,paste,pastetext,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,anchor,image,cleanup,help,code,|,insertdate,inserttime,preview,|,forecolor,backcolor',\n", false)
				tinyMceInitBuilder.yield("	theme_advanced_buttons3 : 'tablecontrols,|,hr,removeformat,visualaid,|,sub,sup,|,charmap,emotions,iespell,media,advhr,|,print,|,ltr,rtl,|,fullscreen',\n", false)
				tinyMceInitBuilder.yield("	theme_advanced_buttons4 : 'insertlayer,moveforward,movebackward,absolute,|,styleprops,|,cite,abbr,acronym,del,ins,attribs,|,visualchars,nonbreaking,template,pagebreak',\n", false)
				tinyMceInitBuilder.yield("	theme_advanced_toolbar_location : 'top',\n", false)
				tinyMceInitBuilder.yield("	theme_advanced_toolbar_align : 'left',\n", false)
				tinyMceInitBuilder.yield("	theme_advanced_resizing : true\n", false)
			}
			else {
				tinyMceInitBuilder.yield("	theme : 'advanced',\n", false)
			}

			tinyMceInitBuilder.yield("});\n", false)
		}

		resources << tinyMceInit

		// CSS
		Resource css = new Resource()
		css.builder.script(type: "text/javascript", src: "${resourcePath}/js/tinymce/tiny_mce.js", "")

		if (attrs.skin) {
			if (attrs.skin != "default") {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				css.builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
				css.name = "${applicationResourcePath}/css/${attrs.skin}.css"
			}
		}

		resources << css

		return resources
	}

	protected void renderResourcesContent(Map attrs, MarkupBuilder builder, String resourcePath) throws RenderException {
		builder.yield("<!-- RichTextEditor -->", false)

		builder.script(type: "text/javascript", src: "$resourcePath/js/tinymce/tiny_mce.js", "")
		builder.script(type: "text/javascript") {
			builder.yield("tinyMCE.init({\n", false)
			builder.yield("	mode : 'none',\n", false)

			if (!attrs.type ||attrs.type == "medium") {
				builder.yield("	theme : 'advanced',\n", false)
				builder.yield("	theme_advanced_buttons1 : 'bold,italic,underline,separator,strikethrough,justifyleft,justifycenter,justifyright,justifyfull,bullist,numlist,undo,redo,link,unlink',\n", false)
				builder.yield("	theme_advanced_buttons2 : '',\n", false)
				builder.yield("	theme_advanced_buttons3 : '',\n", false)
				builder.yield("	theme_advanced_toolbar_location : 'top',\n", false)
				builder.yield("	theme_advanced_toolbar_align : 'left',\n", false)
				builder.yield("	extended_valid_elements : 'a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]'\n", false)
			}
			else if (attrs.type == "simple") {
				builder.yield("	theme : 'simple',\n", false)
				builder.yield("	editor_selector : 'mceSimple'\n", false)
			}
			else if (attrs.type == "advanced") {
				builder.yield("	theme : 'advanced',\n", false)
				builder.yield("	theme_advanced_buttons1 : 'bold,italic,underline,separator,strikethrough,justifyleft,justifycenter,justifyright,justifyfull,|,styleselect,formatselect,fontselect,fontsizeselect,|,bullist,numlist,undo,redo,link,unlink',\n", false)
				builder.yield("	theme_advanced_buttons2 : '',\n", false)
				builder.yield("	theme_advanced_buttons3 : '',\n", false)
				builder.yield("	theme_advanced_toolbar_location : 'top',\n", false)
				builder.yield("	theme_advanced_toolbar_align : 'left',\n", false)
				builder.yield("	extended_valid_elements : 'a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]'\n", false)
			}
			else if (attrs.type == "full") {
				builder.yield("	theme : 'advanced',\n", false)
				builder.yield("	plugins : 'safari,pagebreak,style,layer,table,save,advhr,advimage,advlink,emotions,iespell,inlinepopups,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template',\n", false)

				builder.yield("	theme_advanced_buttons1 : 'save,newdocument,|,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,|,styleselect,formatselect,fontselect,fontsizeselect',\n", false)
				builder.yield("	theme_advanced_buttons2 : 'cut,copy,paste,pastetext,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,anchor,image,cleanup,help,code,|,insertdate,inserttime,preview,|,forecolor,backcolor',\n", false)
				builder.yield("	theme_advanced_buttons3 : 'tablecontrols,|,hr,removeformat,visualaid,|,sub,sup,|,charmap,emotions,iespell,media,advhr,|,print,|,ltr,rtl,|,fullscreen',\n", false)
				builder.yield("	theme_advanced_buttons4 : 'insertlayer,moveforward,movebackward,absolute,|,styleprops,|,cite,abbr,acronym,del,ins,attribs,|,visualchars,nonbreaking,template,pagebreak',\n", false)
				builder.yield("	theme_advanced_toolbar_location : 'top',\n", false)
				builder.yield("	theme_advanced_toolbar_align : 'left',\n", false)
				builder.yield("	theme_advanced_resizing : true\n", false)
			}
			else {
				builder.yield("	theme : 'advanced',\n", false)
			}

			builder.yield("});\n", false)
		}

		if (attrs.skin) {
			if (attrs.skin != "default") {
				String applicationResourcePath = getApplicationResourcePath(resourcePath)
				builder.link(rel: "stylesheet", type: "text/css", href: "${applicationResourcePath}/css/${attrs.skin}.css")
			}
		}
	}
}
