from django.test import TestCase
from django.test.client import Client
from django.core.urlresolvers import reverse
from django.utils import simplejson as json
from models import User


class UserViewTest(TestCase):
    def test_post(self):
        client = Client()
        result = client.post(reverse('reststuff-user'),
                json.dumps(dict(first='Super', last='Man', email="e@mail.com")),
                content_type='application/json')
        self.assertEqual(result.status_code, 200)
        resultdata = json.loads(result.content)
        self.assertEqual(resultdata, dict(success=True))

    def test_put(self):
        client = Client()
        superman = User(email="supr@man.com", first="S", last="M")
        superman.save()
        result = client.put(reverse('reststuff-user', args=[superman.id]),
                json.dumps(dict(first='Super', last='Man', email="e@mail.com")),
                content_type='application/json')
        self.assertEqual(result.status_code, 200)
        resultdata = json.loads(result.content)
        self.assertTrue(resultdata['success'])
        self.assertEquals(User.objects.get(id=superman.id).first, 'Super')

    def test_put_invalid(self):
        client = Client()
        invalid_id = 101010101010
        with self.assertRaises(User.DoesNotExist):
            User.objects.get(id=invalid_id)
        result = client.put(reverse('reststuff-user', args=[invalid_id]),
                json.dumps(dict(first='Super', last='Man', email="e@mail.com")),
                content_type='application/json')
        self.assertEqual(result.status_code, 404)
        resultdata = json.loads(result.content)
        self.assertFalse(resultdata['success'])
