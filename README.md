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
- [ ] Allow User to Signup
- [ ] Log In using username and password
- [ ] Store userID once logged in to keep the user logged in (even after restarting the app)

2 - Product Listing:
- [ ] List Product Categories
- [ ] On clicking a Category, list Products in that Category
- [ ] On clicking a Product, show Product description, show buy button and controls to change quantity.

3 - Cart:
- [ ] Show cart summary
- [ ] Show total amount
- [ ] Purchase button to place an order, show order notification

4 - Show order history:
- [ ] List users orders
- [ ] On clicking an Order, show Order details and Products ordered
- [ ] On clicking a Product, take them to Product description page created for 3.3

5 - Show User details:
- [ ] Use the stored userID to show user details
- [ ] Show a random circular profile image from https://thispersondoesnotexist.com/
- [ ] Show Logout button, on click take back to Signup / Log In page (Restart should not auto login after logout)

6 - UI/Implementations Requirements:
- [ ] RecyclerView used for all Lists: Categories, Products, Orders
- [ ] ViewPager2 with bottom TabLayout for: Shop, Orders, Profile icons
- [ ] If logged in, attach authentication token to all requests until logout
- [ ] Add a small "About this app" button in the profile page, that shows a page with your copyright details and credits.

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
- Picasso GitHub: https://github.com/square/picasso
- Firebase Documentation: https://firebase.google.com/docs/android/setup?authuser=0&%3Bhl=pt&hl=pt
- Fake Store Documentation: https://fakestoreapi.com/docs

## Copyright Disclaimer:
Please note that this app project is part of Dorset College's sophomore semester final project, however, it may contain some part of code that may be copyrighted, if so, please contact me so I can delete or give due to copyright. All the people were duly referenced in the "References" section above.

Please note that this project is non-profit or not intended to be monetized.

Regards,
Josue Santos
