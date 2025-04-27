@echo off
REM Récupère le répertoire où se trouve ce fichier .bat
cd /d %~dp0

REM Vérifie si la branche Gnote_3.4 existe et change de branche
git checkout Gnote_3.4

REM Ajoute tous les fichiers modifiés
git add .

REM Commit avec un message personnalisé
git commit -m "Mise à jour de la version Gnote_3.4"

REM Pousse la branche vers GitHub
git push origin Gnote_3.4

REM Affiche un message de confirmation
echo La branche Gnote_3.4 a été mise à jour et envoyée vers GitHub avec succès.
pause
