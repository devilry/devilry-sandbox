from django.conf.urls.defaults import patterns, include, url
from django.conf.urls.static import static
from django.contrib.staticfiles.urls import staticfiles_urlpatterns

urlpatterns = patterns('',
    url('^reststuff/', include('reststuff.urls'))
)

urlpatterns += staticfiles_urlpatterns()
#urlpatterns = static(prefix=settings.STATIC_URL, document_root=settings.MEDIA_ROOT,
                    #show_indexes=True)
