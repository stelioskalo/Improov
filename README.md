# Improov
Improov is an android application which allows users to sign up as "students" or "coaches" for any business area (21 business areas to choose from). 
The main goal of the application is to provide individuals the ability to easily find a mentor/coach in order to improve their professional skills.
The main functionalities include having coaching sessions with a coach, read articles and take part in discussions with other people with similar interests.

Upon first time log in, users are prompted to select 5 business areas they are interested in out of a list of 21. This was used in order to show users
content more relevant to their interests. Entities with a business area attribute would be compared to the users 5 interest and if they matched 1, then the
entity would be displayed to the user (e.g Article, Discussion).

Students are able to request an hourly session or a monthly access to a specific coach by selecting a time and date for the session. 
Coaches set their own rates when creating their programs and they have to accept requests coming from students. 
Once a session has been accepted, a new session appears in the "manage sessions" tab were both students and coach can manage their session. 
Students are prompted to pay for their session using PayPal in order for the real-time communications to be unlocked. 

Students pay the application and the application would then pay the coach. This acts as a safeguard against scamming. Furthermore, the 
application holds a 10% of the session as a commicion.
When a coach decides to retrieve their money (after a session) they do that by inserting their email they wish to retrieve the moeny at. 
An HTTP request is done to PayPal in order to process that.

For the undertaking of the actual session, three ways of communicating were implemented; Instant Messaging, Audio and Video calls. 
Instant messaging were done using firebase as the database for sending and receiving messages, were Audio and Video calls were implemented
using WebRTC.

# Separate Programs for this app
An Admin UI was separately implemented using PyCharm were it allowed admins to create new discussion on behalf of the application, add new articels
and view users reports. 

An Article program was also developed using python, were the admin could insert a URL that would retrieve an article from a website
using an HTML parser. An AI model was trained in order to classify the category of the article.

# Technologies used in this repository
Android studio (Java)

Facebook Login API

Firebase

Mobile Camera Sensor

WebRTC

PayPal SDK

JavaScript (Firebase Functions)
