# Pizzaland

# Members

<pre>
Maxime Bimont
Loïc Lecointe
GRP I
</pre>

Nous réalison le projet sur un même pc, c'est donc pour cela que tous les commits sont réalisé par Maxime Bimont.

# Gestions de la base de donnée.

Pour notre base de donnée nous avons décidé de créer trois table:

-   Une première table `Ingredients` qui va stoquait tous nos ingrédients. Pour cela elle a un ID, un Nom est un prix.

-   Une deuxième table `Pizzas` qui va stoquait toutes nos pizzas sans les ingredients. Pour cela elle a : un ID, un Nom, un type de pattes est un prix.

-  Une  troisème table `confection` qui va permettre de faire lacociation de nos pizzas avec leurs ingredients et de stoqué le prix final de nos pizzas (ingredients + prix de base de la pizzas). Pour cela elle récupère l'id de nos pizzas, l'id des ingredients et un colone finalPrice. 

- Une Quatrième table `Utilisateur` qui stoque tous nos utilisateurs.

- Une cinquième table ``

# Liste des commandes Utilisées 

## Les ingredients :

```bash
$ http GET http://localhost:8080/pizzaland/ingredients
```
permet de récuperer la liste de tous les ingrédients contenue dans notre base de donnée. 

```bash
$ http GET http://localhost:8080/pizzaland/ingredients/{id}
```

permet de récuperer l'ingredients qui correspond à l'id passé en paramettre.

```bash
$ http GET http://localhost:8080/pizzaland/ingredients/{id}/name
```
permet de récuperer le nom de l'ingredients qui correspond à l'id passé en paramettre.

```bash
$ http POST http://localhost:8080/pizzaland/ingredients id={...} name="{...}" price={...}
```
permet d'ajouter un ingrédients à la base de donnée avec ces différents paramettres.  

```bash
$ http DELETE http://localhost:8080/pizzaland/ingredients/{id}
```
permet de supprimer un ingrédients de la base de donnée grâce a l'id passé en paramettre  

## Les pizzas 

```bash
$ http GET http://localhost:8080/pizzaland/pizzas
```
permet de récuperer la liste de toute les pizzas contenue dans notre base de donnée. 

```bash
$ http GET http://localhost:8080/pizzaland/pizzas/{id}
```
permet de récuperer la pizzas qui correspond à l'id passé en paramettre.

```bash
$ curl -X POST http://localhost:8080/pizzaland/pizzas -H "Content-Type: application/json" -d '{"id":{...}, "name": "{...}", "type": "{...}","price":{...}, "ingredients":[{"id":{...}}, {"id":{...}}, {"id":{...}}]}'
```
permet d'ajouter une pizzas à la base de donnée avec ces différents paramettres.  

```bash
$ http DELETE http://localhost:8080/pizzaland/pizzas/{id}
```
permet de supprimer une pizzas de la base de donnée grâce a l'id passé en paramettre  

```bash
$ curl -i -X PATCH localhost:8080/pizzaland/pizzas/{id} id '{...}'
```
permet de modifier le prix d'une pizzas.

```bash
$ http POST http://localhost:8080/pizzaland/pizzas/{id}  ingredients[id={...}]
```
permet d'ajouter un ingredient a une pizza.

```bash
$ http DELETE http://localhost:8080/pizzaland/pizzas/{id}/{idIngredients}
```
permet de supprimer un ingredients à une pizza.

```bash
$ http GET http://localhost:8080/pizzaland/pizzas/{id}/prixfinal
```
permet de recupérer le prixfinal d'une pizzas, c'est à dire le prix des ingredients + le prix de base de la pizza.

## Commandes

```bash
$ http GET http://localhost:8080/pizzaland/commandes
```
permet de récupérer la liste de toutes les commandes en cours.

```bash
$ http GET http://localhost:8080/pizzaland/commandes/{id}
```
permet de récupérer le détail d'un commande grâce a sont id passé en paramettre.