echo entrez le chemin du dossier DB.....
read chemin
echo COMPILATION DES SCRIPTS.......
javac -d Code/MiniSGBD_BENZENATI_BORDJAH/bin  -cp Code/MiniSGBD_BENZENATI_BORDJAH/src  Code/MiniSGBD_BENZENATI_BORDJAH/src/miniSGBD/*.java
echo LANCEMENT DE LA BASE DE DONNEE....
java -cp Code/MiniSGBD_BENZENATI_BORDJAH/bin  miniSGBD.Main $chemin
