from django.db import models



class User(models.Model):
    first = models.CharField(max_length=20, db_index=True)
    last = models.CharField(max_length=20, db_index=True)
    email = models.EmailField(db_index=True)
    score = models.IntegerField()

    def __unicode__(self):
        return u'{first} {last} <{email}> ({score})'.format(first = self.first,
                                                            last = self.last,
                                                            email = self.email,
                                                            score = self.score)
