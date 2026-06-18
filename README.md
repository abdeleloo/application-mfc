# MFC Desktop (Java)

Application PC Java (Swing) reliée à la base MySQL `mfc` (même base que le site).

## Pré-requis

- JDK 11+
- Maven
- MySQL/MariaDB (XAMPP) avec la base `mfc`

## Configuration BDD

Fichier : `config/app.properties`

```
db.host=127.0.0.1
db.port=3306
db.name=mfc
db.user=root
db.password=
```

## Lancer l’application

```bash
mvn -f pom.xml clean package
java -jar target/mfc-desktop-1.0.0.jar
```

Dans l’application, clique sur “Connexion BDD”, puis utilise les onglets Formations / Sessions.
