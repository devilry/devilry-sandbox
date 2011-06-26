from django.db import models



class User(models.Model):
    first = models.CharField(max_length=20)
    last = models.CharField(max_length=20)
    email = models.EmailField()
    score = models.IntegerField()

    def __unicode__(self):
        return u'{first} {last} <{email}> ({score})'.format(first = self.first,
                                                            last = self.last,
                                                            email = self.email,
                                                            score = self.score)
