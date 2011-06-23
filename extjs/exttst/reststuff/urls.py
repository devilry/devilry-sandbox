from django.conf.urls.defaults import patterns, url
from views import UserView, UserTable

urlpatterns = patterns('',
    url(r'^user/(\d+)?$', UserView.as_view(), name='reststuff-user'),
    url(r'^usertable$', UserTable.as_view(), name='reststuff-usertable'),
)

