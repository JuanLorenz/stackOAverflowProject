# Capstone:  Equipment Borrowing System (WT)
### ***stackOAverflow*** Members
- Balazon, John Vincent B.
- Balogbog, Rheyvene L.
- Pastor, Khylle Josh B.
- Rebusit, John Lawrence T.
- Sabagkit, John Arcel R.

## Project Description
**EBS** is an accessible and easy-to-use desktop application designed to track who and when general equipment, such as laboratory or engineering equipment, was used and borrowed by students, staff, and outside patrons. It solves a vital organizational gap, where instead of separate log books and Excel files scattered all over the system in different departments, EBS offers a centralized interface, locating desired equipment, verifying their real-time availability, and managing reservations wherever and whenever.   

### Proposed Features
- User login and Authentication
- Role-Based Access Control
- Search or Filtering
- Inventory Tracking and Category Management
- Automatic Synchronization
- Create/Update/Delete Records

### Planned Technologies
- **Java**
- **JavaFX**
- **JDBC**
- **Database (SQLite/MySQL)**

### Evaluation Criteria Mapping (Initial)
1. **OOP Principles**
	- Implemented by the use of classes such as Transaction, abstract classes such as User (for different users) and Equipment (for different equipment), and database management classes such as TransactionDAO and EquipmentDAO. The concepts of abstraction and inheritance are used, with polymorphism also included from abstract classes, and encapsulation of fields to ensure the user only interacts with those that are safe to interact (public).
	
2. **Generics**
	- Implemented by the use of arrays (List<>) to handle different types of equipment. The use of generics ensures that it can scale when more types of equipment are added.
	
3. **Database Connectivity**
	- Implemented by the use of DBMS servers (with the help of JDBC) to ensure atomicity, consistency, isolation, and durability of data. In order to interact with a database, a class named DatabaseConnection is added to connect to the server. It also enables CRUD operations when necessary, and data is properly stored somewhere even when the application is closed.
	
4. **Multithreading**
	- Implemented especially for processing a lot of borrowing requests at the same time. Multiple threads are utilized for proper resource management, and race conditions are minimized by using synchronized classes, so a user can’t borrow what is already being borrowed if two borrows at the same time.
	
5. **GUI**
	- Implemented by the use of JavaFX to enable modern and adaptable Graphical User Interface for the java application.
	
6. **UML**
	- Implemented to visualize the class diagram and the use case diagram for the entire project, as shown in the repository.
	
7. **Design Patterns**
	- Implemented Creational Design Patterns such as the Factory to strengthen and centralize things in the system. The use of Factory enables scaling, so when more types of equipment is added, the code doesn’t get too messy since it’s decoupled and it’s easier to add a new variant of the supertype.
