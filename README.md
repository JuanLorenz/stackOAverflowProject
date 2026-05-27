# Equiplink
#### Members of ***stackOAverflow*** <br>
> **Balazon, John Vincent B.** <br>
> **Balogbog, Rheyvene L.** <br>
> **Pastor, Khylle Josh B.** <br>
> **Rebusit, John Lawrence T.** <br>
> **Sabagkit, John Arcel R.**

## Project Description
**Equiplink** is an accessible and easy-to-use inventory management system designed to track who and when general equipment, such as laboratory or engineering equipment, was used and borrowed by students, staff, and outside patrons. 
It solves a vital organizational gap, where instead of separate log books and Excel files scattered all over the system in different departments, Equiplink offers a centralized interface, locating desired equipment, verifying their real-time availability, and managing reservations wherever and whenever.   

## App Features
- **User login and Customization** - Users can register an account and change their profile picture and password whenever they want.
- **Role-Based Access Control** - Users can only see and borrow equipment, while Admins can add and update equipment to the database.
- **Authentication & Serialization** - Secure login system where user sessions are serialized upon login and cleared upon logout.
- **Interface Navigation** - Interface provides a collapsible side pane of tabs (buttons) they can navigate to, including logging out.
- **Dynamic Dashboard** - A grid-based layout displaying equipment cards with images, equipment name, and real-time status.
- **Advanced Filtering** - Search for an equipment's name, or filter by equipment category (e.g. Engineering, Multimedia, Chemistry).
- **Equipment Transaction** - Users can borrow an equipment, and then view their list and see their due date. User-blocking is enforced to ensure accountability.

## System Flow
### For Users (Borrowers)
1. **Onboard**: Register a new account and log in to the system.
2. **Home**: View every equipment you're currently borrowing. Includes quick shortcuts to past categories of borrowed equipment.
3. **Dashboard**: A grid layout of equipment. Clicking an equipment card allows you to view details and borrow them. 
4. **Settings**: Manage personal details such as name, password, and profile photo.
5. **Records**: Access history of what you borrowed and returned in the past.

### For Admins (Managers)
1. **Onboard**: Only an existing Admin can add or delete admin accounts. Login your credentials the same way.
2. **Home**: View existing admins from the database and register new admin accounts.
3. **Dashboard**: A grid layout of equipment. You can add equipment here, or click an equipment card to update details.
4. **Settings**: Manage personal details such as name, password, and profile photo.
5. **Records**: Monitor what's currently borrowed and what's been returned, with the name of the Borrower included.

## Technologies
- Language: **Java**
- UI Framework: **JavaFX**
- Database Connectivity: **JDBC**
- Database Engine: **MySQL (via XAMPP)**

## Evaluation Criteria Mapping
1. **OOP Principles**
	- **Abstraction & Inheritance**: Implemented by the use of abstract class `Equipment` and inheriting that to create subtypes such as `EngineerEquipment` and `ChemistryEquipment`.
	- **Encapsulation**: Almost all data fields are kept private, ensuring that they are only accessed by getters and setters to maintain integrity.
	- **Polymorphism**: Utilized method overriding and abstract supertypes to handle various equipment types dynamically through late-binding (e.g. `List<Equipment>`).
	- **File Handling & Serialization**: Implemented for user serialization using `SerializeManager` and for image upload logic using `ImageManager`.
	
3. **Generics**
	- Implemented by the use of `List<T>` to handle different types of equipment or other object types.
	- Also utilized for `DataReceiver<T>` and `static void showOverlay(String, T)` for popups that require data, which could be an `Equipment` or `Runnable`. The use of generics ensures that it can scale when more types of equipment or types of data are needed.
	
4. **Database Connectivity**
	- Implemented by the use of a dedicated `ConnectionSQL` class that handles the lifecycle of a JDBC connection. Helper `DAO` classes (separated by which table it communicates with) use that connection for structured CRUD operations using SQL queries.
	
5. **Multithreading**
	- Implemented with the use of JavaFX in the first place.
	- Additional multithreading capabilities are also specified with the use of `backgroundLoading: True` in creating an `ImageView`, and the use of `DashboardViewModel` for User and Admin to handle equipment loading in a different thread, ensuring it maintains its speed when the user goes to the Dashboard.
	
6. **GUI**
	- Implemented by the use of JavaFX to enable modern and adaptable Graphical User Interface for the java application. Centralized all common screens into one `ApplicationShell.fxml` that features a collapsible side-pane navigation (Shell) to ensure that the UI is consistent and predictable.
	- Also makes use of CSS to upgrade the UI made by SceneBuilder even further.
	
7. **UML**
	- Implemented to guide the project by visualizing a comprehensive `ClassDiagram` and `UseCaseDiagram`, as shown in the project's repository.
	
8. **Design Patterns**
	- **Builder & Factory**: Implemented for the creation of different equipment subtypes (`EquipmentBuilder`) and for decoupling the logic of creating cards in the Dashboard (`EquipmentCardFactory`).
	- **Model-View-ViewModel**: Implemented for the Dashboard pattern, so the logic is easier to follow through. It becomes clear what the job of each class is.
	- **Data Access Object** - Implemented in the DAO classes such as `UserDAO`, `EquipmentDAO` and `TransactionDAO`. Each contain raw SQL queries and talk to different tables in the database.
	- **Service** - Implemented in the Service classes such as `EquipmentService` and `AuthService`, which acts as the middleman between the Controller and the DAO.
	- **Mediator** - Scenes don't talk to each other, they talk to the `SceneManager` which handles the navigation between different screens, or activating the right popup files.
	- **Observer** - Implemented by the use of `ObservableList` and `FilteredList` for the dashboard, and other screens that require dynamic data.
	- **Composition** - The use of a main `ApplicationShell` is a composition, where it "composes" other views inside its `contentArea` pane.
	- **Singleton** - Utilized for various classes that need to be only instantiated once, such as the `DashboardViewModel` and `ApplicationShellController`.
