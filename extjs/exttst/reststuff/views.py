from django.views.generic import View
from django.http import HttpResponse, HttpResponseNotFound
from django.utils import simplejson as json
from django.forms import ModelForm

from models import User



class UserForm(ModelForm):
    class Meta:
        model = User
        fields = ('email', 'first', 'last')


def valid_user(f):
    def wrapper(self, request, id):
        try:
            user = User.objects.get(id=id)
        except User.DoesNotExist:
            data = dict(success=False, error="Invalid user {0}.".format(id))
            return HttpResponseNotFound(json.dumps(data))
        else:
            return f(self, request, user)
    return wrapper


class UserView(View):
    @valid_user
    def get(self, request, user):
        data = dict(ud=user.id, first=user.first, last=user.last, email=user.email)
        return HttpResponse(json.dumps(data))

    def post(self, request, username=None):
        """ Create """
        data = json.loads(request.raw_post_data)
        userform = UserForm(data)
        userform.save()
        return HttpResponse(json.dumps(dict(success=True)))

    @valid_user
    def put(self, request, user):
        data = json.loads(request.raw_post_data)
        userform = UserForm(data, instance=user)
        userform.save()
        return HttpResponse(json.dumps(dict(success=True)))

    @valid_user
    def delete(self, request, user):
        user.delete()
        return HttpResponse(json.dumps(dict(success=True)))
