# SAE S4 - Pizzaland

Nous réalison le projet sur un même pc, c'est donc pour cela que tous les commits sont réalisé par Maxime Bimont.

## 🏹 Travail à réaliser :

L’entreprise Pizzaland souhaite mettre à disposition des entreprises tierces, la gestion de ses pizzas et leurs
ingrédients ainsi que les commandes qui sont effectuées par les clients. Elle souhaite mettre en place une API REST
pour assurer cette tâche.

Pour simplifier le travail, Pizzaland a décidé de ne communiquer qu’en JSON. Bien évidemment ce travail necessite
une base de données. Pour faciliter la maintenance future de l’application on souhaite isoler les requêtes SQL dans des
DAO, et la connexion à la base de données à un seul endoit.

## Gestions de la base de donnée.

Pour notre base de données, nous avons décidé de l'organiser ainsi :

-   Une première table `Ingredients` qui va stocker tous nos ingrédients. Pour cela, elle contient un ID, un Nom est un prix.

-   Une deuxième table `Pizzas` qui va stocker toutes nos pizzas sans les ingrédients. Pour cela, elle a : un ID, un Nom, un type de pattes est un prix.

-  Une troisième table `confection` qui va permettre de faire l'association de nos pizzas avec leurs ingrédients et de stocker le prix final de nos pizzas (ingrédients + prix de base de la pizza). Pour cela, elle récupère l'id de nos pizzas, l'id des ingrédients et une colonne finalPrice. 

- Une quatrième table `Utilisateur` qui stocker tous nos utilisateurs. Pour cela, elle a : un ID et un Nom.

- Une cinquième table `commande` qui va stocker toutes nos commandes sans les pizzas associées à ses commandes. Pour cela, elle a : un ID, un ID d'un utilisateur et une date.

- Une sixième table `commandefini` qui va permettre de faire l'association entre les commandes et les pizzas correspondantes et d'établir le prix final des commandes. Pour cela, elle récupère l'id de no commandes, l'id de nos pizzas et une colonne finalPrice.

## 🔗 Liste des commandes utilisables

### 🍅 Les ingredients :

```bash
$ http GET http://localhost:8080/pizzaland/ingredients
```

Permet de récuperer la liste de tous les ingrédients contenue dans notre base de donnée. 

```bash
$ http GET http://localhost:8080/pizzaland/ingredients/{id}
```

Permet de récuperer l'ingredients qui correspond à l'id passé en paramettre.

```bash
$ http GET http://localhost:8080/pizzaland/ingredients/{id}/name
```

Permet de récuperer le nom de l'ingredients qui correspond à l'id passé en paramettre.

```bash
$ http POST http://localhost:8080/pizzaland/ingredients id={...} name="{...}" price={...}
```

Permet d'ajouter un ingrédients à la base de donnée avec ces différents paramettres.  

```bash
$ http DELETE http://localhost:8080/pizzaland/ingredients/{id}
```

Permet de supprimer un ingrédients de la base de donnée grâce a l'id passé en paramettre  

### 🍕 Les pizzas :

```bash
$ http GET http://localhost:8080/pizzaland/pizzas
```
Permet de récuperer la liste de toutes les pizzas contenue dans notre base de donnée. 

```bash
$ http GET http://localhost:8080/pizzaland/pizzas/{id}
```
Permet de récuperer la pizza qui correspond à l'id passé en paramettre.

```bash
$ curl -X POST http://localhost:8080/pizzaland/pizzas -H "Content-Type: application/json" -d '{"id":{...}, "name": "{...}", "type": "{...}","price":{...}, "ingredients":[{"id":{...}}, {"id":{...}}, {"id":{...}}]}'
```
Permet d'ajouter une pizza à la base de donnée avec ces différents paramettres.  

```bash
$ http DELETE http://localhost:8080/pizzaland/pizzas/{id}
```
Permet de supprimer une pizza de la base de donnée grâce a l'id passé en paramettre  

```bash
$ curl -i -X PATCH localhost:8080/pizzaland/pizzas/{id} id '{...}'
```
Permet de modifier le prix d'une pizza.

```bash
$ http POST http://localhost:8080/pizzaland/pizzas/{id}  ingredients[id={...}]
```
Permet d'ajouter un ingredient a une pizza.

```bash
$ http DELETE http://localhost:8080/pizzaland/pizzas/{id}/{idIngredients}
```
Permet de supprimer l'ingredients d'une pizza.

```bash
$ http GET http://localhost:8080/pizzaland/pizzas/{id}/prixfinal
```
Permet de recupérer le prixfinal d'une pizza.

### 📃 Les Commandes :

```bash
$ http GET http://localhost:8080/pizzaland/commandes
```
Permet de récupérer la liste de toutes les commandes en cours.

```bash
$ http GET http://localhost:8080/pizzaland/commandes/{id}
```
Permet de récupérer le détail d'une commande grâce à sont id passé en paramettre.

```bash
$ http POST http://localhost:8080/pizzaland/commandes
```
Permet d'ajouter une nouvelle commande.

```bash
$ http GET http://localhost:8080/pizzaland/commandes/{id}/prixfinal
```
Permet de recupérer le prixfinal d'une commandes.

## 👥 Authors

<pre>
Maxime Bimont
Loïc Lecointe
</pre>
