# Project_OnlineShoppingApp
Project - Online Shopping App

- Module Title: Mobile App 1
- Lecture name: Saravanabalagi Ramachandran

## Online shopping App
- Student Name: Josue Dilmo dos Santos
- Student Number: 24061
- Year: 2
- Semester: 2

## Requirements:
1 - Authentication:
- [x] Allow User to Signup
- [x] Log In using username and password
- [x] Store userID once logged in to keep the user logged in (even after restarting the app)

2 - Product Listing:
- [x] List Product Categories
- [x] On clicking a Category, list Products in that Category
- [x] On clicking a Product, show Product description, show buy button and controls to change quantity.

3 - Cart:
- [x] Show cart summary
- [x] Show total amount
- [x] Purchase button to place an order, show order notification

4 - Show order history:
- [x] List users orders
- [x] On clicking an Order, show Order details and Products ordered
- [x] On clicking a Product, take them to Product description page created for 3.3

5 - Show User details:
- [x] Use the stored userID to show user details
- [x] Show a random circular profile image from https://thispersondoesnotexist.com/
- [x] Show Logout button, on click take back to Signup / Log In page (Restart should not auto login after logout)

6 - UI/Implementations Requirements:
- [x] RecyclerView used for all Lists: Categories, Products, Orders
- [x] ViewPager2 with bottom TabLayout for: Shop, Orders, Profile icons
- [x] If logged in, attach authentication token to all requests until logout
- [x] Add a small "About this app" button in the profile page, that shows a page with your copyright details and credits.

## JSON API Links:
- Root URL: https://fakestoreapi.com
- User Signup: POST /users
- User Sign In: POST /auth/login
- Product Categories: GET /products/categories
- Products in a particular category: GET /products/categories/electronics
- Purchase a cart: POST /carts
- Retrieve order history for user id 2: GET /carts/user/2
- More details available on https://fakestoreapi.com/docs

## References:
- We all need: https://stackoverflow.com
- A little help from: https://developer.android.com/
- Google Fonts: https://fonts.google.com/specimen/Montserrat?query=montse
- OkHttp Documentation: https://square.github.io/okhttp/
- Retrofit Documentation: https://square.github.io/retrofit/
- Glide: https://bumptech.github.io/glide/
- Picasso: https://github.com/square/picasso
- Swipe to: https://medium.com/@kitek/recyclerview-swipe-to-delete-easier-than-you-thought-cff67ff5e5f6
- Firebase Documentation: https://firebase.google.com/docs/android/setup?authuser=0&%3Bhl=pt&hl=pt
- Fake Store Documentation: https://fakestoreapi.com/docs

## Copyright Disclaimer:
Please note that this app project is part of Dorset College's sophomore semester final project, however, it may contain some part of code that may be copyrighted, if so, please contact me so I can delete or give due to copyright. All the people were duly referenced in the "References" section above.

Please note that this project is non-profit or not intended to be monetized.

Regards,
Josue Santos
