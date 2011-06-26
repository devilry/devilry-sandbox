from django.conf.urls.defaults import patterns, url
from views import UserView, RestExamples

urlpatterns = patterns('',
    url(r'^user/(\d+)?$', UserView.as_view(), name='reststuff-user'),
    url(r'^$', RestExamples.as_view()),
)

