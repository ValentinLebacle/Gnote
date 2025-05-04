@echo off

cd /d %~dp0

git checkout -b Gnote_3.6

git add .

git commit -m "JFX | Finalisation des tests unitaires"

git push origin Gnote_3.6

echo La branche Gnote_3.6 a été mise à jour et envoyée vers GitHub avec succès.
pause