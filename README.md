# Execute on Battery Status Change
A small Java application which executes another program every time a Windows laptop or tablet is plugged in or unplugged from the charger. Works with Windows 7 or newer.

# Background / When to use
I've made this program for a DIY project: a digital picture frame. It requires a Windows 10 tablet to turn off its screen 1 minute after unplugging power and to automatically turn on, and stay on, when plugging in again. 
Turning off after 1 minute is no problem. Windows energy settings support this. But turning on is where the problem lies:

If you don't click the mouse within 10 seconds of powering on, the screen will turn off again, no matter which power options you choose. 
As soon as the mouse is clicked it will follow the power options again, which you can set to "never turn off" on AC power. 

This is where the little program comes in handy: it detects when the device has been plugged in and executes the mouse click program immediately. 

# How to use
## Prerequisites
This application requires the Java Runtime Environment, version 1.8.0 or later, which can be downloaded from https://www.java.com/nl/download/

Install this before launching the application. This is a one-time process.

## Download the application
You can download the latest version [here](https://github.com/arjanvlek/execute-on-battery-status-change/releases/download/1.1.1/execute-on-battery-status-change.jar)

### Release notes
#### 1.1.1
- Fixed log output which takes place on power change always containing the same values for the 'from' and 'to' states.

#### 1.1.0
- Allow detecting empty WMI power state outputs. 

Some devices report nothing when discharging between 95 and 100%, because recharging is then disabled. 
This will be mapped to a special power state which allows the program to detect a change to and from this state.

- Log output to files named "output.log" and "error.log" (in current working directory)

#### 1.0.0
Initial release

## Creating the shortcut to launch it
Create a shortcut which has the following command:

```
"C:\Program Files\Java\jre1.8.0_<version>\javaw.exe" -jar "<<path_to_execute-on-battery-status-change.jar>>" "<program_to_run>" "<arguments_for_program_to_run"
```

![Creating a shortcut](creating_a_shortcut.jpg)

Here's an example with Java 1.8.0_251 and with the program Auto Mouse Click:

```
"C:\Program Files\Java\jre1.8.0_251\bin\javaw.exe" -jar "C:\Users\Owner\Documents\execute-on-battery-status-change.jar" "C:\Program Files\Auto Mouse Click by MurGee.com\Schedule.exe" "C:\Users\Owner\Documents\click mouse.mamc"
```
Owner needs to be replaced with your user account name.

## Launch it manually
Click on the created shortcut. This will start the Java process.

If you plug in or disconnect your PC from the charger it should open the specified program. 

You can also verify that the Java program is running by opening Task Manager and clicking on More Details

## Automatically launch when starting up your computer
- Right-click the Windows start menu and click Run
- Type `shell:startup` and press Enter
- The Startup folder will open. Place the shortcut you've just created here
- Restart your PC to see the effect


# Developing this application
Due to the usage of WMIC to check the battery status, the application can only be developed on Windows-based computers.

However, the project can be loaded into any IDE which allows developing Java 8 applications, such as Eclipse, IntelliJ IDEA, VS Code or NetBeans. 

There are - except from needing to use Windows - no external dependencies and no package manager is needed. Only a Java 8 JDK is required (Oracle JDK or OpenJDK both work fine).

Launch the app with `--DEBUG` to have it print out the battery information every time it checks it.
