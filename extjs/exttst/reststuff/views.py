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



class Response(HttpResponse):
    def __init__(self, request, responseData, status):
        super(Response, self).__init__(
                json.dumps(responseData, indent=2),
                content_type='application/json', status=status)

class SuccessResponse(Response):
    def __init__(self, request, data):
        responseData = dict(
                success = True,
                data = data)
        super(SuccessResponse, self).__init__(request, responseData, status=200)

class BadRequestResponse(Response):
    def __init__(self, request, errors):
        responseData = dict(
                success = False,
                errors = errors)
        super(BadRequestResponse, self).__init__(request, responseData, status=400)


class UserView(View):
    #@valid_user
    #def get(self, request, user):
        #data = dict(id=user.id, first=user.first, last=user.last, email=user.email)
        #return HttpResponse(json.dumps(data))

    def get(self, request, username=None):
        users = [dict(id=user.id, first=user.first, last=user.last, email=user.email) \
                for user in User.objects.all()]
        data = dict(success=True, message='Loaded data', data=users)
        return SuccessResponse(request, users)

    def post(self, request, username=None):
        """ Create """
        data = json.loads(request.raw_post_data)
        userform = UserForm(data)
        if userform.is_valid():
            user = userform.save()
            return SuccessResponse(request, data)
        else:
            # userform.errors is a django.forms.util.ErrorDict, which is a thin
            # wrapper around dict (it only adds a couple of methods, and
            # overrides __unicode__ and __str__). Since Ext.form.Basic.mastkInvalid
            # can use errors as field=msg pairs, we can just JSON serialize userform.errors.
            return BadRequestResponse(request, userform.errors)

    @valid_user
    def put(self, request, user):
        data = json.loads(request.raw_post_data)
        userform = UserForm(data, instance=user)
        userform.save()
        return SuccessResponse(request, data)

    @valid_user
    def delete(self, request, user):
        user.delete()
        return SuccessResponse(request, data=[])



class UserTable(View):
    def get(self, request):
        return render(request, 'usertable.html')
