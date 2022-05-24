# Hourglass

### Description

This self-improvement application is called **Hourglass**, which mainly serves as a *time tracker*. As the name suggests, this application can track time to increase productivity and know how much time the users waste in unproductive activities. Users include (but are not limited to) students and workers who strive to improve their time management skills.

As a student myself, I need to manage time well between school, social, and balanced life. But, as most people do, I procrastinate a lot or unknowingly spend all day doing nothing. By creating this application, hopefully, users (specifically me) can identify problems to improve productivity.

### User Stories

- As a user, I want to be able to add multiple activities to my activity list
- As a user, I want to be able to track the list of my activities
- As a user, I want to be able to set time when the activity started and finished
- As a user, I want to be able to delete an activity from my activity list
- As a user, I want to be able to automatically save my activity list when stop is pressed.
- As a user, I want to be able to have option to save my activity list to file when exiting
- As a user, I want to be able to automatically load my activity list from file when opening

### Phase 4: Task 2
Implement tests and increases robustness in Activity class. Previously the getStartTime and getEndTime required clauses of start() and/or stop(). Now, it throws a TimerException and will be caught in the ActivitiesTest, JsonReaderTest, and JsonWriterTest. Also add some more test cases for null startTime and endTime where exception is expected.

### Phase 4: Task 3
If there is enough time, it is better to have a panel class that is separated from the GUI frame. Also makes Activity not the field in GUI since it can actually be accessed through Activities alone. Other than that, the clarity and elegance of the writing can be improved as well.
