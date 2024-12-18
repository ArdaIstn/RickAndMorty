 Rick And Morty App
In the Rick and Morty universe, you can access locations and list the characters living in those locations. Additionally, you can navigate to the details of any character to learn more about them. You also have the ability to add or remove characters from your favorites and filter them based on their status.

## Features

## Splash Screen
- **Functionality:** Displays a splash screen for 2 seconds when the app launches.
- **Purpose:** Provides a smooth transition into the app and sets up initial loading.

## Sign in and Sign up Screen
#### Functionality  
- Enables users to create a new account or sign in to an existing one.
#### Features
- **Authentication:** Validates user credentials for secure login and registration.   
- **Form Validation:** Ensures proper input for email and password fields.  
- **Enhanced Registration:** Includes features for password confirmation, nickname, and phone number registration during sign-up.  

## Character List Screen (CharacterFragment)
#### Functionality  
- Displays locations in a horizontal list and allows users to select one.  
- Shows characters from the selected location in a vertical list.
- Enables searching for characters within the selected location.
- Allows filtering characters based on their status (e.g., Alive, Dead, Unknown).
#### Features  
- **Horizontal View for Locations:** Locations are presented in a scrollable horizontal format for easy navigation.  
- **Vertical View for Characters:** Characters in the selected location are displayed in a vertically scrollable list.  
- **Interactive Selection:** Dynamically updates the character list based on the chosen location.
- **Character Search:** Provides a search bar to filter characters within the selected location.
- **Status Filtering:** Offers options to filter characters by their status for more specific browsing.  

## Character Detail Screen(CharacterDetailFragment)  
#### Functionality  
- Displays detailed information of the selected character, including their status, species, gender, origin, episodes, location, and creation date.  
- Allows users to add the character to their favorites list.
- Includes a navigation button to go back to the previous screen.   
#### Features  
- **Character Information:** Shows the following details of the selected character:  
  - **Status:** Current status (e.g., Alive, Dead, Unknown)  
  - **Species:** The species of the character  
  - **Gender:** The gender of the character  
  - **Origin:** The origin of the character  
  - **Episodes:** List of episodes the character appears in  
  - **Location:** The current location of the character  
  - **Created At:** The date the character was created in the system  
- **Favorite Option:** Allows users to add  the character from their favorites list.

## Favorites Screen(FavouritesFragment)   
#### Functionality  
- Displays a list of characters that the user has added to their favorites.  
- Allows users to remove characters from the favorites list, either individually or all at once.  
#### Features  
- **Favorites List:** Shows a list of all characters added to the favorites.  
- **Individual Removal:** Provides an option to remove characters from the favorites list one by one.  
- **Remove All Option:** Includes a feature to remove all characters from the favorites list at once.  

## Profile Screen(ProfileFragment) 
#### Functionality  
- Displays the user's email, nickname, and phone number.  
- Provides an option to sign out of the account.  
#### Features  
- **Profile Information:** Shows the user's email, nickname, and phone number.  
- **Sign Out Button:** Allows the user to log out of their account by clicking the sign-out button.  

## Architecture
For this project,I used MVVM.
MVVM is an architectural pattern used in application development that aims to separate the user interface (UI) from the business logic, making the application easier to maintain and test. It consists of three core components:
1. **Model:** Represents the data layer of the application. It typically handles database operations or network requests. The Model contains the data structures and business logic of the application.
2. **View:** Represents the user interface. It includes UI elements such as buttons, text fields, lists, etc., and interacts with the user. The View is responsible for displaying data but does not modify it.
3. **ViewModel:** Serves as the intermediary between the View and the Model. It processes user input from the View and provides data from the Model to the View. The ViewModel synchronizes the View with changes in the Model, typically using reactive programming patterns like **LiveData** or **StateFlow**.  

In MVVM, the **View** is kept decoupled from the **Model**, making the app more modular and scalable.
Changes to the data automatically propagate to the UI, ensuring the View is always in sync with the state of the Model.


## Libraries Used  

This project is XML based and written in Kotlin. Below are the libraries and tools utilized in the project:  

#### 1. **Jetpack Navigation**  
   - Manages in-app navigation, including fragment transactions and passing data between destinations, with a clear and consistent navigation graph.
     
#### 2. **Viewpager2 With Tablayout**  
   - Enables smooth navigation between fragments using swipe gestures, integrated with TabLayout for a clear and visually organized way to switch between different tabs. 

#### 3. **Room**  
   - A database library that provides an abstraction layer over SQLite, making it easier to perform database operations while ensuring compile-time safety.  

#### 4. **Retrofit**  
   - A type-safe HTTP client for Android and Java that simplifies making API calls and parsing responses into models.  

#### 5. **Glide**  
   - An image-loading library that efficiently fetches, displays, and caches images from URLs or local resources.  

#### 6. **Hilt**  
   - A dependency injection framework for Android, built on top of Dagger, which simplifies managing dependencies across the application.  

#### 7. **Coroutines**  
   - A Kotlin library for asynchronous programming, making it easier to manage background tasks and maintain a responsive UI.  

#### 8. **Flow**  
   - A reactive streams API from Kotlin that allows working with asynchronous data streams in a structured and thread-safe manner.  

#### 9. **StateFlow**  
   - A Kotlin state management library built on Flow, used to manage and observe state updates in a reactive and lifecycle-aware manner.  

#### 10. **LiveData**  
   - A lifecycle-aware observable data holder from Jetpack, often used to keep UI components up to date with data changes in the ViewModel.  

#### 11. **Firebase Firestore**  
   - A NoSQL cloud database from Firebase for storing and syncing data in real time across devices.  

#### 12. **Firebase Auth**  
   - Provides authentication services for managing user sign-in and sign-up securely, including email and password authentication.  

#### 13. **Lottie**  
   - A library for rendering high-quality, lightweight animations exported as JSON from Adobe After Effects.  



## How to Run

1. Clone the repository to your local machine.
2. Open the project in Android Studio.
3. Run the app on an emulator or a physical device.

4. Enjoy the explore characters!



## Project Video
![gif](https://github.com/user-attachments/assets/dc8bd6bf-b614-4fbc-bb48-68f874e91c5c)


## Screenshoots

  <tr>
    <td><img src="https://github.com/user-attachments/assets/64649ee8-6e8a-4a0d-9bd7-aeeecec36478" width="290"></td>
    <td><img src="https://github.com/user-attachments/assets/da212193-a706-44b8-b392-a78433b8c351" width="290"></td>
    <td><img src="https://github.com/user-attachments/assets/6805433c-9f94-444b-ab1c-42e2a64cfc03" width="290"></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/f8a6f7b3-f0bb-4518-916b-5b58874e7ee7" width="290"></td>
    <td><img src="https://github.com/user-attachments/assets/4aec0158-7315-45c9-a814-97ed4e5c7ad5" width="290"></td>
    <td><img src="https://github.com/user-attachments/assets/5f1ac6bf-6da6-47db-b28d-a4f06401c874" width="290"></td>
  </tr>
    <tr>
    <td><img src="https://github.com/user-attachments/assets/269ea852-ef0a-4d85-a1c5-4061629486a9" width="290"></td>
    <td><img src="https://github.com/user-attachments/assets/5a7d2de0-05c7-4bf7-8845-01401614cc38" width="290"></td>
    <td><img src="https://github.com/user-attachments/assets/fb2ba313-09ab-4171-9f3c-4277ee0f7f0b" width="290"></td>
  </tr>
</table>


