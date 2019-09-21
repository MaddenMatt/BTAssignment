# Getting Started

This repository contains a Groovy application that was written using the IntelliJ Idea IDE.  Maven is used to manage the dependencies for this project, so please make sure that Maven is installed on your machine.  This project was written using Groovy version 2.5.8 and Java 8.  Please follow the steps below to import and run the project.

## Import Project Into IntelliJ Idea

Intellij Idea has built-in support for Git so importing projects into the IDE is streamlined.  

### Step 1. Open IntelliJ Idea and Begin the Import Process

There are two different methods to import a repository from Git.  You can use the IntelliJ Idea project management menu or using the file menu when you have an existing project open. Both of these methods start differently, but the instructions starting from Step 2 should be the same.

#### Option 1: Using the Project Management Menu(Recommended)

Start IntelliJ Idea, if there is no project currently open in ItelliJ Idea then the project management menu will open by default.  If there is currently a project open, the project management menu can be opened by clicking File -> Close Project.  The project that was open in the IDE will also be available to open from the project management menu if you wish to reopen it. 

In the project management menu click on Check out from Version Control -> Git.  Continued in Step 2.

#### Option 2: Using the File Menu

If you have a project open in IntelliJ Idea you can import a new project by navigating in the file menu to File -> New -> Project from Version Control... -> Git.  Continued in Step 2.

### Step 2. Use IntelliJ Idea's Git Support to Import the Project

A dialog box will open and ask for the Git URL to the repository and the path to the directory that the repository will be cloned to.  Enter the URL for this repository and the path of your choice then press the "Clone" button.

If you chose option one(imported using the project management menu) a prompt will open asking if you would like to open the imported project, click "Yes".

If you chose option two(imported using the file menu) a prompt will open asking if you would like to open the imported project in a new window or the current window.  Opening the project in a new window will leave the window with your current project open.  Opening the project in the current window will close your current project and load the new project into that window.  Either option will suffice, but if you have a project that you would like to leave open in the IDE then the "new window" is the better choice.

## Running the Application

The application should open in a new IntelliJ Idea window.  All dependencies should automatically be resolved by Maven, but just to be safe open the terminal view(located in the bottom left corner) and run "mvn clean install".

After the Maven build runs, navigate to {Your project directory}/src/main/groovy in the project view.

Right click on the App.class file and select "Run 'App.main()'.

The output of the program running will be printed to the console(this should automatically open) and written to the {Your project directory}/logs/AppOutput.log file. 


