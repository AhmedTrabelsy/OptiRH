# OptiRH - Gestion des Ressources Humaines

<div align="center">
  <a href="https://github.com/mariem-jls/OptiRH-gestion-rh" target="_blank">
    <img src="https://github.com/user-attachments/assets/0f3bdb15-6321-42da-b294-c12b76d025d3" width="200" alt="OptiRH Logo">
  </a>
</div>

## 📌 Présentation

**OptiRH** est une solution innovante de gestion des ressources humaines qui combine une **application desktop en JavaFX** et une **plateforme web développée avec Symfony**. Ce projet vise à **simplifier, automatiser et optimiser** la gestion des RH pour les entreprises de toutes tailles.

La version **web** permet une gestion dématérialisée et centralisée des ressources humaines, accessible depuis n'importe quel navigateur.

## 🚀 Fonctionnalités principales

- **Gestion des utilisateurs** : Création et gestion des comptes avec rôles et permissions, accessibles depuis l'interface web.
- **Module Offre et Demande** : Publication et suivi des offres d'emploi et de stage, avec des options de recherche avancée.
- **Module Événement** : Planification et suivi des événements internes (réunions, formations, etc.), avec notifications en temps réel.
- **Module Réclamation** : Suivi structuré des réclamations, avec analyse automatique des tickets grâce à l'IA.
- **Module Transport** : Gestion et réservation des trajets, optimisé pour le web avec une interface intuitive.
- **Module Mission** : Attribution et suivi des projets et tâches, accessible via le back-office web.

## 🎯 Objectifs

- **Automatisation avancée** : Réduction des tâches manuelles et erreurs humaines grâce à l'automatisation des processus RH.
- **Centralisation des données** : Un accès rapide et sécurisé aux informations RH, avec une gestion centralisée via la version web.
- **Accessibilité & intuitivité** : Une interface moderne et fluide, accessible via navigateur, offrant une expérience utilisateur optimisée.
- **Scalabilité** : Adaptabilité aux besoins des PME et grandes entreprises, avec des fonctionnalités extensibles.

## 🛠️ Technologies utilisées

- **Frontend** : Symfony (pour la partie web) et JavaFX (pour la partie desktop)
- **Backend** : PHP (Symfony)
- **Base de données** : MySQL
- **Sécurité** : Authentification et gestion des accès sécurisés (JWT, OAuth, etc.)
- **Gestion des tâches** : Symfony Workflow pour les processus de validation et d'assignation

## 📖 Installation & Configuration

### Prérequis

- PHP 8.0 ou supérieur
- Symfony 5.x ou supérieur
- Composer
- MySQL Server
- Node.js (pour les assets frontend)
- JDK 17+ (pour l'application desktop JavaFX)

### Étapes d'installation

1. **Cloner le dépôt**

   ```bash
   git clone https://github.com/mariem-jls/OptiRH-gestion-rh.git
   cd OptiRH
   ```

2. **Installer les dépendances PHP**

   ```bash
   composer install
   ```

3. **Installer les dépendances frontend**

   ```bash
   npm install
   npm run build
   ```

4. **Configurer l'environnement**

   - Copiez le fichier `.env` pour créer un fichier `.env.local` :

     ```bash
     cp .env .env.local
     ```

   - Modifiez `.env.local` pour configurer les paramètres de connexion à la base de données MySQL, par exemple :

     ```env
     DATABASE_URL="mysql://utilisateur:mot_de_passe@localhost:3306/optirh_db"
     ```

   - Configurez les autres variables d'environnement (JWT, OAuth, etc.) si nécessaire.

5. **Créer la base de données**

   ```bash
   php bin/console doctrine:database:create
   ```

6. **Exécuter les migrations**

   ```bash
   php bin/console doctrine:migrations:migrate
   ```

7. **Lancer le serveur de développement**

   ```bash
   symfony server:start
   ```

8. **(Optionnel) Configurer l'application desktop JavaFX**

   - Assurez-vous d'avoir un JDK 17+ installé.
   - Importez le projet JavaFX dans votre IDE (par exemple, IntelliJ IDEA ou Eclipse).
   - Compilez et exécutez l'application desktop depuis le module dédié.

### Vérifications post-installation

- Accédez à l'interface web via `http://localhost:8000` pour vérifier que l'application est fonctionnelle.
- Testez l'authentification et les fonctionnalités principales (gestion des utilisateurs, offres, etc.).
- Vérifiez les logs (`var/log/dev.log`) en cas d'erreur.

## 📝 Notes supplémentaires

- Pour un environnement de production, configurez un serveur web comme **Nginx** ou **Apache**, et assurez-vous que les permissions des dossiers `var/` et `public/` sont correctement définies.
- Activez les notifications en temps réel en configurant un service de messagerie (par exemple, Mercure ou WebSocket) si nécessaire.
- Pour des fonctionnalités avancées comme l'analyse des réclamations par IA, intégrez un service tiers (par exemple, une API d'IA ou un modèle local).

## 📬 Contact

Pour toute question ou contribution, ouvrez une issue ou contactez l'équipe via [GitHub](https://github.com/mariem-jls/OptiRH-gestion-rh).

---

*OptiRH - Simplifiez la gestion de vos ressources humaines avec une solution moderne et intuitive !*
