from django.db import models



class User(models.Model):
    first = models.CharField(max_length=20)
    last = models.CharField(max_length=20)
    email = models.CharField(max_length=40)
