from django.views.generic import View
from django.http import HttpResponse, HttpResponseNotFound
from django.utils import simplejson as json
from django.contrib.auth.models import User
from django.forms import ModelForm




class UserForm(ModelForm):
    class Meta:
        model = User
        fields = ('username', 'first_name', 'last_name')


def valid_user(f):
    def wrapper(self, request, username):
        print request.method
        print request.raw_post_data
        try:
            user = User.objects.get(username=username)
        except User.DoesNotExist:
            data = dict(success=False, error="Invalid user {0}.".format(username))
            return HttpResponseNotFound(json.dumps(data))
        else:
            return f(self, request, user)
    return wrapper


class UserView(View):
    @valid_user
    def get(self, request, user):
        data = dict(username=user.username, first=user.first_name, last=user.last_name)
        return HttpResponse(json.dumps(data))

    def post(self, request, username=None):
        """ Create """
        userform = UserForm(request.POST)
        userform.save()
        return HttpResponse(json.dumps(dict(success=True)))

    @valid_user
    def put(self, request, user):
        userform = UserForm(request.POST, instance=user)
        userform.save()
        return HttpResponse(json.dumps(dict(success=True)))

    @valid_user
    def delete(self, request, user):
        user.delete()
        return HttpResponse(json.dumps(dict(success=True)))
