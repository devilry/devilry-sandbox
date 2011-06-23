from django.conf.urls.defaults import patterns, include, url

urlpatterns = patterns('',
    url(r'^static/(?P<path>.*)$', 'django.views.static.serve'),
    url('^reststuff/', include('reststuff.urls'))
)
