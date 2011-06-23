from django.conf.urls.defaults import patterns, url
from views import UserView

urlpatterns = patterns('',
    url(r'^user/(\w+)?$', UserView.as_view(), name='reststuff-user'),
)

