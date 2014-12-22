import org.slf4j.Logger
import org.slf4j.LoggerFactory

class RichuiGrailsPlugin {

	Logger log = LoggerFactory.getLogger(getClass())

	def version = '0.8'
	def grailsVersion = "2.3 > *"
	def title = 'RichUI Plugin'
	def description = 'Provides a set of AJAX components'
	def documentation = 'http://grails.org/plugin/richui'
	def license = 'APACHE'
	def developers = [
		[name: 'Miguel Angel Alacio', email: 'mcvela@hotmail.com'],
		[name: 'Andreas Schmitt']
	]
	def issueManagement = [system: 'GITHUB', url: 'https://github.com/mcvela/richui-two/issues']
	def scm = [url: 'https://github.com/mcvela/richui-two']

	def doWithSpring = {
		try {
			RichuiConfig.renderers.each { key, value ->
				try {
					//Override default renderer configuration
					if (application.config.richui."$key") {
						value = application.config.richui."$key"
					}

					Class clazz = Class.forName(value, true, Thread.currentThread().contextClassLoader)

					//Add to spring
					"$key"(clazz) { bean ->
						bean.autowire = 'byName'
					}
				}
				catch (ClassNotFoundException e) {
					log.error("Couldn't find class: $value", e)
				}
			}
		}
		catch (e) {
			log.error("Error initializing RichUI plugin", e)
		}
		catch (Error e) {
			//Strange error which happens when using generate-all and hibernate.cfg
			log.error("Error initializing RichUI plugin")
		}
	}
}
