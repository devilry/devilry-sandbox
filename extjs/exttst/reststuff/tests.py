from django.test import TestCase
from django.test.client import Client
from django.core.urlresolvers import reverse
from django.utils import simplejson as json
from django.contrib.auth.models import User


class UserViewTest(TestCase):
    def test_post(self):
        client = Client()
        result = client.post(reverse('reststuff-user'), dict(username="superman", first_name='Super',
            last_name='Man'))
        self.assertEqual(result.status_code, 200)
        resultdata = json.loads(result.content)
        self.assertEqual(resultdata, dict(success=True))

    def test_put_invalid(self):
        client = Client()
        result = client.put(reverse('reststuff-user'), dict(username="superman", first_name='Super',
            last_name='Man'))
        self.assertEqual(result.status_code, 404)
        resultdata = json.loads(result.content)
        self.assertFalse(resultdata['success'])

    def test_put(self):
        client = Client()
        superman = User(username="superman", first_name="S", last_name="M")
        superman.save()
        result = client.put(reverse('reststuff-user'), dict(username="superman", first_name='Super',
            last_name='Man'))
        print result.content
        self.assertEqual(result.status_code, 200)
        resultdata = json.loads(result.content)
        self.assertTrue(resultdata['success'])
        self.assertEquals(User.objects.filter(username='superman').count(), 1)
