import groovy.transform.CompileStatic

@CompileStatic
class RichuiConfig {

	static Map classes = [
		feedRenderer:                  'FeedRenderer',
		fontRenderer:                  'FontRenderer',
		tagCloudRenderer:              'TagCloudRenderer',
		autoCompleteRenderer:          'AutoCompleteRenderer',
		timelineRenderer:              'TimelineRenderer',
		mapRenderer:                   'GoogleMapsRenderer',
		googleMapsRenderer:            'GoogleMapsRenderer',
		yahooMapsRenderer:             'YahooMapsRenderer',
		microsoftVirtualEarthRenderer: 'MicrosoftVirtualEarthRenderer',
		ratingRenderer:                'RatingRenderer',
		tooltipRenderer:               'TooltipRenderer',
		dateChooserRenderer:           'DateChooserRenderer',
		treeViewRenderer:              'TreeViewRenderer',
		checkedTreeViewRenderer:       'CheckedTreeViewRenderer',
		tabViewRenderer:               'TabViewRenderer',
		tabLabelsRenderer:             'TabLabelsRenderer',
		tabLabelRenderer:              'TabLabelRenderer',
		tabContentsRenderer:           'TabContentsRenderer',
		tabContentRenderer:            'TabContentRenderer',
		progressBarRenderer:           'ProgressBarRenderer',
		reflectionImageRenderer:       'ReflectionImageRenderer',
		calendarDayViewRenderer:       'CalendarDayViewRenderer',
		calendarMonthViewRenderer:     'CalendarMonthViewRenderer',
		calendarWeekViewRenderer:      'CalendarWeekViewRenderer',
		richTextEditorRenderer:        'RichTextEditorRenderer',
		carouselRenderer:              'CarouselRenderer',
		carouselItemRenderer:          'CarouselItemRenderer',
		portletRenderer:               'PortletRenderer',
		portletViewRenderer:           'PortletViewRenderer',
		flowRenderer:                  'FlowRenderer',
//		flowRenderer:                  'RunwayFlowRenderer',
		accordionRenderer:             'AccordionRenderer',
		accordionItemRenderer:         'AccordionItemRenderer',
		sliderRenderer:                'GlowSliderRenderer',
		lightBoxRenderer:              'LightBoxRenderer']

	static Map renderers = [:]
	static {
		classes.each { name, className ->
			renderers[name] = 'de.andreasschmitt.richui.taglib.renderer.' + className
		}
	}
}
