# Spacetime Calculator

## What will the application do?

This application will allow users to plot events in two-dimensional spacetime. They will be able to see how events transform between reference frames, and how concepts such as simultaneity or two events being in the same place are frame-dependent. Additionally, it will allow users to calculate how far two events are from each other in spacetime; whether they are timelike, spacelike, or null separated; and whether they appear to be simultaneous or in the same place in a given frame.

## Who will use it?

The primary audience for this application would be physics students looking to gain a deeper understanding of Lorentz transformations and special relativity. However, it may also be of interest to those with just a cursory interest in modern physics who are looking to qualitatively learn about special relativity.

## Why is this project of interest to you?

As a student pursuing a combined honours in physics and computer science, I have a deep interest in computational physics and would like to gain more experience in the field. Additionally, I believe that developing and using this application will allow me to more thoroughly understand concepts that I am learning in my courses on linear algebra and relativity and quanta.

## User Stories

* As a user, I want to be able to add an arbitrary number of events to my spacetime diagram.
* As a user, I want to be able to create an arbitrary number of reference frames moving relative to a stationary frame.
* As a user, I want to be able to determine whether two events are timelike, spacelike, or null separated.
* As a user, I want to be able to switch reference frames and see how an event's position and time change.
* As a user, I want to be able to save a world's events and reference frames to file.
* As a user, I want to be able to load a world's events and reference frames from file.

## Phase 4: Task 2

Include a type hierarchy in your code *other than* the one that uses the `Saveable`    interface introduced in Phase 2.  You must have more than one subclass and your subclasses *must* have distinct functionality.  They must therefore override at least one method inherited from a super type and override it in different ways in each of the subclasses.

* The abstract class `ReferenceFrame` is extended by the MasterFrame and RelativeFrame subclasses, which override the `boost` and `relativeVelocityTo`

## Phase 4: Task 3

* There is too much coupling in the subclasses of ReferenceFrame. Having a separate `MasterFrame` class is both physically unrealistic and results in some unnecessary code repetition, as a `MasterFrame` is essentially just a `RelativeFrame` with zero velocity, and a list of other frames defined relative to it. Were I to remake this project, I would attempt to implement frames being defined in terms of each other through a tree structure implemented using the composite pattern or possibly even a graph.
* There is poor cohesion in the `SpacetimeGUI` and `SpacetimeCLI` classes. Both are responsible for all user interactions. This is a bigger problem in `SpacetimeGUI`, where one class is responsible for everything except handling the buttons in the UI, including displaying popups to the user and handling all the data being displayed. I would likely try to break up the functionality of these classes were I to redo the project.
* There is some coupling in the Button classes. This is because I was unable to find a way to parameterize an anonymous listener class' behaviour. If I were able to find a way to simply pass such a function as a parameter to a `Button` class' constructor, I would opt for that if rewriting the program.