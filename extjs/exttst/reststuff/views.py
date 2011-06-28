import re

from django.views.generic import View
from django.http import HttpResponse, HttpResponseNotFound
from django.utils import simplejson as json
from django.forms import ModelForm
from django.shortcuts import render
from django.db.models import Q

from models import User



class UserForm(ModelForm):
    class Meta:
        model = User
        fields = ('id', 'email', 'first', 'last', 'score')


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
    def __init__(self, responseData, status):
        super(Response, self).__init__(
                json.dumps(responseData, indent=2),
                content_type='application/json', status=status)

class SuccessResponse(Response):
    def __init__(self, data):
        responseData = dict(
                success = True,
                data = data)
        super(SuccessResponse, self).__init__(responseData, status=200)

class BadRequestResponse(Response):
    def __init__(self, errors):
        responseData = dict(
                success = False,
                errors = errors)
        super(BadRequestResponse, self).__init__(responseData, status=400)



class FilterValue(object):
    filterPattern = re.compile('(OR|AND)?[ ]?(>=|<=|<|>)?(.+)')
    comparisonMap = {'>': 'gt', '<': 'lt',
                     '>=': 'gte', '<=': 'lte'}

    def __init__(self, fieldname, value):
        match = self.filterPattern.match(value)
        if not match:
            raise ValueError('Could not parse filter value for {0}: {1}'.format(fieldname, value))
        self.logicalConnective, self.comparisonOperator, self.value = match.groups()
        djangoComparison = self.get_django_comparison_op()
        key = '{0}__{1}'.format(fieldname, djangoComparison)
        queryParam = {key: self.value}
        self.djangoQry = Q(**queryParam)

    def get_django_comparison_op(self):
        return self.comparisonMap.get(self.comparisonOperator, 'icontains')

    def djang_logical_connect(self, otherDjangoQry):
        if self.logicalConnective == 'OR':
            return otherDjangoQry | self.djangoQry
        else:
            return otherDjangoQry & self.djangoQry


def validate_fieldname(formcls, fieldname):
    if not fieldname in formcls._meta.fields:
        raise ValueError('Illegal filter property: {0}'.format(fieldname))


def filters_to_qry(formcls, filters):
    filterQry = None
    for filterprop in filters:
        fieldname = str(filterprop['property'])
        validate_fieldname(formcls, fieldname)

        value = filterprop['value']
        filtered = FilterValue(fieldname, value)
        if filterQry:
            filterQry = filtered.djang_logical_connect(filterQry)
        else:
            filterQry = filtered.djangoQry
    return filterQry




def extjssort_to_djangoorderby(formcls, sortspecifications):
    def ext_to_django(sortspec):
        fieldname = str(sortspec['property'])
        validate_fieldname(formcls, fieldname)
        direction = sortspec['direction']
        if direction == 'DESC':
            fieldname = '-' + fieldname
        return fieldname
    return map(ext_to_django, sortspecifications)


class UserView(View):
    #@valid_user
    #def get(self, request, user):
        #data = dict(id=user.id, first=user.first, last=user.last, email=user.email)
        #return HttpResponse(json.dumps(data))

    def get(self, request, username=None):
        #validFilterFields = set()
        matchedusers = User.objects.all()

        filters = request.GET.get('filter')
        if filters:
            filters = json.loads(filters)
            try:
                filterQry = filters_to_qry(UserForm, filters)
            except ValueError, e:
                return BadRequestResponse(dict(filters=unicode(e)))
            else:
                matchedusers = matchedusers.filter(filterQry)

        sortspecifications = request.GET.get('sort')
        if sortspecifications:
            sortspecifications = json.loads(sortspecifications)
            orderby = extjssort_to_djangoorderby(UserForm, sortspecifications)
            matchedusers = matchedusers.order_by(*orderby)

        users = [dict(id=user.id, first=user.first, last=user.last, email=user.email, score=user.score) \
                for user in matchedusers]
        data = dict(success=True, message='Loaded data', data=users)
        return SuccessResponse(users)



    def _post_or_put(self, instance=None):
        request = self.request
        data = json.loads(request.raw_post_data)
        userform = UserForm(data, instance=instance)
        if userform.is_valid():
            user = userform.save()
            return SuccessResponse(data)
        else:
            # userform.errors is a django.forms.util.ErrorDict, which is a thin
            # wrapper around dict (it only adds a couple of methods, and
            # overrides __unicode__ and __str__). Since Ext.form.Basic.mastkInvalid
            # can use errors as field=msg pairs, we can just JSON serialize userform.errors.
            return BadRequestResponse(userform.errors)

    def post(self, request, username=None):
        """ Create """
        return self._post_or_put()

    @valid_user
    def put(self, request, user):
        return self._post_or_put(user)

    @valid_user
    def delete(self, request, user):
        user.delete()
        return SuccessResponse(data=[])



class RestExamples(View):
    def get(self, request):
        return render(request, 'restexamples.html')
