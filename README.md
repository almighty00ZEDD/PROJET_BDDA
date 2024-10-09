# **Base de données relationnelle : Conçue avec l'api standard JAVA 8** 🚀

![Build Status](https://img.shields.io/badge/build-passing-brightgreen) ![Version](https://img.shields.io/badge/version-1.0.0-blue) ![License](https://img.shields.io/badge/license-MIT-green)  
**Description** : 
Projet de développement effectué dans le cadre du module "Base de données avancées" de l'université de Paris descartes.

Le repo contiens le code source d'une base de données relationnelle implémentée en utilisant uniquement l'API standard de Java 8. Ce projet met en œuvre les concepts fondamentaux des bases de données relationnelles tels que les tables, les relations, les requêtes, et la gestion des transactions, sans recours à des bibliothèques ou frameworks externes. L'objectif est de simuler le fonctionnement d'une base de données en exploitant les structures de données et fonctionnalités offertes par Java 8, notamment les collections, les streams, et la gestion des exceptions. Le projet constitue une démonstration pédagogique de la manipulation de données relationnelles avec des outils natifs à Java.

---

## **Auteurs**
- **BENZENATI ZINE EDDINE** - [Profil LinkedIn](https://www.linkedin.com/in/zine-eddine-benzenati-545388174/).
- **BORDJAH NADIR** - [Profil LinkedIn](https://www.linkedin.com/in/nadir-bordjah-234675206/).

## **Installation**
1. **Cloner le repository** :

   Ouvrez votre terminal et exécutez la commande suivante :

   ```bash
   git clone https://github.com/almighty00ZEDD/PROJET_BDDA.git
   ```
2. **Ouvrir le projet dans Eclipse** :
   Cette étape couvre le dossier Code qui contient le source concret du projet contenant aussi les metadonnées requises pour une ouverture dans eclipse

- Lancez Eclipse.
- Cliquez sur File > Import.
- Sélectionnez Existing Projects into Workspace et cliquez sur Next.
- Cliquez sur Browse pour sélectionner le dossier que vous venez de cloner.
- Sélectionnez le projet et cliquez sur Finish.

## **Lancement et utilisation**
Le projet une fois ouvert dans eclipse peut etre lancé graphiquement en appuyant sur le bouton 'RUN' et utilisant la console propre à eclipse.

Le projet en dehors du source (le dossier Code) contiens aussi le script `Begin.sh` permettant de lancer la base de données depuis un terminal linux ou powershell s'occupant ainsi de la compilation et du lancement.

Le programme demandera un chemin vers le dossier de stockage qu'il faudra fournir, ou à défaut créer un dossier vide servant de stockage pour cette base de données.

Enfin voici la liste des commandes que prends en charge la base de données :
- `CREATEREL` - Création d'une nouvelle relation suivant le modèle attribut-valeur
- `INSERT` - Insertion d'un nouvel enregistrement compatible avec une relation existante
- `BATCHINSERT` - Insertion de multiples enregistrements compatibles avec une relation existante à partir d'un fichier .csv
- `SELECTALL` - Affichage de tout les enregistrements d'une relation existante donnée
- `SELECTC` - Recherche et effichage d'un ou plusieurs enregistrements donnant des critères
- `SELECTS` - Recherche et affichage d'un enregistrement précis donnant son identifiant unique
- `UPDATE` - Modification d'un enregistrement dans la BDD
- `RESET` - Effectue un nettoyage général de la BDD remettant à zéro tout le stockage et la table des relations et d'indexage
to Markdown converter
- `EXIT` - Quitte le programme et ferme le terminal
