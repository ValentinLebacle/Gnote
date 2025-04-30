@echo off
REM Récupère le répertoire où se trouve ce fichier .bat
cd /d %~dp0

REM Vérifie si la branche Gnote_3.4 existe et change de branche
git checkout -b Gnote_3.5

REM Ajoute tous les fichiers modifiés
git add .

REM Commit avec un message personnalisé
git commit -m "JFX | Ajout de la fonctionnalité modification user "

REM Pousse la branche vers GitHub
git push origin Gnote_3.5

REM Affiche un message de confirmation
echo La branche Gnote_3.5 a été mise à jour et envoyée vers GitHub avec succès.
pause
