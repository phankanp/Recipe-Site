# Recipe-Site

Recipe website which allows users to find and share meal recipes. Users are able to sign up for an account, sign in, and build recipes with ingredients and instructions. The search feature provides provide users the ability to find recipes by description or ingredients. Recipes can also be sorted by categories. Each recipe has a Name, Category, an Image, a list of Ingredients with their measurements, a list of Instructions, a preparation time, and a cook time. Users are able to favorite recipes, and their profile page will feature a list of the recipes they have favorited. The website features REST endpoints for CRUD operations. Data is persisted using Hibernate and H2.

**Deployed to Heroku** : https://hidden-plateau-18308.herokuapp.com/

**Test user credentials:**

- **Admin username** : user1
**password** : password
- **username** : user2
**password** : password

Steps

- Create model classes, DAO interfaces, services, and add unit tests to components.
- Create the template for the recipe list page. Use the following requirements list to ensure all functionality is included in the recipe list page.
  - Recipes
    - Displays a list of recipes by name and indicates with a heart icon whether a user has favorited the recipe
    - Allows the user to filter the list by the selected category
    - Allows the user to add a new recipe
    - A user must have an account
    - Allows the user to edit or delete a recipe
    - A user must own the recipe
- Create the template for the recipe detail page. Use the following requirements list to ensure all functionality is included in the recipe detail page.
  - Recipe Detail
    - Allows a user to add a recipe, or edit the recipe if they are the owner
    - Allows a user to upload an image of the recipe
    - Allows a user to provide a recipe name, description, category (from a list of values), prep time, and cook time
    - Allows a user to provide a list of ingredients
    - Each ingredient includes an item, condition, and quantity
    - Allows a user to provide a list of steps
    - Each step includes a description
    - Any user can add the recipe to their favorites
- The recipe list page should have a search feature. A user can enter a search term and the recipe list will display results that have the search phrase in the description or ingredients.
- Enable user authentication with Spring Security. Use the supplied files to create templates for login page, registration page, and profile page. You must build the registration component, as it does not come with Spring Security. Create necessary controllers, services, and DAO to add a new user. Make sure to include validation so that a user may not use a username that already exists.
- Create REST endpoints for CRUD operations.
- Ensure data is persisted using Hibernate and any database provider.

- Add recipe owner/last edited by username to the recipe detail page
