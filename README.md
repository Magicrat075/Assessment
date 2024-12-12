#RPN Calculator

##Setup

The basic setup of the project is MVVM architecture, Java with XML layouts, a singleton for resource handling, and a simple Activity and Viewmodel that calls a calculator class for calculating the results.

##Core design

The app was designed with 2 methods of calculation. 

The first is simple mode where the user can enter single calculations. For example 1 1 + will result in 2 in the output. The user will click next to enter the second value and then the operator to calculate the result. Once the calculation is complete, the user must click clear to enter a new calculation.

The second method is called batch mode where the user can enter larger calculations for example 15 7 1 1 * - / then calculate. Once the calculation is complete, the user must click clear to enter a new calculation string.

###ViewModel

The viewmodel is what controls any business logic for the activity. The viewmodel handles user interactions leaving the activity light and free of any logic that doesn't deal with an android widget.

Some notable things in the viewmodel are the currentIndex StringBuilder.  This is so the user can enter complex number such as decimals and larger numbers.  At theis time it does not support negative numbers, however it will give a negative result.  The viewmodel holds a reference to the calculator class which is the main class for determining results.

###Calculator

The calculator class holds a reference to a Deque which is a more modern version of a Java Stack.  As numbers are entered they are pushed to a stack then when a calculation is required the last two values are popped off the stack. Then the result is returned through the peek function. At this time only the four basic operators are supported.

###Error Handling

The Single mode is pretty simple and not as prone to errors, however it is covered with code coverage to ensure accuracy.  In batch mode since it is a string based with less restriction it is easier to create user error. Therefore there are a bunch of checks in place to ensure no extra numbers or operators are added to the string and will output the proper error based on these conditions. That said the user must click clear otherwise the previous result will remain in the stack and alter the next result.