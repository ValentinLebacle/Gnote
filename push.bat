@echo off

cd /d %~dp0

git checkout -b Gnote_3.5

git add .

git commit -m "JFX | Ajout du formulaire de note"

git push origin Gnote_3.5

echo La branche Gnote_3.5 a été mise à jour et envoyée vers GitHub avec succès.
pause