

resetdb: ALWAYS
	rm -f tull.sqlite
	python manage.py syncdb --noinput
	python manage.py loaddata data.json

ALWAYS:
