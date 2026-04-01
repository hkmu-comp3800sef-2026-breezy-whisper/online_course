# COMP 3800SEF/ 3820SEF/ S380F (2026)

# COMP 3800SEF / 3820SEF / S380F Web Applications: Design and Development

# Group Project (50% of OCAS)

**Theme:** You are required to implement a web application for an **Online Course Website**.

**Basic Features (60% of the project):**

1. Your web application should fulfill the following basic requirements on web pages and
    functionalities:  
       a. Using major techniques introduced in the lectures and labs, like Jakarta EE, JSP, EL, and JSTL. You are NOT allowed to use a non-Jakarta EE server for the system. Using CSS and Bootstrap are allowed.  
       b. You are required to use **Spring MVC, Spring Boot, and Spring Security**.  
       c. Dynamic page generation is based on the user’s input or request.  
       d. Using the **H2 database** as a backend for data storage ( **necessary** for uploaded files).  
       e. The web application should be easy to use for normal users.  
2. In the basic part, you must implement ALL of the following features to receive a full mark:
    a. Website hierarchy:  
        i. The website has an **index page** , which shows the course name, a short course description, a list of lectures, and a list of multiple-choice (MC) polls, i.e., more than one lecture and poll.
        ii. Each lecture has a **course material page** , which shows  
        1. The _lecture title,_  
        2. The _download links_ of the lecture notes,  
        3. A brief summary of each lecture’s content,  
        4. A list of _comments_ from registered users (including the teachers and students).  
    iii. Each poll has a **poll page** , which shows  
        1. A _poll question_ (e.g., “Which topic should be introduced in the next
class?”),  
        2. **Exactly five** MC options (if a user has selected an option, the selected option will be shown and can be edited),  
        3. The _current number of votes_ for each MC option,  
        4. A list of _comments_ from registered users (including the teachers and students).  

    b. Student/Teacher registration (for unregistered users) and login function:
        i. Information includes username, password, full name, email address, and phone number.
    c. Unregistered users can read all content on the index page but not on the other pages.
    d. Registered students can read content on all pages, and do the following:
        i. Write new comments.
        ii. Vote for polls (If the user has voted, the chosen option can be edited by the same user).
        iii. Update the user’s personal information except the username.
    e. Registered teachers (at least one, and can be more than one) can do anything a registered student can do, plus the following:
        i. Edit (add, delete, update) the list of registered users and their information.
        ii. Add and delete course material pages and poll pages.
        iii. Add and delete course materials or comments.

Note that all features are supposed to be fully functional.

1. A comment history page of a registered user (including the teachers and students).
2. Multiple languages (e.g., English, Traditional Chinese), e.g., provide a button to switch between
    Chinese and English; Note that using a translation service (e.g., Google Translate) is not
    allowed.