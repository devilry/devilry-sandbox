from django.views.generic import View
from django.http import HttpResponse, HttpResponseNotFound
from django.utils import simplejson as json
from django.forms import ModelForm
from django.shortcuts import render
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



class JsonSuccessResponse(HttpResponse):
    def __init__(self, data):
        response = dict(
                success = True,
                data = data)
        super(JsonSuccessResponse, self).__init__(
                json.dumps(response, indent=2),
                content_type='application/json')


class UserView(View):
    #@valid_user
    #def get(self, request, user):
        #data = dict(id=user.id, first=user.first, last=user.last, email=user.email)
        #return HttpResponse(json.dumps(data))

    def get(self, request, username=None):
        users = [dict(id=user.id, first=user.first, last=user.last, email=user.email) \
                for user in User.objects.all()]
        data = dict(success=True, message='Loaded data', data=users)
        return JsonSuccessResponse(users)

    def post(self, request, username=None):
        """ Create """
        data = json.loads(request.raw_post_data)
        userform = UserForm(data)
        user = userform.save()
        return JsonSuccessResponse(data)

    @valid_user
    def put(self, request, user):
        data = json.loads(request.raw_post_data)
        userform = UserForm(data, instance=user)
        userform.save()
        return JsonSuccessResponse(data)

    @valid_user
    def delete(self, request, user):
        user.delete()
        return HttpResponse(json.dumps(dict(success=True)), content_type='application/json')
        return JsonSuccessResponse(data=[])



class UserTable(View):
    def get(self, request):
        return render(request, 'usertable.html')
